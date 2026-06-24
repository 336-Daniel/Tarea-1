package com.tarea.svc_catalogo.controller;

import com.tarea.svc_catalogo.dto.LibroRequest;
import com.tarea.svc_catalogo.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LibroController {

    @Autowired
    private LibroService libroService;

    @PostMapping("/libros")
    public Map<String,Object> createLibro(@Valid @RequestBody LibroRequest dto){
        return libroService.createLibro(dto);
    }

    @GetMapping("/libros/{id}")
    public Map<String,Object> getLibro(@PathVariable String id){
        return libroService.getLibro(id);
    }

    @GetMapping("/health")
    public Map<String,String> health(){
        return Map.of("servicio","SVC-Catalogo","Estado","OK");
    }
}
