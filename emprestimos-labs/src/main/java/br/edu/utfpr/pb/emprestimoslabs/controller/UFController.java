package br.edu.utfpr.pb.emprestimoslabs.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.pb.emprestimoslabs.controller.generic.CrudControllerImpl;
import br.edu.utfpr.pb.emprestimoslabs.entity.UF;
import br.edu.utfpr.pb.emprestimoslabs.service.UFService;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudService;

@RestController
@RequestMapping("/estados")
public class UFController extends CrudControllerImpl<UF, Long> {

	@Autowired
	private UFService ufService;
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Override
	protected CrudService<UF, Long> getService() {
		return ufService;
	}

	@Override
	protected ApplicationEventPublisher getPublisher() {
		return publisher;
	}
	
	@Override
	@Cacheable(value = "estados-all")
	@GetMapping
	public List<UF> findAll() {
		return super.findAll();
	}
	
	@Override
	@CacheEvict(value = "estados-all", allEntries = true)
	@PostMapping
	public ResponseEntity<UF> insert(@RequestBody @Valid UF entidade, HttpServletResponse resposta) {
		return super.insert(entidade, resposta);
	}
	
	@Override
	@CacheEvict(value = "estados-all", allEntries = true)
	@PutMapping("/{id}")
	public ResponseEntity<UF> edit(@PathVariable("id") Long id, @RequestBody @Valid UF entidade) {
		return super.edit(id, entidade);
	}
	
	@Override
	@CacheEvict(value = "estados-all", allEntries = true)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		super.delete(id);
	}
}
