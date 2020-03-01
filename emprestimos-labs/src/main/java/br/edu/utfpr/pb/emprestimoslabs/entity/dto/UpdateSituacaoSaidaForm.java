package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import javax.validation.constraints.NotNull;

public class UpdateSituacaoSaidaForm {

	@NotNull
	private String situacao;

	public UpdateSituacaoSaidaForm() {
	}

	public UpdateSituacaoSaidaForm(@NotNull String situacao) {
		super();
		this.situacao = situacao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

}
