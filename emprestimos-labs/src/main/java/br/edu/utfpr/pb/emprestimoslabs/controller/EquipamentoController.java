package br.edu.utfpr.pb.emprestimoslabs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.pb.emprestimoslabs.controller.generic.CrudControllerImpl;
import br.edu.utfpr.pb.emprestimoslabs.entity.Equipamento;
import br.edu.utfpr.pb.emprestimoslabs.entity.filter.EquipamentoFiltro;
import br.edu.utfpr.pb.emprestimoslabs.service.EquipamentoService;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudService;

@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController extends CrudControllerImpl<Equipamento, Long>{

	@Autowired
	private EquipamentoService equipamentoService;
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Override
	protected CrudService<Equipamento, Long> getService() {
		return equipamentoService;
	}

	@Override
	protected ApplicationEventPublisher getPublisher() {
		return publisher;
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Equipamento>> findByNomeOrPatrimonioOrSiorg(
			@RequestParam("chavePesquisa") String nomeOuPatrimonioOuSiorg) {
		List<Equipamento> equipamentos = equipamentoService.findByNomeOrPatrimonioOrSiorg(nomeOuPatrimonioOuSiorg);
		return ResponseEntity.ok(equipamentos);
	}
	
	@GetMapping("/filter")
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public ResponseEntity<Page<Equipamento>> findByPatrimonioAndNomeAndLocalizacao(
			EquipamentoFiltro equipamentoFiltro, Pageable pageable) {
		Page<Equipamento> equipamentos = equipamentoService.findByPatrimonioAndNomeAndLocalizacao(equipamentoFiltro, pageable);
		return ResponseEntity.ok(equipamentos);
	}
}