package com.inube.veterinaria.repository;

import com.inube.veterinaria.model.CitaSeguimientoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaSeguimientoRepository extends JpaRepository<CitaSeguimientoModel,Integer> {
    List<CitaSeguimientoModel> findByEstado(Integer estado);
    List<CitaSeguimientoModel> findByMascotaIdMascotaAndEstadoOrderByFechaCitaDesc(Integer idMascota, Integer estado);
}