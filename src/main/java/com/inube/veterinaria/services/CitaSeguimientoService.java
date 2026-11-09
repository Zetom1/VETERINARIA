package com.inube.veterinaria.services;

import com.inube.veterinaria.dto.CitaSeguimientoRequest;
import com.inube.veterinaria.dto.CitaSeguimientoResponseDTO;
import com.inube.veterinaria.model.CitaSeguimientoModel;
import com.inube.veterinaria.model.MascotaModel;
import com.inube.veterinaria.repository.CitaSeguimientoRepository;
import com.inube.veterinaria.repository.MascotaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.inube.veterinaria.utils.UtilConstants.*;

@Service
@RequiredArgsConstructor
public class CitaSeguimientoService {

    private final CitaSeguimientoRepository citaRepository;
    private final MascotaRepository mascotaRepository;

    @Transactional
    public CitaSeguimientoResponseDTO guardar(CitaSeguimientoRequest request) {
        if (request.getFechaCita() == null || request.getFechaCita().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(MSG24);
        }

        MascotaModel mascota = mascotaRepository.findById(request.getIdMascota())
                .orElseThrow(() -> new RuntimeException(MSG16));
        if (!CODEPOS.equals(mascota.getEstado())) {
            throw new RuntimeException(MSG16);
        }

        // La cita de seguimiento solo aplica a mascotas con un adoptante activo
        if (mascota.getAdoptante() == null || !CODEPOS.equals(mascota.getAdoptante().getEstado())) {
            throw new RuntimeException(MSG38);
        }

        CitaSeguimientoModel cita = new CitaSeguimientoModel();
        cita.setMascota(mascota);
        cita.setFechaCita(request.getFechaCita());
        cita.setMotivo(request.getMotivo());
        cita.setObservaciones(request.getObservaciones());
        cita.setEstatus(ESTATUS_PROGRAMADA);

        return mapearADTO(citaRepository.save(cita));
    }

    @Transactional
    public List<CitaSeguimientoResponseDTO> listar() {
        return citaRepository.findByEstado(CODEPOS).stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Transactional
    public List<CitaSeguimientoResponseDTO> listarPorMascota(Integer idMascota) {
        return citaRepository.findByMascotaIdMascotaAndEstadoOrderByFechaCitaDesc(idMascota, CODEPOS).stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Transactional
    public CitaSeguimientoResponseDTO buscarPorId(Integer id) {
        return mapearADTO(obtenerCita(id));
    }

    @Transactional
    public CitaSeguimientoResponseDTO actualizar(Integer id, CitaSeguimientoRequest request) {
        CitaSeguimientoModel cita = obtenerCita(id);

        if (!ESTATUS_PROGRAMADA.equals(cita.getEstatus())) {
            throw new RuntimeException(MSG25);
        }

        cita.setFechaCita(request.getFechaCita());
        cita.setMotivo(request.getMotivo());
        cita.setObservaciones(request.getObservaciones());
        // La mascota no se reasigna: la cita pertenece a la mascota con la que se registró

        return mapearADTO(citaRepository.save(cita));
    }

    @Transactional
    public CitaSeguimientoResponseDTO completar(Integer id, String observaciones) {
        CitaSeguimientoModel cita = obtenerCita(id);

        if (!ESTATUS_PROGRAMADA.equals(cita.getEstatus())) {
            throw new RuntimeException(MSG26);
        }

        cita.setEstatus(ESTATUS_COMPLETADA);
        if (observaciones != null) {
            cita.setObservaciones(observaciones);
        }
        return mapearADTO(citaRepository.save(cita));
    }

    @Transactional
    public CitaSeguimientoResponseDTO cancelar(Integer id) {
        CitaSeguimientoModel cita = obtenerCita(id);

        if (!ESTATUS_PROGRAMADA.equals(cita.getEstatus())) {
            throw new RuntimeException(MSG27);
        }

        cita.setEstatus(ESTATUS_CANCELADA);
        return mapearADTO(citaRepository.save(cita));
    }

    @Transactional
    public void eliminar(Integer id) {
        CitaSeguimientoModel cita = obtenerCita(id);
        cita.setEstado(CODENEG);
        citaRepository.save(cita);
    }

    private CitaSeguimientoModel obtenerCita(Integer id) {
        return citaRepository.findById(id).orElseThrow(() -> new RuntimeException(MSG18));
    }

    private CitaSeguimientoResponseDTO mapearADTO(CitaSeguimientoModel cita) {
        CitaSeguimientoResponseDTO dto = new CitaSeguimientoResponseDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setIdMascota(cita.getMascota().getIdMascota());
        dto.setNombreMascota(cita.getMascota().getNombre());
        dto.setFechaCita(cita.getFechaCita());
        dto.setMotivo(cita.getMotivo());
        dto.setObservaciones(cita.getObservaciones());
        dto.setEstatus(cita.getEstatus());
        dto.setEstado(cita.getEstado());
        dto.setFechaRegistro(cita.getFechaRegistro());
        return dto;
    }
}