package com.project.vesthubApi.service;

import com.project.vesthubApi.dto.DesempenhoEntradaDto;
import com.project.vesthubApi.dto.DesempenhosDto;
import com.project.vesthubApi.exceptions.EntidadeNaoEncontradaException;
import com.project.vesthubApi.exceptions.NegocioException;
import com.project.vesthubApi.model.Desempenhos;
import com.project.vesthubApi.model.Modalidades;
import com.project.vesthubApi.model.Universidades;
import com.project.vesthubApi.model.Usuarios;
import com.project.vesthubApi.repository.DesempenhosRepository;
import com.project.vesthubApi.repository.ModalidadesRepository;
import com.project.vesthubApi.repository.UniversidadesRepository;
import com.project.vesthubApi.repository.UsuariosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesempenhosService {

    @Autowired
    private DesempenhosRepository desempenhosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private UniversidadesRepository universidadesRepository;

    @Autowired
    private ModalidadesRepository modalidadesRepository;

    public List<DesempenhosDto> listar(String email) {
        return desempenhosRepository.findByUsuarioEmail(email).stream()
                .map(this::paraDto)
                .toList();
    }

    // Cria o desempenho se ainda não existe naquela universidade; senão, atualiza (nunca duplica)
    @Transactional
    public DesempenhosDto salvar(String email, DesempenhoEntradaDto dto) {
        Usuarios usuario = usuariosRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado"));

        Universidades universidade = universidadesRepository.findById(dto.getUniversidadeId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Universidade não encontrada"));

        Modalidades modalidade = modalidadesRepository.findById(dto.getModalidadeId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Modalidade não encontrada"));

        // Regra obrigatória: a modalidade precisa ser da universidade informada
        if (!modalidade.getUniversidade().getId().equals(universidade.getId())) {
            throw new NegocioException("Esta modalidade não pertence a esta universidade");
        }

        Desempenhos desempenho = desempenhosRepository
                .findByUsuarioEmailAndUniversidadeId(email, universidade.getId())
                .orElseGet(() -> {
                    Desempenhos novo = new Desempenhos();
                    novo.setUsuario(usuario);
                    novo.setUniversidade(universidade);
                    return novo;
                });

        desempenho.setModalidade(modalidade);
        desempenho.setNota(dto.getNota());

        return paraDto(desempenhosRepository.save(desempenho));
    }

    @Transactional
    public void remover(String email, Long universidadeId) {
        Desempenhos desempenho = desempenhosRepository
                .findByUsuarioEmailAndUniversidadeId(email, universidadeId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Você não tem nota cadastrada nesta universidade"));

        desempenhosRepository.delete(desempenho);
    }

    private DesempenhosDto paraDto(Desempenhos d) {
        return new DesempenhosDto(d.getUniversidade().getId(), d.getUniversidade().getSigla(),
                d.getModalidade().getId(), d.getModalidade().getSigla(), d.getNota());
    }
}
