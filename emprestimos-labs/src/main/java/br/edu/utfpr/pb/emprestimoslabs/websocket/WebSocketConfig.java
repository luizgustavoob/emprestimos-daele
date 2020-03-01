package br.edu.utfpr.pb.emprestimoslabs.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import br.edu.utfpr.pb.emprestimoslabs.config.EmprestimosLabsApiProperty;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

	@Autowired
	private EmprestimosLabsApiProperty config;
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/queue");
	}
	
	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
        	.addEndpoint("/ws")
        	.setAllowedOrigins(config.getSeguranca().getCorsOriginEnable())
        	.withSockJS();
    }
}
