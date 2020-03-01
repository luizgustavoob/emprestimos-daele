package br.edu.utfpr.pb.emprestimoslabs.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import br.edu.utfpr.pb.emprestimoslabs.security.util.JwtUtil;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	private UserDetailsService userDetailsService;
	private JwtUtil jwtUtil;

	public AuthorizationFilter(AuthenticationManager manager, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
		super(manager);
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			autenticarToken(token);
		}

		chain.doFilter(request, response);
	}

	private void autenticarToken(String token) {
		if (!jwtUtil.isTokenValido(token)) {
			return;
		}

		String email = jwtUtil.getUsernameByToken(token);
		UserDetails usuario = userDetailsService.loadUserByUsername(email);
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
