package com.pi.energyflow.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi.energyflow.model.ConsumoTempoReal;
import com.pi.energyflow.repository.ConsumoTempoRealRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Consumo Tempo Real", description = "API para obter o consumo de energia em tempo real dos dispositivos.")
@RestController
@RequestMapping("/api/consumo")
public class ConsumoTempoRealController {

    @Autowired
    private ConsumoTempoRealRepository consumoRepository;

    @Operation(summary = "Buscar último consumo em tempo real", description = "Retorna o último registro de consumo em tempo real para um dispositivo específico.")
    @GetMapping("/tempo-real/{idDispositivo}")
    public ResponseEntity<?> buscarUltimoConsumo(@PathVariable Long idDispositivo) {
        List<ConsumoTempoReal> lista = consumoRepository
                .buscarUltimos(idDispositivo, PageRequest.of(0, 1));

        if (lista.isEmpty()) 
            return ResponseEntity.noContent().build();

        ConsumoTempoReal consumo = lista.get(0);
        return ResponseEntity.ok(Map.of(
                "dispositivoId", idDispositivo,
                "consumo", consumo.getConsumoAtual(),
                "data", consumo.getDataRegistro()
        ));
    }
}
