package br.edu.utfpr.pb.emprestimoslabs.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.edu.utfpr.pb.emprestimoslabs.data.EntradaItemData;
import br.edu.utfpr.pb.emprestimoslabs.data.EquipamentoData;
import br.edu.utfpr.pb.emprestimoslabs.entity.Equipamento;
import br.edu.utfpr.pb.emprestimoslabs.entity.filter.EquipamentoFiltro;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudServiceImpl;

@Service
public class EquipamentoService extends CrudServiceImpl<Equipamento, Long> {

	@Autowired
	private EquipamentoData equipamentoData;
	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private EntradaItemData entradaItemData;
	
	@Override
	protected JpaRepository<Equipamento, Long> getData() {
		return equipamentoData;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Equipamento findById(Long id) {
		Equipamento equipamento = super.findById(id);
		equipamento.setValorUltimaCompra(entradaItemData.findValorUnitarioUltimaCompra(equipamento.getId()).orElse(new BigDecimal(0)));
		return equipamento;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Equipamento> findAll() {
		List<Equipamento> equipamentos = super.findAll();
		equipamentos.forEach((equip) -> {
			equip.setValorUltimaCompra(entradaItemData.findValorUnitarioUltimaCompra(equip.getId()).orElse(new BigDecimal(0)));
		});
		return equipamentos;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Equipamento update(Long id, Equipamento equipamento) {
		Equipamento equipamentoAtual = equipamentoData.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(equipamento, equipamentoAtual, "idEquipamento");
		return equipamentoData.save(equipamentoAtual);
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Page<Equipamento> findAllPaginated(Pageable pageable) {
		Page<Equipamento> equipamentos = super.findAllPaginated(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("nome")));
	
		equipamentos.forEach((equip) -> {
			equip.setValorUltimaCompra(entradaItemData.findValorUnitarioUltimaCompra(equip.getId()).orElse(new BigDecimal(0)));
		});
		
		return equipamentos;
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Equipamento> findByNomeOrPatrimonioOrSiorg(String chavePesquisa) {
		TypedQuery<Equipamento> query = this.manager.createQuery("select e from Equipamento e "
				+ "where lower(e.nome) like lower(:chavePesquisa) or "
				+ " CONCAT(e.patrimonio, '') like :chavePesquisa or "
				+ " CONCAT(e.siorg, '') like :chavePesquisa", Equipamento.class);
		query.setParameter("chavePesquisa", "%" + chavePesquisa + "%");
		return query.getResultList();
	}
		
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Page<Equipamento> findByPatrimonioAndNomeAndLocalizacao(EquipamentoFiltro equipamentoFiltro, Pageable pageable) {
		String qry = "SELECT e FROM Equipamento e WHERE 0 = 0";
		
		if (equipamentoFiltro.getPatrimonio() != null && equipamentoFiltro.getPatrimonio() > 0) {
			qry += " AND e.patrimonio = :patrimonio";
		}
		
		if (equipamentoFiltro.getNome() != null && !equipamentoFiltro.getNome().isEmpty()) {
			qry += " AND LOWER(e.nome) LIKE LOWER(:nome)";
		}
		
		if (equipamentoFiltro.getLocalizacao() != null && !equipamentoFiltro.getLocalizacao().isEmpty()) {
			qry += " AND LOWER(e.localizacao) LIKE LOWER(:localizacao)";
		}
		
		TypedQuery<Equipamento> query = manager.createQuery(qry, Equipamento.class);
		
		if (qry.contains(":patrimonio")) {
			query.setParameter("patrimonio", equipamentoFiltro.getPatrimonio());
		}
		
		if (qry.contains(":nome")) {
			query.setParameter("nome", "%"+equipamentoFiltro.getNome()+"%");
		}
		
		if (qry.contains(":localizacao")) {
			query.setParameter("localizacao", "%"+equipamentoFiltro.getLocalizacao()+"%");
		}
		
		List<Equipamento> equipamentos = query.getResultList();
		
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		List<Equipamento> equipamentosComPaginacao = query
				.setFirstResult(primeiroRegistroDaPagina)
				.setMaxResults(totalRegistrosPorPagina)
				.getResultList();
		
		equipamentos.forEach((equip) -> {
			equip.setValorUltimaCompra(entradaItemData.findValorUnitarioUltimaCompra(equip.getId()).orElse(new BigDecimal(0)));
		});
				
		return new PageImpl<Equipamento>(equipamentosComPaginacao, pageable, equipamentos.size());
	}

}
