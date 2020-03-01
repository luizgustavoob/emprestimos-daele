package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CIDADE")
public class Cidade implements Serializable, EntidadeBD {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cidade_id")
	@SequenceGenerator(name = "cidade_id", sequenceName = "cidade_id", allocationSize = 1)
	@Column(name = "idcidade")
	private Long idCidade;

	@Column(nullable = false, length = 50)
	@NotNull
	@Length(min = 1, max = 50)
	private String nome;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "iduf", referencedColumnName = "iduf")
	@Valid
	private UF uf;

	@Column(name = "ibge")
	private Integer codigoIbge;

	public Cidade() {
	}

	public Cidade(Long idCidade, String nome, UF uf, Integer codigoIbge) {
		this.idCidade = idCidade;
		this.nome = nome;
		this.uf = uf;
		this.codigoIbge = codigoIbge;
	}

	@Override
	@JsonIgnore
	public Long getId() {
		return idCidade;
	}

	public Long getIdCidade() {
		return idCidade;
	}

	public void setIdCidade(Long idCidade) {
		this.idCidade = idCidade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public UF getUf() {
		return uf;
	}

	public void setUf(UF uf) {
		this.uf = uf;
	}

	public Integer getCodigoIbge() {
		return codigoIbge;
	}

	public void setCodigoIbge(Integer codigoIbge) {
		this.codigoIbge = codigoIbge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCidade == null) ? 0 : idCidade.hashCode());
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
		Cidade other = (Cidade) obj;
		if (idCidade == null) {
			if (other.idCidade != null)
				return false;
		} else if (!idCidade.equals(other.idCidade))
			return false;
		return true;
	}

}
