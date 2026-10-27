package com.project.vesthubApi.security;

import com.project.vesthubApi.model.Usuarios;
import com.project.vesthubApi.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailService implements UserDetailsService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário pelo email e exibe erro caso não encontre.
        Usuarios usuario = usuariosRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Traduz o Usuarios para o formato UserDetails e devolve para o Spring Security.
        return
                User.withUsername(usuario.getEmail()) // email do usuário
                .password(usuario.getSenha()) // senha do usuário em hash
                .roles("USER") // papel/permissões
                .build();

    }
}
