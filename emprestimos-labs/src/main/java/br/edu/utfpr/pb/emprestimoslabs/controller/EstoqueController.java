package br.edu.utfpr.pb.emprestimoslabs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.pb.emprestimoslabs.entity.dto.EquipamentoDto;
import br.edu.utfpr.pb.emprestimoslabs.entity.filter.FichaEstoqueFiltro;
import br.edu.utfpr.pb.emprestimoslabs.service.EstoqueService;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {
	
	@Autowired
	private EstoqueService estoqueService;
	
	@GetMapping("/estoque-esgotando")
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public ResponseEntity<List<EquipamentoDto>> findEquipamentosComEstoqueEsgotando() {
		List<EquipamentoDto> equipamentos = estoqueService.findEquipamentosComEstoqueEsgotando();
		return ResponseEntity.ok(equipamentos);
	}
	
	@GetMapping("/ficha-estoque")
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public ResponseEntity<byte[]> relatorioFichaDeEstoque(FichaEstoqueFiltro filtro) throws Exception {
		byte[] relatorio = estoqueService.relatorioFichaDeEstoque(filtro.getData(), filtro.getIdsEquipamentos());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}
}
