package com.tarea.svc_miembros.repository;

import com.tarea.svc_miembros.model.Miembros;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MiembrosRepository extends JpaRepository<Miembros,String> {

    Optional<Miembros> findById(String id);
}
