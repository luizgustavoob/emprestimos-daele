package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.edu.utfpr.pb.emprestimoslabs.entity.enums.FinalidadeSaida;
import br.edu.utfpr.pb.emprestimoslabs.entity.enums.SituacaoSaida;
import br.edu.utfpr.pb.emprestimoslabs.entity.enums.TipoSaida;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SAIDA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idSaida"})
public class Saida implements Serializable, EntidadeBD {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "saida_id")
	@SequenceGenerator(name = "saida_id", sequenceName = "saida_id", allocationSize = 1)
	@Column(name = "idsaida")
	private Long idSaida;

	@Column(name = "datasaida", nullable = false)
	private LocalDate data;

	@Enumerated(EnumType.STRING)
	@Column(name = "tiposaida", nullable = false)
	private TipoSaida tipoSaida;

	@Enumerated(EnumType.STRING)
	@Column(name = "finalidade", nullable = false)
	private FinalidadeSaida finalidade;

	@Enumerated(EnumType.STRING)
	@Column(name = "situacao")
	private SituacaoSaida situacao = SituacaoSaida.PENDENTE;

	@ManyToOne
	@JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
	private Usuario usuario;

	@Column(name = "observacao", length = 200, nullable = true)
	private String observacao;

	@OneToMany(mappedBy = "idSaidaItem.saida")
	@Valid
	private List<SaidaItem> itens = new ArrayList<>();

	@Override
	@JsonIgnore
	public Long getId() {
		return idSaida;
	}

	@PrePersist
	private void prePersist() {
		if (this.tipoSaida == TipoSaida.BAIXA) {
			this.finalidade = null;
			this.situacao = SituacaoSaida.ENCERRADA;
		}
	}
}
