package com.project.vesthubApi.repository;

import com.project.vesthubApi.model.UniversidadesFavoritas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UniversidadesFavoritasRepository extends JpaRepository<UniversidadesFavoritas, Long> {

    // "UsuarioEmail" é lido como usuario.email (propriedade aninhada)
    List<UniversidadesFavoritas> findByUsuarioEmail(String email);

    Optional<UniversidadesFavoritas> findByUsuarioEmailAndUniversidadeId(String email, Long universidadeId);

    boolean existsByUsuarioIdAndUniversidadeId(Long usuarioId, Long universidadeId);
}
