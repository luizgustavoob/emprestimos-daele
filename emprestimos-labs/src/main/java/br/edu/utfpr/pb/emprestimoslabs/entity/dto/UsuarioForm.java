package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;
import br.edu.utfpr.pb.emprestimoslabs.entity.enums.Permissao;

public class UsuarioForm {

	@Email
	@NotNull
	@Length(min = 1, max = 50)
	private String email;
	@NotNull
	@Length(min = 1, max = 256)
	private String senha;
	@NotNull
	@Length(min = 1, max = 100)
	private String nome;
	private Integer permissao;
	private Integer nrora;
	private boolean ativo;

	public Usuario toEntity(PasswordEncoder encoder) {
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setSenha(encoder.encode(senha));
		usuario.setNome(nome);
		usuario.setPermissao(Permissao.toEnum(permissao));
		usuario.setNrora(nrora);
		usuario.setAtivo(ativo);
		return usuario;
	}

	public UsuarioForm() {
	}

	public UsuarioForm(String email, String senha, String nome, Integer permissao, Integer nrora, boolean ativo) {
		this.email = email;
		this.senha = senha;
		this.nome = nome;
		this.permissao = permissao;
		this.nrora = nrora;
		this.ativo = ativo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getPermissao() {
		return permissao;
	}

	public void setPermissao(Integer permissao) {
		this.permissao = permissao;
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
}
