package com.tarea.svc_catalogo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "libro")
public class Libro {
    @Id
    private String isbn;
    private String titulo;
    private String autor;
    private String genero;
    private int anioPublicacion;
    private Boolean disponible;
    private int copiasTotales;
    private int copiasDisponibles;



}
