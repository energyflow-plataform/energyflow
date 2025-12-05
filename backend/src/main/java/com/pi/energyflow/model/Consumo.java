package com.pi.energyflow.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "consumo")
public class Consumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_dispositivo")
    private Dispositivo dispositivo;

    private LocalDateTime inicioIntervalo;
    private LocalDateTime fimIntervalo;

    private Double consumoMin;
    private Double consumoMax;
    private Double consumoMedio;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Dispositivo getDispositivo() {
		return dispositivo;
	}
	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}
	public LocalDateTime getInicioIntervalo() {
		return inicioIntervalo;
	}
	public void setInicioIntervalo(LocalDateTime inicioIntervalo) {
		this.inicioIntervalo = inicioIntervalo;
	}
	public LocalDateTime getFimIntervalo() {
		return fimIntervalo;
	}
	public void setFimIntervalo(LocalDateTime fimIntervalo) {
		this.fimIntervalo = fimIntervalo;
	}
	public Double getConsumoMin() {
		return consumoMin;
	}
	public void setConsumoMin(Double consumoMin) {
		this.consumoMin = consumoMin;
	}
	public Double getConsumoMax() {
		return consumoMax;
	}
	public void setConsumoMax(Double consumoMax) {
		this.consumoMax = consumoMax;
	}
	public Double getConsumoMedio() {
		return consumoMedio;
	}
	public void setConsumoMedio(Double consumoMedio) {
		this.consumoMedio = consumoMedio;
	}
}