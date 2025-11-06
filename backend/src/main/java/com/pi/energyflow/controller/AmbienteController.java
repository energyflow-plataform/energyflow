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

import com.pi.energyflow.model.Ambiente;
import com.pi.energyflow.repository.AmbienteRepository;
import com.pi.energyflow.repository.UnidadeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Ambientes", description = "Gerenciamento de ambientes")
@RestController
@RequestMapping("/api/ambientes")
public class AmbienteController {
	
	@Autowired
	private AmbienteRepository ambienteRepository;
	
	@Autowired
	private UnidadeRepository unidadeRepository;
	
	@Operation(summary = "Lista todos os ambientes", description = "Retorna uma lista com todos os ambientes cadastrados.")
	@GetMapping
	public ResponseEntity<List<Ambiente>> getAll() {
		return ResponseEntity.ok(ambienteRepository.findAll());
	}
	
	@Operation(summary = "Busca ambiente por ID", description = "Retorna um ambiente específico com base no ID fornecido.")
	@GetMapping("/{id}")
	public ResponseEntity<Ambiente> getById(@PathVariable Long id) {
		return ambienteRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@Operation(summary = "Busca ambientes por nome", description = "Retorna uma lista de ambientes cujo nome contenha o valor fornecido.")
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Ambiente>> getAllByNome(@PathVariable String nome) {
		return ResponseEntity.ok(ambienteRepository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@Operation(summary = "Busca ambientes por ID da unidade", description = "Retorna uma lista de ambientes associados a uma unidade específica com base no ID da unidade fornecido.")
	@GetMapping("/unidades/{id}")
	public ResponseEntity<List<Ambiente>> getAllByUnidadeId(@PathVariable Long id) {
		if (!unidadeRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(ambienteRepository.findAllByUnidadeId(id));
	}
	
	@Operation(summary = "Busca ambientes por nome da unidade", description = "Retorna uma lista de ambientes associados a unidades cujo nome contenha o valor fornecido.")
	@GetMapping("/unidades/nome/{nome}")
	public ResponseEntity<List<Ambiente>> getAllByUnidadeNome(@PathVariable String nome) {
		return ResponseEntity.ok(ambienteRepository.findAllByUnidadeNomeContainingIgnoreCase(nome));
	}
	
	@Operation(summary = "Cria um novo ambiente", description = "Adiciona um novo ambiente ao sistema.")
	@PostMapping
	public ResponseEntity<Ambiente> post(@Valid @RequestBody Ambiente ambiente) {
		if (unidadeRepository.existsById(ambiente.getUnidade().getId())) {
			ambiente.setId(null);
			return ResponseEntity.status(201).body(ambienteRepository.save(ambiente));
		}
		return ResponseEntity.badRequest().build();
	}
	
	@Operation(summary = "Atualiza um ambiente existente", description = "Atualiza os detalhes de um ambiente existente com base no ID fornecido.")
	@PutMapping
	public ResponseEntity<Ambiente> put(@Valid @RequestBody Ambiente ambiente) {
		if (ambienteRepository.existsById(ambiente.getId())) {
			if (unidadeRepository.existsById(ambiente.getUnidade().getId())) {
				return ResponseEntity.ok(ambienteRepository.save(ambiente));
			}
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@Operation(summary = "Deleta um ambiente", description = "Remove um ambiente do sistema com base no ID fornecido.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Ambiente> ambiente = ambienteRepository.findById(id);
		if (ambiente.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		ambienteRepository.deleteById(id);
	}
}
