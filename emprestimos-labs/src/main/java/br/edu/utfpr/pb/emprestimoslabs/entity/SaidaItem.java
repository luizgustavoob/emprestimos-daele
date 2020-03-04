package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "SAIDAITEM")
@Data
@EqualsAndHashCode(of = {"idSaidaItem"})
public class SaidaItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SaidaItemPK idSaidaItem = new SaidaItemPK();

	@Column(nullable = false)
	@NotNull @Min(1)
	private Integer quantidade;

	@Column(name = "quantidadedevolvida")
	private Integer quantidadeDevolvida;

	@Column(name = "datadevolucao")
	private LocalDate dataDevolucao;

	public SaidaItem() {
		this.quantidade = 0;
		this.quantidadeDevolvida = 0;
	}
	
}
