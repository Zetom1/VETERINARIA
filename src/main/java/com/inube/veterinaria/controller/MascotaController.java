package com.inube.veterinaria.controller;

import com.inube.veterinaria.dto.ApiResponse;
import com.inube.veterinaria.dto.MascotaRequest;
import com.inube.veterinaria.services.MascotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.inube.veterinaria.utils.UtilConstants.*;

@RestController
@RequestMapping("/api/mascota")
@RequiredArgsConstructor
@Tag(name = "Mascota", description = "Operaciones de mascotas")
public class MascotaController {

    private final MascotaService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> guardar(@RequestBody MascotaRequest request){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG2, service.guardar(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listar(){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG1, service.listar()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG1, service.buscarPorId(id)));
    }

    @GetMapping("/adoptante/{idAdoptante}")
    public ResponseEntity<ApiResponse<?>> listarPorAdoptante(@PathVariable Integer idAdoptante){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG1, service.listarPorAdoptante(idAdoptante)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> actualizar(@PathVariable Integer id, @RequestBody MascotaRequest request){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG3, service.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> eliminar(@PathVariable Integer id){
        service.eliminar(id);
        return ResponseEntity.ok(new ApiResponse<>(true, MSG5, null));
    }
}