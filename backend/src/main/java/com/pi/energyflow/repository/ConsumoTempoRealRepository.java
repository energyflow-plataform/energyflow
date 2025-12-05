package com.pi.energyflow.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pi.energyflow.model.ConsumoTempoReal;

@Repository
public interface ConsumoTempoRealRepository extends JpaRepository<ConsumoTempoReal, Long> {

    @Query("""
        SELECT c FROM ConsumoTempoReal c 
        WHERE c.dispositivo.id = :idDispositivo
        ORDER BY c.dataRegistro DESC
    """)
    List<ConsumoTempoReal> buscarUltimos(@Param("idDispositivo") Long idDispositivo, Pageable pageable);

    @Query("""
        SELECT c FROM ConsumoTempoReal c 
        WHERE c.dataRegistro >= :inicio
    """)
    List<ConsumoTempoReal> buscarPorPeriodo(@Param("inicio") LocalDateTime inicio);
    
    @Query("""
    	    SELECT c FROM ConsumoTempoReal c
    	    WHERE c.dataRegistro BETWEEN :inicio AND :fim
    	""")
    	List<ConsumoTempoReal> buscarPorIntervalo(
    	        @Param("inicio") LocalDateTime inicio,
    	        @Param("fim") LocalDateTime fim
    	);

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM ConsumoTempoReal c
        WHERE c.dataRegistro <= :limite
    """)
    void deletarAte(@Param("limite") LocalDateTime limite);

}

