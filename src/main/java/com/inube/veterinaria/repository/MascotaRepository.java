package com.inube.veterinaria.repository;

import com.inube.veterinaria.model.MascotaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<MascotaModel,Integer> {
    List<MascotaModel> findByEstado(Integer estado);
    List<MascotaModel> findByAdoptanteIdAdoptanteAndEstado(Integer idAdoptante, Integer estado);
}