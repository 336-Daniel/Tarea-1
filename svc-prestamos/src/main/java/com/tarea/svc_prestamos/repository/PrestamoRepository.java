package com.tarea.svc_prestamos.repository;

import com.tarea.svc_prestamos.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrestamoRepository extends JpaRepository<Prestamo, String>{

    Optional<Prestamo> findById(String id);
}
