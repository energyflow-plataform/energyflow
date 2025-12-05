package com.pi.energyflow.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pi.energyflow.model.Consumo;
import com.pi.energyflow.model.ConsumoTempoReal;
import com.pi.energyflow.model.Dispositivo;
import com.pi.energyflow.repository.ConsumoRepository;
import com.pi.energyflow.repository.ConsumoTempoRealRepository;
import com.pi.energyflow.repository.DispositivoRepository;

@Service
public class ConsumoAgregadorService {

    @Autowired
    private ConsumoTempoRealRepository consumoTempoRealRepository;

    @Autowired
    private ConsumoRepository consumoRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void processarConsumoPorIntervalo() {

        LocalDateTime fim = LocalDateTime.now();
        LocalDateTime inicio = fim.minusMinutes(5);

        System.out.println("Iniciando agregação: " + inicio + " até " + fim);

        List<ConsumoTempoReal> registros =
                consumoTempoRealRepository.buscarPorIntervalo(inicio, fim);

        if (registros.isEmpty()) {
            System.out.println("Nenhum registro encontrado no intervalo.");
            return;
        }

        Map<Long, List<ConsumoTempoReal>> agrupados =
                registros.stream()
                        .collect(Collectors.groupingBy(r ->
                                r.getDispositivo().getId()));

        for (var entry : agrupados.entrySet()) {

            Long idDispositivo = entry.getKey();
            List<ConsumoTempoReal> lista = entry.getValue();

            Double min = lista.stream()
                    .map(ConsumoTempoReal::getConsumoAtual)
                    .min(Double::compare)
                    .orElse(0.0);

            Double max = lista.stream()
                    .map(ConsumoTempoReal::getConsumoAtual)
                    .max(Double::compare)
                    .orElse(0.0);

            Double media = lista.stream()
                    .mapToDouble(ConsumoTempoReal::getConsumoAtual)
                    .average()
                    .orElse(0.0);

            Dispositivo dispositivo = dispositivoRepository
                    .findById(idDispositivo)
                    .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado"));

            Consumo consumo = new Consumo();
            consumo.setDispositivo(dispositivo);
            consumo.setInicioIntervalo(inicio);
            consumo.setFimIntervalo(fim);
            consumo.setConsumoMin(min);
            consumo.setConsumoMax(max);
            consumo.setConsumoMedio(media);

            consumoRepository.save(consumo);
        }

        consumoTempoRealRepository.deletarAte(fim);

        System.out.println("Agregação finalizada com sucesso.");
    }
}