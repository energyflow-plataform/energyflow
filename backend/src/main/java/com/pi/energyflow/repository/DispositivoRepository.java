package com.pi.energyflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pi.energyflow.model.Dispositivo;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {

	List<Dispositivo> findAllByNomeContainingIgnoreCase(String nome);
	List<Dispositivo> findAllByTipoContainingIgnoreCase(String tipo);
	List<Dispositivo> findAllByStatus(Boolean status);
	List<Dispositivo> findAllByAmbienteId(Long id);
	List<Dispositivo> findAllByAmbienteNomeContainingIgnoreCase(String nome);

}