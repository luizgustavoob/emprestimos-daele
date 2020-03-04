package br.edu.utfpr.pb.emprestimoslabs.entity.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EquipamentoFiltro {

	private Long patrimonio;
	private String nome;
	private String localizacao;

}
