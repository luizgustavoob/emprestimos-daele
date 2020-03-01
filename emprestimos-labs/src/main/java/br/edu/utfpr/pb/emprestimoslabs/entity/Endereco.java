package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Embeddable
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 100)
	@NotNull
	@Length(min = 1, max = 100)
	private String logradouro;

	@Column(nullable = false, length = 5)
	@NotNull
	@Length(min = 1, max = 5)
	private String numero;

	@Column(length = 50)
	@Length(min = 0, max = 50)
	private String complemento;

	@Column(nullable = false, length = 50)
	@NotNull
	@Length(min = 1, max = 50)
	private String bairro;

	@Column(nullable = false, length = 8)
	@NotNull
	@Length(min = 1, max = 8)
	private String cep;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idcidade", referencedColumnName = "idcidade")
	@Valid
	private Cidade cidade;

	public Endereco() {
	}

	public Endereco(String logradouro, String numero, String complemento, String bairro, String cep, Cidade cidade) {
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.cidade = cidade;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

}
