package com.project.vesthubApi.controller;

import com.project.vesthubApi.dto.RegistroDto;
import com.project.vesthubApi.dto.UsuariosDto;
import com.project.vesthubApi.service.UsuariosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController // Define que esta classe receberá requisições da Internet e envia respostas em JSON.
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuariosService usuariosService;

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuariosDto registrar(@Valid @RequestBody RegistroDto dto){
        return usuariosService.registrar(dto);
    }

    @GetMapping("/login")
    public UsuariosDto login(Authentication authentication) {
        return usuariosService.buscarPorEmail(authentication.getName());
    }
}
