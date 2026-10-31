
package com.example.demo.repository;

import com.example.demo.model.Receta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RecetaRepository
        extends JpaRepository<Receta, Long>,
                JpaSpecificationExecutor<Receta> {

    // Obtener las 3 recetas mas recientes
    List<Receta> findTop3ByOrderByFechaCreacionDesc();

    // Obtener las 3 recetas mas populares
    List<Receta> findTop3ByOrderByVisitasDesc();
}
