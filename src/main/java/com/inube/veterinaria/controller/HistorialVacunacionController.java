package com.inube.veterinaria.controller;

import com.inube.veterinaria.dto.ApiResponse;
import com.inube.veterinaria.dto.HistorialVacunacionRequest;
import com.inube.veterinaria.services.HistorialVacunacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.inube.veterinaria.utils.UtilConstants.*;

@RestController
@RequestMapping("/api/historial-vacunacion")
@RequiredArgsConstructor
@Tag(name = "Historial de vacunación", description = "Operaciones del historial de vacunación")
public class HistorialVacunacionController {

    private final HistorialVacunacionService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> guardar(@RequestBody HistorialVacunacionRequest request){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG32, service.guardar(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listar(){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG1, service.listar()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG1, service.buscarPorId(id)));
    }

    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<ApiResponse<?>> listarPorMascota(@PathVariable Integer idMascota){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG1, service.listarPorMascota(idMascota)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> actualizar(@PathVariable Integer id, @RequestBody HistorialVacunacionRequest request){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG33, service.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> eliminar(@PathVariable Integer id){
        service.eliminar(id);
        return ResponseEntity.ok(new ApiResponse<>(true, MSG34, null));
    }
}