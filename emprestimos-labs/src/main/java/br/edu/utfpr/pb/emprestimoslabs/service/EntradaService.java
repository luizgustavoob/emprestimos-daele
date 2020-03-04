package br.edu.utfpr.pb.emprestimoslabs.service;

import java.util.ArrayList;
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
		List<EntradaItem> itensAntesDoUpdate = new ArrayList<EntradaItem>();
		entradaAtual.getItens().forEach(ei -> {
			EntradaItem item = new EntradaItem();
			item.setIdEntradaItem(ei.getIdEntradaItem());
			item.setEquipamento(ei.getEquipamento());
			item.setQuantidade(ei.getQuantidade());
			item.setValorUnitario(ei.getValorUnitario());
			itensAntesDoUpdate.add(item);
		});
		
		BeanUtils.copyProperties(entrada, entradaAtual, "idEntrada", "itens");
		entradaAtual.setUsuario(UsuarioAutenticado.get());
		entradaAtual.getItens().clear();
		
		entradaAtual = entradaData.save(entradaAtual);
		
		for (EntradaItem entradaItem : entrada.getItens()) {
			EntradaItem novoItem = new EntradaItem();
			novoItem.setIdEntradaItem(entradaItem.getIdEntradaItem());
			novoItem.setEquipamento(entradaItem.getEquipamento());
			novoItem.setQuantidade(entradaItem.getQuantidade());
			novoItem.setValorUnitario(entradaItem.getValorUnitario());
			entradaAtual.getItens().add(novoItem);			
		}
		
		for (EntradaItem item : entradaAtual.getItens()) {
			item.getIdEntradaItem().setEntrada(entradaAtual);
		}
		
		
		for (EntradaItem itemAntesDoUpdate : itensAntesDoUpdate) {
			if (!entradaAtual.getItens().contains(itemAntesDoUpdate)) {
				entradaItemData.delete(itemAntesDoUpdate);
			}
		}
		
		entradaItemData.saveAll(entradaAtual.getItens());		
		
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
		return entrada;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(Entrada entrada) {
		for (EntradaItem item : entrada.getItens()) {
			entradaItemData.delete(item);
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
