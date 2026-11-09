package com.inube.veterinaria.repository;

import com.inube.veterinaria.model.ChipRastreoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChipRastreoRepository extends JpaRepository<ChipRastreoModel,Integer> {
    List<ChipRastreoModel> findByEstado(Integer estado);
    boolean existsByMascotaIdMascota(Integer idMascota);
    boolean existsByCodigoChip(String codigoChip);
    Optional<ChipRastreoModel> findByMascotaIdMascotaAndEstado(Integer idMascota, Integer estado);
}