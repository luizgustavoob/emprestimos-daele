package br.edu.utfpr.pb.emprestimoslabs.entity.enums;

import lombok.Getter;
import lombok.Setter;

public enum TipoSaida {

	EMPRESTIMO(1, "EMPRESTIMO"), BAIXA(2, "BAIXA");

	@Getter @Setter
	private Integer idTipoSaida;
	@Getter @Setter
	private String descricao;

	private TipoSaida(Integer idTipoSaida, String descricao) {
		this.idTipoSaida = idTipoSaida;
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
