package com.project.vesthubApi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table (name = "usuarios")
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Ela compara objetos com base em campos específicos (ex: id), em vez de referências de memória, garantindo que objetos com o mesmo valor sejam tratados como iguais
    private Long Id;

    @Column(name = "nome")
    private String name;

    @Column (name = "email", length = 320) // Limite máximo teórico de tamanho de um email é de 320 segundo as normas técnicas (RFC 5321 e RFC 3696)
    private String email;

    @Column (name = "senha", length = 20)
    private String senha;

    @Column (name = "token_recuperacao")
    private String tokenRecuperacao;

    @Column (name = "token_expiracao")
    private LocalDateTime tokenExpiracao;

}
