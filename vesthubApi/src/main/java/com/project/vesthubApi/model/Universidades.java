package com.project.vesthubApi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "universidades")
public class Universidades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", length = 120)
    private String nome;

    @Column(name = "sigla", length = 10)
    private String sigla;

    @Column(name = "status", length = 30)
    private String status;
}
