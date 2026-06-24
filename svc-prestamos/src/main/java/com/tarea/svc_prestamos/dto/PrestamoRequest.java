package com.tarea.svc_prestamos.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrestamoRequest {

    @NotBlank(message = "isbn obligatorio")
    private String isbn;
    @NotBlank(message = "ID del miembro obligatorio")
    private String miembroId;
    private String fechaDevolucionEstimada;
    private String fechaDevolucionReal;
    private String estado;

}
