package br.edu.utfpr.pb.emprestimoslabs.entity.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import br.edu.utfpr.pb.emprestimoslabs.entity.enums.SituacaoSaida;

public class SaidaFiltro {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	private Integer nrora;
	private SituacaoSaida[] situacao;

	public SaidaFiltro() {
	}

	public SaidaFiltro(LocalDate data, Integer nrora, SituacaoSaida[] situacao) {
		this.data = data;
		this.nrora = nrora;
		this.situacao = situacao;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Integer getNrora() {
		return nrora;
	}

	public void setNrora(Integer nrora) {
		this.nrora = nrora;
	}

	public SituacaoSaida[] getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoSaida[] situacao) {
		this.situacao = situacao;
	}

}
