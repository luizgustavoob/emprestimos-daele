package br.edu.utfpr.pb.emprestimoslabs.entity.dto;

import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
