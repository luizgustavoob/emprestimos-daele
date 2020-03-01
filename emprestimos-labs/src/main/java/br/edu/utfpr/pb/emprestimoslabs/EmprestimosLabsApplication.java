package br.edu.utfpr.pb.emprestimoslabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import br.edu.utfpr.pb.emprestimoslabs.config.EmprestimosLabsApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(EmprestimosLabsApiProperty.class)
@EnableCaching
public class EmprestimosLabsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmprestimosLabsApplication.class, args);
	}

}
