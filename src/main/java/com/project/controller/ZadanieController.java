package com.project.controller;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.error.NotFoundException;
import com.project.model.Zadanie;
import com.project.service.ZadanieService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Zadanie")
public class ZadanieController {

    private final ZadanieService zadanieService;

    @Autowired
    public ZadanieController(ZadanieService zadanieService) {
        this.zadanieService = zadanieService;
    }

    // GET http://localhost:8080/api/zadania/1
    @GetMapping("/zadania/{zadanieId}")
    public ResponseEntity<Zadanie> getZadanie(@PathVariable("zadanieId") Integer zadanieId) {
        return ResponseEntity.ok(zadanieService.getZadanie(zadanieId)
                .orElseThrow(() -> new NotFoundException("Zadanie o id=" + zadanieId + " nie istnieje")));
    }

    // POST http://localhost:8080/api/zadania
    @PostMapping("/zadania")
    public ResponseEntity<Void> createZadanie(@Valid @RequestBody Zadanie zadanie) {
        Zadanie created = zadanieService.createZadanie(zadanie);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{zadanieId}")
                .buildAndExpand(created.getZadanieId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // PUT http://localhost:8080/api/zadania/1
    @PutMapping("/zadania/{zadanieId}")
    public ResponseEntity<Void> updateZadanie(@Valid @RequestBody Zadanie zadanie,
                                              @PathVariable("zadanieId") Integer zadanieId) {

        zadanieService.getZadanie(zadanieId)
                .orElseThrow(() -> new NotFoundException("Zadanie o id=" + zadanieId + " nie istnieje"));
        zadanie.setZadanieId(zadanieId); // ważne
        zadanieService.updateZadanie(zadanie);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // DELETE http://localhost:8080/api/zadania/1
    @DeleteMapping("/zadania/{zadanieId}")
    public ResponseEntity<Void> deleteZadanie(@PathVariable("zadanieId") Integer zadanieId) {

        zadanieService.getZadanie(zadanieId)
                .orElseThrow(() -> new NotFoundException("Zadanie o id=" + zadanieId + " nie istnieje"));
        zadanieService.deleteZadanie(zadanieId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // GET http://localhost:8080/api/zadania?page=0&size=10&sort=nazwa,asc
    @GetMapping("/zadania")
    public Page<Zadanie> getZadania(Pageable pageable) {
        return zadanieService.getZadania(pageable);
    }

    // GET http://localhost:8080/api/projekty/1/zadania?page=0&size=10
    @GetMapping("/projekty/{projektId}/zadania")
    public Page<Zadanie> getZadaniaProjektu(@PathVariable("projektId") Integer projektId,
                                            Pageable pageable) {
        return zadanieService.getZadaniaProjektu(projektId, pageable);
    }
}
