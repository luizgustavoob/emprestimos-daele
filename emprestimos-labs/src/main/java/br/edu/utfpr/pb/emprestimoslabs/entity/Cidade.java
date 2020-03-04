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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CIDADE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idCidade"})
public class Cidade implements Serializable, EntidadeBD {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cidade_id")
	@SequenceGenerator(name = "cidade_id", sequenceName = "cidade_id", allocationSize = 1)
	@Column(name = "idcidade")
	private Long idCidade;

	@Column(nullable = false, length = 50)
	@NotNull @NotBlank @Length(min = 1, max = 50)
	private String nome;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "iduf", referencedColumnName = "iduf")
	@Valid
	private UF uf;

	@Column(name = "ibge")
	private Integer codigoIbge;

	@Override
	@JsonIgnore
	public Long getId() {
		return idCidade;
	}
}
