package com.pi.energyflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pi.energyflow.model.Ambiente;
import com.pi.energyflow.model.Dispositivo;
import com.pi.energyflow.model.Usuario;
import com.pi.energyflow.repository.AmbienteRepository;
import com.pi.energyflow.repository.DispositivoRepository;
import com.pi.energyflow.repository.UsuarioRepository;

@Service
public class DispositivoService {

	@Autowired
	private DispositivoRepository dispositivoRepository;

	@Autowired
	private AmbienteRepository ambienteRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private Usuario getUsuarioLogado() {
		String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

		return usuarioRepository.findByEmail(emailUsuario)
				.orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
	}

	public List<Dispositivo> getAll() {
		Usuario usuario = getUsuarioLogado();
		return dispositivoRepository.findAllByAmbienteUnidadeCriadoPorIdAndAtivoTrue(usuario.getId());
	}

	public Optional<Dispositivo> getById(Long id) {
		Usuario usuario = getUsuarioLogado();

		return dispositivoRepository.findById(id)
				.filter(d -> d.getAmbiente().getUnidade().getCriadoPor().getId().equals(usuario.getId()));
	}

	public List<Dispositivo> getByNome(String nome) {
		Usuario usuario = getUsuarioLogado();
		return dispositivoRepository.findAllByNomeContainingIgnoreCaseAndAmbienteUnidadeCriadoPorIdAndAtivoTrue(nome,
				usuario.getId());
	}

	public List<Dispositivo> getByTipo(String tipo) {
		Usuario usuario = getUsuarioLogado();
		return dispositivoRepository.findAllByTipoContainingIgnoreCaseAndAmbienteUnidadeCriadoPorIdAndAtivoTrue(tipo,
				usuario.getId());
	}

	public List<Dispositivo> getByStatus(Boolean status) {
		Usuario usuario = getUsuarioLogado();
		return dispositivoRepository.findAllByStatusAndAmbienteUnidadeCriadoPorIdAndAtivoTrue(status, usuario.getId());
	}

	public List<Dispositivo> getByAmbienteId(Long id) {
		Usuario usuario = getUsuarioLogado();
		return dispositivoRepository.findAllByAmbienteIdAndAmbienteUnidadeCriadoPorIdAndAtivoTrue(id, usuario.getId());
	}

	public List<Dispositivo> getByAmbienteNome(String nome) {
		Usuario usuario = getUsuarioLogado();
		return dispositivoRepository.findAllByAmbienteNomeContainingIgnoreCaseAndAmbienteUnidadeCriadoPorIdAndAtivoTrue(nome,
				usuario.getId());
	}

	public Optional<Dispositivo> salvar(Dispositivo dispositivo) {

		Usuario usuario = getUsuarioLogado();

		if (dispositivo.getAmbiente() == null || dispositivo.getAmbiente().getId() == null)
			return Optional.empty();

		Ambiente ambiente = ambienteRepository.findById(dispositivo.getAmbiente().getId())
				.orElseThrow(() -> new RuntimeException("Ambiente não encontrado"));

		if (!ambiente.getUnidade().getCriadoPor().getId().equals(usuario.getId())) {
			throw new RuntimeException("Você não pode adicionar dispositivos em ambientes de outros usuários.");
		}

		dispositivo.setId(null);
		dispositivo.setAmbiente(ambiente);

		return Optional.of(dispositivoRepository.save(dispositivo));
	}

	public Optional<Dispositivo> atualizar(Dispositivo dispositivo) {

		Usuario usuario = getUsuarioLogado();

		Dispositivo dispositivoDB = dispositivoRepository.findById(dispositivo.getId())
				.orElseThrow(() -> new RuntimeException("Dispositivo não encontrado."));

		if (!dispositivoDB.getAmbiente().getUnidade().getCriadoPor().getId().equals(usuario.getId())) {
			throw new RuntimeException("Você não pode editar dispositivos de outros usuários.");
		}

		return Optional.of(dispositivoRepository.save(dispositivo));
	}

	public boolean delete(Long id) {
		Usuario usuario = getUsuarioLogado();
		Dispositivo dispositivo = dispositivoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Dispositivo não encontrado."));

		if (!dispositivo.getAmbiente().getUnidade().getCriadoPor().getId().equals(usuario.getId())) {
			throw new RuntimeException("Você não pode excluir dispositivos de outros usuários.");
		}

		dispositivo.setAtivo(false);
		dispositivoRepository.save(dispositivo); 

		return true;
	}

}