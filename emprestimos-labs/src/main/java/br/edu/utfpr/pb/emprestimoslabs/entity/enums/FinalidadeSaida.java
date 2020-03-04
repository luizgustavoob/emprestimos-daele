package br.edu.utfpr.pb.emprestimoslabs.entity.enums;

import lombok.Getter;
import lombok.Setter;

public enum FinalidadeSaida {

	PROJETO(1, "PROJETO"), AULA_PRATICA(2, "AULA_PRATICA");
	
	@Getter @Setter
	private Integer idFinalidadeSaida;
	@Getter @Setter
	private String descricao;

	private FinalidadeSaida(Integer idFinalidadeSaida, String descricao) {
		this.idFinalidadeSaida = idFinalidadeSaida;
		this.descricao = descricao;
	}

	public static FinalidadeSaida toEnum(Integer idFinalidadeSaida) {
		if (idFinalidadeSaida == 0) {
			return null;
		}

		for (FinalidadeSaida finalidade : FinalidadeSaida.values()) {
			if (finalidade.getIdFinalidadeSaida() == idFinalidadeSaida) {
				return finalidade;
			}
		}

		throw new IllegalArgumentException("Finalidade de saída inválida");
	}
}
