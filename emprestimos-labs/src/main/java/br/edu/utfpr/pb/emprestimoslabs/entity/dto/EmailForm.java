package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class EmailForm {
	
	@Email @NotNull
	private String email;

	public EmailForm() {
	}
	
	public EmailForm(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
