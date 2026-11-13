package com.project.vesthubApi.controller;

import com.project.vesthubApi.dto.DesempenhoEntradaDto;
import com.project.vesthubApi.dto.DesempenhosDto;
import com.project.vesthubApi.service.DesempenhosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/desempenhos")
public class DesempenhosController {

    @Autowired
    private DesempenhosService desempenhosService;

    // O usuário vem sempre do Authentication (login), nunca da URL ou do corpo
    @GetMapping
    public List<DesempenhosDto> listar(Authentication authentication) {
        return desempenhosService.listar(authentication.getName());
    }

    @PutMapping
    public DesempenhosDto salvar(@Valid @RequestBody DesempenhoEntradaDto dto, Authentication authentication) {
        return desempenhosService.salvar(authentication.getName(), dto);
    }

    @DeleteMapping("/{universidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long universidadeId, Authentication authentication) {
        desempenhosService.remover(authentication.getName(), universidadeId);
    }
}
