package br.edu.utfpr.pb.emprestimoslabs.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.edu.utfpr.pb.emprestimoslabs.data.UFData;
import br.edu.utfpr.pb.emprestimoslabs.entity.UF;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudServiceImpl;

@Service
public class UFService extends CrudServiceImpl<UF, Long> {

	@Autowired
	private UFData ufData;
	
	@Override
	protected JpaRepository<UF, Long> getData() {
		return ufData;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public UF update(Long id, UF uf) {
		UF ufAtual = ufData.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(uf, ufAtual, "idUf");
		return ufData.save(ufAtual);
	}

}
