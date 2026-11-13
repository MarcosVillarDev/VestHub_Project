package com.project.vesthubApi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "desempenhos")
public class Desempenhos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nota", precision = 6, scale = 2)
    private BigDecimal nota;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "universidade_id")
    private Universidades universidade;

    @ManyToOne
    @JoinColumn(name = "modalidade_id")
    private Modalidades modalidade;
}
