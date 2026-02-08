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

import com.project.model.Student;
import com.project.service.StudentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // GET http://localhost:8080/api/studenci/1
    @GetMapping("/studenci/{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable("studentId") Integer studentId) {
        return ResponseEntity.of(studentService.getStudent(studentId));
    }

    // POST http://localhost:8080/api/studenci
    @PostMapping("/studenci")
    public ResponseEntity<Void> createStudent(@Valid @RequestBody Student student) {
        Student created = studentService.createStudent(student);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{studentId}")
                .buildAndExpand(created.getStudentId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // POST http://localhost:8080/api/register
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        Student student = new Student(request.getImie(), request.getNazwisko(),
                request.getNrIndeksu(), request.getEmail(), request.getStacjonarny());
        Student created = studentService.registerStudent(student, request.getPassword());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/studenci/{studentId}")
                .buildAndExpand(created.getStudentId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // PUT http://localhost:8080/api/studenci/1
    @PutMapping("/studenci/{studentId}")
    public ResponseEntity<Void> updateStudent(@Valid @RequestBody Student student,
                                              @PathVariable("studentId") Integer studentId) {

        return studentService.getStudent(studentId)
                .map(s -> {
                    student.setStudentId(studentId); // ważne
                    studentService.updateStudent(student);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE http://localhost:8080/api/studenci/1
    @DeleteMapping("/studenci/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("studentId") Integer studentId) {

        return studentService.getStudent(studentId)
                .map(s -> {
                    studentService.deleteStudent(studentId);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET http://localhost:8080/api/studenci?page=0&size=10
    @GetMapping("/studenci")
    public Page<Student> getStudenci(Pageable pageable) {
        return studentService.getStudenci(pageable);
    }

    // GET http://localhost:8080/api/studenci?email=jan&page=0&size=10
    @GetMapping(value = "/studenci", params = "email")
    public Page<Student> getStudenciByEmail(@RequestParam(name = "email") String email,
                                            Pageable pageable) {
        return studentService.searchByEmail(email, pageable);
    }

    // GET http://localhost:8080/api/studenci?imie=Jan&page=0&size=10
    @GetMapping(value = "/studenci", params = "imie")
    public Page<Student> getStudenciByImie(@RequestParam(name = "imie") String imie,
                                           Pageable pageable) {
        return studentService.searchByImie(imie, pageable);
    }

    // GET http://localhost:8080/api/studenci?nazwisko=Kow&page=0&size=10
    @GetMapping(value = "/studenci", params = "nazwisko")
    public Page<Student> getStudenciByNazwisko(@RequestParam(name = "nazwisko") String nazwisko,
                                               Pageable pageable) {
        return studentService.searchByNazwisko(nazwisko, pageable);
    }

    // (opcjonalnie) GET http://localhost:8080/api/studenci/nrIndeksu/12345
    @GetMapping("/studenci/nrIndeksu/{nrIndeksu}")
    public ResponseEntity<Student> getStudentByNrIndeksu(@PathVariable("nrIndeksu") String nrIndeksu) {
        return ResponseEntity.of(studentService.getByNrIndeksu(nrIndeksu));
    }
}
