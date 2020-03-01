package br.edu.utfpr.pb.emprestimoslabs.email;

import br.edu.utfpr.pb.emprestimoslabs.entity.Saida;
import br.edu.utfpr.pb.emprestimoslabs.entity.Usuario;

public interface EmailService {

	void enviarNovaSenha(Usuario usuario, String senha);
	void enviarAprovacaoDeUsuario(Usuario usuario);
	void enviarReprovacaoDeUsuario(Usuario usuario);
	void enviarAprovacaoDeEmprestimo(Saida saida);
	void enviarReprovacaoDeEmprestimo(Saida saida);
}