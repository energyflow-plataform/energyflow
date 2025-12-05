package com.pi.energyflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pi.energyflow.model.Unidade;

public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

    List<Unidade> findByCriadoPorId(Long usuarioId);

    List<Unidade> findByNomeContainingIgnoreCaseAndCriadoPorId(String nome, Long usuarioId);
}
