package com.tarea.svc_catalogo.repository;

import com.tarea.svc_catalogo.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, String> {


    Optional<Libro> findById(String libroId);
}
