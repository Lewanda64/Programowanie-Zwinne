package com.project.controller;
import java.net.URI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.project.error.NotFoundException;
import com.project.model.Projekt;
import com.project.service.ProjektService;
import io.swagger.v3.oas.annotations.tags.Tag;
 // dzięki adnotacji @RestController klasa jest traktowana jako zarządzany 
@RestController // przez kontener Springa REST-owy kontroler obsługujący sieciowe żądania
@RequestMapping("/api") // adnotacja @RequestMapping umieszczona w tym miejscu pozwala definiować 
// cześć wspólną adresu, wstawianą przed wszystkimi poniższymi ścieżkami
@Tag(name = "Projekt") // zmiana nazwy, uwzględniania m.in. przy generowaniu specyfikacji za pomocą OpenAPI
public class ProjektController { 
 private ProjektService projektService; //serwis jest automatycznie wstrzykiwany poprzez konstruktor
 @Autowired
 public ProjektController(ProjektService projektService) {
 this.projektService = projektService;
 }
 
 // PRZED KAŻDĄ Z PONIŻSZYCH METOD JEST UMIESZCZONA ADNOTACJA (@GetMapping, PostMapping, ... ), KTÓRA OKREŚLA 
 // RODZAJ METODY HTTP, A TAKŻE ADRES I PARAMETRY ŻĄDANIA
 //Przykład żądania wywołującego metodę: GET http://localhost:8080/api/projekty/1
 @GetMapping("/projekty/{projektId}")
 ResponseEntity<Projekt> getProjekt(@PathVariable("projektId") Integer projektId){// @PathVariable oznacza,
 return ResponseEntity.ok(projektService.getProjekt(projektId)
         .orElseThrow(() -> new NotFoundException("Projekt o id=" + projektId + " nie istnieje"))); // że wartość parametru
 } // przekazywana jest w ścieżce
 // @Valid włącza automatyczną walidację na podstawie adnotacji zawartych 
 // w modelu np. NotNull, Size, NotEmpty itd. (z jakarta.validation.constraints.*)
 @PostMapping(path = "/projekty")
 ResponseEntity<Void> createProjekt(@Valid @RequestBody Projekt projekt) {// @RequestBody oznacza, że dane 
 // projektu (w formacie JSON) są 
 Projekt createdProjekt = projektService.createProjekt(projekt); // przekazywane w ciele żądania 
 URI location = ServletUriComponentsBuilder.fromCurrentRequest() // link wskazujący utworzony projekt 
 .path("/{projektId}").buildAndExpand(createdProjekt.getProjektId()).toUri();
 return ResponseEntity.created(location).build(); // zwracany jest kod odpowiedzi 201 - Created 
 } // z linkiem location w nagłówku
 @PutMapping("/projekty/{projektId}")
 public ResponseEntity<Void> updateProjekt(@Valid @RequestBody Projekt projekt,
@PathVariable("projektId") Integer projektId) {
 projektService.getProjekt(projektId)
         .orElseThrow(() -> new NotFoundException("Projekt o id=" + projektId + " nie istnieje"));
 projekt.setProjektId(projektId);
 projektService.updateProjekt(projekt);
 return new ResponseEntity<Void>(HttpStatus.OK); // 200 (można też zwracać 204 - No content)
 }
 @DeleteMapping("/projekty/{projektId}")
 public ResponseEntity<Void> deleteProjekt(@PathVariable("projektId") Integer projektId) {
 projektService.getProjekt(projektId)
         .orElseThrow(() -> new NotFoundException("Projekt o id=" + projektId + " nie istnieje"));
 projektService.deleteProjekt(projektId);
 return new ResponseEntity<Void>(HttpStatus.OK); // 200
 }
 //Przykład żądania wywołującego metodę: http://localhost:8080/api/projekty?page=0&size=10&sort=nazwa,desc
 @GetMapping(value = "/projekty")
 Page<Projekt> getProjekty(Pageable pageable) { // @RequestHeader HttpHeaders headers – jeżeli potrzebny 
 return projektService.getProjekty(pageable); // byłby nagłówek, wystarczy dodać drugą zmienną z adnotacją
 }
 
 // Przykład żądania wywołującego metodę: GET http://localhost:8080/api/projekty?nazwa=webowa
 // Metoda zostanie wywołana tylko, gdy w żądaniu będzie przesyłana wartość parametru nazwa.
 @GetMapping(value = "/projekty", params="nazwa")
 Page<Projekt> getProjektyByNazwa(@RequestParam(name="nazwa") String nazwa, Pageable pageable) {
 return projektService.searchByNazwa(nazwa, pageable);
 }
}
