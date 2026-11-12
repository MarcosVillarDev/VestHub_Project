package com.project.vesthubApi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "cursos")
public class Cursos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", length = 150)
    private String nome;

    @Column(name = "campus", length = 120)
    private String campus;

    @Column(name = "turno", length = 15)
    private String turno;

    @ManyToOne
    @JoinColumn(name = "universidade_id")
    private Universidades universidade;
}
