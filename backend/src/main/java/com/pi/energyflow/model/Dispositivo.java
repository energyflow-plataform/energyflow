package com.pi.energyflow.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	private BigDecimal potencia;
	
	@NotNull(message = "Status é obrigatório.")
	private Boolean status;
	
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
	
	public BigDecimal getPotencia() {
		return potencia;
	}
	
	public void setPotencia(BigDecimal potencia) {
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
}
