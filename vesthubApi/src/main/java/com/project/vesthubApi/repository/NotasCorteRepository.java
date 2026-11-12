package com.project.vesthubApi.repository;

import com.project.vesthubApi.model.NotasCorte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotasCorteRepository extends JpaRepository<NotasCorte, Long> {

    List<NotasCorte> findByCursoId(Long cursoId);
}
