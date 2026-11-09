package com.inube.veterinaria.controller;

import com.inube.veterinaria.dto.ApiResponse;
import com.inube.veterinaria.dto.CitaSeguimientoRequest;
import com.inube.veterinaria.services.CitaSeguimientoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.inube.veterinaria.utils.UtilConstants.*;

@RestController
@RequestMapping("/api/cita-seguimiento")
@RequiredArgsConstructor
@Tag(name = "Cita de seguimiento", description = "Operaciones de citas de seguimiento")
public class CitasSeguimientoController {

    private final CitaSeguimientoService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> guardar(@RequestBody CitaSeguimientoRequest request){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG12, service.guardar(request)));
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
    public ResponseEntity<ApiResponse<?>> actualizar(@PathVariable Integer id, @RequestBody CitaSeguimientoRequest request){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG13, service.actualizar(id, request)));
    }

    @PutMapping("/{id}/completar")
    public ResponseEntity<ApiResponse<?>> completar(@PathVariable Integer id, @RequestParam(required = false) String observaciones){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG19, service.completar(id, observaciones)));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponse<?>> cancelar(@PathVariable Integer id){
        return ResponseEntity.ok(new ApiResponse<>(true, MSG20, service.cancelar(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> eliminar(@PathVariable Integer id){
        service.eliminar(id);
        return ResponseEntity.ok(new ApiResponse<>(true, MSG14, null));
    }
}