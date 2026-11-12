package com.project.vesthubApi.service;

import com.project.vesthubApi.dto.ModalidadesDto;
import com.project.vesthubApi.dto.UniversidadesDto;
import com.project.vesthubApi.exceptions.EntidadeNaoEncontradaException;
import com.project.vesthubApi.model.Universidades;
import com.project.vesthubApi.repository.ModalidadesRepository;
import com.project.vesthubApi.repository.UniversidadesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversidadesService {

    @Autowired
    private UniversidadesRepository universidadesRepository;

    @Autowired
    private ModalidadesRepository modalidadesRepository;

    public List<UniversidadesDto> listar() {
        return universidadesRepository.findAll().stream()
                .map(u -> new UniversidadesDto(u.getId(), u.getNome(), u.getSigla(), u.getStatus()))
                .toList();
    }

    public UniversidadesDto buscarPorId(Long id) {
        Universidades universidade = universidadesRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Universidade não encontrada"));

        return new UniversidadesDto(universidade.getId(), universidade.getNome(),
                universidade.getSigla(), universidade.getStatus());
    }

    public List<ModalidadesDto> listarModalidades(Long universidadeId) {
        // Garante o 404 caso a universidade não exista
        if (!universidadesRepository.existsById(universidadeId)) {
            throw new EntidadeNaoEncontradaException("Universidade não encontrada");
        }

        return modalidadesRepository.findByUniversidadeId(universidadeId).stream()
                .map(m -> new ModalidadesDto(m.getId(), m.getSigla(), m.getNome()))
                .toList();
    }
}
