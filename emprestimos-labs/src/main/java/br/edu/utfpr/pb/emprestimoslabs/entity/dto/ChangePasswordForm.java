package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordForm {

	@Email @NotNull @NotBlank
	private String email;
	@NotNull @NotBlank
	private String senhaAtual;
	@NotNull @NotBlank
	private String novaSenha;

}
