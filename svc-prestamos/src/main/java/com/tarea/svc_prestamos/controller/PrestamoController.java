package com.tarea.svc_prestamos.controller;

import com.tarea.svc_prestamos.dto.PrestamoRequest;
import com.tarea.svc_prestamos.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @PostMapping
    public Map<String,Object> createPrestamo(@Valid @RequestBody PrestamoRequest dto){
        return prestamoService.createPrestamo(dto);
    }

    @GetMapping("/{id}")
    public Map<String,Object> getPrestamo(@PathVariable String id){
        return prestamoService.getPrestamo(id);
    }

    @PutMapping("/{id}/devolver")
    public Map<String,Object> devolverPrestamo(@PathVariable String id){
        return prestamoService.editarPrestamo(id);
    }

    @GetMapping("/health")
    public Map<String,Object> health(){
        return Map.of("servicio","SVC-prestamos","Estado","OK");
    }
}
