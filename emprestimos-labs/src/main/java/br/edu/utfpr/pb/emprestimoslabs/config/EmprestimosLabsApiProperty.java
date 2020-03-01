package br.edu.utfpr.pb.emprestimoslabs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("emprestimos-labs")
public class EmprestimosLabsApiProperty {

	private final Seguranca seguranca = new Seguranca();
	private final Mail email = new Mail();

	public Seguranca getSeguranca() {
		return seguranca;
	}

	public Mail getEmail() {
		return email;
	}

	public static class Seguranca {

		private String jwtSecret = "";
		private Long jwtExpiration = 0L;
		private String corsOriginEnable = "";

		public String getJwtSecret() {
			return jwtSecret;
		}

		public void setJwtSecret(String jwtSecret) {
			this.jwtSecret = jwtSecret;
		}

		public Long getJwtExpiration() {
			return jwtExpiration;
		}

		public void setJwtExpiration(Long jwtExpiration) {
			this.jwtExpiration = jwtExpiration;
		}

		public String getCorsOriginEnable() {
			return corsOriginEnable;
		}

		public void setCorsOriginEnable(String corsOriginEnable) {
			this.corsOriginEnable = corsOriginEnable;
		}

	}

	public static class Mail {

		private String host;
		private Integer port;
		private String sender;
		private String username;
		private String password;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public String getSender() {
			return sender;
		}

		public void setSender(String sender) {
			this.sender = sender;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public boolean isValid() {
			return this.hostValid() && this.portValid() && this.senderValid() && this.usernameValid()
					&& this.passwordValid();
		}

		private boolean hostValid() {
			return this.host != null && !this.host.isEmpty();
		}

		private boolean portValid() {
			return this.port != null && this.port > 0;
		}

		private boolean senderValid() {
			return this.sender != null && !this.sender.isEmpty();
		}

		private boolean usernameValid() {
			return this.username != null && !this.username.isEmpty();
		}

		private boolean passwordValid() {
			return this.password != null && !this.password.isEmpty();
		}
	}
}
