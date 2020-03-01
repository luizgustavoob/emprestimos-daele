package br.edu.utfpr.pb.emprestimoslabs.controller.generic;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CrudController<T, ID extends Serializable> {

	List<T> findAll();
	Page<T> findAllPaginated(Pageable pageable);
	T findById(ID id);
	ResponseEntity<T> insert(T entity, HttpServletResponse response);
	ResponseEntity<T> edit(ID id, T entidade);
	void delete(ID id);
	long count();
}
