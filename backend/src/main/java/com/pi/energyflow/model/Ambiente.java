package com.pi.energyflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_ambientes")
public class Ambiente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Nome é obrigatório.")
	private String nome;
	
	@Column(length = 500)
	@Size(max = 500, message = "A descrição deve conter no máximo 500 caracteres.")
	private String descricao;
	
	@ManyToOne
	@JsonIgnoreProperties("ambiente")
	private Unidade unidade;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ambiente")
	@JsonIgnoreProperties(value = "ambiente", allowSetters = true)
	private List<Dispositivo> dispositivo;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public List<Dispositivo> getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(List<Dispositivo> dispositivo) {
		this.dispositivo = dispositivo;
	}
}
