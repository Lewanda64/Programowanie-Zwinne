package com.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.error.NotFoundException;
import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.repository.ProjektRepository;
import com.project.repository.ZadanieRepository;

@Service
public class ZadanieServiceImpl implements ZadanieService {

    private final ZadanieRepository zadanieRepository;
    private final ProjektRepository projektRepository;

    @Autowired
    public ZadanieServiceImpl(ZadanieRepository zadanieRepository, ProjektRepository projektRepository) {
        this.zadanieRepository = zadanieRepository;
        this.projektRepository = projektRepository;
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
        Projekt projekt = zadanie.getProjekt();
        if (projekt == null || projekt.getProjektId() == null) {
            throw new IllegalArgumentException("Projekt jest wymagany");
        }
        Projekt existingProjekt = projektRepository.findById(projekt.getProjektId())
                .orElseThrow(() -> new NotFoundException(
                        "Projekt o id=" + projekt.getProjektId() + " nie istnieje"));
        zadanie.setProjekt(existingProjekt);
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
        return zadanieRepository.findAll(normalizeSort(pageable));
    }

    @Override
    public Page<Zadanie> getZadaniaProjektu(Integer projektId, Pageable pageable) {
        return zadanieRepository.findZadaniaProjektu(projektId, normalizeSort(pageable));
    }

    private Pageable normalizeSort(Pageable pageable) {
        if (pageable == null || pageable.getSort().isUnsorted()) {
            return pageable;
        }
        boolean changed = false;
        java.util.List<Sort.Order> orders = new java.util.ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            if ("dataCzasDodania".equals(order.getProperty())) {
                orders.add(new Sort.Order(order.getDirection(), "dataczasDodania"));
                changed = true;
            } else {
                orders.add(order);
            }
        }
        if (!changed) {
            return pageable;
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
    }
}
