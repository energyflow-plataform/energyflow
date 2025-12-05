package com.pi.energyflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pi.energyflow.model.Ambiente;
import com.pi.energyflow.model.Unidade;
import com.pi.energyflow.model.Usuario;
import com.pi.energyflow.repository.AmbienteRepository;
import com.pi.energyflow.repository.UnidadeRepository;
import com.pi.energyflow.repository.UsuarioRepository;

@Service
public class AmbienteService {

    @Autowired
    private AmbienteRepository ambienteRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    private Usuario getUsuarioLogado() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
    }

    public List<Ambiente> getAll() {
        Usuario usuario = getUsuarioLogado();
        return ambienteRepository.findAllByUnidadeCriadoPorId(usuario.getId());
    }

    public Optional<Ambiente> getById(Long id) {
        Usuario usuario = getUsuarioLogado();

        return ambienteRepository.findById(id)
            .filter(a -> a.getUnidade().getCriadoPor().getId().equals(usuario.getId()));
    }

    public List<Ambiente> getByNome(String nome) {
        Usuario usuario = getUsuarioLogado();
        return ambienteRepository.findAllByNomeContainingIgnoreCaseAndUnidadeCriadoPorId(nome, usuario.getId());
    }

    public List<Ambiente> getByUnidade(Long id) {
        Usuario usuario = getUsuarioLogado();
        return ambienteRepository.findAllByUnidadeIdAndUnidadeCriadoPorId(id, usuario.getId());
    }

    public Ambiente salvar(Ambiente ambiente) {
        Usuario usuario = getUsuarioLogado();

        Unidade unidade = unidadeRepository.findById(ambiente.getUnidade().getId())
            .orElseThrow(() -> new RuntimeException("Unidade não encontrada."));

        if (!unidade.getCriadoPor().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não pode criar ambientes em unidades de outros usuários.");
        }

        ambiente.setUnidade(unidade);

        return ambienteRepository.save(ambiente);
    }

    public Ambiente atualizar(Ambiente ambiente) {
        Usuario usuario = getUsuarioLogado();

        Ambiente ambienteDB = ambienteRepository.findById(ambiente.getId())
            .orElseThrow(() -> new RuntimeException("Ambiente não encontrado."));

        if (!ambienteDB.getUnidade().getCriadoPor().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não pode editar ambientes de outros usuários.");
        }

        return ambienteRepository.save(ambiente);
    }

    public void delete(Long id) {
        Usuario usuario = getUsuarioLogado();

        Ambiente ambiente = ambienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ambiente não encontrado."));

        if (!ambiente.getUnidade().getCriadoPor().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não pode excluir ambientes de outros usuários.");
        }

        ambienteRepository.deleteById(id);
    }
}
