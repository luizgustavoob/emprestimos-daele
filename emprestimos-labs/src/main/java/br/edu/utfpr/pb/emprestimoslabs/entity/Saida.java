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

@Entity
@Table(name = "SAIDA")
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

	public Saida() {
	}

	public Saida(Long idSaida, LocalDate data, TipoSaida tipoSaida, FinalidadeSaida finalidade, SituacaoSaida situacao,
			Usuario usuario, String observacao, List<SaidaItem> itens) {
		this.idSaida = idSaida;
		this.data = data;
		this.tipoSaida = tipoSaida;
		this.finalidade = finalidade;
		this.situacao = situacao;
		this.usuario = usuario;
		this.observacao = observacao;
		this.itens = itens;
	}

	public Long getIdSaida() {
		return idSaida;
	}

	public void setIdSaida(Long idSaida) {
		this.idSaida = idSaida;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public TipoSaida getTipoSaida() {
		return tipoSaida;
	}

	public void setTipoSaida(TipoSaida tipoSaida) {
		this.tipoSaida = tipoSaida;
	}

	public FinalidadeSaida getFinalidade() {
		return finalidade;
	}

	public void setFinalidade(FinalidadeSaida finalidade) {
		this.finalidade = finalidade;
	}

	public SituacaoSaida getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoSaida situacao) {
		this.situacao = situacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public List<SaidaItem> getItens() {
		return itens;
	}

	public void setItens(List<SaidaItem> itens) {
		this.itens = itens;
	}

	@PrePersist
	private void prePersist() {
		if (this.tipoSaida == TipoSaida.BAIXA) {
			this.finalidade = null;
			this.situacao = SituacaoSaida.ENCERRADA;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idSaida == null) ? 0 : idSaida.hashCode());
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
		Saida other = (Saida) obj;
		if (idSaida == null) {
			if (other.idSaida != null)
				return false;
		} else if (!idSaida.equals(other.idSaida))
			return false;
		return true;
	}
}
