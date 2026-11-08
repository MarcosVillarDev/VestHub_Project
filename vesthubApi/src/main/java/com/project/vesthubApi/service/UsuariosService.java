package com.project.vesthubApi.service;

import com.project.vesthubApi.dto.RegistroDto;
import com.project.vesthubApi.dto.UsuariosDto;
import com.project.vesthubApi.exceptions.EntidadeNaoEncontradaException;
import com.project.vesthubApi.exceptions.NegocioException;
import com.project.vesthubApi.model.Usuarios;
import com.project.vesthubApi.repository.UsuariosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuariosDto registrar(RegistroDto dto) {
        String email = dto.getEmail().trim().toLowerCase();

        if (usuariosRepository.existsByEmail(email)) {
            throw new NegocioException("Já existe uma conta com este e-mail");
        }

        Usuarios usuario = new Usuarios();
        usuario.setNome(dto.getNome().trim());
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        Usuarios salvo = usuariosRepository.save(usuario);

        return new UsuariosDto(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }

    public UsuariosDto buscarPorEmail(String email) {
        Usuarios usuario = usuariosRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado"));

        return new UsuariosDto(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}