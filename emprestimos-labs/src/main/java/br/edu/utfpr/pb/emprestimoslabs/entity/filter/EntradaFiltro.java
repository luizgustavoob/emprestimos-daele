package br.edu.utfpr.pb.emprestimoslabs.entity.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class EntradaFiltro {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	private String fornecedor;

	public EntradaFiltro() {
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

}
