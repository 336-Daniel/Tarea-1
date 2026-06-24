package com.tarea.svc_miembros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "miembros")
public class Miembros {

    @Id
    private String id;
    private String nombre;
    private String email;
    private String tipoMiembro;
    private String fechaRegistro;
    private Integer prestamosActivos;
}
