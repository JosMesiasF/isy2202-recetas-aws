
package com.example.demo.controller;

import com.example.demo.model.Receta;
import com.example.demo.repository.RecetaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class DetalleRecetaController {

    private final RecetaRepository recetaRepository;

    public DetalleRecetaController(
            RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @GetMapping("/recetas/{id}")
    public String detalle(
            @PathVariable Long id,
            Model model) {

        Receta receta = recetaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Receta no encontrada"
            ));

        model.addAttribute("receta", receta);

        return "detalle";
    }
}
