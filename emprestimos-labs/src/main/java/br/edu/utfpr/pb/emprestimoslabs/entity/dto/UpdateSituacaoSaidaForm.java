package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSituacaoSaidaForm {

	@NotNull @NotBlank
	private String situacao;

}
