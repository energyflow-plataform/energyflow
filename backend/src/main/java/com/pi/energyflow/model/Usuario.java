package com.pi.energyflow.model;
 
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
 
@Entity
@Table(name = "tb_usuarios")
public class Usuario {
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
 
	@NotBlank(message = "Nome é obrigatório.")
	private String nome;
	
	@NotBlank(message = "Sobrenome é obrigatório.")
	private String sobrenome;
 
	@NotBlank(message = "E-mail é obrigatório.")
	@Email(message = "Deve ser um e-mail válido.")
	private String email;
 
	@NotBlank(message = "Senha é obrigatória.")
	@Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres.")
	private String senha;
 
	@Size(max = 1000, message = "O link da foto não pode ser maior do que 1000 caracteres")
	private String foto;
	
	private String tokenRecuperacao;
	private LocalDateTime expiracaoToken;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "criadoPor", cascade = CascadeType.REMOVE)
	private List<Unidade> unidadesCriadas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getTokenRecuperacao() {
		return tokenRecuperacao;
	}

	public void setTokenRecuperacao(String tokenRecuperacao) {
		this.tokenRecuperacao = tokenRecuperacao;
	}

	public LocalDateTime getExpiracaoToken() {
		return expiracaoToken;
	}

	public void setExpiracaoToken(LocalDateTime expiracaoToken) {
		this.expiracaoToken = expiracaoToken;
	}

	public List<Unidade> getUnidadesCriadas() {
		return unidadesCriadas;
	}

	public void setUnidadesCriadas(List<Unidade> unidadesCriadas) {
		this.unidadesCriadas = unidadesCriadas;
	}
}