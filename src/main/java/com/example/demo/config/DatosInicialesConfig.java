
package com.example.demo.config;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatosInicialesConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner cargarUsuarios(
            UsuarioRepository repositorio,
            PasswordEncoder encoder,
            @Value("${APP_ADMIN_PASSWORD}") String claveAdmin,
            @Value("${APP_USUARIO1_PASSWORD}") String claveUsuario1,
            @Value("${APP_USUARIO2_PASSWORD}") String claveUsuario2) {

        return args -> {

            crearUsuario(
                repositorio, encoder,
                "Administrador",
                "admin@recetas.local",
                claveAdmin,
                "ROLE_ADMIN"
            );

            crearUsuario(
                repositorio, encoder,
                "Usuario Uno",
                "usuario1@recetas.local",
                claveUsuario1,
                "ROLE_USER"
            );

            crearUsuario(
                repositorio, encoder,
                "Usuario Dos",
                "usuario2@recetas.local",
                claveUsuario2,
                "ROLE_USER"
            );
        };
    }

    private void crearUsuario(
            UsuarioRepository repositorio,
            PasswordEncoder encoder,
            String nombre,
            String email,
            String clave,
            String rol) {

        if (repositorio.findByEmail(email).isPresent()) {
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPasswordHash(encoder.encode(clave));
        usuario.setRol(rol);

        repositorio.save(usuario);
    }
}
