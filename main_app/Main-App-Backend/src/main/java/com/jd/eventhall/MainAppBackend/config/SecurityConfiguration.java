package com.jd.eventhall.MainAppBackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jd.eventhall.MainAppBackend.service.LogoutService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private LogoutService logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(
                "/api/auth/**"
                ,"/api/game/**"
                ,"/api/event/view/**"
                ,"/api/user/**"
                ).permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                
            )
            .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout ->
                logout.logoutUrl("/api/auth/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler((request, response, authentication) -> 
                    SecurityContextHolder.clearContext())
            );

        return http.build();
    }
}
