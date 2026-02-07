package com.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.EntityListeners;


@Entity
@Table(name = "zadanie")
@EntityListeners(AuditingEntityListener.class)
public class Zadanie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zadanie_id")
    private Integer zadanieId;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String nazwa;

    @Size(max = 1000)
    @Column(length = 1000)
    private String opis;

    @NotNull
    @Column(nullable = false)
    private Integer kolejnosc;

    @CreatedDate
    @Column(name = "dataczas_dodania", nullable = false, updatable = false)
    private LocalDateTime dataczasDodania;
  
    @ManyToOne
    @JoinColumn(name = "projekt_id", nullable = false)
    @JsonIgnoreProperties({"zadania", "studenci"}) 
    private Projekt projekt;
    
    public Zadanie() {
    }

    public Zadanie(String nazwa, String opis, Integer kolejnosc) {
        this.nazwa = nazwa;
        this.opis = opis;
        this.kolejnosc = kolejnosc;
    }

	public Integer getZadanieId() {
		return zadanieId;
	}

	public void setZadanieId(Integer zadanieId) {
		this.zadanieId = zadanieId;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public Projekt getProjekt() {
		return projekt;
	}

	public void setProjekt(Projekt projekt) {
		this.projekt = projekt;
	}

	public Integer getKolejnosc() {
		return kolejnosc;
	}

	public void setKolejnosc(Integer kolejnosc) {
		this.kolejnosc = kolejnosc;
	}

	public LocalDateTime getDataczasDodania() {
		return dataczasDodania;
	}

	public void setDataczasDodania(LocalDateTime dataczasDodania) {
		this.dataczasDodania = dataczasDodania;
	}   
}
