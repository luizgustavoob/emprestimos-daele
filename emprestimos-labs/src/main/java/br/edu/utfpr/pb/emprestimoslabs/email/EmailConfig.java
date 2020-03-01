package br.edu.utfpr.pb.emprestimoslabs.email;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import br.edu.utfpr.pb.emprestimoslabs.config.EmprestimosLabsApiProperty;

@Configuration
public class EmailConfig {
	
	@Autowired
	private EmprestimosLabsApiProperty config;

	@Bean
	public JavaMailSender javaMailSender() {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.connectiontimeout", 10000);
		
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setJavaMailProperties(props);
		sender.setHost(config.getEmail().getHost());
		sender.setPort(config.getEmail().getPort());
		sender.setUsername(config.getEmail().getUsername());
		sender.setPassword(config.getEmail().getPassword());
		
		return sender;
	}

}
