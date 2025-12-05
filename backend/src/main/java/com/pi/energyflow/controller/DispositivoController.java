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

import com.pi.energyflow.model.Dispositivo;
import com.pi.energyflow.service.DispositivoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Dispositivos", description = "Gerenciamento de dispositivos")
@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoController {

    @Autowired
    private DispositivoService dispositivoService;

    @Operation(summary = "Lista todos os dispositivos")
    @GetMapping
    public ResponseEntity<List<Dispositivo>> getAll() {
        return ResponseEntity.ok(dispositivoService.getAll());
    }

    @Operation(summary = "Busca dispositivo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Dispositivo> getById(@PathVariable Long id) {
        return dispositivoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Busca dispositivos por nome")
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Dispositivo>> getByNome(@PathVariable String nome) {
        return ResponseEntity.ok(dispositivoService.getByNome(nome));
    }

    @Operation(summary = "Busca dispositivos por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Dispositivo>> getByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(dispositivoService.getByTipo(tipo));
    }

    @Operation(summary = "Busca dispositivos por status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Dispositivo>> getByStatus(@PathVariable Boolean status) {
        return ResponseEntity.ok(dispositivoService.getByStatus(status));
    }

    @Operation(summary = "Busca dispositivos por ID do ambiente")
    @GetMapping("/ambientes/{id}")
    public ResponseEntity<List<Dispositivo>> getByAmbienteId(@PathVariable Long id) {
        return ResponseEntity.ok(dispositivoService.getByAmbienteId(id));
    }

    @Operation(summary = "Busca dispositivos pelo nome do ambiente")
    @GetMapping("/ambientes/nome/{nome}")
    public ResponseEntity<List<Dispositivo>> getByAmbienteNome(@PathVariable String nome) {
        return ResponseEntity.ok(dispositivoService.getByAmbienteNome(nome));
    }

    @Operation(summary = "Cria um novo dispositivo")
    @PostMapping
    public ResponseEntity<Dispositivo> post(@Valid @RequestBody Dispositivo dispositivo) {

        return dispositivoService.salvar(dispositivo)
                .map(salvo -> ResponseEntity.status(HttpStatus.CREATED).body(salvo))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Atualiza um dispositivo existente")
    @PutMapping
    public ResponseEntity<Dispositivo> put(@Valid @RequestBody Dispositivo dispositivo) {

        return dispositivoService.atualizar(dispositivo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta um dispositivo")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        boolean removido = dispositivoService.delete(id);

        if (!removido)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
