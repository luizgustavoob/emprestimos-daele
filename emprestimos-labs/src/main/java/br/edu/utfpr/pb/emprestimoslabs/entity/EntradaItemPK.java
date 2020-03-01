package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Embeddable
public class EntradaItemPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "identrada", foreignKey = @ForeignKey(name = "fk_entradaitem_entrada"))
	@JsonIgnore
	private Entrada entrada;

	@Column(name = "identradaitem", nullable = false)
	private Long idEntradaItem;

	public EntradaItemPK() {}

	public EntradaItemPK(Entrada entrada, Long idEntradaItem) {
		this.entrada = entrada;
		this.idEntradaItem = idEntradaItem;
	}

	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public Long getIdEntradaItem() {
		return idEntradaItem;
	}

	public void setIdEntradaItem(Long idEntradaItem) {
		this.idEntradaItem = idEntradaItem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entrada == null) ? 0 : entrada.hashCode());
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
		EntradaItemPK other = (EntradaItemPK) obj;
		if (entrada == null) {
			if (other.entrada != null)
				return false;
		} else if (!entrada.equals(other.entrada))
			return false;
		if (idEntradaItem == null) {
			if (other.idEntradaItem != null)
				return false;
		} else if (!idEntradaItem.equals(other.idEntradaItem))
			return false;
		return true;
	}
}