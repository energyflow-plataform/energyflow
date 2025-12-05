package com.pi.energyflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi.energyflow.repository.ConsumoRepository.ConsumoHistoricoDTO;
import com.pi.energyflow.service.ConsumoHistoricoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Consumo Histórico", description = "API para obter o consumo histórico de energia dos dispositivos.")
@RestController
@RequestMapping("/api/consumo")
public class ConsumoHistoricoController {

    @Autowired
    private ConsumoHistoricoService consumoHistoricoService;

    @Operation(summary = "Buscar consumo das últimas 24 horas", description = "Retorna o consumo histórico dos últimos 24 horas para um dispositivo específico.")
    @GetMapping("/dispositivo/{id}/24h")
    public ResponseEntity<ConsumoHistoricoDTO> ultimas24h(@PathVariable Long id) {
        return ResponseEntity.ok(consumoHistoricoService.buscarUltimas24h(id));
    }

    @Operation(summary = "Buscar consumo da última semana", description = "Retorna o consumo histórico da última semana para um dispositivo específico.")
    @GetMapping("/dispositivo/{id}/semana")
    public ResponseEntity<ConsumoHistoricoDTO> ultimaSemana(@PathVariable Long id) {
        return ResponseEntity.ok(consumoHistoricoService.buscarUltimaSemana(id));
    }

    @Operation(summary = "Buscar consumo do último mês", description = "Retorna o consumo histórico do último mês para um dispositivo específico.")
    @GetMapping("/dispositivo/{id}/mes")
    public ResponseEntity<ConsumoHistoricoDTO> ultimoMes(@PathVariable Long id) {
        return ResponseEntity.ok(consumoHistoricoService.buscarUltimoMes(id));
    }
}