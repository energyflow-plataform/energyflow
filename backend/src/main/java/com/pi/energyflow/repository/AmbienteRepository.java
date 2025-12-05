package com.pi.energyflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pi.energyflow.model.Ambiente;

public interface AmbienteRepository extends JpaRepository<Ambiente, Long> {

    List<Ambiente> findAllByUnidadeCriadoPorId(Long usuarioId);

    List<Ambiente> findAllByNomeContainingIgnoreCaseAndUnidadeCriadoPorId(String nome, Long usuarioId);

    List<Ambiente> findAllByUnidadeIdAndUnidadeCriadoPorId(Long unidadeId, Long usuarioId);
}
