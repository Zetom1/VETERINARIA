package com.inube.veterinaria.services;

import com.inube.veterinaria.dto.HistorialVacunacionRequest;
import com.inube.veterinaria.dto.HistorialVacunacionResponseDTO;
import com.inube.veterinaria.model.HistorialVacunacionModel;
import com.inube.veterinaria.model.MascotaModel;
import com.inube.veterinaria.model.VacunaModel;
import com.inube.veterinaria.repository.HistorialVacunacionRepository;
import com.inube.veterinaria.repository.MascotaRepository;
import com.inube.veterinaria.repository.VacunaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.inube.veterinaria.utils.UtilConstants.*;

@Service
@RequiredArgsConstructor
public class HistorialVacunacionService {

    private final HistorialVacunacionRepository historialRepository;
    private final MascotaRepository mascotaRepository;
    private final VacunaRepository vacunaRepository;

    @Transactional
    public HistorialVacunacionResponseDTO guardar(HistorialVacunacionRequest request) {
        validarFechas(request);

        MascotaModel mascota = mascotaRepository.findById(request.getIdMascota())
                .orElseThrow(() -> new RuntimeException(MSG16));
        if (!CODEPOS.equals(mascota.getEstado())) {
            throw new RuntimeException(MSG16);
        }

        VacunaModel vacuna = vacunaRepository.findById(request.getIdVacuna())
                .orElseThrow(() -> new RuntimeException(MSG31));
        if (!CODEPOS.equals(vacuna.getEstado())) {
            throw new RuntimeException(MSG31);
        }

        HistorialVacunacionModel historial = new HistorialVacunacionModel();
        historial.setMascota(mascota);
        historial.setVacuna(vacuna);
        historial.setFechaAplicacion(request.getFechaAplicacion());

        return mapearADTO(historialRepository.save(historial));
    }

    @Transactional
    public List<HistorialVacunacionResponseDTO> listar() {
        return historialRepository.findByEstado(CODEPOS).stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Transactional
    public List<HistorialVacunacionResponseDTO> listarPorMascota(Integer idMascota) {
        return historialRepository.findByMascotaIdMascotaAndEstadoOrderByFechaAplicacionDesc(idMascota, CODEPOS).stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Transactional
    public HistorialVacunacionResponseDTO buscarPorId(Integer id) {
        return mapearADTO(obtenerHistorial(id));
    }

    @Transactional
    public HistorialVacunacionResponseDTO actualizar(Integer id, HistorialVacunacionRequest request) {
        validarFechas(request);

        HistorialVacunacionModel historial = obtenerHistorial(id);
        historial.setFechaAplicacion(request.getFechaAplicacion());

        return mapearADTO(historialRepository.save(historial));
    }

    @Transactional
    public void eliminar(Integer id) {
        HistorialVacunacionModel historial = obtenerHistorial(id);
        historial.setEstado(CODENEG);
        historialRepository.save(historial);
    }

    private void validarFechas(HistorialVacunacionRequest request) {
        if (request.getFechaAplicacion() == null) {
            throw new RuntimeException(MSG36);
        }
    }

    private HistorialVacunacionModel obtenerHistorial(Integer id) {
        return historialRepository.findById(id).orElseThrow(() -> new RuntimeException(MSG35));
    }

    private HistorialVacunacionResponseDTO mapearADTO(HistorialVacunacionModel historial) {
        HistorialVacunacionResponseDTO dto = new HistorialVacunacionResponseDTO();
        dto.setIdHistorial(historial.getIdHistorial());
        dto.setIdMascota(historial.getMascota().getIdMascota());
        dto.setNombreMascota(historial.getMascota().getNombre());
        dto.setIdVacuna(historial.getVacuna().getIdVacuna());
        dto.setNombreVacuna(historial.getVacuna().getNombreVacuna());
        dto.setEstado(historial.getEstado());
        dto.setFechaRegistro(historial.getFechaRegistro());
        return dto;
    }
}