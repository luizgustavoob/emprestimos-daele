package br.edu.utfpr.pb.emprestimoslabs.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.edu.utfpr.pb.emprestimoslabs.data.CidadeData;
import br.edu.utfpr.pb.emprestimoslabs.entity.Cidade;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudServiceImpl;

@Service
public class CidadeService extends CrudServiceImpl<Cidade, Long>{

	@Autowired
	private CidadeData cidadeData;
	
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	protected JpaRepository<Cidade, Long> getData() {
		return cidadeData;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Cidade update(Long id, Cidade cidade) {
		Cidade cidadeAtual = cidadeData.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(cidade, cidadeAtual, "idCidade");
		return cidadeData.save(cidadeAtual);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Cidade> findByNomeOrUF(String chavePesquisa) {
		TypedQuery<Cidade> query = manager.createQuery("select c from Cidade c "
				+ "where lower(c.nome) like lower(:chavePesquisa) or lower(c.uf.nome) like lower(:chavePesquisa)", Cidade.class);
		query.setParameter("chavePesquisa", "%" + chavePesquisa + "%");
		return query.getResultList();
	}

}
