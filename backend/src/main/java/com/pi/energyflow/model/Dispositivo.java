package com.pi.energyflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_dispositivos")
public class Dispositivo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Nome é obrigatório.")
	private String nome;
	
	@NotBlank(message = "Tipo é obrigatório.")
	private String tipo;
	
	@NotNull(message = "Potência é obrigatória.")
	private Double potencia;
	
	@NotNull(message = "Status é obrigatório.")
	@Column(columnDefinition = "TINYINT(1)")
	private Boolean status;
	
	@NotNull
	@Column(columnDefinition = "TINYINT(1)")
	private Boolean ativo = true;
	
	@ManyToOne
	@JsonIgnoreProperties("dispositivo")
	private Ambiente ambiente;
	
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
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Double getPotencia() {
		return potencia;
	}
	
	public void setPotencia(Double potencia) {
		this.potencia = potencia;
	}
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public Ambiente getAmbiente() {
		return ambiente;
	}
	
	public void setAmbiente(Ambiente ambiente) {
		this.ambiente = ambiente;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getStatus() {
		return status;
	}
}
