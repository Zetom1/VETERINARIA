package com.inube.veterinaria.controller;

import com.inube.veterinaria.dto.ApiResponse;
import com.inube.veterinaria.model.VacunaModel;
import com.inube.veterinaria.services.VacunaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.inube.veterinaria.utils.UtilConstants.*;

@RestController
@RequestMapping("/api/vacuna")
@RequiredArgsConstructor
@Tag(name = "Vacuna", description = "Catálogo de vacunas")
public class VacunaController {

    private final VacunaService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> guardar(@RequestBody VacunaModel vacuna){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG28, service.guardar(vacuna)));
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
    public ResponseEntity<ApiResponse<?>> actualizar(@PathVariable Integer id, @RequestBody VacunaModel vacuna){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG29, service.actualizar(id, vacuna)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> eliminar(@PathVariable Integer id){
        service.eliminar(id);
        return ResponseEntity.ok(new ApiResponse<>(true, MSG30, null));
    }
}