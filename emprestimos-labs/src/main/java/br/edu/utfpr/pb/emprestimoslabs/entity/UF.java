package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UF")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idUf"})
public class UF implements Serializable, EntidadeBD {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uf_id")
	@SequenceGenerator(name = "uf_id", sequenceName = "uf_id", allocationSize = 1)
	@Column(name = "iduf")
	private Long idUf;

	@Column(nullable = false, unique = true)
	@NotNull @NotBlank @Length(min = 2, max = 2)
	private String uf;

	@Column(nullable = false, unique = true, length = 50)
	@NotNull @NotBlank @Length(min = 1, max = 50)
	private String nome;

	@Column(name = "codigouf", nullable = false, unique = true)
	@NotNull
	private Long codigoUf;

	@Override
	@JsonIgnore
	public Long getId() {
		return idUf;
	}
}
