package be.ddd.infra.config.auth0;

import be.ddd.api.login.Auth0JwtFilter;
import be.ddd.api.login.Auth0JwtVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Auth0JwtVerifier auth0JwtVerifier;

    @Bean
    public Auth0JwtFilter auth0JwtFilter() {
        return new Auth0JwtFilter(auth0JwtVerifier);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .httpBasic(b -> b.disable())
            .formLogin(f -> f.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/demo/public").permitAll()
                .anyRequest().authenticated()
            )

            .addFilterBefore(auth0JwtFilter(), UsernamePasswordAuthenticationFilter.class);

            return http.build();
    }

}
