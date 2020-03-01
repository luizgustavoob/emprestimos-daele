package br.edu.utfpr.pb.emprestimoslabs.event;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class NovoRecursoListener implements ApplicationListener<NovoRecurso> {

	@Override
	public void onApplicationEvent(NovoRecurso novoRecurso) {
		HttpServletResponse resposta = novoRecurso.getResposta();
		Long idRecurso = novoRecurso.getIdRecurso();
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(idRecurso).toUri();
		resposta.setHeader("Location", uri.toASCIIString());
	}

}
