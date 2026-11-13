package com.project.vesthubApi.controller;

import com.project.vesthubApi.dto.RankingDto;
import com.project.vesthubApi.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    // O usuário vem sempre do Authentication (login); universidadeId é um filtro opcional
    @GetMapping
    public List<RankingDto> listar(@RequestParam(required = false) Long universidadeId,
                                   Authentication authentication) {
        return rankingService.listar(authentication.getName(), universidadeId);
    }
}
