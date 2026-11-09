package com.inube.veterinaria.repository;

import com.inube.veterinaria.model.AdoptanteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptanteRepository extends JpaRepository<AdoptanteModel, Integer> {
    List<AdoptanteModel> findByEstado(Integer estado);
}
