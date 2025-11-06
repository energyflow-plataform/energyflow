package com.pi.energyflow.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi.energyflow.model.Usuario;
import com.pi.energyflow.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuários", description = "Gerenciamento de usuários")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Operation(summary = "Lista todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados.")
	@GetMapping
	public ResponseEntity<List<Usuario>> getAll() {
		return ResponseEntity.ok(usuarioService.getAll());
	}
	
	@Operation(summary = "Busca usuário por ID", description = "Retorna um usuário específico baseado no ID fornecido.")
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getById(@PathVariable Long id) {
		return usuarioService.getById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@Operation(summary = "Atualiza um usuário", description = "Atualiza as informações de um usuário existente.")
	@PutMapping("/{id}")
	public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario) {
		return usuarioService.atualizarUsuario(usuario)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@Operation(summary = "Solicita recuperação de senha", description = "Envia um e-mail para o usuário com instruções para recuperação de senha.")
	@PostMapping("/senha/recuperar")
	public ResponseEntity<String> solicitarRecuperacao(@RequestBody Map<String, String> request) {
	    String email = request.get("email");
	    boolean enviado = usuarioService.solicitarRecuperacaoSenha(email);
	    return enviado
	        ? ResponseEntity.ok("E-mail de recuperação enviado com sucesso.")
	        : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
	}

	@Operation(summary = "Redefine a senha do usuário", description = "Permite ao usuário redefinir sua senha usando um token de recuperação.")
	@PostMapping("/senha/resetar")
	public ResponseEntity<String> resetarSenha(@RequestBody Map<String, String> request) {
	    String token = request.get("token");
	    String novaSenha = request.get("novaSenha");
	    boolean sucesso = usuarioService.redefinirSenha(token, novaSenha);
	    return sucesso
	        ? ResponseEntity.ok("Senha atualizada com sucesso.")
	        : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido ou expirado.");
	}

}
