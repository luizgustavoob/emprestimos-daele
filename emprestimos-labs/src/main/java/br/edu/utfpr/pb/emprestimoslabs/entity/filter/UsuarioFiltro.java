package br.edu.utfpr.pb.emprestimoslabs.entity.filter;

public class UsuarioFiltro {

	private String email;

	public UsuarioFiltro() {
	}

	public UsuarioFiltro(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}