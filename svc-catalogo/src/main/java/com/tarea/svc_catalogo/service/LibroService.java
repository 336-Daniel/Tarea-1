package com.tarea.svc_catalogo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarea.svc_catalogo.dto.LibroRequest;
import com.tarea.svc_catalogo.model.Libro;
import com.tarea.svc_catalogo.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> createLibro(LibroRequest dto){

        String idLibro = UUID.randomUUID().toString();
        Libro libro = new Libro();
        libro.setIsbn(idLibro);
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setGenero(dto.getGenero());
        libro.setAnioPublicacion(dto.getAnioPublicacion());
        libro.setDisponible(dto.getDisponible());
        libro.setCopiasTotales(dto.getCopiasTotales());
        libro.setCopiasDisponibles(dto.getCopiasDisponibles());

        libroRepository.save(libro);

        redis.delete("libro:all");

        return Map.of("Mensaje", "Libro guardado exitosamente","id", idLibro);

    }

    public Map<String, Object> getLibro(String idLibro){

        String cacheKey = "libro:"+idLibro;
        String cached = redis.opsForValue().get(cacheKey);

        if(cached!=null){
            try {
                Map<String,Object> libroCache = objectMapper.readValue(
                        cached, new TypeReference<Map<String,Object>>(){});
                return Map.of("fuente","CACHE Redis (2ms)","datos", libroCache);

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        Optional<Libro> libroOpt = libroRepository.findById(idLibro);

        if(libroOpt.isEmpty()){
            return Map.of("error", "Libro no encontrado"+idLibro);
        }
        Libro libro = libroOpt.get();
        try {
            String jsonLibro = objectMapper.writeValueAsString(libro);
            redis.opsForValue().set(cacheKey, jsonLibro, 60, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return Map.of("Fuente", "base de datos POSTGRESQL","datos", libro);
    }

}
