package com.inube.veterinaria.services;

import com.inube.veterinaria.dto.ChipRastreoRequest;
import com.inube.veterinaria.dto.ChipRastreoResponseDTO;
import com.inube.veterinaria.model.ChipRastreoModel;
import com.inube.veterinaria.model.MascotaModel;
import com.inube.veterinaria.repository.ChipRastreoRepository;
import com.inube.veterinaria.repository.MascotaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.inube.veterinaria.utils.UtilConstants.*;

@Service
@RequiredArgsConstructor
public class ChipRastreoService {

    private final ChipRastreoRepository chipRepository;
    private final MascotaRepository mascotaRepository;

    @Transactional
    public ChipRastreoResponseDTO guardar(ChipRastreoRequest request) {
        MascotaModel mascota = mascotaRepository.findById(request.getIdMascota())
                .orElseThrow(() -> new RuntimeException(MSG16));

        // id_mascota es unique: una mascota solo puede tener un chip
        if (chipRepository.existsByMascotaIdMascota(mascota.getIdMascota())) {
            throw new RuntimeException(MSG21);
        }
        if (chipRepository.existsByCodigoChip(request.getCodigoChip())) {
            throw new RuntimeException(MSG22);
        }

        ChipRastreoModel chip = new ChipRastreoModel();
        chip.setMascota(mascota);
        chip.setCodigoChip(request.getCodigoChip());
        chip.setFechaImplantacion(request.getFechaImplantacion());
        chip.setFabricante(request.getFabricante());

        return mapearADTO(chipRepository.save(chip));
    }

    @Transactional
    public List<ChipRastreoResponseDTO> listar() {
        return chipRepository.findByEstado(CODEPOS).stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Transactional
    public ChipRastreoResponseDTO buscarPorId(Integer id) {
        return mapearADTO(obtenerChip(id));
    }

    @Transactional
    public ChipRastreoResponseDTO buscarPorMascota(Integer idMascota) {
        ChipRastreoModel chip = chipRepository.findByMascotaIdMascotaAndEstado(idMascota, CODEPOS)
                .orElseThrow(() -> new RuntimeException(MSG17));
        return mapearADTO(chip);
    }

    @Transactional
    public ChipRastreoResponseDTO actualizar(Integer id, ChipRastreoRequest request) {
        ChipRastreoModel chip = obtenerChip(id);
        chip.setCodigoChip(request.getCodigoChip());
        chip.setFechaImplantacion(request.getFechaImplantacion());
        chip.setFabricante(request.getFabricante());
        // La mascota no se reasigna: el chip pertenece a la mascota con la que se registró

        return mapearADTO(chipRepository.save(chip));
    }

    @Transactional
    public void eliminar(Integer id) {
        ChipRastreoModel chip = obtenerChip(id);
        chip.setEstado(CODENEG);
        chipRepository.save(chip);
    }

    private ChipRastreoModel obtenerChip(Integer id) {
        return chipRepository.findById(id).orElseThrow(() -> new RuntimeException(MSG17));
    }

    private ChipRastreoResponseDTO mapearADTO(ChipRastreoModel chip) {
        ChipRastreoResponseDTO dto = new ChipRastreoResponseDTO();
        dto.setIdChip(chip.getIdChip());
        dto.setCodigoChip(chip.getCodigoChip());
        dto.setFechaImplantacion(chip.getFechaImplantacion());
        dto.setFabricante(chip.getFabricante());
        dto.setIdMascota(chip.getMascota().getIdMascota());
        dto.setNombreMascota(chip.getMascota().getNombre());
        dto.setEstado(chip.getEstado());
        dto.setFechaRegistro(chip.getFechaRegistro());
        return dto;
    }
}