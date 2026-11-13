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
public class DesempenhosDto {

    private Long universidadeId;
    private String universidadeSigla;
    private Long modalidadeId;
    private String modalidadeSigla;
    private BigDecimal nota;
}
