package br.edu.utfpr.pb.emprestimoslabs.controller.exception;

public class IncorrectPasswordException extends RuntimeException {

	public IncorrectPasswordException() {
		super("Senha incorreta.");
	}
}
