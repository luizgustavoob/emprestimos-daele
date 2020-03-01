package br.edu.utfpr.pb.emprestimoslabs.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.edu.utfpr.pb.emprestimoslabs.data.EntradaData;
import br.edu.utfpr.pb.emprestimoslabs.data.EntradaItemData;
import br.edu.utfpr.pb.emprestimoslabs.entity.Entrada;
import br.edu.utfpr.pb.emprestimoslabs.entity.EntradaItem;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.EntradaDto;
import br.edu.utfpr.pb.emprestimoslabs.entity.filter.EntradaFiltro;
import br.edu.utfpr.pb.emprestimoslabs.security.util.UsuarioAutenticado;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudServiceImpl;

@Service
public class EntradaService extends CrudServiceImpl<Entrada, Long>{

	@Autowired
	private EntradaData entradaData;
	@Autowired
	private EntradaItemData entradaItemData;
	@Autowired
	private EstoqueService estoqueService;
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	protected JpaRepository<Entrada, Long> getData() {
		return entradaData;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Entrada update(Long id, Entrada entrada) {
		Entrada entradaAtual = entradaData.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		
		Entrada entradaAntiga = new Entrada();
		BeanUtils.copyProperties(entradaAtual, entradaAntiga, "idEntrada");
		for (EntradaItem item : entradaAntiga.getItens()) {
			estoqueService.atualizarEstoqueEntrada(item.getEquipamento(),
					entradaAntiga.getData(), item.getQuantidade() * -1);
		}
						
		BeanUtils.copyProperties(entrada, entradaAtual, "idEntrada", "itens");
		entradaAtual.setUsuario(UsuarioAutenticado.get());
		entradaAtual = entradaData.save(entradaAtual);
		
		entradaAtual.getItens().clear();
		for (EntradaItem item: entrada.getItens()) {
			EntradaItem itemEntrada = new EntradaItem();
			itemEntrada.setIdEntradaItem(item.getIdEntradaItem());
			itemEntrada.setEquipamento(item.getEquipamento());
			itemEntrada.setQuantidade(item.getQuantidade());
			itemEntrada.setValorUnitario(item.getValorUnitario());
			entradaAtual.getItens().add(itemEntrada);
		}
		
		entradaItemData.saveAll(entradaAtual.getItens());
		
		for (EntradaItem item : entradaAtual.getItens()) {
			estoqueService.atualizarEstoqueEntrada(item.getEquipamento(),
					entradaAntiga.getData(), item.getQuantidade());
		}
		
		return entradaAtual;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Entrada save(Entrada entrada) {
		entrada.setUsuario(UsuarioAutenticado.get());
		entrada = entradaData.save(entrada);
		for (EntradaItem item : entrada.getItens()) {
			item.getIdEntradaItem().setEntrada(entrada);
		}
		entradaItemData.saveAll(entrada.getItens());
		for (EntradaItem item : entrada.getItens()) {
			estoqueService.atualizarEstoqueEntrada(item.getEquipamento(), entrada.getData(), item.getQuantidade());
		}
		return entrada;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(Entrada entrada) {
		for (EntradaItem item : entrada.getItens()) {
			entradaItemData.delete(item);
			estoqueService.atualizarEstoqueEntrada(item.getEquipamento(), entrada.getData(), item.getQuantidade());
		}
		super.delete(entrada);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(Long id) {
		Entrada entrada = entradaData.findById(id).orElseThrow(() -> new RuntimeException("Entrada não encontrada"));
		this.delete(entrada);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Page<EntradaDto> findByDataAndFornecedor(EntradaFiltro entradaFiltro, Pageable pageable) {
		String qry = "SELECT e FROM Entrada e WHERE 0 = 0";
		
		if (entradaFiltro.getData() != null) {
			qry += " AND e.data = :data";
		}
		
		if (entradaFiltro.getFornecedor() != null && !entradaFiltro.getFornecedor().isEmpty()) {
			qry += " AND e.fornecedor.razaoSocial = :fornecedor";
		}
		
		qry += " ORDER BY e.data DESC";
		
		TypedQuery<Entrada> query = manager.createQuery(qry, Entrada.class);
		
		if (qry.contains(":data")) {
			query.setParameter("data", entradaFiltro.getData());
		}
		
		if (qry.contains(":fornecedor")) {
			query.setParameter("fornecedor", entradaFiltro.getFornecedor());
		}
		
		List<Entrada> entradas = query.getResultList();
		
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		List<EntradaDto> entradasDtoComPaginacao = query
				.setFirstResult(primeiroRegistroDaPagina)
				.setMaxResults(totalRegistrosPorPagina)
				.getResultList()
				.stream()
				.map(entrada -> new EntradaDto(entrada))
				.collect(Collectors.toList());
		
		return new PageImpl<EntradaDto>(entradasDtoComPaginacao, pageable, entradas.size());
	}

}
