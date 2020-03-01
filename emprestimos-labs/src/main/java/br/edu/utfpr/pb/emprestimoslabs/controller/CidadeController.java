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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.pb.emprestimoslabs.controller.generic.CrudControllerImpl;
import br.edu.utfpr.pb.emprestimoslabs.entity.Cidade;
import br.edu.utfpr.pb.emprestimoslabs.service.CidadeService;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudService;

@RestController
@RequestMapping("/cidades")
public class CidadeController extends CrudControllerImpl<Cidade, Long> {
	
	@Autowired
	private CidadeService cidadeService;
	@Autowired
	private ApplicationEventPublisher publisher;

	@Override
	protected CrudService<Cidade, Long> getService() {
		return cidadeService;
	}

	@Override
	protected ApplicationEventPublisher getPublisher() {
		return publisher;
	}
	
	@Override
	@GetMapping
	@Cacheable(value = "cidades-all")
	public List<Cidade> findAll() {
		return super.findAll();
	}
	
	@Override
	@PostMapping
	@CacheEvict(value = "cidades-all", allEntries = true)
	public ResponseEntity<Cidade> insert(@RequestBody @Valid Cidade cidade, HttpServletResponse response) {
		return super.insert(cidade, response);
	}

	@Override
	@PutMapping("/{id}")
	@CacheEvict(value = "cidades=all", allEntries = true)
	public ResponseEntity<Cidade> edit(@PathVariable("id") Long id, @RequestBody @Valid Cidade cidade) {
		return super.edit(id, cidade);
	}
	
	@Override
	@DeleteMapping("/{id}")
	@CacheEvict(value = "cidades-all", allEntries = true)
	public void delete(@PathVariable("id") Long id) {
		super.delete(id);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Cidade>> findByNomeOrUF(@RequestParam("chavePesquisa") String nomeOuUF) {
		List<Cidade> cidades = cidadeService.findByNomeOrUF(nomeOuUF);
		return ResponseEntity.ok(cidades);
	}
}
