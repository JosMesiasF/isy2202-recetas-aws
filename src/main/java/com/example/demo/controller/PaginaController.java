
package com.example.demo.controller;

import com.example.demo.repository.RecetaRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    private final RecetaRepository recetaRepository;

    public PaginaController(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @GetMapping({"/", "/recetas"})
    public String inicio(Model model) {

        model.addAttribute(
            "titulo",
            "Recetas del Mundo"
        );

        // Obtener las 3 recetas mas recientes desde MySQL
        model.addAttribute(
            "recientes",
            recetaRepository.findTop3ByOrderByFechaCreacionDesc()
        );

        // Obtener las 3 recetas mas populares desde MySQL
        model.addAttribute(
            "populares",
            recetaRepository.findTop3ByOrderByVisitasDesc()
        );

        return "inicio";
    }
}
