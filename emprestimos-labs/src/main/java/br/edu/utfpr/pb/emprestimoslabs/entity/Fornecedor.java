package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "FORNECEDOR")
public class Fornecedor implements Serializable, EntidadeBD {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fornecedor_id")
	@SequenceGenerator(name = "fornecedor_id", sequenceName = "fornecedor_id", allocationSize = 1)
	@Column(name = "idfornecedor")
	private Long idFornecedor;

	@Column(name = "razaosocial", nullable = false, length = 100)
	@NotNull
	@Length(min = 5, max = 100)
	private String razaoSocial;

	@Column(name = "fantasia", length = 50)
	@Length(max = 50)
	private String nomeFantasia;

	@Column(nullable = false, unique = true, length = 14)
	@NotNull
	@Length(min = 1, max = 14)
	@CNPJ
	private String cnpj;

	@Embedded
	@Valid
	private Endereco endereco;

	public Fornecedor() {
	}

	@Override
	@JsonIgnore
	public Long getId() {
		return idFornecedor;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
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
		Fornecedor other = (Fornecedor) obj;
		if (idFornecedor == null) {
			if (other.idFornecedor != null)
				return false;
		} else if (!idFornecedor.equals(other.idFornecedor))
			return false;
		return true;
	}

}
