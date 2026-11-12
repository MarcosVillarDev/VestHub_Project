package com.project.vesthubApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotasCorteDto {

    private String modalidadeSigla;
    private String modalidadeNome;
    private BigDecimal notaCorte;
}
