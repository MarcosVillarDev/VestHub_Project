package com.project.vesthubApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniversidadesDto {

    private Long id;
    private String nome;
    private String sigla;
    private String status;
}
