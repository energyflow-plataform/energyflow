package com.pi.energyflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pi.energyflow.model.Ambiente;

public interface AmbienteRepository extends JpaRepository<Ambiente, Long> {

	List<Ambiente> findAllByNomeContainingIgnoreCase(String nome);
	List<Ambiente> findAllByUnidadeId(Long id);
	List<Ambiente> findAllByUnidadeNomeContainingIgnoreCase(String nome);
}