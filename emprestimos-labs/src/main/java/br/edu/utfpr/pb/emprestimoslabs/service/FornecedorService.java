package br.edu.utfpr.pb.emprestimoslabs.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.edu.utfpr.pb.emprestimoslabs.data.FornecedorData;
import br.edu.utfpr.pb.emprestimoslabs.entity.Fornecedor;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudServiceImpl;

@Service
public class FornecedorService extends CrudServiceImpl<Fornecedor, Long> {

	@Autowired
	private FornecedorData fornecedorData;
	
	@Override
	protected JpaRepository<Fornecedor, Long> getData() {
		return fornecedorData;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Fornecedor update(Long id, Fornecedor fornecedor) {
		Fornecedor fornecedorAtual = fornecedorData.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(fornecedor, fornecedorAtual, "idFornecedor");
		return fornecedorData.save(fornecedorAtual);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Page<Fornecedor> findAllPaginated(Pageable pageable) {
		return super.findAllPaginated(
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("razaoSocial")));
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Fornecedor> findByRazaoSocialOrCNPJ(String chavePesquisa) {
		return fornecedorData.findRazaoSocialOrCNPJ(chavePesquisa);
	}
	
}
