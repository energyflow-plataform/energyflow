package com.pi.energyflow.util;

import com.pi.energyflow.model.Usuario;
import com.pi.energyflow.model.UsuarioLogin;

public class TestBuilder {

	public static Usuario criarUsuario(Long id, String nome, String sobrenome, String email, String senha) {
		Usuario novoUsuario = new Usuario();
		novoUsuario.setId(id);
		novoUsuario.setNome(nome);
		novoUsuario.setSobrenome(sobrenome);
		novoUsuario.setEmail(email);
		novoUsuario.setSenha(senha);
		novoUsuario.setFoto("-");
		return novoUsuario;
	}
	
	public static UsuarioLogin criarUsuarioLogin(String email, String senha) {
		UsuarioLogin novoUsuarioLogin =  new UsuarioLogin();
		novoUsuarioLogin.setEmail(email);
		novoUsuarioLogin.setSenha(senha);
		return novoUsuarioLogin;
	}
}
