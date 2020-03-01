package br.edu.utfpr.pb.emprestimoslabs.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.edu.utfpr.pb.emprestimoslabs.entity.enums.Permissao;

@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable, EntidadeBD, UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_id")
	@SequenceGenerator(name = "usuario_id", sequenceName = "usuario_id", allocationSize = 1)
	@Column(name = "idusuario")
	private Long idUsuario;

	@Column(nullable = false, unique = true, length = 50)
	@NotNull
	@Length(max = 50)
	@Email
	private String email;

	@Column(nullable = false, length = 256)
	@NotNull
	@Length(max = 256)
	private String senha;

	@Column(length = 100, nullable = false)
	@NotNull
	@Length(min = 1, max = 100)
	private String nome;

	private boolean ativo;

	@Enumerated(EnumType.STRING)
	@Column(name = "permissao")
	private Permissao permissao;

	@Column(name = "dtinclusao")
	private LocalDate dataInclusao;

	@Column(name = "dtinativo")
	private LocalDate dataInativacao;

	@Column(name = "nrora", unique = true)
	private Integer nrora;

	public Usuario() {
	}

	public Usuario(Long idUsuario, String email, String senha, String nome, boolean ativo, Permissao permissao,
			LocalDate dataInclusao, LocalDate dataInativacao, Integer nrora) {
		this.idUsuario = idUsuario;
		this.email = email;
		this.senha = senha;
		this.nome = nome;
		this.ativo = ativo;
		this.permissao = permissao;
		this.dataInclusao = dataInclusao;
		this.dataInativacao = dataInativacao;
		this.nrora = nrora;
	}

	@PrePersist
	private void prePersist() {
		this.dataInclusao = LocalDate.now();
	}

	@Override
	@JsonIgnore
	public Long getId() {
		return idUsuario;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(permissao).stream().map(permissao -> new SimpleGrantedAuthority(permissao.getDescricao()))
				.collect(Collectors.toList());
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return this.senha;
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return this.email;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return this.ativo;
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

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	public LocalDate getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(LocalDate dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public LocalDate getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(LocalDate dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	public Integer getNrora() {
		return nrora;
	}

	public void setNrora(Integer nrora) {
		this.nrora = nrora;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idUsuario == null) ? 0 : idUsuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		return true;
	}
}