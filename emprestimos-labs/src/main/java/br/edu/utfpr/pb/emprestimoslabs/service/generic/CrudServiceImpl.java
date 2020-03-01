package br.edu.utfpr.pb.emprestimoslabs.service.generic;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class CrudServiceImpl<T, ID extends Serializable> implements CrudService<T, ID> {

	protected abstract JpaRepository<T, ID> getData();
	
	@Override @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public abstract T update(ID id, T entidade);
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public T save(T entidade) {
		return getData().save(entidade);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(ID id) {
		getData().deleteById(id);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(T entidade) {
		getData().delete(entidade);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public T findById(ID id) {
		return getData().findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<T> findAll() {
		return getData().findAll();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<T> findAllSorted(Sort sort) {
		return getData().findAll(sort);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Page<T> findAllPaginated(Pageable pageable) {
		return getData().findAll(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public long count() {
		return getData().count();
	}
}
