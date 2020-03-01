package br.edu.utfpr.pb.emprestimoslabs.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

public class NovoRecurso extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private HttpServletResponse resposta;
	private Long idRecurso;

	public NovoRecurso(Object source, HttpServletResponse resposta, Long idRecurso) {
		super(source);
		this.resposta = resposta;
		this.idRecurso = idRecurso;
	}

	public HttpServletResponse getResposta() {
		return resposta;
	}

	public Long getIdRecurso() {
		return idRecurso;
	}

}
