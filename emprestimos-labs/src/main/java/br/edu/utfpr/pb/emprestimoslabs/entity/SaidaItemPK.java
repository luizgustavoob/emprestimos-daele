package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"saida", "equipamento"})
public class SaidaItemPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "idsaida", foreignKey = @ForeignKey(name = "fk_saidaitem_saida"))
	private Saida saida;

	@ManyToOne
	@JoinColumn(name = "idequipamento", foreignKey = @ForeignKey(name = "fk_saidaitem_equipamento"))
	private Equipamento equipamento;

}
