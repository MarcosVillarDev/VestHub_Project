package com.project.vesthubApi.controller;

import com.project.vesthubApi.dto.ModalidadesDto;
import com.project.vesthubApi.dto.UniversidadesDto;
import com.project.vesthubApi.service.UniversidadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/universidades")
public class UniversidadesController {

    @Autowired
    private UniversidadesService universidadesService;

    @GetMapping
    public List<UniversidadesDto> listar() {
        return universidadesService.listar();
    }

    @GetMapping("/{id}")
    public UniversidadesDto buscarPorId(@PathVariable Long id) {
        return universidadesService.buscarPorId(id);
    }

    @GetMapping("/{id}/modalidades")
    public List<ModalidadesDto> listarModalidades(@PathVariable Long id) {
        return universidadesService.listarModalidades(id);
    }
}
