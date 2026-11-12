package com.project.vesthubApi.repository;

import com.project.vesthubApi.dto.CursosFilter;
import com.project.vesthubApi.model.Cursos;

import java.util.List;

public interface CursosRepositoryQuery {

    List<Cursos> filtrar(CursosFilter filter);
}
