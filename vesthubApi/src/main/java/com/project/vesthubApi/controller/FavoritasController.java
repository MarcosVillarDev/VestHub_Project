package com.project.vesthubApi.controller;

import com.project.vesthubApi.dto.UniversidadesDto;
import com.project.vesthubApi.service.FavoritasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritas")
public class FavoritasController {

    @Autowired
    private FavoritasService favoritasService;

    // O usuário vem sempre do Authentication (login), nunca da URL
    @GetMapping
    public List<UniversidadesDto> listar(Authentication authentication) {
        return favoritasService.listar(authentication.getName());
    }

    @PostMapping("/{universidadeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UniversidadesDto favoritar(@PathVariable Long universidadeId, Authentication authentication) {
        return favoritasService.favoritar(authentication.getName(), universidadeId);
    }

    @DeleteMapping("/{universidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long universidadeId, Authentication authentication) {
        favoritasService.remover(authentication.getName(), universidadeId);
    }
}
