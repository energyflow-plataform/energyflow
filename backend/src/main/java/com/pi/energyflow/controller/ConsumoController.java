package com.pi.energyflow.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pi.energyflow.repository.ConsumoRepository.ConsumoDTO;
import com.pi.energyflow.service.ConsumoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Consumo", description = "Gerenciamento de consumo de energia")
@RestController
@RequestMapping("/consumo")
public class ConsumoController {

    @Autowired
    private ConsumoService consumoService;

    @Operation(summary = "Buscar consumo por intervalo", description = "Busca o consumo de energia de um dispositivo em um intervalo de datas.")
    @GetMapping("/dispositivo/{id}")
    public ResponseEntity<List<ConsumoDTO>> buscarPorIntervalo(
            @PathVariable Long id,
            @RequestParam String inicio,
            @RequestParam String fim
    ) {

        LocalDateTime dataInicio = LocalDateTime.parse(inicio);
        LocalDateTime dataFim = LocalDateTime.parse(fim);

        return ResponseEntity.ok(
                consumoService.buscarConsumoPorIntervalo(id, dataInicio, dataFim)
        );
    }
}