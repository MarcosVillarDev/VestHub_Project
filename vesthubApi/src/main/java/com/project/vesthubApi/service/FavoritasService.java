package com.project.vesthubApi.service;

import com.project.vesthubApi.dto.UniversidadesDto;
import com.project.vesthubApi.exceptions.EntidadeNaoEncontradaException;
import com.project.vesthubApi.exceptions.NegocioException;
import com.project.vesthubApi.model.Universidades;
import com.project.vesthubApi.model.UniversidadesFavoritas;
import com.project.vesthubApi.model.Usuarios;
import com.project.vesthubApi.repository.UniversidadesFavoritasRepository;
import com.project.vesthubApi.repository.UniversidadesRepository;
import com.project.vesthubApi.repository.UsuariosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritasService {

    @Autowired
    private UniversidadesFavoritasRepository favoritasRepository;

    @Autowired
    private UniversidadesRepository universidadesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    public List<UniversidadesDto> listar(String email) {
        return favoritasRepository.findByUsuarioEmail(email).stream()
                .map(f -> paraDto(f.getUniversidade()))
                .toList();
    }

    @Transactional
    public UniversidadesDto favoritar(String email, Long universidadeId) {
        Usuarios usuario = usuariosRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado"));

        Universidades universidade = universidadesRepository.findById(universidadeId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Universidade não encontrada"));

        if (favoritasRepository.existsByUsuarioIdAndUniversidadeId(usuario.getId(), universidadeId)) {
            throw new NegocioException("Esta universidade já está nas suas favoritas");
        }

        UniversidadesFavoritas favorita = new UniversidadesFavoritas();
        favorita.setUsuario(usuario);
        favorita.setUniversidade(universidade);
        favoritasRepository.save(favorita);

        return paraDto(universidade);
    }

    @Transactional
    public void remover(String email, Long universidadeId) {
        UniversidadesFavoritas favorita = favoritasRepository
                .findByUsuarioEmailAndUniversidadeId(email, universidadeId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Esta universidade não está nas suas favoritas"));

        favoritasRepository.delete(favorita);
    }

    private UniversidadesDto paraDto(Universidades u) {
        return new UniversidadesDto(u.getId(), u.getNome(), u.getSigla(), u.getStatus());
    }
}
