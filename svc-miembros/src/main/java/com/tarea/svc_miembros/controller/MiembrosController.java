package com.tarea.svc_miembros.controller;

import com.tarea.svc_miembros.dto.MiembrosRequest;
import com.tarea.svc_miembros.service.MiembrosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MiembrosController {

    @Autowired
    private MiembrosService miembrosService;


    @PostMapping
    public Map<String,Object> createMiembro(@Valid @RequestBody MiembrosRequest dto){
        return miembrosService.createMiembros(dto);
    }

    @GetMapping("/{id}")
    public Map<String,Object> getMiembro(@PathVariable String id){
        return miembrosService.getMiembros(id);
    }

    @GetMapping("/health")
    public Map<String,Object> health(){
        return Map.of("servicio","SVC-miembros","Estado","OK");
    }

}
