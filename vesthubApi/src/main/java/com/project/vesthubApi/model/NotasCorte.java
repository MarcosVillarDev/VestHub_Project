package com.project.vesthubApi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "notas_corte")
public class NotasCorte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nota_corte", precision = 6, scale = 2)
    private BigDecimal notaCorte;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Cursos curso;

    @ManyToOne
    @JoinColumn(name = "modalidade_id")
    private Modalidades modalidade;
}
