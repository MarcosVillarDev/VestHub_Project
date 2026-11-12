package com.project.vesthubApi.repository;

import com.project.vesthubApi.dto.CursosFilter;
import com.project.vesthubApi.model.Cursos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

// O nome precisa ser CursosRepositoryImpl (nome do repository + Impl) para o Spring juntar as duas partes.
public class CursosRepositoryImpl implements CursosRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Cursos> filtrar(CursosFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Cursos> criteria = builder.createQuery(Cursos.class);
        Root<Cursos> root = criteria.from(Cursos.class);

        // Cada filtro só entra na consulta se foi preenchido
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getUniversidadeId() != null) {
            predicates.add(builder.equal(root.get("universidade").get("id"), filter.getUniversidadeId()));
        }

        if (StringUtils.hasText(filter.getNome())) {
            predicates.add(builder.like(builder.lower(root.get("nome")),
                    "%" + filter.getNome().trim().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(filter.getTurno())) {
            predicates.add(builder.equal(builder.upper(root.get("turno")),
                    filter.getTurno().trim().toUpperCase()));
        }

        if (StringUtils.hasText(filter.getCampus())) {
            predicates.add(builder.like(builder.lower(root.get("campus")),
                    "%" + filter.getCampus().trim().toLowerCase() + "%"));
        }

        criteria.select(root)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(builder.asc(root.get("nome")), builder.asc(root.get("campus")));

        return manager.createQuery(criteria).getResultList();
    }
}
