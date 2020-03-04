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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.edu.utfpr.pb.emprestimoslabs.entity.enums.Permissao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idUsuario"})
public class Usuario implements Serializable, EntidadeBD, UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_id")
	@SequenceGenerator(name = "usuario_id", sequenceName = "usuario_id", allocationSize = 1)
	@Column(name = "idusuario")
	private Long idUsuario;

	@Column(nullable = false, unique = true, length = 50)
	@NotNull @NotBlank @Length(max = 50)
	@Email
	private String email;

	@Column(nullable = false, length = 256)
	@NotNull @NotBlank @Length(max = 256)
	private String senha;

	@Column(length = 100, nullable = false)
	@NotNull @NotBlank @Length(min = 1, max = 100)
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
}