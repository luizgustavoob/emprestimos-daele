package br.edu.utfpr.pb.emprestimoslabs.entity.enums;

public enum Permissao {

	ALUNO(1, "ROLE_ALUNO"), LABORATORISTA(2, "ROLE_LABORATORISTA"), ADMIN(3, "ROLE_ADMIN"),
	PROFESSOR(4, "ROLE_PROFESSOR");

	private Integer idPerfil;
	private String descricao;

	private Permissao(Integer idPerfil, String descricao) {
		this.idPerfil = idPerfil;
		this.descricao = descricao;
	}

	public Integer getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
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
