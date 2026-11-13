package com.project.vesthubApi.service;

import com.project.vesthubApi.dto.RankingDto;
import com.project.vesthubApi.model.Cursos;
import com.project.vesthubApi.model.Desempenhos;
import com.project.vesthubApi.model.NotasCorte;
import com.project.vesthubApi.repository.DesempenhosRepository;
import com.project.vesthubApi.repository.NotasCorteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class RankingService {

    @Autowired
    private DesempenhosRepository desempenhosRepository;

    @Autowired
    private NotasCorteRepository notasCorteRepository;

    public List<RankingDto> listar(String email, Long universidadeId) {
        // Usuário sem desempenho cai aqui com lista vazia e devolve [] (não é erro)
        return desempenhosRepository.findByUsuarioEmail(email).stream()
                // Filtro opcional: só os desempenhos da universidade pedida
                .filter(d -> universidadeId == null || d.getUniversidade().getId().equals(universidadeId))
                // Para cada desempenho, pega as notas de corte da MODALIDADE em que o usuário concorre
                .flatMap(d -> notasCorteRepository.findByModalidadeId(d.getModalidade().getId()).stream()
                        .map(n -> montar(d, n)))
                // Maior diferença primeiro: os cursos com maior chance ficam no topo
                .sorted(Comparator.comparing(RankingDto::getDiferenca, Comparator.reverseOrder())
                        .thenComparing(RankingDto::getCurso))
                .toList();
    }

    private RankingDto montar(Desempenhos desempenho, NotasCorte notaCorte) {
        Cursos curso = notaCorte.getCurso();
        BigDecimal diferenca = desempenho.getNota().subtract(notaCorte.getNotaCorte());
        String situacao = diferenca.signum() >= 0 ? "ACIMA_DO_CORTE" : "ABAIXO_DO_CORTE";

        return new RankingDto(
                curso.getId(),
                curso.getNome(),
                curso.getCampus(),
                curso.getTurno(),
                curso.getUniversidade().getSigla(),
                desempenho.getModalidade().getSigla(),
                notaCorte.getNotaCorte(),
                desempenho.getNota(),
                diferenca,
                situacao);
    }
}
