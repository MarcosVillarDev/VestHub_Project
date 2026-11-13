package com.project.vesthubApi.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DesempenhoEntradaDto {

    @NotNull(message = "A universidade é obrigatória")
    private Long universidadeId;

    @NotNull(message = "A modalidade é obrigatória")
    private Long modalidadeId;

    @NotNull(message = "A nota é obrigatória")
    @PositiveOrZero(message = "A nota não pode ser negativa")
    @Digits(integer = 4, fraction = 2, message = "A nota deve ter até 4 dígitos inteiros e 2 casas decimais")
    private BigDecimal nota;
}
