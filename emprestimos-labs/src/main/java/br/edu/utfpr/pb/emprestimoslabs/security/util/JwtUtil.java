package br.edu.utfpr.pb.emprestimoslabs.security.util;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import br.edu.utfpr.pb.emprestimoslabs.config.EmprestimosLabsApiProperty;
import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	@Autowired
	private EmprestimosLabsApiProperty config;

	public String gerarToken(Usuario usuario) {
		return Jwts.builder().setIssuer("API Empréstimos Labs")
				.setSubject(usuario.getUsername())
				.claim("authorities", usuario.getAuthorities()
						.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.claim("userName", usuario.getNome())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + config.getSeguranca().getJwtExpiration()))
				.signWith(SignatureAlgorithm.HS512, config.getSeguranca().getJwtSecret().getBytes())
				.compact();
	}

	public boolean isTokenValido(String token) {
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(config.getSeguranca().getJwtSecret().getBytes())
					.parseClaimsJws(token)
					.getBody();
			String username = claims.getSubject();
			Date dataExpira = claims.getExpiration();
			Date agora = new Date(System.currentTimeMillis());
			return (username != null && dataExpira != null && agora.before(dataExpira));
		} catch (Exception ex) {
			return false;
		}
	}

	public String getUsernameByToken(String token) {
		try {
			return Jwts.parser()
					.setSigningKey(config.getSeguranca().getJwtSecret().getBytes())
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
		} catch (Exception ex) {
			return null;
		}
	}

}
