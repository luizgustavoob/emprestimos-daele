package br.edu.utfpr.pb.emprestimoslabs.entity.enums;

import lombok.Getter;
import lombok.Setter;

public enum Permissao {

	ALUNO(1, "ROLE_ALUNO"), 
	LABORATORISTA(2, "ROLE_LABORATORISTA"), 
	ADMIN(3, "ROLE_ADMIN"),
	PROFESSOR(4, "ROLE_PROFESSOR");

	@Getter @Setter
	private Integer idPerfil;
	@Getter @Setter
	private String descricao;

	private Permissao(Integer idPerfil, String descricao) {
		this.idPerfil = idPerfil;
		this.descricao = descricao;
	}

	public static Permissao toEnum(Integer idPerfil) {
		if (idPerfil == 0) {
			return null;
		}

		for (Permissao perfil : Permissao.values()) {
			if (idPerfil == perfil.getIdPerfil()) {
				return perfil;
			}
		}

		throw new IllegalArgumentException("Permissão inválida");
	}
}
