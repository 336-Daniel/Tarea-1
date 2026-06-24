package com.tarea.svc_catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LibroRequest {

    @NotBlank(message = "Titulo obligatorio")
    private String titulo;
    @NotBlank(message = "Autor obligatorio")
    private String autor;
    private String genero;
    @NotNull(message = "Año de publicacion obligatorio")
    private Integer anioPublicacion;
    private Boolean disponible;
    private int copiasTotales;
    private int copiasDisponibles;

}
