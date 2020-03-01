package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "SAIDAITEM")
public class SaidaItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SaidaItemPK idSaidaItem = new SaidaItemPK();

	@NotNull
	@Min(1)
	@Column(nullable = false)
	private Integer quantidade;

	@Column(name = "quantidadedevolvida")
	private Integer quantidadeDevolvida;

	@Column(name = "datadevolucao")
	private LocalDate dataDevolucao;

	public SaidaItem() {
		this.quantidade = 0;
		this.quantidadeDevolvida = 0;
	}

	public SaidaItemPK getIdSaidaItem() {
		return idSaidaItem;
	}

	public void setIdSaidaItem(SaidaItemPK idSaidaItem) {
		this.idSaidaItem = idSaidaItem;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getQuantidadeDevolvida() {
		return quantidadeDevolvida;
	}

	public void setQuantidadeDevolvida(Integer quantidadeDevolvida) {
		this.quantidadeDevolvida = quantidadeDevolvida;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idSaidaItem == null) ? 0 : idSaidaItem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaidaItem other = (SaidaItem) obj;
		if (idSaidaItem == null) {
			if (other.idSaidaItem != null)
				return false;
		} else if (!idSaidaItem.equals(other.idSaidaItem))
			return false;
		return true;
	}
}
