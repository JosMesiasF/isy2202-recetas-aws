
package com.example.demo.config;

import com.example.demo.repository.UsuarioRepository;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(
            UsuarioRepository repositorio) {

        return email -> {
            var usuario = repositorio.findByEmail(email)
                    .orElseThrow(() ->
                        new UsernameNotFoundException(
                            "Usuario no encontrado"
                        )
                    );

            return new User(
                usuario.getEmail(),
                usuario.getPasswordHash(),
                java.util.List.of(
                    new SimpleGrantedAuthority(usuario.getRol())
                )
            );
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        var provider =
            new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

    @Bean
    public SecretKey jwtSecretKey(
            @Value("${APP_JWT_SECRET}") String secret) {

        byte[] bytes = Base64.getDecoder().decode(secret);

        if (bytes.length < 32) {
            throw new IllegalArgumentException(
                "La clave JWT debe tener al menos 32 bytes"
            );
        }

        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
        return NimbusJwtEncoder.withSecretKey(jwtSecretKey)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
        return NimbusJwtDecoder.withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        var authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("rol");
        authorities.setAuthorityPrefix("");

        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);

        return converter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(
            HttpSecurity http,
            JwtAuthenticationConverter converter) throws Exception {

        http
            .securityMatcher("/api/**")
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    HttpMethod.POST, "/api/auth/login"
                ).permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/recetas",
                    "/api/recetas/buscar"
                ).permitAll()
                .requestMatchers(
                    "/api/auth/administrador"
                ).hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(converter)
                )
            );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurity(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/recetas",
                    "/recetas/buscar",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/error"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults());

        return http.build();
    }
}
