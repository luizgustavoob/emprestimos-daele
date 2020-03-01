package br.edu.utfpr.pb.emprestimoslabs.entity.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class FichaEstoqueFiltro {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	private Integer[] idsEquipamentos;

	public FichaEstoqueFiltro() {
	}

	public FichaEstoqueFiltro(LocalDate data, Integer[] idsEquipamentos) {
		this.data = data;
		this.idsEquipamentos = idsEquipamentos;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Integer[] getIdsEquipamentos() {
		return idsEquipamentos;
	}

	public void setIdsEquipamentos(Integer[] idsEquipamentos) {
		this.idsEquipamentos = idsEquipamentos;
	}

}
