package com.project.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalDate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.Set;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.EntityListeners;

@Entity
@Table(name="projekt")
@EntityListeners(AuditingEntityListener.class) //TODO Indeksować kolumny, które są najczęściej wykorzystywane do wyszukiwania projektów (Pozniej jak bedziemy wiedzieli co oplaca sie indeksowac)
public class Projekt {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="projekt_id")
private Integer projektId;

@NotBlank(message = "Pole nazwa nie może być puste!")
@Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
@Column(nullable = false, length = 50)
private String nazwa;

@Size(max = 1000, message = "Opis może mieć maksymalnie {max} znaków!")
@Column(length = 1000)
private String opis;

@CreatedDate
@Column(name = "dataczas_utworzenia", nullable = false, updatable = false)
private LocalDateTime dataczasUtworzenia;

@LastModifiedDate
@Column(name = "dataczas_modyfikacji", insertable = false)
private LocalDateTime lastModifiedDate;

@Column(name = "data_oddania")
private LocalDate dataOddania;

@OneToMany(mappedBy = "projekt")
@JsonIgnoreProperties({"projekt"})
private List<Zadanie> zadania;

@ManyToMany
@JoinTable(
    name = "projekt_student",
    joinColumns = { @JoinColumn(name = "projekt_id") },
    inverseJoinColumns = { @JoinColumn(name = "student_id") }
)
@JsonIgnoreProperties({"projekty"})
private Set<Student> studenci;


public List<Zadanie> getZadania() {
	return zadania;
}

public void setZadania(List<Zadanie> zadania) {
	this.zadania = zadania;
}

public Set<Student> getStudenci() {
	return studenci;
}

public void setStudenci(Set<Student> studenci) {
	this.studenci = studenci;
}

public Projekt() {
}

public Projekt(String nazwa, String opis) {
    this.nazwa = nazwa;
    this.opis = opis;
}

public Integer getProjektId() {
	return projektId;
}

public void setProjektId(Integer projektId) {
	this.projektId = projektId;
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

public LocalDateTime getDataczasUtworzenia() {
	return dataczasUtworzenia;
}

public void setDataczasUtworzenia(LocalDateTime dataczasUtworzenia) {
	this.dataczasUtworzenia = dataczasUtworzenia;
}

public LocalDateTime getLastModifiedDate() {
	return lastModifiedDate;
}

public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
	this.lastModifiedDate = lastModifiedDate;
}

public LocalDate getDataOddania() {
	return dataOddania;
}

public void setDataOddania(LocalDate dataOddania) {
	this.dataOddania = dataOddania;
}

}
 