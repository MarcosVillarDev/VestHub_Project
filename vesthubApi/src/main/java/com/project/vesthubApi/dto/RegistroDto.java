package com.project.vesthubApi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDto {

    // Define como os dados devem ser enviados e retornados
    @NotBlank(message = "Informe o e-mail")
    @Email(message = "E-mail inválido")
    @Size(max = 150, message = "O e-mail pode ter no máximo 150 caracteres")
    private String email;

    @NotBlank(message = "Informe um nome")
    @Size(max = 120, message = "O nome pode ter no máximo 120 caracteres")
    private String nome;

    @NotBlank(message = "Informe uma senha")
    @Size(min = 8, max = 72, message = "Senha deve ter entre 8 e 72 caracteres")
    private String senha;
}

