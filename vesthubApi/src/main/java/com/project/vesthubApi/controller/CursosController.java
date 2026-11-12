package com.project.vesthubApi.controller;

import com.project.vesthubApi.dto.CursosDto;
import com.project.vesthubApi.dto.CursosFilter;
import com.project.vesthubApi.dto.NotasCorteDto;
import com.project.vesthubApi.service.CursosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursosController {

    @Autowired
    private CursosService cursosService;

    // Os parâmetros da URL (?universidadeId=1&nome=...) preenchem o CursosFilter automaticamente
    @GetMapping
    public List<CursosDto> listar(CursosFilter filter) {
        return cursosService.listar(filter);
    }

    @GetMapping("/{id}/notas-corte")
    public List<NotasCorteDto> listarNotasCorte(@PathVariable Long id) {
        return cursosService.listarNotasCorte(id);
    }
}
