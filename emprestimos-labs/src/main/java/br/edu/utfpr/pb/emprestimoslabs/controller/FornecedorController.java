package br.edu.utfpr.pb.emprestimoslabs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.pb.emprestimoslabs.controller.generic.CrudControllerImpl;
import br.edu.utfpr.pb.emprestimoslabs.entity.Fornecedor;
import br.edu.utfpr.pb.emprestimoslabs.service.FornecedorService;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudService;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController extends CrudControllerImpl<Fornecedor, Long> {

	@Autowired
	private FornecedorService fornecedorService;
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Override
	protected CrudService<Fornecedor, Long> getService() {
		return fornecedorService;
	}

	@Override
	protected ApplicationEventPublisher getPublisher() {
		return publisher;
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Fornecedor>> findByRazaoSocialOrCNPJ(@RequestParam("chavePesquisa") String razaoSocialOuCNPJ) {
		List<Fornecedor> fornecedores = fornecedorService.findByRazaoSocialOrCNPJ(razaoSocialOuCNPJ);
		return ResponseEntity.ok(fornecedores);
	}

}
