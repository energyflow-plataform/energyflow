package com.pi.energyflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pi.energyflow.model.Dispositivo;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {

	List<Dispositivo> findAllByAmbienteUnidadeCriadoPorIdAndAtivoTrue(Long usuarioId);
	List<Dispositivo> findAllByNomeContainingIgnoreCaseAndAmbienteUnidadeCriadoPorIdAndAtivoTrue(String nome, Long usuarioId);
	List<Dispositivo> findAllByTipoContainingIgnoreCaseAndAmbienteUnidadeCriadoPorIdAndAtivoTrue(String tipo, Long usuarioId);
	List<Dispositivo> findAllByStatusAndAmbienteUnidadeCriadoPorIdAndAtivoTrue(Boolean status, Long usuarioId);
	List<Dispositivo> findAllByAmbienteIdAndAmbienteUnidadeCriadoPorIdAndAtivoTrue(Long ambienteId, Long usuarioId);
	List<Dispositivo> findAllByAmbienteNomeContainingIgnoreCaseAndAmbienteUnidadeCriadoPorIdAndAtivoTrue(String nome, Long usuarioId);
	List<Dispositivo> findAllByStatus(Boolean status);
}
