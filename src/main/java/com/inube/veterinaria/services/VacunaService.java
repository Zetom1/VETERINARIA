package com.inube.veterinaria.services;

import com.inube.veterinaria.model.VacunaModel;
import com.inube.veterinaria.repository.VacunaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.inube.veterinaria.utils.UtilConstants.*;

@Service
@RequiredArgsConstructor
public class VacunaService {

    private final VacunaRepository repository;

    public VacunaModel guardar(VacunaModel vacuna) {
        return repository.save(vacuna);
    }

    public List<VacunaModel> listar() {
        return repository.findByEstado(CODEPOS);
    }

    public VacunaModel buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException(MSG31));
    }

    public VacunaModel actualizar(Integer id, VacunaModel request) {
        VacunaModel vacuna = buscarPorId(id);
        vacuna.setNombreVacuna(request.getNombreVacuna());
        vacuna.setDescripcion(request.getDescripcion());

        return repository.save(vacuna);
    }

    public void eliminar(Integer id) {
        VacunaModel vacuna = buscarPorId(id);
        vacuna.setEstado(CODENEG);
        repository.save(vacuna);
    }
}