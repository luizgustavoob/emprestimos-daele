package br.edu.utfpr.pb.emprestimoslabs.controller.exception;

public class InactiveUserException extends RuntimeException {
	
	public InactiveUserException(String message) {
		super(message + "Usuário está inativo.");
	}
}
