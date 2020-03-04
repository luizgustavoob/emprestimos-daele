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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.edu.utfpr.pb.emprestimoslabs.entity.enums.GrupoItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EQUIPAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idEquipamento"})
public class Equipamento implements Serializable, EntidadeBD {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipamento_id")
	@SequenceGenerator(name = "equipamento_id", sequenceName = "equipamento_id", allocationSize = 1)
	@Column(name = "idequipamento")
	private Long idEquipamento;

	@Column(name = "nome", nullable = false, length = 20)
	@NotNull @NotBlank @Length(min = 1, max = 20)
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
	@NotNull @Min(1)
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
}
