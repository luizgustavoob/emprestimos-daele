package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import br.edu.utfpr.pb.emprestimoslabs.entity.Entrada;
import br.edu.utfpr.pb.emprestimoslabs.entity.EntradaItem;
import br.edu.utfpr.pb.emprestimoslabs.entity.Equipamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntradaItemDto {

	private Long idEntradaItem;
	@NotNull
	private Equipamento equipamento;
	@NotNull
	@Min(1)
	private Integer quantidade;
	@NotNull
	private BigDecimal valorUnitario;

	public EntradaItemDto(EntradaItem entradaItem) {
		this.idEntradaItem = entradaItem.getIdEntradaItem().getIdEntradaItem();
		this.equipamento = entradaItem.getEquipamento();
		this.quantidade = entradaItem.getQuantidade();
		this.valorUnitario = entradaItem.getValorUnitario();
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
