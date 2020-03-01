package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.edu.utfpr.pb.emprestimoslabs.entity.Entrada;
import br.edu.utfpr.pb.emprestimoslabs.entity.Fornecedor;
import br.edu.utfpr.pb.emprestimoslabs.service.EquipamentoService;
import br.edu.utfpr.pb.emprestimoslabs.service.FornecedorService;

public class EntradaDto {

	private Long idEntrada;
	@NotNull
	private LocalDate data;
	@NotNull
	private Fornecedor fornecedor;
	private String usuario;
	@NotEmpty
	private List<EntradaItemDto> itens = new ArrayList<>();

	public EntradaDto() {
		this.idEntrada = 0L;
	}

	public EntradaDto(Entrada entrada) {
		this.idEntrada = entrada.getIdEntrada();
		this.data = entrada.getData();
		this.fornecedor = entrada.getFornecedor();
		this.usuario = entrada.getUsuario().getNome();
		this.itens = entrada.getItens().stream().map(itemEntrada -> new EntradaItemDto(itemEntrada))
				.collect(Collectors.toList());
	}

	public Long getIdEntrada() {
		return idEntrada;
	}

	public void setIdEntrada(Long idEntrada) {
		this.idEntrada = idEntrada;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<EntradaItemDto> getItens() {
		return itens;
	}

	public void setItens(List<EntradaItemDto> itens) {
		this.itens = itens;
	}

	public Entrada toEntity(FornecedorService fornecedorService, EquipamentoService equipamentoService) {
		Entrada entrada = new Entrada();
		if (idEntrada != null && idEntrada > 0) {
			entrada.setIdEntrada(idEntrada);
		}
		entrada.setData(data);
		entrada.setFornecedor(this.fornecedor);

		entrada.setItens(
				itens.stream().map(entradaItemForm -> entradaItemForm.toEntity(entrada)).collect(Collectors.toList()));

		return entrada;
	}

}
