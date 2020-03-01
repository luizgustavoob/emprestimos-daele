package br.edu.utfpr.pb.emprestimoslabs.security.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.utfpr.pb.emprestimoslabs.controller.exception.InactiveUserException;
import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;
import br.edu.utfpr.pb.emprestimoslabs.security.util.JwtUtil;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager manager;
	private JwtUtil jwtUtil;

	public AuthenticationFilter(AuthenticationManager manager, JwtUtil jwtUtil) {
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth/**", "POST"));
		this.manager = manager;
		this.jwtUtil = jwtUtil;
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			Credenciais credenciais = new ObjectMapper().readValue(request.getInputStream(), Credenciais.class);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					credenciais.getUsername(), credenciais.getPassword());
			return manager.authenticate(authentication);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		Usuario usuario = (Usuario) auth.getPrincipal();
		String token = jwtUtil.gerarToken(usuario);
		response.addHeader("Authorization", token);
	}

	public static class Credenciais {

		private String username;
		private String password;
		
		public Credenciais() {}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
	
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException exception) throws IOException, ServletException {
			String mensagem = "";
			
			if (exception.getCause() instanceof InactiveUserException) {
				mensagem = exception.getMessage();
			} else {
				mensagem = "E-mail ou senha inválido(s)";
			}
			
			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().append(json(mensagem));
		}

		private String json(String mensagem) {
			long date = new Date().getTime();
			return "[{\"timestamp\": " + date + ", " + "\"status\": 401, " + "\"error\": \"Não autorizado\", "
					+ "\"mensagemUsuario\": \"" + mensagem + "\"}]";
		}
	}

}
