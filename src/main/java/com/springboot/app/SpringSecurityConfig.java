package com.springboot.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.springboot.app.auth.handler.LoginSuccessHandler;
import com.springboot.app.services.JpaUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig {

	@Autowired
	private LoginSuccessHandler lsh;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private BCryptPasswordEncoder pwdEncoder;

	@Autowired
	private JpaUserDetailsService userDetailsService;

	@Bean
	public static BCryptPasswordEncoder pwdEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.authorizeHttpRequests((authz) ->
		{
			try
			{
				authz.antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar**", "/locale", "/api/clientes/*")
						.permitAll()
						/*
						 * .antMatchers("/ver/**")
						 * .hasAnyRole("USER").antMatchers("/uploads/**").hasAnyRole("USER").antMatchers ("/form/**")
						 * .hasAnyRole("ADMIN").antMatchers("/eliminar/**").hasAnyRole("ADMIN").
						 * antMatchers("/factura/**") .hasAnyRole("ADMIN")
						 */.anyRequest().authenticated().and().formLogin().successHandler(lsh).loginPage("/login")
						.permitAll().and().logout().permitAll().and().exceptionHandling()
						.accessDeniedPage("/error_403");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
		return http.build();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception
	{

		builder.userDetailsService(userDetailsService).passwordEncoder(pwdEncoder);

		// builder.jdbcAuthentication().dataSource(dataSource).passwordEncoder(pwdEncoder)
		// .usersByUsernameQuery("select username, pwd, enabled from users where username=?")
		// .authoritiesByUsernameQuery(
		// "select u.username, a.authority from authorities a inner join users u on (a.user_id = u.id) where
		// u.username=?");

		// PasswordEncoder encoder = pwdEncoder();
		// UserBuilder users = User.builder().passwordEncoder(pwd -> encoder.encode(pwd));// (encoder::encode)
		//
		// builder.inMemoryAuthentication().withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
		// .withUser(users.username("gon").password("12345").roles("USER"));

	}

}
