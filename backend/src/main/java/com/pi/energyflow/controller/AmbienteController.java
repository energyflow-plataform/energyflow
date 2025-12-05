package com.pi.energyflow.controller;

import java.util.List;

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
import com.pi.energyflow.service.AmbienteService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Ambientes", description = "Gerenciamento de ambientes")
@RestController
@RequestMapping("/api/ambientes")
public class AmbienteController {
	
    @Autowired
    private AmbienteService ambienteService;

    @GetMapping
    public ResponseEntity<List<Ambiente>> getAll() {
        return ResponseEntity.ok(ambienteService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ambiente> getById(@PathVariable Long id) {
        return ambienteService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Ambiente>> getByNome(@PathVariable String nome) {
        return ResponseEntity.ok(ambienteService.getByNome(nome));
    }

    @GetMapping("/unidades/{id}")
    public ResponseEntity<List<Ambiente>> getByUnidade(@PathVariable Long id) {
        return ResponseEntity.ok(ambienteService.getByUnidade(id));
    }

    @PostMapping
    public ResponseEntity<Ambiente> salvar(@Valid @RequestBody Ambiente ambiente) {
        return ResponseEntity.status(201).body(ambienteService.salvar(ambiente));
    }

    @PutMapping
    public ResponseEntity<Ambiente> atualizar(@Valid @RequestBody Ambiente ambiente) {
        return ResponseEntity.ok(ambienteService.atualizar(ambiente));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    	Ambiente ambiente = ambienteService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!ambiente.getDispositivo().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Não é possível excluir um ambiente que possui dispositivos vinculados."
            );
        }

        ambienteService.delete(id);
    }
}