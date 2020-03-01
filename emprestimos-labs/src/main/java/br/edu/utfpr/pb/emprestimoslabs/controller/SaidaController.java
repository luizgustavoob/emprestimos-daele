package br.edu.utfpr.pb.emprestimoslabs.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.pb.emprestimoslabs.entity.Saida;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.SaidaDto;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.UpdateSituacaoSaidaForm;
import br.edu.utfpr.pb.emprestimoslabs.entity.enums.SituacaoSaida;
import br.edu.utfpr.pb.emprestimoslabs.entity.filter.SaidaFiltro;
import br.edu.utfpr.pb.emprestimoslabs.event.NovoRecurso;
import br.edu.utfpr.pb.emprestimoslabs.service.EquipamentoService;
import br.edu.utfpr.pb.emprestimoslabs.service.SaidaService;
import br.edu.utfpr.pb.emprestimoslabs.websocket.Queue;

@RestController
@RequestMapping("/saidas")
public class SaidaController {

	@Autowired
	private SaidaService saidaService;
	@Autowired
	private EquipamentoService equipamentoService;
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private SimpMessagingTemplate message;
	
	@GetMapping(value = { "", "/" })
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public ResponseEntity<List<SaidaDto>> findAll() {
		List<Saida> saidas = saidaService.findAll();
		List<SaidaDto> saidasDto = saidas
				.stream()
				.map(saida -> new SaidaDto(saida))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(saidasDto);
	}
	
	@GetMapping("/page")
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public Page<SaidaDto> findAllPaginated(Pageable pageable) {
		Page<Saida> saidas = saidaService.findAllPaginated(pageable);
		Page<SaidaDto> saidasDto = saidas.map(saida -> new SaidaDto(saida));
		return saidasDto;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SaidaDto> findById(@PathVariable("id") Long id) {
		Saida saida = saidaService.findById(id);
		return ResponseEntity.ok().body(new SaidaDto(saida));
	}
	
	@PostMapping
	public ResponseEntity<SaidaDto> insert(@RequestBody @Valid SaidaDto saidaDto, HttpServletResponse response) {
		Saida saida = saidaDto.toEntity(equipamentoService);
		saida = saidaService.save(saida);
		if (saida.getSituacao().equals(SituacaoSaida.PENDENTE)) {
			message.convertAndSend(Queue.NOVOS_EMPRESTIMOS, "update");
		} else if (!saida.getSituacao().equals(SituacaoSaida.REPROVADA)) {
			message.convertAndSend(Queue.ATUALIZAR_ESTOQUE, "update");
		}
		publisher.publishEvent(new NovoRecurso(this, response, saida.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(new SaidaDto(saida));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SaidaDto> edit(@PathVariable("id") Long id,
			@Valid @RequestBody SaidaDto saidaDto) {
		Saida saida = saidaDto.toEntity(equipamentoService);
		saida = saidaService.update(id, saida);
		if (!saida.getSituacao().equals(SituacaoSaida.REPROVADA)) {
			message.convertAndSend(Queue.ATUALIZAR_ESTOQUE, "update");
		}
		return ResponseEntity.ok(new SaidaDto(saida));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		saidaService.delete(id);
	}
	
	@GetMapping("/filter")
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public ResponseEntity<Page<SaidaDto>> findByDataAndUsuarioAndSituacao(SaidaFiltro saidaFiltro, Pageable pageable) {
		Page<SaidaDto> emprestimos = saidaService.findByDataAndUsuarioAndSituacao(saidaFiltro, pageable);
		return ResponseEntity.ok(emprestimos);
	}
	
	@PatchMapping("/{id}/situacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateSituacao(@PathVariable("id") Long id, @RequestBody @NotNull UpdateSituacaoSaidaForm situacaoForm) {
		SituacaoSaida situacao = SituacaoSaida.toEnum(situacaoForm.getSituacao());
		saidaService.updateSituacao(id, situacao);
	}
	
	@GetMapping("/meus-emprestimos")
	@PreAuthorize("hasRole('ALUNO') or hasRole('PROFESSOR')")
	public Page<SaidaDto> getEmprestimosDoUsuario(Pageable pageable){
		Page<SaidaDto> emprestimos = saidaService.getEmprestimosDoUsuario(pageable);
		return emprestimos;
	}
	
	@GetMapping("/emprestimos-pendentes")
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public ResponseEntity<List<SaidaDto>> getEmprestimosPendentes() {
		List<SaidaDto> emprestimos = saidaService.getEmprestimosPendentes();
		return ResponseEntity.ok(emprestimos);
	}
	
	@GetMapping("/baixas")
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public ResponseEntity<Page<SaidaDto>> findBaixasDeEstoque(Pageable pageable) {
		Page<SaidaDto> baixas = saidaService.findBaixasDeEstoque(pageable);
		return ResponseEntity.ok(baixas);
	}
}
