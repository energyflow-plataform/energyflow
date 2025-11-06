package com.pi.energyflow.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pi.energyflow.model.Dispositivo;
import com.pi.energyflow.repository.AmbienteRepository;
import com.pi.energyflow.repository.DispositivoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Dispositivos", description = "Gerenciamento de dispositivos")
@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoController {
	
	@Autowired
	private DispositivoRepository dispositivoRepository;
	
	@Autowired
	private AmbienteRepository ambienteRepository;
	
	@Operation(summary = "Lista todos os dispositivos", description = "Retorna uma lista com todos os dispositivos cadastrados.")
	@GetMapping
	public ResponseEntity<List<Dispositivo>> getAll() {
		return ResponseEntity.ok(dispositivoRepository.findAll());
	}
	
	@Operation(summary = "Busca dispositivo por ID", description = "Retorna um dispositivo específico com base no ID fornecido.")
	@GetMapping("/{id}")
	public ResponseEntity<Dispositivo> getById(@PathVariable Long id) {
		return dispositivoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@Operation(summary = "Busca dispositivos por nome", description = "Retorna uma lista de dispositivos cujo nome contenha o valor fornecido.")
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Dispositivo>> getAllByNome(@PathVariable String nome) {
		return ResponseEntity.ok(dispositivoRepository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@Operation(summary = "Busca dispositivos por tipo", description = "Retorna uma lista de dispositivos cujo tipo contenha o valor fornecido.")
	@GetMapping("/tipo/{tipo}")
	public ResponseEntity<List<Dispositivo>> getAllByTipo(@PathVariable String tipo) {
		return ResponseEntity.ok(dispositivoRepository.findAllByTipoContainingIgnoreCase(tipo));
	}
	
	@Operation(summary = "Busca dispositivos por status", description = "Retorna uma lista de dispositivos com o status fornecido (true para ligado, false para desligado).")
	@GetMapping("/status/{status}")
	public ResponseEntity<List<Dispositivo>> getAllByStatus(@PathVariable Boolean status) {
		return ResponseEntity.ok(dispositivoRepository.findAllByStatus(status));
	}
	
	@Operation(summary = "Busca dispositivos por ID do ambiente", description = "Retorna uma lista de dispositivos associados a um ambiente específico com base no ID do ambiente fornecido.")
	@GetMapping("/ambientes/{id}")
	public ResponseEntity<List<Dispositivo>> getAllByAmbienteId(@PathVariable Long id) {
		if (!ambienteRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dispositivoRepository.findAllByAmbienteId(id));
	}
	
	@Operation(summary = "Busca dispositivos por nome do ambiente", description = "Retorna uma lista de dispositivos associados a ambientes cujo nome contenha o valor fornecido.")
	@GetMapping("/ambientes/nome/{nome}")
	public ResponseEntity<List<Dispositivo>> getAllByAmbienteNome(@PathVariable String nome) {
		return ResponseEntity.ok(dispositivoRepository.findAllByAmbienteNomeContainingIgnoreCase(nome));
	}
	
	@Operation(summary = "Cria um novo dispositivo", description = "Adiciona um novo dispositivo ao sistema.")
	@PostMapping
	public ResponseEntity<Dispositivo> post(@Valid @RequestBody Dispositivo dispositivo) {
	    if (dispositivo.getAmbiente() == null || dispositivo.getAmbiente().getId() == null) 
	        return ResponseEntity.badRequest().build();

	    if (!ambienteRepository.existsById(dispositivo.getAmbiente().getId())) 
	        return ResponseEntity.notFound().build(); 
	   
	    dispositivo.setId(null);
	    Dispositivo salvo = dispositivoRepository.save(dispositivo);
	    return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
	}

	
	@Operation(summary = "Atualiza um dispositivo existente", description = "Atualiza os detalhes de um dispositivo existente com base no ID fornecido.")
	@PutMapping
	public ResponseEntity<Dispositivo> put(@Valid @RequestBody Dispositivo dispositivo) {
	    if (!dispositivoRepository.existsById(dispositivo.getId())) 
	        return ResponseEntity.notFound().build();
	    
	    if (dispositivo.getAmbiente() == null || dispositivo.getAmbiente().getId() == null) 
	        return ResponseEntity.badRequest().build();
	   
	    if (!ambienteRepository.existsById(dispositivo.getAmbiente().getId()))
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(null); 

	    Dispositivo atualizado = dispositivoRepository.save(dispositivo);
	    return ResponseEntity.ok(atualizado);
	}
	
	@Operation(summary = "Deleta um dispositivo", description = "Remove um dispositivo do sistema com base no ID fornecido.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Dispositivo> dispositivo = dispositivoRepository.findById(id);
		if (dispositivo.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		dispositivoRepository.deleteById(id);
	}
}
