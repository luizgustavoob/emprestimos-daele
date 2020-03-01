package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "UF")
public class UF implements Serializable, EntidadeBD {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uf_id")
	@SequenceGenerator(name = "uf_id", sequenceName = "uf_id", allocationSize = 1)
	@Column(name = "iduf")
	private Long idUf;

	@Column(nullable = false, unique = true)
	@NotNull
	@Length(min = 2, max = 2)
	private String uf;

	@Column(nullable = false, unique = true, length = 50)
	@NotNull
	@Length(min = 1, max = 50)
	private String nome;

	@Column(name = "codigouf", nullable = false, unique = true)
	@NotNull
	private Long codigoUf;

	public UF() {
	}

	@Override
	@JsonIgnore
	public Long getId() {
		return idUf;
	}

	public Long getIdUf() {
		return idUf;
	}

	public void setIdUf(Long idUf) {
		this.idUf = idUf;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getCodigoUf() {
		return codigoUf;
	}

	public void setCodigoUf(Long codigoUf) {
		this.codigoUf = codigoUf;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idUf == null) ? 0 : idUf.hashCode());
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
		UF other = (UF) obj;
		if (idUf == null) {
			if (other.idUf != null)
				return false;
		} else if (!idUf.equals(other.idUf))
			return false;
		return true;
	}

}
