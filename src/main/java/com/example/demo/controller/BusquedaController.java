
package com.example.demo.controller;

import com.example.demo.model.Receta;
import com.example.demo.repository.RecetaRepository;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BusquedaController {

    private final RecetaRepository recetaRepository;

    public BusquedaController(
            RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @GetMapping("/recetas/buscar")
    public String buscar(
            @RequestParam(defaultValue = "") String nombre,
            @RequestParam(defaultValue = "") String tipoCocina,
            @RequestParam(defaultValue = "") String ingredientes,
            @RequestParam(defaultValue = "") String paisOrigen,
            @RequestParam(defaultValue = "") String dificultad,
            Model model) {

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

            return cb.and(condiciones.toArray(new Predicate[0]));
        };

        List<Receta> resultados = recetaRepository.findAll(
            filtros,
            Sort.by("nombre").ascending()
        );

        model.addAttribute("resultados", resultados);
        model.addAttribute("nombre", nombre);
        model.addAttribute("tipoCocina", tipoCocina);
        model.addAttribute("ingredientes", ingredientes);
        model.addAttribute("paisOrigen", paisOrigen);
        model.addAttribute("dificultad", dificultad);

        return "buscar";
    }
}
