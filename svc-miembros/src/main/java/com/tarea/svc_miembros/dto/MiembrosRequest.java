package com.tarea.svc_miembros.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MiembrosRequest {

    @NotBlank(message = "nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "email es obligatorio")
    private String email;
    private String tipoMiembro;
    private Integer prestamosActivos;
}
