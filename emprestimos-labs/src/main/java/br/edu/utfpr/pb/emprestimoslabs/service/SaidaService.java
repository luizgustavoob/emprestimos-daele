package br.edu.utfpr.pb.emprestimoslabs.service;

import java.time.LocalDate;
import java.util.ArrayList;
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
import br.edu.utfpr.pb.emprestimoslabs.entity.Saida;
import br.edu.utfpr.pb.emprestimoslabs.entity.SaidaItem;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.SaidaDto;
import br.edu.utfpr.pb.emprestimoslabs.entity.enums.SituacaoSaida;
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
	private EmailService emailService;
	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private SimpMessagingTemplate message;
	
	@Override
	protected JpaRepository<Saida, Long> getData() {
		return saidaData;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Saida update(Long id, Saida saida) {
		Saida saidaAtual = saidaData.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		List<SaidaItem> itensAntesDoUpdate = new ArrayList<SaidaItem>();
		saidaAtual.getItens().forEach(si -> {
			SaidaItem item = new SaidaItem();
			item.setIdSaidaItem(si.getIdSaidaItem());
			item.setQuantidade(si.getQuantidade());
			item.setDataDevolucao(si.getDataDevolucao());
			item.setQuantidadeDevolvida(si.getQuantidadeDevolvida());
			itensAntesDoUpdate.add(item);
		});
		
		BeanUtils.copyProperties(saida, saidaAtual, "idSaida", "itens");
		saidaAtual.getItens().clear();		
		
		saidaAtual = saidaData.save(saidaAtual);
		
		for (SaidaItem saidaItem : saida.getItens()) {
			SaidaItem novoItem = new SaidaItem();
			novoItem.setIdSaidaItem(saidaItem.getIdSaidaItem());
			novoItem.setQuantidade(saidaItem.getQuantidade());
			novoItem.setDataDevolucao(saidaItem.getDataDevolucao());
			novoItem.setQuantidadeDevolvida(saidaItem.getQuantidadeDevolvida());
			saidaAtual.getItens().add(novoItem);
		}
		
		for (SaidaItem item : saidaAtual.getItens()) {
			item.getIdSaidaItem().setSaida(saidaAtual);
		}
		
		for (SaidaItem itemAntesDoUpdate : itensAntesDoUpdate) {
			if (!saidaAtual.getItens().contains(itemAntesDoUpdate)) {
				saidaItemData.delete(itemAntesDoUpdate);
			}
		}
		
		saidaItemData.saveAll(saidaAtual.getItens());
		
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
