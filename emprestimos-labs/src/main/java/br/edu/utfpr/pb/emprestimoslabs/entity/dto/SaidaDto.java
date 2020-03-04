package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.edu.utfpr.pb.emprestimoslabs.entity.Saida;
import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;
import br.edu.utfpr.pb.emprestimoslabs.entity.enums.FinalidadeSaida;
import br.edu.utfpr.pb.emprestimoslabs.entity.enums.SituacaoSaida;
import br.edu.utfpr.pb.emprestimoslabs.entity.enums.TipoSaida;
import br.edu.utfpr.pb.emprestimoslabs.service.EquipamentoService;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaidaDto {

	private Long idSaida;
	@NotNull
	private LocalDate data;
	@NotNull
	private TipoSaida tipoSaida;
	private FinalidadeSaida finalidadeSaida;
	@NotNull
	private SituacaoSaida situacao;
	private Usuario usuario;
	private String observacao;
	@NotEmpty
	private List<SaidaItemDto> itens = new ArrayList<>();
	
	public SaidaDto() {
		this.idSaida = 0L;
	}
	
	public SaidaDto(Saida saida) {
		this.idSaida = saida.getIdSaida();
		this.data = saida.getData();
		this.tipoSaida = saida.getTipoSaida();
		this.finalidadeSaida = saida.getFinalidade();
		this.situacao = saida.getSituacao();
		this.usuario = saida.getUsuario();
		this.observacao = saida.getObservacao();
		this.itens = saida.getItens().stream().map(item -> new SaidaItemDto(item)).collect(Collectors.toList());
	}
	
	public Saida toEntity(EquipamentoService equipamentoService) {
		Saida saida = new Saida();
		if (idSaida > 0) {
			saida.setIdSaida(idSaida);
		}
		
		saida.setData(data);
		saida.setTipoSaida(tipoSaida);
		saida.setFinalidade(finalidadeSaida);
		saida.setSituacao(situacao);
		saida.setUsuario(usuario);
		saida.setObservacao(observacao);
		
		saida.setItens(itens.stream().map(itemDto -> itemDto.toEntity(saida, equipamentoService))
				.collect(Collectors.toList()));
		
		return saida;
	}
}
