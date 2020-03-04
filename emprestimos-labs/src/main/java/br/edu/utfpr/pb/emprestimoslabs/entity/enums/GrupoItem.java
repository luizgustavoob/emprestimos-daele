package br.edu.utfpr.pb.emprestimoslabs.entity.enums;

import lombok.Getter;
import lombok.Setter;

public enum GrupoItem {

	CONSUMO(1, "CONSUMO"), PERMANENTE(2, "PERMANENTE");

	@Getter @Setter	
	private Integer idGrupoItem;
	@Getter @Setter
	private String descricao;

	private GrupoItem(Integer idGrupoItem, String descricao) {
		this.idGrupoItem = idGrupoItem;
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
