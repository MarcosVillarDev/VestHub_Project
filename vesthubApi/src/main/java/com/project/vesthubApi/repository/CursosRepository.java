package com.project.vesthubApi.repository;

import com.project.vesthubApi.model.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursosRepository extends JpaRepository<Cursos, Long>, CursosRepositoryQuery {
}
