//package com.rumango.median.iso.security;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.access.AccessDeniedHandler;
//
//@Configuration
//@EnableWebSecurity
//public class SecureConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	private AccessDeniedHandler accessDeniedHandler;
//
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//	@Autowired
//	private DataSource dataSource;
//
//	@Value("${spring.queries.users-query}")
//	private String usersQuery;
//
//	@Value("${spring.queries.roles-query}")
//	private String rolesQuery;
//
//	// roles admin allow to access /admin/**
//	// roles user allow to access /user/**
//	// custom 403 access denied handler
//	@Override
//	protected void configure(HttpSecurity http) throws Exception { // .failureUrl("/login?error=true")
//		// http.csrf().disable().authorizeRequests().antMatchers("/", "/home",
//		// "/about").permitAll()
//		// .antMatchers("/admin/**").hasAnyRole("ALLROLE").antMatchers("/user/**")
//		// .hasAnyRole("ALLROLE", "REPORTROLE",
//		// "DATAMARTROLE").anyRequest().authenticated().and().formLogin()
//		// .loginPage("/login").defaultSuccessUrl("/success").permitAll().and().logout().permitAll().and()
//		// .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
//
//		http.csrf().disable().authorizeRequests().antMatchers("/", "/home", "/about").permitAll()
//				.antMatchers("/admin/**").hasAnyRole("ROLE_ADMIN").antMatchers("/user/**").hasAnyRole("ROLE_USER")
//				.anyRequest().authenticated().and().formLogin().usernameParameter("username")
//				.passwordParameter("password").loginPage("/login").defaultSuccessUrl("/success").permitAll().and()
//				.logout().permitAll().and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
//	}
//
//	// create two users, admin and user
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		// auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER").and().withUser("admin")
//		// .password("{noop}admin").roles("ADMIN");
//
//		auth.jdbcAuthentication().dataSource(dataSource)
//				.usersByUsernameQuery("select username,password, enabled from users where username=?")
//				.authoritiesByUsernameQuery("select username, role from user_roles where username=?");
//
//		// try {
//		// System.out.println("INside AuthenticationManagerBuilder");
//		// auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(usersQuery)
//		// .authoritiesByUsernameQuery(rolesQuery).passwordEncoder(bCryptPasswordEncoder);//
//		// } catch (Exception e) {
//		// e.getCause();
//		// }
//
//	}
//
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
//	}
//
//	/*
//	 * @Bean public PasswordEncoder passwordEncoder() { return new
//	 * StandardPasswordEncoder("verysecret"); }
//	 */
//}