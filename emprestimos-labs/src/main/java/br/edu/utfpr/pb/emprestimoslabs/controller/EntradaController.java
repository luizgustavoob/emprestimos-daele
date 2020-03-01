package br.edu.utfpr.pb.emprestimoslabs.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.pb.emprestimoslabs.entity.Entrada;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.EntradaDto;
import br.edu.utfpr.pb.emprestimoslabs.entity.filter.EntradaFiltro;
import br.edu.utfpr.pb.emprestimoslabs.event.NovoRecurso;
import br.edu.utfpr.pb.emprestimoslabs.service.EntradaService;
import br.edu.utfpr.pb.emprestimoslabs.service.EquipamentoService;
import br.edu.utfpr.pb.emprestimoslabs.service.FornecedorService;
import br.edu.utfpr.pb.emprestimoslabs.websocket.Queue;

@RestController
@RequestMapping("/entradas")
public class EntradaController  {
	
	@Autowired
	private EntradaService entradaService;
	@Autowired
	private FornecedorService fornecedorService;
	@Autowired
	private EquipamentoService equipamentoService;
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private SimpMessagingTemplate message;
	
	@GetMapping(value = { "", "/" })
	public ResponseEntity<List<EntradaDto>> findAll() {
		List<Entrada> entradas = entradaService.findAll();
		List<EntradaDto> entradasDto = entradas.stream()
				.map(entrada -> new EntradaDto(entrada)).collect(Collectors.toList());
		return ResponseEntity.ok().body(entradasDto);
	}

	@GetMapping("/page")
	public Page<EntradaDto> findAllPaginated(Pageable pageable) {
		Page<Entrada> entradas = entradaService.findAllPaginated(pageable);
		Page<EntradaDto> entradasDto = entradas.map(entrada -> new EntradaDto(entrada));
		return entradasDto;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EntradaDto> findById(@PathVariable("id") Long id) {
		Entrada entrada = entradaService.findById(id);
		return ResponseEntity.ok().body(new EntradaDto(entrada));
	}

	@PostMapping
	public ResponseEntity<EntradaDto> insert(@RequestBody @Valid EntradaDto entradaDto, HttpServletResponse response) {
		Entrada entrada = entradaDto.toEntity(fornecedorService, equipamentoService);
		entrada = entradaService.save(entrada);
		publisher.publishEvent(new NovoRecurso(this, response, entrada.getId()));
		message.convertAndSend(Queue.ATUALIZAR_ESTOQUE, "update");
		return ResponseEntity.status(HttpStatus.CREATED).body(new EntradaDto(entrada));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EntradaDto> edit(@PathVariable("id") Long id,
			@Valid @RequestBody EntradaDto entradaDto) {
		Entrada entrada = entradaDto.toEntity(fornecedorService, equipamentoService);
		entrada = entradaService.update(id, entrada);
		message.convertAndSend(Queue.ATUALIZAR_ESTOQUE, "update");
		return ResponseEntity.ok(new EntradaDto(entrada));
	}

	@DeleteMapping("/{id}")
	public void excluir(@PathVariable("id") Long id) {
		entradaService.delete(id);
		message.convertAndSend(Queue.ATUALIZAR_ESTOQUE, "update");
	}
	
	@GetMapping("/count")
	public long count() {
		return entradaService.count();
	}
	
	@GetMapping("/filter")
	public ResponseEntity<Page<EntradaDto>> findByDataAndFornecedor(EntradaFiltro entradaFiltro, Pageable pageable) {
		Page<EntradaDto> entradas = entradaService.findByDataAndFornecedor(entradaFiltro, pageable);
		return ResponseEntity.ok(entradas);
	}
}
