package com.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.error.NotFoundException;
import com.project.model.Zadanie;
import com.project.repository.ZadanieRepository;

@Service
public class ZadanieServiceImpl implements ZadanieService {

    private final ZadanieRepository zadanieRepository;

    @Autowired
    public ZadanieServiceImpl(ZadanieRepository zadanieRepository) {
        this.zadanieRepository = zadanieRepository;
    }

    @Override
    public Optional<Zadanie> getZadanie(Integer zadanieId) {
        return zadanieRepository.findById(zadanieId);
    }

    @Override
    public Zadanie createZadanie(Zadanie zadanie) {
        // tu zakładamy, że "zadanie.projekt" jest już ustawione w żądaniu (JSON),
        // bo w tabeli jest FK projekt_id NOT NULL
        if (zadanie.getZadanieId() != null) {
            throw new IllegalArgumentException("Nowe zadanie nie powinno miec ustawionego ID");
        }
        return zadanieRepository.save(zadanie);
    }

    @Override
    public Zadanie updateZadanie(Zadanie zadanie) {
        if (zadanie.getZadanieId() == null) {
            throw new IllegalArgumentException("Zadanie do aktualizacji musi miec ustawione ID");
        }

        Zadanie existing = zadanieRepository.findById(zadanie.getZadanieId())
                .orElseThrow(() -> new NotFoundException(
                        "Zadanie o id=" + zadanie.getZadanieId() + " nie istnieje"));

        existing.setNazwa(zadanie.getNazwa());
        existing.setOpis(zadanie.getOpis());
        existing.setKolejnosc(zadanie.getKolejnosc());
        existing.setProjekt(zadanie.getProjekt()); // pozwala też przepiąć do innego projektu (jeśli chcesz)

        return zadanieRepository.save(existing);
    }

    @Override
    public void deleteZadanie(Integer zadanieId) {
        zadanieRepository.deleteById(zadanieId);
    }

    @Override
    public Page<Zadanie> getZadania(Pageable pageable) {
        return zadanieRepository.findAll(pageable);
    }

    @Override
    public Page<Zadanie> getZadaniaProjektu(Integer projektId, Pageable pageable) {
        return zadanieRepository.findZadaniaProjektu(projektId, pageable);
    }
}
