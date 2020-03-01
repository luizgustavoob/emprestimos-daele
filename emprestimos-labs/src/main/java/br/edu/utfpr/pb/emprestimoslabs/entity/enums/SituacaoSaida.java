package br.edu.utfpr.pb.emprestimoslabs.entity.enums;

public enum SituacaoSaida {

	PENDENTE(1, "PENDENTE"), APROVADA(2, "APROVADA"), ENCERRADA(3, "ENCERRADA"), REPROVADA(4, "REPROVADA");

	private Integer idSituacaoSaida;
	private String descricao;

	private SituacaoSaida(Integer idSituacaoSaida, String descricao) {
		this.idSituacaoSaida = idSituacaoSaida;
		this.descricao = descricao;
	}

	public Integer getIdSituacaoSaida() {
		return idSituacaoSaida;
	}

	public void setIdSituacaoSaida(Integer idSituacaoSaida) {
		this.idSituacaoSaida = idSituacaoSaida;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static SituacaoSaida toEnum(Integer idSituacaoSaida) {
		if (idSituacaoSaida == 0) {
			return null;
		}

		for (SituacaoSaida situacao : SituacaoSaida.values()) {
			if (situacao.getIdSituacaoSaida() == idSituacaoSaida) {
				return situacao;
			}
		}

		throw new IllegalArgumentException("Situação de saída inválida");
	}

	public static SituacaoSaida toEnum(String descricao) {
		if (descricao.isEmpty()) {
			return null;
		}

		for (SituacaoSaida situacao : SituacaoSaida.values()) {
			if (situacao.getDescricao().equals(descricao)) {
				return situacao;
			}
		}

		throw new IllegalArgumentException("Situação de saída inválida");
	}
}
