package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ENTRADAITEM")
public class EntradaItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private EntradaItemPK idEntradaItem = new EntradaItemPK();

	@ManyToOne
	@JoinColumn(name = "idequipamento", referencedColumnName = "idequipamento")
	@NotNull
	private Equipamento equipamento;

	@Column(nullable = false)
	@Min(1)
	private Integer quantidade;

	@Column(name = "valorunitario", nullable = false)
	@NotNull
	private BigDecimal valorUnitario;

	public EntradaItem() {
	}

	public EntradaItem(EntradaItemPK idEntradaItem, Equipamento equipamento, Integer quantidade,
			BigDecimal valorUnitario) {
		this.idEntradaItem = idEntradaItem;
		this.equipamento = equipamento;
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
	}

	public EntradaItemPK getIdEntradaItem() {
		return idEntradaItem;
	}

	public void setIdEntradaItem(EntradaItemPK idEntradaItem) {
		this.idEntradaItem = idEntradaItem;
	}

	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEntradaItem == null) ? 0 : idEntradaItem.hashCode());
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
		EntradaItem other = (EntradaItem) obj;
		if (idEntradaItem == null) {
			if (other.idEntradaItem != null)
				return false;
		} else if (!idEntradaItem.equals(other.idEntradaItem))
			return false;
		return true;
	}
}
