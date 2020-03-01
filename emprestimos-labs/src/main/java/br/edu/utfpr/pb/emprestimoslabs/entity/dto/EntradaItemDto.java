package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import br.edu.utfpr.pb.emprestimoslabs.entity.Entrada;
import br.edu.utfpr.pb.emprestimoslabs.entity.EntradaItem;
import br.edu.utfpr.pb.emprestimoslabs.entity.Equipamento;

public class EntradaItemDto {

	private Long idEntradaItem;
	@NotNull
	private Equipamento equipamento;
	@NotNull
	@Min(1)
	private Integer quantidade;
	@NotNull
	private BigDecimal valorUnitario;

	public EntradaItemDto() {
	}

	public EntradaItemDto(EntradaItem entradaItem) {
		this.idEntradaItem = entradaItem.getIdEntradaItem().getIdEntradaItem();
		this.equipamento = entradaItem.getEquipamento();
		this.quantidade = entradaItem.getQuantidade();
		this.valorUnitario = entradaItem.getValorUnitario();
	}

	public Long getIdEntradaItem() {
		return idEntradaItem;
	}

	public void setIdEntradaItem(Long idEntradaItem) {
		this.idEntradaItem = idEntradaItem;
	}

	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public EntradaItem toEntity(Entrada entrada) {
		EntradaItem entradaItem = new EntradaItem();
		entradaItem.getIdEntradaItem().setEntrada(entrada);
		entradaItem.getIdEntradaItem().setIdEntradaItem(idEntradaItem);
		entradaItem.setEquipamento(this.equipamento);
		entradaItem.setQuantidade(quantidade);
		entradaItem.setValorUnitario(valorUnitario);
		return entradaItem;
	}
}
