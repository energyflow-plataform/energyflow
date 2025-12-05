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

import com.pi.energyflow.model.Unidade;
import com.pi.energyflow.service.UnidadeService;

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

    @GetMapping("/{id}")
    public ResponseEntity<Unidade> getById(@PathVariable Long id) {
        return unidadeService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Unidade>> getByNome(@PathVariable String nome) {
        return ResponseEntity.ok(unidadeService.getByNome(nome));
    }

    @PutMapping
    public ResponseEntity<Unidade> put(@Valid @RequestBody Unidade unidade) {

        if (unidade.getId() == null || unidadeService.getById(unidade.getId()).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Unidade atualizada = unidadeService.salvar(unidade);
        return ResponseEntity.ok(atualizada);
    }

    @PostMapping
    public ResponseEntity<Unidade> salvar(@RequestBody Unidade unidade) {
        Unidade salva = unidadeService.salvar(unidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        Unidade unidade = unidadeService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!unidade.getAmbiente().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Não é possível excluir uma unidade que possui ambientes vinculados."
            );
        }

        unidadeService.deleteById(id);
    }
}