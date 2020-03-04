package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 100)
	@NotNull @NotBlank @Length(min = 1, max = 100)
	private String logradouro;

	@Column(nullable = false, length = 5)
	@NotNull @NotBlank @Length(min = 1, max = 5)
	private String numero;

	@Column(length = 50)
	@Length(min = 0, max = 50)
	private String complemento;

	@Column(nullable = false, length = 50)
	@NotNull @NotBlank @Length(min = 1, max = 50)
	private String bairro;

	@Column(nullable = false, length = 8)
	@NotNull @NotBlank @Length(min = 1, max = 8)
	private String cep;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idcidade", referencedColumnName = "idcidade")
	@Valid
	private Cidade cidade;

}
