package com.pi.energyflow.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi.energyflow.model.Usuario;
import com.pi.energyflow.model.UsuarioLogin;
import com.pi.energyflow.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Autenticação", description = "Gerenciamento de autenticação de usuários")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
	private UsuarioService usuarioService;

    @Operation(summary = "Login de usuário", description = "Autentica um usuário com email e senha.")
    @PostMapping("/login")
    public ResponseEntity<UsuarioLogin> login(@Valid @RequestBody Optional<UsuarioLogin> usuarioLogin) {
        return usuarioService.autenticarUsuario(usuarioLogin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @Operation(summary = "Registro de usuário", description = "Cadastra um novo usuário no sistema.")
    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@Valid @RequestBody Usuario usuario) {
        return usuarioService.cadastrarUsuario(usuario)
                .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
                .orElse(ResponseEntity.badRequest().build());
    }
}
