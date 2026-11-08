package com.project.vesthubApi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data // Gera os métodos Getter e Setter dos atributos da classe.
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table (name = "usuarios")
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Ela compara objetos com base em campos específicos (ex: id), em vez de referências de memória, garantindo que objetos com o mesmo valor sejam tratados como iguais
    private Long id;

    @Column(name = "nome", length = 120)
    private String nome;

    @Column (name = "email", length = 150) // Limite máximo teórico de tamanho de um email é de 320 segundo as normas técnicas (RFC 5321 e RFC 3696)
    private String email;

    @Column (name = "senha", length = 60)
    private String senha;

    @Column (name = "token_recuperacao", length = 36)
    private String tokenRecuperacao;

    @Column (name = "token_expiracao")
    private LocalDateTime tokenExpiracao;

}
