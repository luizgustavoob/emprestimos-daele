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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.pb.emprestimoslabs.controller.exception.InactiveUserException;
import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.ChangePasswordForm;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.EmailForm;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.UsuarioDto;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.UsuarioForm;
import br.edu.utfpr.pb.emprestimoslabs.entity.filter.UsuarioFiltro;
import br.edu.utfpr.pb.emprestimoslabs.event.NovoRecurso;
import br.edu.utfpr.pb.emprestimoslabs.service.UsuarioService;
import br.edu.utfpr.pb.emprestimoslabs.websocket.Queue;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private SimpMessagingTemplate message;
	
	@GetMapping(value = { "", "/" })
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public ResponseEntity<List<UsuarioDto>> findAll() {
		List<Usuario> usuarios = usuarioService.findAll();
		List<UsuarioDto> usuariosDto = usuarios.stream()
				.map(usuario -> new UsuarioDto(usuario)).collect(Collectors.toList());
		return ResponseEntity.ok().body(usuariosDto);
	}
	
	@GetMapping("/page")
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public Page<UsuarioDto> findAllPaginated(Pageable pageable) {
		Page<Usuario> ususarios = usuarioService.findAllPaginated(pageable);
		Page<UsuarioDto> usuariosDto = ususarios.map(usuario -> new UsuarioDto(usuario));
		return usuariosDto;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDto> findById(@PathVariable("id") Long id) {
		Usuario usuario = usuarioService.findById(id);
		return ResponseEntity.ok().body(new UsuarioDto(usuario));
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDto> insert(@RequestBody @Valid UsuarioForm usuarioForm, HttpServletResponse response) {
		Usuario usuario = usuarioForm.toEntity(passwordEncoder);
		usuario = usuarioService.save(usuario);
		publisher.publishEvent(new NovoRecurso(this, response, usuario.getId()));
		message.convertAndSend(Queue.NOVOS_USUARIOS, "update");
		return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioDto(usuario));
	}
		
	@PatchMapping("/{id}/ativar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateStatus(@PathVariable("id") Long id, @RequestBody @NotNull boolean ativo) {
		usuarioService.updateStatus(id, ativo);
		message.convertAndSend(Queue.NOVOS_USUARIOS, "update");
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@Valid @RequestBody EmailForm emailForm) {
		usuarioService.generateNewPassword(emailForm.getEmail());
		return ResponseEntity.noContent().build();
	}
		
	@PostMapping("/change-password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void changePassword(@Valid @RequestBody ChangePasswordForm changePasswordForm) {
		usuarioService.changePassword(changePasswordForm);
	}
	
	@GetMapping("/usuarios-pendentes")
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public ResponseEntity<List<UsuarioDto>> findUsuariosPendentes() {
		List<UsuarioDto> users = usuarioService.findUsuariosPendentes();
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/search")
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public ResponseEntity<List<UsuarioDto>> findByNroraOrEmail(@RequestParam("chavePesquisa") String nroRAOrEmail) {
		List<UsuarioDto> users = usuarioService.findByNroraOrEmail(nroRAOrEmail);
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/filter")
	@PreAuthorize("hasRole('ADMIN') or hasRole('LABORATORISTA')")
	public ResponseEntity<Page<UsuarioDto>> findByFiltros(UsuarioFiltro usuarioFiltro, Pageable pageable) {
		Page<UsuarioDto> usuarios = usuarioService.findByFiltros(usuarioFiltro, pageable);
		return ResponseEntity.ok(usuarios);
	}
	
	@GetMapping("/email/{email}")
	public boolean emailExiste(@PathVariable("email") String email) {
		boolean existe;
		try {
			existe = usuarioService.loadUserByUsername(email) != null;
		} catch(UsernameNotFoundException ex) {
			existe = false;
		} catch (InactiveUserException ex) {
			existe = true;
		}
		return existe;
	}

}