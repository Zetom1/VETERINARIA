package com.inube.veterinaria.repository;

import com.inube.veterinaria.model.HistorialVacunacionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialVacunacionRepository extends JpaRepository<HistorialVacunacionModel,Integer> {
    List<HistorialVacunacionModel> findByEstado(Integer estado);
    List<HistorialVacunacionModel> findByMascotaIdMascotaAndEstadoOrderByFechaAplicacionDesc(Integer idMascota, Integer estado);
}