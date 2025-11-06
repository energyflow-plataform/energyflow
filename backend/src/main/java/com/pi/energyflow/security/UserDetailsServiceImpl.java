package com.pi.energyflow.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pi.energyflow.model.Usuario;
import com.pi.energyflow.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (username == null || username.trim().isEmpty()) {
			throw new UsernameNotFoundException("E-mail não pode ser vazio");
		}
		Optional<Usuario> usuario = usuarioRepository.findByEmail(username);

		if (usuario.isPresent()) {
			return new UserDetailsImpl(usuario.get());
		} else {
			throw new UsernameNotFoundException("Usuário não encontrado: " + username);
		}
	}
}