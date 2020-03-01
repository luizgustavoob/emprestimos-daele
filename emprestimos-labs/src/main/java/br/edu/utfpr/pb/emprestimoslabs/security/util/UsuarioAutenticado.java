package br.edu.utfpr.pb.emprestimoslabs.security.util;

import org.springframework.security.core.context.SecurityContextHolder;

import br.edu.utfpr.pb.emprestimoslabs.controller.exception.TokenExpiredException;
import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;

public class UsuarioAutenticado {
	
	public static Usuario get() {
		try {
			Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return usuario;
		} catch (Exception ex) {
			throw new TokenExpiredException();
		}
	}
}