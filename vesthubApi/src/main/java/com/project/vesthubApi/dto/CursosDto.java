package com.project.vesthubApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursosDto {

    private Long id;
    private String nome;
    private String campus;
    private String turno;
    private String universidadeSigla;
}
