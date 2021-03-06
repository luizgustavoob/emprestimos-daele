package br.edu.utfpr.pb.emprestimoslabs.entity.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import br.edu.utfpr.pb.emprestimoslabs.entity.enums.SituacaoSaida;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaidaFiltro {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	private Integer nrora;
	private SituacaoSaida[] situacao;

}
