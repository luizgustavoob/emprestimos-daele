package br.edu.utfpr.pb.emprestimoslabs.entity.filter;

public class EquipamentoFiltro {

	private Long patrimonio;
	private String nome;
	private String localizacao;

	public EquipamentoFiltro() { }
	
	public Long getPatrimonio() {
		return patrimonio;
	}

	public void setPatrimonio(Long patrimonio) {
		this.patrimonio = patrimonio;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

}
