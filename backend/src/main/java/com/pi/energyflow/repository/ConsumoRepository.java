package com.pi.energyflow.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pi.energyflow.model.Consumo;

@Repository
public interface ConsumoRepository extends JpaRepository<Consumo, Long> {

    @Query("""
       SELECT c 
       FROM Consumo c 
       WHERE c.dispositivo.id = :dispositivoId
       AND c.inicioIntervalo >= :inicio
       AND c.fimIntervalo <= :fim
       ORDER BY c.inicioIntervalo ASC
    """)
    List<Consumo> buscarPorIntervalo(
            @Param("dispositivoId") Long dispositivoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
    
    public record ConsumoDTO(
            LocalDateTime inicioIntervalo,
            LocalDateTime fimIntervalo,
            Double consumoMin,
            Double consumoMax,
            Double consumoMedio
    ) {}
    
    public record ConsumoPontoDTO(
            LocalDateTime timestamp,
            Double min,
            Double max,
            Double medio
    ) {}

    public record ResumoConsumoDTO(
            Double consumoTotal,
            Double mediaGeral,
            Double maxGeral,
            Double minGeral
    ) {}

    public record ConsumoHistoricoDTO(
            Long dispositivoId,
            String periodo,
            LocalDateTime inicio,
            LocalDateTime fim,
            List<ConsumoPontoDTO> pontos,
            ResumoConsumoDTO resumo
    ) {}

}
