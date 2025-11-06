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
    
    public List<Unidade> getAll() {
        return unidadeRepository.findAll();
    }
    
    public List<Unidade> getByNome(String nome) {
    	return unidadeRepository.findByNomeContainingIgnoreCase(nome);
    }

	public Optional<Unidade> atualizarUnidade(Unidade unidade) {
        if (unidadeRepository.existsById(unidade.getId())) {
            Unidade updatedUnidade = salvar(unidade);
            return Optional.of(updatedUnidade);
        } else 
            return Optional.empty();
	}
    
    public Unidade salvar(Unidade unidade) {
        Endereco end = unidade.getEndereco();
        if (end != null && end.getCep() != null && (end.getLogradouro() == null || end.getLogradouro().isBlank())) {
            Endereco enderecoCompleto = enderecoService.buscarPorCep(end.getCep());
            if (enderecoCompleto != null) {
                enderecoCompleto.setNumero(end.getNumero());
                unidade.setEndereco(enderecoCompleto);
            }
        }

        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));

        unidade.setCriadoPor(usuario);
        unidade.setCriadoEm(LocalDateTime.now());

        return unidadeRepository.save(unidade);
    }

    public void deleteById(Long id) {
        unidadeRepository.deleteById(id);
    }

    public Optional<Unidade> getById(Long id) {
		return unidadeRepository.findById(id);
	}
}
