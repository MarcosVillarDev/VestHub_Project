package com.project.vesthubApi.repository;

import com.project.vesthubApi.model.Modalidades;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModalidadesRepository extends JpaRepository<Modalidades, Long> {

    // O Spring entende "UniversidadeId" como universidade.id (propriedade aninhada)
    List<Modalidades> findByUniversidadeId(Long universidadeId);
}
