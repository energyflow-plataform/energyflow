package com.pi.energyflow.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pi.energyflow.model.Consumo;
import com.pi.energyflow.repository.ConsumoRepository;
import com.pi.energyflow.repository.ConsumoRepository.ConsumoHistoricoDTO;
import com.pi.energyflow.repository.ConsumoRepository.ConsumoPontoDTO;
import com.pi.energyflow.repository.ConsumoRepository.ResumoConsumoDTO;

@Service
public class ConsumoHistoricoService {

    @Autowired
    private ConsumoRepository consumoRepository;

    public ConsumoHistoricoDTO buscarUltimas24h(Long dispositivoId) {
        LocalDateTime fim = LocalDateTime.now();
        LocalDateTime inicio = fim.minusHours(24);

        return montarHistorico(dispositivoId, inicio, fim, "24H");
    }

    public ConsumoHistoricoDTO buscarUltimaSemana(Long dispositivoId) {
        LocalDateTime fim = LocalDateTime.now();
        LocalDateTime inicio = fim.minusDays(7);

        return montarHistorico(dispositivoId, inicio, fim, "SEMANA");
    }

    public ConsumoHistoricoDTO buscarUltimoMes(Long dispositivoId) {
        LocalDateTime fim = LocalDateTime.now();
        LocalDateTime inicio = fim.minusDays(30);

        return montarHistorico(dispositivoId, inicio, fim, "MES");
    }

    private ConsumoHistoricoDTO montarHistorico(Long dispositivoId,
                                                 LocalDateTime inicio,
                                                 LocalDateTime fim,
                                                 String periodo) {

        List<Consumo> consumos =
                consumoRepository.buscarPorIntervalo(dispositivoId, inicio, fim);

        List<ConsumoPontoDTO> pontos = consumos.stream()
                .map(c -> new ConsumoPontoDTO(
                        c.getInicioIntervalo(),
                        c.getConsumoMin(),
                        c.getConsumoMax(),
                        c.getConsumoMedio()
                ))
                .toList();

        Double consumoTotal = consumos.stream()
                .mapToDouble(Consumo::getConsumoMedio)
                .sum();

        Double mediaGeral = consumos.stream()
                .mapToDouble(Consumo::getConsumoMedio)
                .average()
                .orElse(0.0);

        Double maxGeral = consumos.stream()
                .mapToDouble(Consumo::getConsumoMax)
                .max()
                .orElse(0.0);

        Double minGeral = consumos.stream()
                .mapToDouble(Consumo::getConsumoMin)
                .min()
                .orElse(0.0);

        ResumoConsumoDTO resumo = new ResumoConsumoDTO(
                consumoTotal,
                mediaGeral,
                maxGeral,
                minGeral
        );

        return new ConsumoHistoricoDTO(
                dispositivoId,
                periodo,
                inicio,
                fim,
                pontos,
                resumo
        );
    }
}