package br.edu.utfpr.pb.emprestimoslabs.email;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.edu.utfpr.pb.emprestimoslabs.config.EmprestimosLabsApiProperty;
import br.edu.utfpr.pb.emprestimoslabs.entity.Saida;
import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private EmprestimosLabsApiProperty config;

	private ExecutorService emailExecutor;
	
	public EmailServiceImpl() {
		this.emailExecutor = Executors.newCachedThreadPool();
	}
	
	private void enviarEmail(String destinatario, String assunto, String mensagem) {
		emailExecutor.execute(new Runnable() {
			@Override
			public void run() {
				if (!config.getEmail().isValid()) {
					return;
				}
				
				try {
					MimeMessage message = mailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
					helper.setFrom(config.getEmail().getSender());
					helper.setTo(destinatario);
					helper.setSubject(assunto);
					helper.setText(mensagem, true);
					mailSender.send(message);
				} catch (MessagingException ex) {
					throw new RuntimeException("Erro ao enviar e-mail: " + ex.getMessage());
				}
			}
		});
	}
	
	@Override
	public void enviarNovaSenha(Usuario usuario, String senha) {
		StringBuilder builder = new StringBuilder();
		builder.append("Olá " + usuario.getNome());
		builder.append(", utilize a senha \"" + senha + "\" para acessar novamente o sistema. ");
		builder.append("Você poderá redefini-la depois.");
		
		this.enviarEmail(usuario.getEmail(), "Recuperação de Senha - DAELE", builder.toString());
	}

	@Override
	public void enviarAprovacaoDeUsuario(Usuario usuario) {
		StringBuilder builder = new StringBuilder();
		builder.append("Olá " + usuario.getNome());
		builder.append(", seu cadastro no portal de empréstimos do DAELE foi aprovado. ");
		builder.append("Bem vindo!");
		
		this.enviarEmail(usuario.getEmail(), "Aprovação de Cadastro - DAELE", builder.toString());
	}
	
	@Override
	public void enviarReprovacaoDeUsuario(Usuario usuario) {
		StringBuilder builder = new StringBuilder();
		builder.append("Olá " + usuario.getNome());
		builder.append(", seu cadastro não foi aprovado no portal de empréstimos do DAELE.");
		
		this.enviarEmail(usuario.getEmail(), "Reprovação de Cadastro - DAELE", builder.toString());
	}

	@Override
	public void enviarAprovacaoDeEmprestimo(Saida saida) {
		StringBuilder builder = new StringBuilder();
		builder.append("Olá " + saida.getUsuario().getNome());
		builder.append(", sua solicitação de empréstimo foi aprovada! Dirija-se ao departamento para retirada dos equipamentos.");

		this.enviarEmail(saida.getUsuario().getEmail(), "Aprovação de empréstimo - DAELE", builder.toString());
	}

	@Override
	public void enviarReprovacaoDeEmprestimo(Saida saida) {
		StringBuilder builder = new StringBuilder();
		builder.append("Olá " + saida.getUsuario().getNome());
		builder.append(", sua solicitação de empréstimo infelizmente não pode ser aprovada devido a falta dos equipamentos solicitados.");

		this.enviarEmail(saida.getUsuario().getEmail(), "Aprovação de empréstimo - DAELE", builder.toString());
	}
}
