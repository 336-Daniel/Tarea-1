package com.tarea.svc_prestamos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "prestamo")
public class Prestamo {
    @Id
    private String id;
    private String isbn;
    private String miembroId;
    private String fechaPrestamo;
    private String fechaDevolucionEstimada;
    private String fechaDevolucionReal;
    private String estado;
}
