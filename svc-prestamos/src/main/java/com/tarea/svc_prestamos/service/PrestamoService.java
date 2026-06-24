package com.tarea.svc_prestamos.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarea.svc_prestamos.dto.PrestamoRequest;
import com.tarea.svc_prestamos.model.Prestamo;
import com.tarea.svc_prestamos.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String,Object> createPrestamo(PrestamoRequest dto){

        String id= "PRE-"+UUID.randomUUID().toString();
        Prestamo prestamo = new Prestamo();
        prestamo.setId(id);
        prestamo.setIsbn(dto.getIsbn());
        prestamo.setMiembroId(dto.getMiembroId());
        prestamo.setFechaPrestamo(new Date().toString());
        prestamo.setFechaDevolucionEstimada(dto.getFechaDevolucionEstimada());
        prestamo.setFechaDevolucionReal(dto.getFechaDevolucionReal());
        prestamo.setEstado(dto.getEstado());

        prestamoRepository.save(prestamo);

        redis.delete("prestamo:all");

        return Map.of("Mensaje","Prestamo guardado exitosamente","ID",id);


    }

    public Map<String,Object> getPrestamo(String id){

        String cacheKey = "prestamo"+id;
        String cached = redis.opsForValue().get(cacheKey);

        if(cached!=null){
            try {
                Map<String,Object> prestamoCache = objectMapper.readValue(
                        cached, new TypeReference<Map<String,Object>>(){});
                return Map.of("fuente","Cache Redis (2ms)","datos",prestamoCache);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        Optional<Prestamo> prestamoOpt = prestamoRepository.findById(id);

        if(prestamoOpt.isEmpty()){
            return Map.of("error","Prestamo no encontrado"+id);
        }
        Prestamo prestamo = prestamoOpt.get();

        try {
            String jsonPrestamo = objectMapper.writeValueAsString(prestamo);
            redis.opsForValue().set(cacheKey,jsonPrestamo,60, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return Map.of("fuente","base de datos POSTGRESQL", "datos", prestamo);

    }

    public Map<String,Object> editarPrestamo(String id) {

        Optional<Prestamo> prestamoOpt = prestamoRepository.findById(id);

        if(prestamoOpt.isEmpty()){
            return Map.of("error", "Préstamo no encontrado con ID: " + id);
        }

        Prestamo prestamo = prestamoOpt.get();

        if ("DEVUELTO".equalsIgnoreCase(prestamo.getEstado())) {
            return Map.of("mensaje", "El préstamo ya se encontraba devuelto", "ID", id);
        }

        prestamo.setFechaDevolucionReal(new Date().toString());
        prestamo.setEstado("DEVUELTO");

        prestamoRepository.save(prestamo);

        String cacheKey = "prestamo" + id;
        redis.delete(cacheKey);
        redis.delete("prestamo:all");

        return Map.of(
                "Mensaje", "Préstamo devuelto exitosamente",
                "ID", id,
                "Estado", prestamo.getEstado(),
                "FechaDevolucionReal", prestamo.getFechaDevolucionReal()
        );
    }


}
