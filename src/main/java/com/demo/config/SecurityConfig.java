package com.demo.config;

import com.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            
        		.requestMatchers(
                        "/",  "/index",
                        "/index.html",
                        "/PaginaWeb/**", 
                        "/pelicula/detalle/**",  
                        "/login/**",
                        "/script/**",
                        "/nosotros",
                        "/contacto",
                        "/enviarMensaje",
                        "/register", 
                        "/css/**", 
                        "/js/**", 
                        "/img/**", 
                        "/imagenes/", 
                        "/cartelera/**", 
                        "/cartelera/", 
                        "/PaginaWeb/cartelera", 
                        "/PaginaWeb/cartelera/**",
                        "/reserva/seleccionar/**"
                    ).permitAll()
            /*.requestMatchers(
                "/", 
                "/PaginaWeb/**", 
                "/pelicula/detalle/**",  
                "/login", 
                "/register", 
                "/css/**", 
                "/js/**", 
                "/img/**", 
                "/imagenes/**", 
                "/cartelera", 
                "/cartelera/**", 
                "/PaginaWeb/cartelera", 
                "/PaginaWeb/cartelera/**",
                "/reserva/seleccionar/**",
                "/contacto",
                "/enviarMensaje",
                "/nosotros"
            ).permitAll()*/
                .requestMatchers("/PaginaAdmin/**").hasRole("ADM")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/procesarLogin")
                .usernameParameter("correo")
                .passwordParameter("clave")
                .successHandler((request, response, authentication) -> {
                    String rol = authentication.getAuthorities().iterator().next().getAuthority();
                    if (rol.equals("ROLE_ADM")) {
                        response.sendRedirect("/PaginaAdmin/PanelAdmin");
                    } else if (rol.equals("ROLE_CLIENTE")) {
                        response.sendRedirect("/PaginaWeb/index");
                    } else {
                        response.sendRedirect("/login?error=true");
                    }
                })
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/PaginaWeb/index")
                .permitAll()
            );

        return http.build();
    }
}

