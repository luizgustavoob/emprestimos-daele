package br.edu.utfpr.pb.emprestimoslabs.controller.exception;

public class TokenExpiredException extends RuntimeException {

	public TokenExpiredException() {
		super("Token do usuário expirou.");
	}
}
