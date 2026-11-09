package com.inube.veterinaria.services;

import com.inube.veterinaria.dto.MascotaRequest;
import com.inube.veterinaria.dto.MascotaResponseDTO;
import com.inube.veterinaria.model.AdoptanteModel;
import com.inube.veterinaria.model.MascotaModel;
import com.inube.veterinaria.repository.AdoptanteRepository;
import com.inube.veterinaria.repository.MascotaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.inube.veterinaria.utils.UtilConstants.*;

@Service
@RequiredArgsConstructor
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final AdoptanteRepository adoptanteRepository;

    @Transactional
    public MascotaResponseDTO guardar(MascotaRequest request) {
        MascotaModel mascota = new MascotaModel();
        mascota.setNombre(request.getNombre());
        mascota.setEspecie(request.getEspecie());
        mascota.setRaza(request.getRaza());
        mascota.setEdad(request.getEdad());
        mascota.setSexo(request.getSexo());

        //validacion para animal nuevo, no tiene adoptante
        if (request.getIdAdoptante() != null) {
            mascota.setAdoptante(obtenerAdoptante(request.getIdAdoptante()));
            mascota.setFechaAdopcion(LocalDateTime.now());
        }

        return mapearADTO(mascotaRepository.save(mascota));
    }

    @Transactional
    public List<MascotaResponseDTO> listar() {
        return mascotaRepository.findByEstado(CODEPOS).stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Transactional
    public MascotaResponseDTO buscarPorId(Integer id) {
        return mapearADTO(obtenerMascota(id));
    }

    @Transactional
    public List<MascotaResponseDTO> listarPorAdoptante(Integer idAdoptante) {
        obtenerAdoptante(idAdoptante); // valida que el adoptante exista y esté activo
        return mascotaRepository.findByAdoptanteIdAdoptanteAndEstado(idAdoptante, CODEPOS).stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Transactional
    public MascotaResponseDTO actualizar(Integer id, MascotaRequest request) {
        MascotaModel mascota = obtenerMascota(id);
        mascota.setNombre(request.getNombre());
        mascota.setEspecie(request.getEspecie());
        mascota.setRaza(request.getRaza());
        mascota.setEdad(request.getEdad());
        mascota.setSexo(request.getSexo());
        //actualizar adoptante
        Integer idAdoptante = request.getIdAdoptante();
        if (idAdoptante != null && (mascota.getAdoptante() == null
                || !idAdoptante.equals(mascota.getAdoptante().getIdAdoptante()))) {
            mascota.setAdoptante(obtenerAdoptante(idAdoptante));
            mascota.setFechaAdopcion(LocalDateTime.now());
        }

        return mapearADTO(mascotaRepository.save(mascota));
    }

    @Transactional
    public void eliminar(Integer id) {
        MascotaModel mascota = obtenerMascota(id);
        mascota.setEstado(CODENEG);
        mascotaRepository.save(mascota);
    }

    private MascotaModel obtenerMascota(Integer id) {
        return mascotaRepository.findById(id).orElseThrow(() -> new RuntimeException(MSG16));
    }

    private AdoptanteModel obtenerAdoptante(Integer id) {
        AdoptanteModel adoptante = adoptanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MSG15));
        if (!CODEPOS.equals(adoptante.getEstado())) {
            throw new RuntimeException(MSG15);
        }
        return adoptante;
    }

    private MascotaResponseDTO mapearADTO(MascotaModel mascota) {
        MascotaResponseDTO dto = new MascotaResponseDTO();
        dto.setIdMascota(mascota.getIdMascota());
        dto.setNombre(mascota.getNombre());
        dto.setEspecie(mascota.getEspecie());
        dto.setRaza(mascota.getRaza());
        dto.setEdad(mascota.getEdad());
        dto.setSexo(mascota.getSexo());
        dto.setFechaIngreso(mascota.getFechaIngreso());
        dto.setFechaAdopcion(mascota.getFechaAdopcion());
        dto.setEstado(mascota.getEstado());

        AdoptanteModel adoptante = mascota.getAdoptante();
        if (adoptante != null) {
            dto.setIdAdoptante(adoptante.getIdAdoptante());
            dto.setNombreAdoptante(adoptante.getNombre() + " " + adoptante.getApellido());
        }
        return dto;
    }
}