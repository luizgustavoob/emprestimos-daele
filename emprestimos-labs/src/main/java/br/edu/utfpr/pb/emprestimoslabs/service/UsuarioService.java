package br.edu.utfpr.pb.emprestimoslabs.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.edu.utfpr.pb.emprestimoslabs.controller.exception.EmailNotFoundException;
import br.edu.utfpr.pb.emprestimoslabs.controller.exception.InactiveUserException;
import br.edu.utfpr.pb.emprestimoslabs.controller.exception.IncorrectPasswordException;
import br.edu.utfpr.pb.emprestimoslabs.data.UsuarioData;
import br.edu.utfpr.pb.emprestimoslabs.email.EmailService;
import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.ChangePasswordForm;
import br.edu.utfpr.pb.emprestimoslabs.entity.dto.UsuarioDto;
import br.edu.utfpr.pb.emprestimoslabs.entity.filter.UsuarioFiltro;
import br.edu.utfpr.pb.emprestimoslabs.security.util.UsuarioAutenticado;
import br.edu.utfpr.pb.emprestimoslabs.service.generic.CrudServiceImpl;
import br.edu.utfpr.pb.emprestimoslabs.websocket.Queue;

@Service
public class UsuarioService extends CrudServiceImpl<Usuario, Long> implements UserDetailsService {

	@Autowired
	private UsuarioData usuarioData;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private EmailService emailService;
	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private SimpMessagingTemplate message;
	
	@Override
	protected JpaRepository<Usuario, Long> getData() {
		return usuarioData;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Usuario update(Long id, Usuario usuario) {
		Usuario usuarioAtual = usuarioData.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(usuario, usuarioAtual, "idUsuario");
		usuarioAtual.setDataInativacao(usuarioAtual.isAtivo() ? null : LocalDate.now());
		return usuarioData.save(usuarioAtual);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioData.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
		if (!usuario.isAtivo() || !usuario.isEnabled()) {
			throw new InactiveUserException("Erro: ");
		}
		return usuario;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateStatus(Long id, boolean ativo) {
		Usuario usuario = usuarioData.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		if (usuario.isAtivo() == ativo) {
			return;
		}
		
		boolean enviarEmailAprovacao = (usuario.isAluno() || usuario.isProfessor())
				&& ativo 
				&& !usuario.isAtivo() 
				&& usuario.getDataInativacao() == null;
		
		boolean enviarEmailReprovacao = 
				(usuario.isAluno() || usuario.isProfessor())
				&& !usuario.isAtivo() 
				&& !ativo;
		
		usuario.setAtivo(ativo);
		usuario.setDataInativacao(ativo ? null : LocalDate.now());
		usuarioData.save(usuario);
		
		if (enviarEmailAprovacao) {
			emailService.enviarAprovacaoDeUsuario(usuario);
		} else if (enviarEmailReprovacao) {
			emailService.enviarReprovacaoDeUsuario(usuario);
		}
		
		if (!ativo) {
			message.convertAndSend(String.format(Queue.NOTIFICA_USUARIO, usuario.getEmail()), "logout");
		}
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<UsuarioDto> findUsuariosPendentes() {
		return usuarioData.findUsuariosPendentes()
				.stream()
				.map((usuario) -> new UsuarioDto(usuario))
				.collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<UsuarioDto> findByNroraOrEmail(String nroRAOrEmail) {
		return usuarioData.findUsuariosByNroRAOrEmail(nroRAOrEmail, nroRAOrEmail)
				.stream()
				.map((usuario) -> new UsuarioDto(usuario))
				.collect(Collectors.toList());
	}

	public void generateNewPassword(String email) {
		Usuario usuario = usuarioData.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Erro no envio. E-mail não encontrado"));
		if (!usuario.isEnabled()) {
			throw new InactiveUserException("Não foi possível enviar o e-mail. ");
		}
		String novaSenha = generateNewPassword();
		usuario.setSenha(encoder.encode(novaSenha));
		usuarioData.save(usuario);
		emailService.enviarNovaSenha(usuario, novaSenha);
	}

	private String generateNewPassword() {
		char[] senha = new char[10];
		for (int i = 0; i < 10; i++) {
			senha[i] = randomChar();
		}
		return new String(senha);
	}
	
	private char randomChar() {
		Random random = new Random();
		int option = random.nextInt(3);
		if (option == 0) { //gera um digito
			return (char) (random.nextInt(10) + 48);
		} else if (option == 1) { // gera um caractere maiusculo
			return (char) (random.nextInt(26) + 65);
		} else { //gera um caractere minusculo
			return (char) (random.nextInt(26) + 97);
		}
	}

	public void changePassword(ChangePasswordForm changePasswordForm) {
		Usuario usuario = usuarioData.findByEmail(changePasswordForm.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
		
		if (!encoder.matches(changePasswordForm.getSenhaAtual(), usuario.getPassword())) {
			throw new IncorrectPasswordException();
		}
		
		usuario.setSenha(encoder.encode(changePasswordForm.getNovaSenha()));
		usuarioData.save(usuario);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Page<UsuarioDto> findByFiltros(UsuarioFiltro usuarioFiltro, Pageable pageable) {
		String qry = "SELECT u FROM Usuario u WHERE u <> :usuarioLogado";
		
		if (usuarioFiltro.getEmail() != null && !usuarioFiltro.getEmail().isEmpty()) {
			qry += " AND u.email = :email";
		}
		
		qry += " ORDER BY u.nome ";
		
		TypedQuery<Usuario> query = manager.createQuery(qry, Usuario.class);
		
		query.setParameter("usuarioLogado", UsuarioAutenticado.get());
		
		if (qry.contains(":email")) {
			query.setParameter("email", usuarioFiltro.getEmail());
		}
		
		List<Usuario> usuarios = query.getResultList();
		
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		List<UsuarioDto> usuariosDtoComPaginacao = query
				.setFirstResult(primeiroRegistroDaPagina)
				.setMaxResults(totalRegistrosPorPagina)
				.getResultList()
				.stream()
				.map(usuario -> new UsuarioDto(usuario))
				.collect(Collectors.toList());
		
		return new PageImpl<UsuarioDto>(usuariosDtoComPaginacao, pageable, usuarios.size());
	}
}
