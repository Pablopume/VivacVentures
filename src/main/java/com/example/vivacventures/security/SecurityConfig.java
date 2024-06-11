package com.example.vivacventures.security;


import com.example.vivacventures.web.domain.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true,
        prePostEnabled = true,
        jsr250Enabled = true
)

@RequiredArgsConstructor
public class SecurityConfig {
private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/*", "/login").permitAll()
                                .requestMatchers("/registro*").permitAll()
                                .requestMatchers("/login*").permitAll()
                                .requestMatchers("/getusers").permitAll()
                                .requestMatchers("/vivacplacesweb").permitAll()
                                .requestMatchers("/menu").permitAll()
                                .requestMatchers("/error401").permitAll()
                                .requestMatchers("/users").permitAll() // Permitir acceso a /users para usuarios autenticados
//                                .requestMatchers("/api/users").hasRole("ADMIN")
//                                .requestMatchers("/getusers").hasRole("ADMIN")
                                .requestMatchers("/web/*").permitAll()
                                .requestMatchers("/web/login*").permitAll()
                                .anyRequest().authenticated()
                ).build();


    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin").password("{noop}password").roles("ADMIN")
//                .and()
//                .withUser("user").password("{noop}password").roles("USER");
//    }




}
