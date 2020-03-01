package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;

public class UsuarioDto {

	private Long idUsuario;
	private String email;
	private String nome;
	private Integer nrora;
	private boolean ativo;
	private Integer permissao;

	public UsuarioDto(Usuario usuario) {
		this.idUsuario = usuario.getIdUsuario();
		this.email = usuario.getEmail();
		this.nome = usuario.getNome();
		this.nrora = usuario.getNrora();
		this.ativo = usuario.isAtivo();
		this.permissao = usuario.getPermissao().getIdPerfil();
	}

	public UsuarioDto() {
	}

	public UsuarioDto(Long idUsuario, String email, String nome, Integer nrora, boolean ativo, Integer permissao) {
		this.idUsuario = idUsuario;
		this.email = email;
		this.nome = nome;
		this.nrora = nrora;
		this.ativo = ativo;
		this.permissao = permissao;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getNrora() {
		return nrora;
	}

	public void setNrora(Integer nrora) {
		this.nrora = nrora;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Integer getPermissao() {
		return permissao;
	}

	public void setPermissao(Integer permissao) {
		this.permissao = permissao;
	}

}
