package br.edu.utfpr.pb.emprestimoslabs.entity.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EntradaFiltro {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	private String fornecedor;
}
