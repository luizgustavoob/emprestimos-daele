package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class ChangePasswordForm {

	@Email
	@NotNull
	private String email;
	@NotNull
	private String senhaAtual;
	@NotNull
	private String novaSenha;

	public ChangePasswordForm() {
	}

	public ChangePasswordForm(String email, String senhaAtual, String novaSenha) {
		this.email = email;
		this.senhaAtual = senhaAtual;
		this.novaSenha = novaSenha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

}
