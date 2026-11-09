package com.inube.veterinaria.controller;

import com.inube.veterinaria.dto.ApiResponse;
import com.inube.veterinaria.model.AdoptanteModel;
import com.inube.veterinaria.services.AdoptanteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.inube.veterinaria.utils.UtilConstants.*;

@RestController
@RequestMapping("/api/adoptante")
@RequiredArgsConstructor
@Tag(name = "Adoptante", description = "Operaciones de adoptantes")
public class AdoptanteController {

    private final AdoptanteService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> guardar(@RequestBody AdoptanteModel adoptante){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG8, service.guardar(adoptante)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listar(){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG1, service.listar()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG1, service.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> actualizar(@PathVariable Integer id, @RequestBody AdoptanteModel adoptante){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG9, service.actualizar(id, adoptante)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> eliminar(@PathVariable Integer id){
        service.eliminar(id);
        return ResponseEntity.ok(new ApiResponse<>(true, MSG10, null));
    }
}