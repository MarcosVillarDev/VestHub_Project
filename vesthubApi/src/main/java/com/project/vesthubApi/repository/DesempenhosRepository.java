package com.project.vesthubApi.repository;

import com.project.vesthubApi.model.Desempenhos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DesempenhosRepository extends JpaRepository<Desempenhos, Long> {

    List<Desempenhos> findByUsuarioEmail(String email);

    Optional<Desempenhos> findByUsuarioEmailAndUniversidadeId(String email, Long universidadeId);
}
