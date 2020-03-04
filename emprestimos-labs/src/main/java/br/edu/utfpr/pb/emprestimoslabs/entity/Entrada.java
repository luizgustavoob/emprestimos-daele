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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ENTRADA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idEntrada"})
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
}
