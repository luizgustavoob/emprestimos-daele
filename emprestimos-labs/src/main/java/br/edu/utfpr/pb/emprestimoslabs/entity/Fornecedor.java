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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FORNECEDOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idFornecedor"})
public class Fornecedor implements Serializable, EntidadeBD {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fornecedor_id")
	@SequenceGenerator(name = "fornecedor_id", sequenceName = "fornecedor_id", allocationSize = 1)
	@Column(name = "idfornecedor")
	private Long idFornecedor;

	@Column(name = "razaosocial", nullable = false, length = 100)
	@NotNull @NotBlank @Length(min = 5, max = 100)
	private String razaoSocial;

	@Column(name = "fantasia", length = 50)
	@Length(max = 50)
	private String nomeFantasia;

	@Column(nullable = false, unique = true, length = 14)
	@NotNull @NotBlank @Length(min = 1, max = 14)
	@CNPJ
	private String cnpj;

	@Embedded
	@Valid
	private Endereco endereco;

	@Override
	@JsonIgnore
	public Long getId() {
		return idFornecedor;
	}

}
