package com.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.model.Projekt;
import com.project.repository.ProjektRepository;

@Service
public class ProjektServiceImpl implements ProjektService {

    private final ProjektRepository projektRepository;

    @Autowired 
    public ProjektServiceImpl(ProjektRepository projektRepository) {
        this.projektRepository = projektRepository;
    }

    @Override
    public Optional<Projekt> getProjekt(Integer projektId) {
        return projektRepository.findById(projektId);
    }

    @Override
    public Projekt createProjekt(Projekt projekt) {
        if (projekt.getProjektId() != null) {
            throw new IllegalArgumentException("Nowy projekt nie powinien miec ustawionego ID");
        }
        return projektRepository.save(projekt);
    }

    @Override
    public Projekt updateProjekt(Projekt projekt) {
        if (projekt.getProjektId() == null) {
            throw new IllegalArgumentException("Projekt do aktualizacji musi miec ustawione ID");
        }

        Projekt existing = projektRepository.findById(projekt.getProjektId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Projekt o id=" + projekt.getProjektId() + " nie istnieje"));

        existing.setNazwa(projekt.getNazwa());
        existing.setOpis(projekt.getOpis());
        existing.setDataOddania(projekt.getDataOddania());

        return projektRepository.save(existing);
    }

    @Override
    public void deleteProjekt(Integer projektId) {
        projektRepository.deleteById(projektId);
    }

    @Override
    public Page<Projekt> getProjekty(Pageable pageable) {
        return projektRepository.findAll(pageable);
    }

    @Override
    public Page<Projekt> searchByNazwa(String nazwa, Pageable pageable) {
        if (nazwa == null || nazwa.isBlank()) {
            return projektRepository.findAll(pageable);
        }
        return projektRepository.findByNazwaContainingIgnoreCase(nazwa, pageable);
    }
}
