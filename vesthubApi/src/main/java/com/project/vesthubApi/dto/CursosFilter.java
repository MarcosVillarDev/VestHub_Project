package com.project.vesthubApi.dto;

import lombok.Getter;
import lombok.Setter;

// Filtros opcionais da listagem de cursos. O que vier nulo ou vazio é ignorado.
@Getter
@Setter
public class CursosFilter {

    private Long universidadeId;
    private String nome;
    private String turno;
    private String campus;
}
