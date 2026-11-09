package com.inube.veterinaria.controller;

import com.inube.veterinaria.dto.ApiResponse;
import com.inube.veterinaria.dto.ChipRastreoRequest;
import com.inube.veterinaria.services.ChipRastreoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.inube.veterinaria.utils.UtilConstants.*;

@RestController
@RequestMapping("/api/chip-rastreo")
@RequiredArgsConstructor
@Tag(name = "Chip de rastreo", description = "Operaciones de chips de rastreo")
public class ChipRastreoController {

    private final ChipRastreoService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> guardar(@RequestBody ChipRastreoRequest request){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG6, service.guardar(request)));
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
    public ResponseEntity<ApiResponse<?>> buscarPorMascota(@PathVariable Integer idMascota){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG1, service.buscarPorMascota(idMascota)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> actualizar(@PathVariable Integer id, @RequestBody ChipRastreoRequest request){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG7, service.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> eliminar(@PathVariable Integer id){
        service.eliminar(id);
        return ResponseEntity.ok(new ApiResponse<>(true, MSG11, null));
    }
}