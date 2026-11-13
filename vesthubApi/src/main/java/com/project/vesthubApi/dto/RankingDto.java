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
public class RankingDto {

    private Long cursoId;
    private String curso;
    private String campus;
    private String turno;
    private String universidade;
    private String modalidade;
    private BigDecimal notaCorte;
    private BigDecimal notaUsuario;
    private BigDecimal diferenca;
    private String situacao;
}
