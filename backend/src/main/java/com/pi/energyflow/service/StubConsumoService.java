package com.pi.energyflow.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pi.energyflow.model.ConsumoTempoReal;
import com.pi.energyflow.model.Dispositivo;
import com.pi.energyflow.repository.ConsumoTempoRealRepository;
import com.pi.energyflow.repository.DispositivoRepository;

@Service
public class StubConsumoService {

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private ConsumoTempoRealRepository consumoTempoRealRepository;

    @Scheduled(fixedRate = 2000)
    public void gerarConsumo() {

        List<Dispositivo> dispositivos = dispositivoRepository.findAllByStatus(true);

        for (Dispositivo dispositivo : dispositivos) {

            double consumo = gerarConsumoSimulado(dispositivo.getPotencia());

            ConsumoTempoReal registro = new ConsumoTempoReal();
            registro.setDispositivo(dispositivo);
            registro.setConsumoAtual(consumo);
            registro.setDataRegistro(LocalDateTime.now());

            consumoTempoRealRepository.save(registro);
        }
    }

    private Double gerarConsumoSimulado(Double potencia) {
        double variacao = Math.random() * 0.4;
        double valor = potencia * (0.6 + variacao);
        return Math.round(valor * 100.0) / 100.0;
    }
}
