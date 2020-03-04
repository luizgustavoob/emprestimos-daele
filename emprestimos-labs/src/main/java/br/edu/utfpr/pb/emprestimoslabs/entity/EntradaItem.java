package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ENTRADAITEM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idEntradaItem"})
public class EntradaItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private EntradaItemPK idEntradaItem = new EntradaItemPK();

	@ManyToOne
	@JoinColumn(name = "idequipamento", referencedColumnName = "idequipamento")
	@NotNull
	private Equipamento equipamento;

	@Column(nullable = false)
	@Min(1)
	private Integer quantidade;

	@Column(name = "valorunitario", nullable = false)
	@NotNull
	private BigDecimal valorUnitario;
	
}
