package com.pi.energyflow.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pi.energyflow.model.Usuario;
import com.pi.energyflow.model.UsuarioLogin;
import com.pi.energyflow.repository.UsuarioRepository;
import com.pi.energyflow.security.JwtService;
import com.pi.energyflow.util.EmailTemplates;

@Service
public class UsuarioService {

	@Value("${frontend.url}")
	private String frontendUrl;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Usuario> getAll() {
		return usuarioRepository.findAll();
	}

	public Optional<Usuario> getById(Long id) {
		return usuarioRepository.findById(id);
	}

	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
		if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent())
			return Optional.empty();

		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		usuario.setId(null);

		Usuario novoUsuario = usuarioRepository.save(usuario);
		enviarEmailBoasVindas(novoUsuario);

		return Optional.of(novoUsuario);
	}

	public Optional<Usuario> atualizarUsuario(Usuario usuario) {
		if (!usuarioRepository.findById(usuario.getId()).isPresent())
			return Optional.empty();

		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
		if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe.", null);

		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

		return Optional.of(usuarioRepository.save(usuario));
	}

	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {
		if (!usuarioLogin.isPresent())
			return Optional.empty();

		UsuarioLogin login = usuarioLogin.get();

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getSenha()));
			return usuarioRepository.findByEmail(login.getEmail())
					.map(usuario -> construirRespostaLogin(login, usuario));

		} catch (Exception e) {
			return Optional.empty();
		}
	}

	private UsuarioLogin construirRespostaLogin(UsuarioLogin usuarioLogin, Usuario usuario) {
		usuarioLogin.setId(usuario.getId());
		usuarioLogin.setNome(usuario.getNome());
		usuarioLogin.setSobrenome(usuario.getSobrenome());
		usuarioLogin.setFoto(usuario.getFoto());
		usuarioLogin.setSenha("");
		usuarioLogin.setToken(gerarToken(usuario.getEmail()));
		return usuarioLogin;
	}

	private String gerarToken(String usuario) {
		return "Bearer " + jwtService.generateToken(usuario);
	}

	public boolean solicitarRecuperacaoSenha(String email) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
		if (usuarioOpt.isEmpty())
			return false;

		Usuario usuario = usuarioOpt.get();
		String token = UUID.randomUUID().toString();

		usuario.setTokenRecuperacao(token);
		usuario.setExpiracaoToken(LocalDateTime.now().plusMinutes(30));
		usuarioRepository.save(usuario);

		String link = frontendUrl + "/resetar-senha.html?token=" + token;
		String corpoHtml = EmailTemplates.recuperacaoSenha(usuario.getNome(), link);

		try {
			emailService.enviarEmailHtml(email, "Recuperação de Senha - EnergyFlow", corpoHtml);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Não foi possível enviar o e-mail de recuperação de senha.");
		}

		return true;

	}

	public boolean redefinirSenha(String token, String novaSenha) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findByTokenRecuperacao(token);
		if (usuarioOpt.isEmpty())
			return false;

		Usuario usuario = usuarioOpt.get();

		if (usuario.getExpiracaoToken().isBefore(LocalDateTime.now()))
			return false;

		usuario.setSenha(passwordEncoder.encode(novaSenha));
		usuario.setTokenRecuperacao(null);
		usuario.setExpiracaoToken(null);
		usuarioRepository.save(usuario);

		return true;
	}

	private void enviarEmailBoasVindas(Usuario usuario) {
		String corpoHtml = EmailTemplates.boasVindas(usuario.getNome());
		try {
			emailService.enviarEmailHtml(usuario.getEmail(), "Bem-vindo ao EnergyFlow ⚡", corpoHtml);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Não foi possível enviar o e-mail de boas-vindas.");
		}
	}

}
