package com.project.vesthubApi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "universidades_favoritas")
public class UniversidadesFavoritas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "universidade_id")
    private Universidades universidade;
}
