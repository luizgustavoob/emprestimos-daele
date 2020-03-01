package br.edu.utfpr.pb.emprestimoslabs.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.utfpr.pb.emprestimoslabs.security.filter.AuthenticationFilter;
import br.edu.utfpr.pb.emprestimoslabs.security.filter.AuthorizationFilter;
import br.edu.utfpr.pb.emprestimoslabs.security.util.JwtUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/auth/**").permitAll()
				
				.antMatchers(HttpMethod.GET, "/ws/**").permitAll()
				.antMatchers(HttpMethod.POST, "/ws/**").permitAll()
				
				.antMatchers(HttpMethod.GET, "/cidades/**").permitAll()
				.antMatchers(HttpMethod.POST, "/cidades/**").denyAll()
				
				.antMatchers(HttpMethod.GET, "/estados/**").permitAll()
				.antMatchers(HttpMethod.POST, "/estados/**").denyAll()
				
				.antMatchers(HttpMethod.POST, "/equipamentos/**").hasAnyRole("LABORATORISTA", "ADMIN")
				.antMatchers(HttpMethod.PUT, "/equipamentos/**").hasAnyRole("LABORATORISTA", "ADMIN")
				.antMatchers(HttpMethod.DELETE, "/equipamentos/**").hasAnyRole("LABORATORISTA", "ADMIN")
				.antMatchers(HttpMethod.GET, "/equipamentos/**").permitAll()
				
				.antMatchers(HttpMethod.PATCH, "/usuarios/**").hasAnyRole("LABORATORISTA", "ADMIN")
				.antMatchers(HttpMethod.GET, "/usuarios/**").permitAll()
				.antMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
				
				.antMatchers(HttpMethod.GET, "/fornecedores/**").hasAnyRole("LABORATORISTA", "ADMIN")
				.antMatchers(HttpMethod.POST, "/fornecedores/**").hasAnyRole("LABORATORISTA", "ADMIN")
				.antMatchers(HttpMethod.PUT, "/fornecedores/**").hasAnyRole("LABORATORISTA", "ADMIN")
				.antMatchers(HttpMethod.DELETE, "/fornecedores/**").hasAnyRole("LABORATORISTA", "ADMIN")
				
				.antMatchers(HttpMethod.GET, "/entradas/**").hasAnyRole("LABORATORISTA", "ADMIN")
				.antMatchers(HttpMethod.POST, "/entradas/**").hasAnyRole("LABORATORISTA", "ADMIN")
				.antMatchers(HttpMethod.PUT, "/entradas/**").hasAnyRole("LABORATORISTA", "ADMIN")
				.antMatchers(HttpMethod.DELETE, "/entradas/**").hasAnyRole("LABORATORISTA", "ADMIN")
				
				.antMatchers(HttpMethod.GET, "/saidas/**").permitAll()
				.antMatchers(HttpMethod.POST, "/saidas/**").permitAll()
				.antMatchers(HttpMethod.PUT, "/saidas/**").permitAll()
				.antMatchers(HttpMethod.DELETE, "/saidas/**").hasAnyRole("LABORATORISTA", "ADMIN")
				.antMatchers(HttpMethod.PATCH, "/saidas/**").hasAnyRole("LABORATORISTA", "ADMIN")
				
				.antMatchers(HttpMethod.GET, "/estoque/**").permitAll()
			.anyRequest().authenticated();
		
		http.addFilter(new AuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new AuthorizationFilter(authenticationManager(), userDetailsService, jwtUtil));
	
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
}
