package com.inube.veterinaria.repository;

import com.inube.veterinaria.model.VacunaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacunaRepository extends JpaRepository<VacunaModel, Integer> {
    List<VacunaModel> findByEstado(Integer estado);
}