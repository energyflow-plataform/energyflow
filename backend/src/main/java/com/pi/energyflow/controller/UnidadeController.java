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

import com.pi.energyflow.model.Unidade;
import com.pi.energyflow.service.UnidadeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Unidade", description = "Gerenciamento de unidades")
@RestController
@RequestMapping("/api/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeService unidadeService;

    @GetMapping
    public ResponseEntity<List<Unidade>> getAll() {
		return ResponseEntity.ok(unidadeService.getAll());
	}

    @Operation(summary = "Busca unidade por ID", description = "Retorna uma unidade específica baseado no ID fornecido.")
    @GetMapping("/{id}")
    public ResponseEntity<Unidade> getById(@PathVariable Long id) {
    	return unidadeService.getById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());  
    }
    
    @Operation(summary = "Busca unidades por nome", description = "Retorna uma lista de unidades que correspondem ao nome fornecido.")
    @GetMapping("/nome/{nome}")
	public ResponseEntity<List<Unidade>> getByNome(@PathVariable String nome) {
		return ResponseEntity.ok(unidadeService.getByNome(nome));
	}
    
    @Operation(summary = "Atualiza uma unidade", description = "Atualiza as informações de uma unidade existente.")
    @PutMapping
	public ResponseEntity<Unidade> put(@Valid @RequestBody Unidade unidade) {
    	if (unidade.getId() == null || unidadeService.getById(unidade.getId()).isEmpty()) 
            return ResponseEntity.notFound().build();

        Unidade atualizada = unidadeService.salvar(unidade);
        return ResponseEntity.ok(atualizada);
    }

    @Operation(summary = "Salva uma nova unidade", description = "Salva uma nova unidade no sistema.")
    @PostMapping
    public ResponseEntity<Unidade> salvar(@RequestBody Unidade unidade) {
        Unidade salva = unidadeService.salvar(unidade);
        return ResponseEntity.ok(salva);
    }

    @Operation(summary = "Deleta uma unidade", description = "Deleta uma unidade existente baseado no ID fornecido.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { 
		Optional<Unidade> unidade = unidadeService.getById(id); 
		if(unidade.isEmpty()) 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		unidadeService.deleteById(id); 
	}
}