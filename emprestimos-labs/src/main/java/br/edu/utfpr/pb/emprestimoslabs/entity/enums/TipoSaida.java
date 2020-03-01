package br.edu.utfpr.pb.emprestimoslabs.entity.enums;

public enum TipoSaida {

	EMPRESTIMO(1, "EMPRESTIMO"), BAIXA(2, "BAIXA");

	private Integer idTipoSaida;
	private String descricao;

	private TipoSaida(Integer idTipoSaida, String descricao) {
		this.idTipoSaida = idTipoSaida;
		this.descricao = descricao;
	}

	public Integer getIdTipoSaida() {
		return idTipoSaida;
	}

	public void setIdTipoSaida(Integer idTipoSaida) {
		this.idTipoSaida = idTipoSaida;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static TipoSaida toEnum(Integer idTipoSaida) {
		if (idTipoSaida == 0) {
			return null;
		}

		for (TipoSaida tipo : TipoSaida.values()) {
			if (tipo.getIdTipoSaida() == idTipoSaida) {
				return tipo;
			}
		}

		throw new IllegalArgumentException("Tipo de saída inválido");
	}
}
