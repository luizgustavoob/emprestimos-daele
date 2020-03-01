package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ENTRADA")
public class Entrada implements Serializable, EntidadeBD {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entrada_id")
	@SequenceGenerator(name = "entrada_id", sequenceName = "entrada_id", allocationSize = 1)
	@Column(name = "identrada")
	private Long idEntrada;

	@NotNull
	@Column(name = "dataentrada", nullable = false)
	private LocalDate data;

	@ManyToOne
	@JoinColumn(name = "idfornecedor", referencedColumnName = "idfornecedor")
	@NotNull
	private Fornecedor fornecedor;

	@ManyToOne
	@JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
	private Usuario usuario;

	@OneToMany(mappedBy = "idEntradaItem.entrada")
	private List<EntradaItem> itens = new ArrayList<>();

	@Override
	@JsonIgnore
	public Long getId() {
		return idEntrada;
	}

	public Entrada() {}

	public Entrada(Long idEntrada, LocalDate data, Fornecedor fornecedor, Usuario usuario, List<EntradaItem> itens) {
		this.idEntrada = idEntrada;
		this.data = data;
		this.fornecedor = fornecedor;
		this.usuario = usuario;
		this.itens = itens;
	}

	public Long getIdEntrada() {
		return idEntrada;
	}

	public void setIdEntrada(Long idEntrada) {
		this.idEntrada = idEntrada;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<EntradaItem> getItens() {
		return itens;
	}

	public void setItens(List<EntradaItem> itens) {
		this.itens = itens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEntrada == null) ? 0 : idEntrada.hashCode());
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
		Entrada other = (Entrada) obj;
		if (idEntrada == null) {
			if (other.idEntrada != null)
				return false;
		} else if (!idEntrada.equals(other.idEntrada))
			return false;
		return true;
	}
}
