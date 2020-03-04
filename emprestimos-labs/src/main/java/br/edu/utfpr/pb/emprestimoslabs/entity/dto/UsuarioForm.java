package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;
import br.edu.utfpr.pb.emprestimoslabs.entity.enums.Permissao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioForm {

	@Email @NotNull @NotBlank @Length(min = 1, max = 50)
	private String email;
	@NotNull @NotBlank @Length(min = 1, max = 256)
	private String senha;
	@NotNull @NotBlank @Length(min = 1, max = 100)
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

}
