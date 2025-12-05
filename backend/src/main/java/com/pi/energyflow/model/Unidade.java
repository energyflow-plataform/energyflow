package com.pi.energyflow.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_unidades")
public class Unidade {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Nome é obrigatório.")
	private String nome;
	
	@NotBlank(message = "Código de acesso é obrigatório.")
	private String codigoAcesso;
	
	@ManyToOne
	@JoinColumn(name = "criado_por", nullable = false)
	@JsonIgnoreProperties("unidadesCriadas")
	private Usuario criadoPor;
	
	@UpdateTimestamp
	private LocalDateTime criadoEm;

	@Embedded
	private Endereco endereco;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unidade")
	@JsonIgnoreProperties(value = "unidade", allowSetters = true)
	private List<Ambiente> ambiente;

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

	public String getCodigoAcesso() {
		return codigoAcesso;
	}

	public void setCodigoAcesso(String codigoAcesso) {
		this.codigoAcesso = codigoAcesso;
	}

	public Usuario getCriadoPor() {
		return criadoPor;
	}

	public void setCriadoPor(Usuario criadoPor) {
		this.criadoPor = criadoPor;
	}

	public List<Ambiente> getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(List<Ambiente> ambiente) {
		this.ambiente = ambiente;
	}

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(LocalDateTime criadoEm) {
		this.criadoEm = criadoEm;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
}
