package com.pi.energyflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi.energyflow.model.Endereco;
import com.pi.energyflow.service.EnderecoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Endereço", description = "Gerenciamento de endereços via CEP")
@RestController
@RequestMapping("/api/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Operation(summary = "Buscar endereço por CEP", description = "Retorna o endereço correspondente ao CEP fornecido.")
    @GetMapping("/{cep}")
    public ResponseEntity<?> buscarEndereco(@PathVariable String cep) {
        Endereco endereco = enderecoService.buscarPorCep(cep);

        if (endereco == null) {
            return ResponseEntity.badRequest().body("CEP inválido ou não encontrado.");
        }

        return ResponseEntity.ok(endereco);
    }
}