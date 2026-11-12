package com.project.vesthubApi.service;

import com.project.vesthubApi.dto.CursosDto;
import com.project.vesthubApi.dto.CursosFilter;
import com.project.vesthubApi.dto.NotasCorteDto;
import com.project.vesthubApi.exceptions.EntidadeNaoEncontradaException;
import com.project.vesthubApi.repository.CursosRepository;
import com.project.vesthubApi.repository.NotasCorteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursosService {

    @Autowired
    private CursosRepository cursosRepository;

    @Autowired
    private NotasCorteRepository notasCorteRepository;

    public List<CursosDto> listar(CursosFilter filter) {
        return cursosRepository.filtrar(filter).stream()
                .map(c -> new CursosDto(c.getId(), c.getNome(), c.getCampus(), c.getTurno(),
                        c.getUniversidade().getSigla()))
                .toList();
    }

    public List<NotasCorteDto> listarNotasCorte(Long cursoId) {
        // Garante o 404 caso o curso não exista
        if (!cursosRepository.existsById(cursoId)) {
            throw new EntidadeNaoEncontradaException("Curso não encontrado");
        }

        return notasCorteRepository.findByCursoId(cursoId).stream()
                .map(n -> new NotasCorteDto(n.getModalidade().getSigla(), n.getModalidade().getNome(),
                        n.getNotaCorte()))
                .toList();
    }
}
