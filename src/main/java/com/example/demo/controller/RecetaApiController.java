
package com.example.demo.controller;

import com.example.demo.model.Receta;
import com.example.demo.repository.RecetaRepository;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/recetas")
public class RecetaApiController {

    private final RecetaRepository recetaRepository;

    public RecetaApiController(
            RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    // Datos que pueden consultar los visitantes
    public record RecetaResumen(
        Long id,
        String nombre,
        String tipoCocina,
        String paisOrigen,
        String dificultad,
        Integer tiempoCoccionMin,
        String fotografia
    ) {}

    // Detalle disponible solo para usuarios autenticados
    public record RecetaDetalle(
        Long id,
        String nombre,
        String tipoCocina,
        String paisOrigen,
        String ingredientes,
        String instrucciones,
        Integer tiempoCoccionMin,
        String dificultad,
        String fotografia
    ) {}

    private RecetaResumen convertirResumen(Receta receta) {
        return new RecetaResumen(
            receta.getId(),
            receta.getNombre(),
            receta.getTipoCocina(),
            receta.getPaisOrigen(),
            receta.getDificultad(),
            receta.getTiempoCoccionMin(),
            receta.getFotografia()
        );
    }

    // API PUBLICA: listar recetas
    @GetMapping
    public List<RecetaResumen> listar() {

        return recetaRepository
            .findAll(Sort.by("nombre").ascending())
            .stream()
            .map(this::convertirResumen)
            .toList();
    }

    // API PUBLICA: buscar con cinco filtros
    @GetMapping("/buscar")
    public List<RecetaResumen> buscar(
            @RequestParam(defaultValue = "") String nombre,
            @RequestParam(defaultValue = "") String tipoCocina,
            @RequestParam(defaultValue = "") String ingredientes,
            @RequestParam(defaultValue = "") String paisOrigen,
            @RequestParam(defaultValue = "") String dificultad) {

        Specification<Receta> filtros = (root, query, cb) -> {

            List<Predicate> condiciones = new ArrayList<>();

            if (!nombre.isBlank()) {
                condiciones.add(cb.like(
                    cb.lower(root.get("nombre")),
                    "%" + nombre.trim().toLowerCase(Locale.ROOT) + "%"
                ));
            }

            if (!tipoCocina.isBlank()) {
                condiciones.add(cb.like(
                    cb.lower(root.get("tipoCocina")),
                    "%" + tipoCocina.trim().toLowerCase(Locale.ROOT) + "%"
                ));
            }

            if (!ingredientes.isBlank()) {
                condiciones.add(cb.like(
                    cb.lower(root.get("ingredientes")),
                    "%" + ingredientes.trim().toLowerCase(Locale.ROOT) + "%"
                ));
            }

            if (!paisOrigen.isBlank()) {
                condiciones.add(cb.like(
                    cb.lower(root.get("paisOrigen")),
                    "%" + paisOrigen.trim().toLowerCase(Locale.ROOT) + "%"
                ));
            }

            if (!dificultad.isBlank()) {
                condiciones.add(cb.equal(
                    cb.lower(root.get("dificultad")),
                    dificultad.trim().toLowerCase(Locale.ROOT)
                ));
            }

            return cb.and(
                condiciones.toArray(new Predicate[0])
            );
        };

        return recetaRepository
            .findAll(filtros, Sort.by("nombre").ascending())
            .stream()
            .map(this::convertirResumen)
            .toList();
    }

    // API PRIVADA: detalle completo de receta
    @GetMapping("/{id}")
    public RecetaDetalle detalle(@PathVariable Long id) {

        Receta receta = recetaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Receta no encontrada"
            ));

        return new RecetaDetalle(
            receta.getId(),
            receta.getNombre(),
            receta.getTipoCocina(),
            receta.getPaisOrigen(),
            receta.getIngredientes(),
            receta.getInstrucciones(),
            receta.getTiempoCoccionMin(),
            receta.getDificultad(),
            receta.getFotografia()
        );
    }
}
