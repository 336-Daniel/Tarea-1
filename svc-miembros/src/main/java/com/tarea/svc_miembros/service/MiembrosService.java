package com.tarea.svc_miembros.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarea.svc_miembros.dto.MiembrosRequest;
import com.tarea.svc_miembros.model.Miembros;
import com.tarea.svc_miembros.repository.MiembrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MiembrosService {

    @Autowired
    private MiembrosRepository miembrosRepository;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String,Object> createMiembros(MiembrosRequest dto){

        String id= "M"+UUID.randomUUID().toString();
        Miembros miembro = new Miembros();
        miembro.setId(id);
        miembro.setNombre(dto.getNombre());
        miembro.setEmail(dto.getEmail());
        miembro.setTipoMiembro(dto.getTipoMiembro());
        miembro.setFechaRegistro(new Date().toString());
        miembro.setPrestamosActivos(dto.getPrestamosActivos());

        miembrosRepository.save(miembro);

        redis.delete("miembros:all");

        return Map.of("Mensaje","Miembro guardado exitosamente","ID",id);

    }

    public Map<String,Object> getMiembros(String id){

        String cacheKey = "miembros:"+id;
        String cached = redis.opsForValue().get(cacheKey);

        if(cached!=null){
            try {
                Map<String,Object> miembrosCache = objectMapper.readValue(
                        cached, new TypeReference<Map<String,Object>>(){});
                return Map.of("fuente","Cache Redis (2ms)","datos",miembrosCache);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        Optional<Miembros> miembroOpt = miembrosRepository.findById(id);

        if(miembroOpt.isEmpty()){
            return Map.of("error","Miembro no encontrado"+id);
        }
        Miembros miembro = miembroOpt.get();

        try {
            String jsonMiembro = objectMapper.writeValueAsString(miembro);
            redis.opsForValue().set(cacheKey,jsonMiembro,60, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return Map.of("fuente","base de datos POSTGRESQL", "datos", miembro);
    }

}
