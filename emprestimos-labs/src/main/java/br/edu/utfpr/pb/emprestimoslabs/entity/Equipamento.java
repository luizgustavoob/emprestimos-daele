package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.edu.utfpr.pb.emprestimoslabs.entity.enums.GrupoItem;

@Entity
@Table(name = "EQUIPAMENTO")
public class Equipamento implements Serializable, EntidadeBD {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipamento_id")
	@SequenceGenerator(name = "equipamento_id", sequenceName = "equipamento_id", allocationSize = 1)
	@Column(name = "idequipamento")
	private Long idEquipamento;

	@Column(name = "nome", nullable = false, length = 20)
	@NotNull
	@Length(min = 1, max = 20)
	private String nome;

	@Column(name = "descricao", nullable = true, length = 100)
	@Length(min = 0, max = 100)
	private String descricao;

	@Column(name = "patrimonio", nullable = true)
	private Long patrimonio;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private GrupoItem grupo;

	@Column(name = "qtdeminima", nullable = false)
	@NotNull
	@Min(1)
	private Integer quantidadeMinima;

	@Column(nullable = true, length = 50)
	private String localizacao;

	@Column(name = "obrigadevolucao", nullable = false)
	@NotNull
	private Boolean devolucaoObrigatoria = Boolean.FALSE;

	private Long siorg;

	@Transient
	private BigDecimal valorUltimaCompra;

	@Override
	@JsonIgnore
	public Long getId() {
		return idEquipamento;
	}

	public Equipamento() {
	}

	public Long getIdEquipamento() {
		return idEquipamento;
	}

	public void setIdEquipamento(Long idEquipamento) {
		this.idEquipamento = idEquipamento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getPatrimonio() {
		return patrimonio;
	}

	public void setPatrimonio(Long patrimonio) {
		this.patrimonio = patrimonio;
	}

	public GrupoItem getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoItem grupo) {
		this.grupo = grupo;
	}

	public Integer getQuantidadeMinima() {
		return quantidadeMinima;
	}

	public void setQuantidadeMinima(Integer quantidadeMinima) {
		this.quantidadeMinima = quantidadeMinima;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public Boolean getDevolucaoObrigatoria() {
		return devolucaoObrigatoria;
	}

	public void setDevolucaoObrigatoria(Boolean devolucaoObrigatoria) {
		this.devolucaoObrigatoria = devolucaoObrigatoria;
	}

	public Long getSiorg() {
		return siorg;
	}

	public void setSiorg(Long siorg) {
		this.siorg = siorg;
	}

	public BigDecimal getValorUltimaCompra() {
		return valorUltimaCompra;
	}

	public void setValorUltimaCompra(BigDecimal valorUltimaCompra) {
		this.valorUltimaCompra = valorUltimaCompra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEquipamento == null) ? 0 : idEquipamento.hashCode());
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
		Equipamento other = (Equipamento) obj;
		if (idEquipamento == null) {
			if (other.idEquipamento != null)
				return false;
		} else if (!idEquipamento.equals(other.idEquipamento))
			return false;
		return true;
	}

}
