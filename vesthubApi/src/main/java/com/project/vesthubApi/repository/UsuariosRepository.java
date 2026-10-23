package com.project.vesthubApi.repository;

import com.project.vesthubApi.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByEmail(String email);

    boolean existsByEmail(String email);
}
