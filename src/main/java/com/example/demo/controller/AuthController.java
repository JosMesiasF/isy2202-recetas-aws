
package com.example.demo.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtEncoder jwtEncoder) {

        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    public record LoginRequest(
        @NotBlank String email,
        @NotBlank String password
    ) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
                )
            );

            String rol = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(value -> value.startsWith("ROLE_"))
                .findFirst()
                .orElseThrow();

            Instant ahora = Instant.now();

            JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("recetas-semana2")
                .issuedAt(ahora)
                .expiresAt(ahora.plus(1, ChronoUnit.HOURS))
                .subject(auth.getName())
                .claim("rol", rol)
                .build();

            JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

            String token = jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
            ).getTokenValue();

            return ResponseEntity.ok(Map.of(
                "token", token,
                "tokenType", "Bearer",
                "expiresIn", 3600,
                "rol", rol
            ));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                    "error", "Credenciales incorrectas"
                ));
        }
    }

    @GetMapping("/verificar")
    public Map<String, String> verificar(
            @AuthenticationPrincipal Jwt jwt) {

        return Map.of(
            "email", jwt.getSubject(),
            "rol", jwt.getClaimAsString("rol")
        );
    }

    @GetMapping("/administrador")
    public Map<String, String> administrador(
            @AuthenticationPrincipal Jwt jwt) {

        return Map.of(
            "mensaje", "Acceso autorizado",
            "usuario", jwt.getSubject()
        );
    }
}
