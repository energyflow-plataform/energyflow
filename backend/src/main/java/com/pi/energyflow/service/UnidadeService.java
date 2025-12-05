package com.pi.energyflow.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pi.energyflow.model.Endereco;
import com.pi.energyflow.model.Unidade;
import com.pi.energyflow.model.Usuario;
import com.pi.energyflow.repository.UnidadeRepository;
import com.pi.energyflow.repository.UsuarioRepository;

@Service
public class UnidadeService {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Retorna apenas as unidades criadas pelo usuário logado
     */
    public List<Unidade> getAll() {
        Usuario usuario = getUsuarioLogado();
        return unidadeRepository.findByCriadoPorId(usuario.getId());
    }

    /**
     * Busca por nome, filtrando também pelo usuário logado
     */
    public List<Unidade> getByNome(String nome) {
        Usuario usuario = getUsuarioLogado();
        return unidadeRepository.findByNomeContainingIgnoreCaseAndCriadoPorId(nome, usuario.getId());
    }

    /**
     * Atualiza unidade apenas se ela pertencer ao usuário logado
     */
    public Optional<Unidade> atualizarUnidade(Unidade unidade) {

        Optional<Unidade> unidadeDB = unidadeRepository.findById(unidade.getId());

        if (unidadeDB.isEmpty()) {
            return Optional.empty();
        }

        // Verifica se pertence ao usuário
        Usuario usuario = getUsuarioLogado();
        if (!unidadeDB.get().getCriadoPor().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar esta unidade.");
        }

        Unidade updated = salvar(unidade);
        return Optional.of(updated);
    }

    /**
     * Cria ou atualiza unidade garantindo vínculo com o usuário logado
     */
    public Unidade salvar(Unidade unidade) {

        // --- Completar endereço via CEP ---
        Endereco end = unidade.getEndereco();
        if (end != null && end.getCep() != null && (end.getLogradouro() == null || end.getLogradouro().isBlank())) {
            Endereco enderecoCompleto = enderecoService.buscarPorCep(end.getCep());
            if (enderecoCompleto != null) {
                enderecoCompleto.setNumero(end.getNumero());
                unidade.setEndereco(enderecoCompleto);
            }
        }

        Usuario usuario = getUsuarioLogado();

        // Se for update, garantir que o usuário é o criador
        if (unidade.getId() != null) {
            Unidade antiga = unidadeRepository.findById(unidade.getId())
                    .orElseThrow(() -> new RuntimeException("Unidade não encontrada."));

            if (!antiga.getCriadoPor().getId().equals(usuario.getId())) {
                throw new RuntimeException("Você não tem permissão para alterar esta unidade.");
            }
        }

        // Sempre garantir que o dono é o usuário logado
        unidade.setCriadoPor(usuario);

        // Se unidade nova → definir data de criação
        if (unidade.getId() == null) {
            unidade.setCriadoEm(LocalDateTime.now());
        }

        return unidadeRepository.save(unidade);
    }

    /**
     * Apaga unidade apenas se for do usuário logado
     */
    public void deleteById(Long id) {

        Usuario usuario = getUsuarioLogado();

        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada."));

        if (!unidade.getCriadoPor().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para excluir esta unidade.");
        }

        unidadeRepository.deleteById(id);
    }

    /**
     * Busca unidade por ID, garantindo que pertence ao usuário logado
     */
    public Optional<Unidade> getById(Long id) {
        Usuario usuario = getUsuarioLogado();

        return unidadeRepository.findById(id)
                .filter(u -> u.getCriadoPor().getId().equals(usuario.getId()));
    }

    /**
     * Retorna o usuário atualmente logado através do SecurityContext
     */
    private Usuario getUsuarioLogado() {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
    }
}
