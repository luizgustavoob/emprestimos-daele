package br.edu.utfpr.pb.emprestimoslabs.service.generic;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface CrudService<T, ID extends Serializable> {

	T save(T entidade);
	T update(ID id, T entidade);
	void delete(ID id);
	void delete(T entidade);
	T findById(ID id);
	List<T> findAll();
	List<T> findAllSorted(Sort sort);
	Page<T> findAllPaginated(Pageable pageable);
	long count();
}
