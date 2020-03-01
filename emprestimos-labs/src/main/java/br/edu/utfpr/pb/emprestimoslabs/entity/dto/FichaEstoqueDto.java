package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import java.time.LocalDate;

public class FichaEstoqueDto {

	private LocalDate data;
	private Long idequipamento;
	private String nome;
	private Long patrimonio;
	private Integer saldo;

	public FichaEstoqueDto() {
	}
	
	public FichaEstoqueDto(LocalDate data, Long idequipamento, String nome, Long patrimonio, Integer saldo) {
		this.data = data;
		this.idequipamento = idequipamento;
		this.nome = nome;
		this.patrimonio = patrimonio;
		this.saldo = saldo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Long getIdequipamento() {
		return idequipamento;
	}

	public void setIdequipamento(Long idequipamento) {
		this.idequipamento = idequipamento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getPatrimonio() {
		return patrimonio;
	}

	public void setPatrimonio(Long patrimonio) {
		this.patrimonio = patrimonio;
	}

	public Integer getSaldo() {
		return saldo;
	}

	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}
}
