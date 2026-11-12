package com.project.vesthubApi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "modalidades")
public class Modalidades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "sigla", length = 10)
    private String sigla;

    @Column(name = "nome", length = 120)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "universidade_id")
    private Universidades universidade;
}
