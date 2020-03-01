package br.edu.utfpr.pb.emprestimoslabs.controller.generic;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.edu.utfpr.pb.emprestimoslabs.entity.EntidadeBD;
import br.edu.utfpr.pb.emprestimoslabs.event.NovoRecurso;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudService;

public abstract class CrudControllerImpl<T extends EntidadeBD, ID extends Serializable>
		implements CrudController<T, ID> {

	protected abstract CrudService<T, ID> getService();

	protected abstract ApplicationEventPublisher getPublisher();

	@Override
	@GetMapping(value = { "", "/" })
	public List<T> findAll() {
		return getService().findAll();
	}

	@Override
	@GetMapping("/page")
	public Page<T> findAllPaginated(Pageable paginacao) {
		return getService().findAllPaginated(paginacao);
	}

	@Override
	@GetMapping("/{id}")
	public T findById(@PathVariable("id") ID id) {
		return getService().findById(id);
	}

	@Override
	@PostMapping
	public ResponseEntity<T> insert(@RequestBody @Valid T entity, HttpServletResponse response) {
		T entidadeSalva = getService().save(entity);
		getPublisher().publishEvent(new NovoRecurso(this, response, entidadeSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(entidadeSalva);
	}

	@Override
	@PutMapping("/{id}")
	public ResponseEntity<T> edit(@PathVariable("id") ID id, @Valid @RequestBody T entity) {
		T entidadeSalva = getService().update(id, entity);
		return ResponseEntity.ok(entidadeSalva);
	}

	@Override
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") ID id) {
		getService().delete(id);
	}
	
	@Override
	@GetMapping("/count")
	public long count() {
		return getService().count();
	}
}
