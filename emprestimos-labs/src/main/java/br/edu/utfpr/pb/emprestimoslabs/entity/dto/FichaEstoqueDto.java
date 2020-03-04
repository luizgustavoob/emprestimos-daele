package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichaEstoqueDto {

	private LocalDate data;
	private Long idequipamento;
	private String nome;
	private Long patrimonio;
	private Integer saldo;

}
