package br.edu.utfpr.pb.emprestimoslabs.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.edu.utfpr.pb.emprestimoslabs.data.SaidaData;
import br.edu.utfpr.pb.emprestimoslabs.data.SaidaItemData;
import br.edu.utfpr.pb.emprestimoslabs.email.EmailService;
import br.edu.utfpr.pb.emprestimoslabs.entity.Equipamento;
import br.edu.utfpr.pb.emprestimoslabs.entity.Saida;
import br.edu.utfpr.pb.emprestimoslabs.entity.SaidaItem;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.SaidaDto;
import br.edu.utfpr.pb.emprestimoslabs.entity.enums.SituacaoSaida;
import br.edu.utfpr.pb.emprestimoslabs.entity.enums.TipoSaida;
import br.edu.utfpr.pb.emprestimoslabs.entity.filter.SaidaFiltro;
import br.edu.utfpr.pb.emprestimoslabs.security.util.UsuarioAutenticado;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudServiceImpl;
import br.edu.utfpr.pb.emprestimoslabs.websocket.Queue;

@Service
public class SaidaService extends CrudServiceImpl<Saida, Long> {

	@Autowired
	private SaidaData saidaData;
	@Autowired
	private SaidaItemData saidaItemData;
	@Autowired
	private EstoqueService estoqueService;
	@Autowired
	private EquipamentoService itemService;
	@Autowired
	private EmailService emailService;
	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private SimpMessagingTemplate message;
	
	@Override
	protected JpaRepository<Saida, Long> getData() {
		return saidaData;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	private void atualizarEstoque(Saida saida, boolean isUpdate) {
		for (SaidaItem itemSaida : saida.getItens()) {
			Equipamento item = itemService.findById(itemSaida.getIdSaidaItem().getEquipamento().getIdEquipamento());
			switch (saida.getSituacao()) {
				case ENCERRADA:
					if (saida.getTipoSaida() == TipoSaida.BAIXA) {
						estoqueService.atualizarEstoqueSaida( item, saida.getData(),
								itemSaida.getQuantidade() * (isUpdate ? -1 : 1) );
					} else {
						estoqueService.atualizarEstoqueEntrada( item, itemSaida.getDataDevolucao(),
								itemSaida.getQuantidadeDevolvida() * (isUpdate ? -1 : 1) );
					}
					break;
				case APROVADA:
					estoqueService.atualizarEstoqueReservas( item, saida.getData(),
							itemSaida.getQuantidade() * (isUpdate ? -1 : 1) );
					break;
					
				default:
					break;
			}
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Saida update(Long id, Saida saida) {
		Saida saidaAtual = saidaData.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		
		Saida saidaAntiga = new Saida();
		BeanUtils.copyProperties(saidaAtual, saidaAntiga, "idSaida");
		
		if (!saida.getSituacao().equals(saidaAntiga.getSituacao())) {
			atualizarEstoque(saidaAntiga, true);
			Saida saidaAjuste = new Saida();
			BeanUtils.copyProperties(saida, saidaAjuste, "idSaida", "situacao");
			saidaAjuste.setSituacao(saidaAntiga.getSituacao());
			atualizarEstoque(saidaAjuste, false);
		} else {
			atualizarEstoque(saidaAntiga, true);
		}
		
		BeanUtils.copyProperties(saida, saidaAtual, "idSaida", "itens");
		saidaAtual = saidaData.save(saidaAtual);
		saidaAtual.getItens().clear();
		for (SaidaItem item: saida.getItens()) {
			SaidaItem itemSaida = new SaidaItem();
			itemSaida.getIdSaidaItem().setSaida(saidaAtual);
			itemSaida.getIdSaidaItem().setEquipamento(item.getIdSaidaItem().getEquipamento());
			itemSaida.setQuantidade(item.getQuantidade());
			itemSaida.setQuantidadeDevolvida(item.getQuantidadeDevolvida());
			itemSaida.setDataDevolucao(item.getDataDevolucao());
			saidaAtual.getItens().add(itemSaida);
		}
		saidaItemData.saveAll(saidaAtual.getItens());
			
		atualizarEstoque(saidaAtual, false);
		
		return saidaAtual;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Saida save(Saida saida) {
		saida.setUsuario(UsuarioAutenticado.get());
		saida = saidaData.save(saida);
		for (SaidaItem item : saida.getItens()) {
			item.getIdSaidaItem().setSaida(saida);
		}
		saidaItemData.saveAll(saida.getItens());
		atualizarEstoque(saida, false);
		return saida;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(Long id) {
		Saida saida = saidaData.findById(id).orElseThrow(() -> new RuntimeException("Saída não encontrada"));
		this.delete(saida);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(Saida saida) {
		for (SaidaItem item : saida.getItens()) {
			saidaItemData.delete(item);
		}
		super.delete(saida);
		atualizarEstoque(saida, true);
		if (!saida.getSituacao().equals(SituacaoSaida.REPROVADA)) {
			message.convertAndSend(Queue.ATUALIZAR_ESTOQUE, "update");
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateSituacao(Long id, SituacaoSaida situacao) {
		Saida saida = saidaData.findById(id).orElseThrow(() -> new RuntimeException("Saída não encontrada"));
		saida.setSituacao(situacao);
		
		if (situacao.equals(SituacaoSaida.ENCERRADA)) {
			saida.getItens().forEach(item -> {
				item.setQuantidadeDevolvida(item.getQuantidade());
				item.setDataDevolucao(LocalDate.now());
			});
		}
		
		saida = saidaData.save(saida);
		atualizarEstoque(saida, false);
		
		message.convertAndSend(String.format(Queue.NOTIFICA_USUARIO, saida.getUsuario().getEmail()), "emprestimo");
		message.convertAndSend(Queue.ATUALIZAR_ESTOQUE, "update");
				
		switch (situacao) {
			case APROVADA:
				emailService.enviarAprovacaoDeEmprestimo(saida);
				break;
			case REPROVADA:
				emailService.enviarReprovacaoDeEmprestimo(saida);
				break;
		default:
			break;
		}
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Page<SaidaDto> getEmprestimosDoUsuario(Pageable pageable) {
		return saidaData.findByUsuarioEqualsOrderByDataDesc(UsuarioAutenticado.get(), pageable)
				.map((saida) -> new SaidaDto(saida));
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<SaidaDto> getEmprestimosPendentes() {
		return saidaData.findEmprestimosPendentes()
			.stream()
			.map((saida) -> new SaidaDto(saida))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Page<SaidaDto> findByDataAndUsuarioAndSituacao(SaidaFiltro saidaFiltro, Pageable pageable) {
		List<SituacaoSaida> situacoes = null;
		
		if (saidaFiltro.getSituacao() == null) {
			situacoes = Arrays.asList(
					SituacaoSaida.PENDENTE,
					SituacaoSaida.APROVADA,
					SituacaoSaida.ENCERRADA,
					SituacaoSaida.REPROVADA);
		} else {
			situacoes = Arrays.asList(saidaFiltro.getSituacao());
		}
		
		String qry = "SELECT s FROM Saida s WHERE s.tipoSaida = 'EMPRESTIMO'";
				
		if (saidaFiltro.getData() != null) {
			qry += " AND s.data = :data";
		}
		
		if (saidaFiltro.getNrora() != null && saidaFiltro.getNrora() > 0) {
			qry += " AND s.usuario.nrora = :nrora";
		}
		
		qry += " AND s.situacao in :situacao";
		qry += " ORDER BY s.data DESC";
				
		TypedQuery<Saida> query = manager.createQuery(qry, Saida.class);
		
		if (qry.contains(":data")) {
			query.setParameter("data", saidaFiltro.getData());
		}
		
		if (qry.contains(":nrora")) {
			query.setParameter("nrora", saidaFiltro.getNrora());
		}
		
		query.setParameter("situacao", situacoes);
			
		List<Saida> emprestimos = query.getResultList();
		
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		List<SaidaDto> emprestimosDtoComPaginacao = query
				.setFirstResult(primeiroRegistroDaPagina)
				.setMaxResults(totalRegistrosPorPagina)
				.getResultList()
				.stream()
				.map(saida -> new SaidaDto(saida))
				.collect(Collectors.toList());
		
		return new PageImpl<SaidaDto>(emprestimosDtoComPaginacao, pageable, emprestimos.size());
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Page<SaidaDto> findBaixasDeEstoque(Pageable pageable) {
		String qry = "SELECT s FROM Saida s WHERE s.tipoSaida = 'BAIXA' ORDER BY s.idSaida";
		
		TypedQuery<Saida> query = manager.createQuery(qry, Saida.class);
		
		List<Saida> baixas = query.getResultList();
		
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		List<SaidaDto> baixasDtoComPaginacao = query
				.setFirstResult(primeiroRegistroDaPagina)
				.setMaxResults(totalRegistrosPorPagina)
				.getResultList()
				.stream()
				.map(saida -> new SaidaDto(saida))
				.collect(Collectors.toList());
		
		return new PageImpl<SaidaDto>(baixasDtoComPaginacao, pageable, baixas.size());
	}
}
