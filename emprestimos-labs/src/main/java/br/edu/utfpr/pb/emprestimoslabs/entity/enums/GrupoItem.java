package br.edu.utfpr.pb.emprestimoslabs.entity.enums;

public enum GrupoItem {

	CONSUMO(1, "CONSUMO"), PERMANENTE(2, "PERMANENTE");

	private Integer idGrupoItem;
	private String descricao;

	private GrupoItem(Integer idGrupoItem, String descricao) {
		this.idGrupoItem = idGrupoItem;
		this.descricao = descricao;
	}

	public Integer getIdGrupoItem() {
		return idGrupoItem;
	}

	public void setIdGrupoItem(Integer idGrupoItem) {
		this.idGrupoItem = idGrupoItem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static GrupoItem toEnum(Integer idGrupoItem) {
		if (idGrupoItem == 0) {
			return null;
		}

		for (GrupoItem grupo : GrupoItem.values()) {
			if (grupo.getIdGrupoItem() == idGrupoItem) {
				return grupo;
			}
		}

		throw new IllegalArgumentException("Grupo de item inválido");
	}
}
