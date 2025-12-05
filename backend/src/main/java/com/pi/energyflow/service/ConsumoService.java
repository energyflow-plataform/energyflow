package com.pi.energyflow.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pi.energyflow.repository.ConsumoRepository;
import com.pi.energyflow.repository.ConsumoRepository.ConsumoDTO;

@Service
public class ConsumoService {

    @Autowired
    private ConsumoRepository consumoRepository;

    public List<ConsumoDTO> buscarConsumoPorIntervalo(
            Long dispositivoId,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        return consumoRepository.buscarPorIntervalo(dispositivoId, inicio, fim)
                .stream()
                .map(consumo -> new ConsumoDTO(
                        consumo.getInicioIntervalo(),
                        consumo.getFimIntervalo(),
                        consumo.getConsumoMin(),
                        consumo.getConsumoMax(),
                        consumo.getConsumoMedio()
                ))
                .toList();
    }
}