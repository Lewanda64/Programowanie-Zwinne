package com.project.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.model.Student;

public interface StudentService {

    Optional<Student> getStudent(Integer studentId);

    Student createStudent(Student student);

    Student updateStudent(Student student);

    Student registerStudent(Student student, String rawPassword);

    void deleteStudent(Integer studentId);

    Page<Student> getStudenci(Pageable pageable);

    Page<Student> searchByEmail(String email, Pageable pageable);

    Page<Student> searchByImie(String imie, Pageable pageable);

    Page<Student> searchByNazwisko(String nazwisko, Pageable pageable);

    Optional<Student> getByNrIndeksu(String nrIndeksu);

    Optional<Student> getByEmail(String email);

    Optional<Student> updateSelf(String email, Student student);
}
