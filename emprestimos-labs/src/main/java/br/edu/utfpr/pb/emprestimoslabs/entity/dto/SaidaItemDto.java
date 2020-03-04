package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import br.edu.utfpr.pb.emprestimoslabs.entity.Equipamento;
import br.edu.utfpr.pb.emprestimoslabs.entity.Saida;
import br.edu.utfpr.pb.emprestimoslabs.entity.SaidaItem;
import br.edu.utfpr.pb.emprestimoslabs.service.EquipamentoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaidaItemDto {

	@NotNull
	private Equipamento equipamento;
	@NotNull @Min(1)
	private Integer quantidade;
	private Integer quantidadeDevolvida;
	private LocalDate dataDevolucao;

	public SaidaItemDto(SaidaItem saidaItem) {
		this.equipamento = saidaItem.getIdSaidaItem().getEquipamento();
		this.quantidade = saidaItem.getQuantidade();
		this.quantidadeDevolvida = saidaItem.getQuantidadeDevolvida();
		this.dataDevolucao = saidaItem.getDataDevolucao();
	}

	public SaidaItem toEntity(Saida saida, EquipamentoService equipamentoService) {
		SaidaItem saidaItem = new SaidaItem();

		saidaItem.getIdSaidaItem().setSaida(saida);
		saidaItem.getIdSaidaItem().setEquipamento(equipamento);
		saidaItem.setQuantidade(quantidade);
		saidaItem.setQuantidadeDevolvida(quantidadeDevolvida);
		saidaItem.setDataDevolucao(dataDevolucao);

		return saidaItem;
	}

}