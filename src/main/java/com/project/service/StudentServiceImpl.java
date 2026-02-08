package com.project.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.project.error.NotFoundException;
import com.project.model.Student;
import com.project.model.Projekt;
import com.project.repository.ProjektRepository;
import com.project.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjektRepository projektRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder,
                              ProjektRepository projektRepository) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.projektRepository = projektRepository;
    }

    @Override
    public Optional<Student> getStudent(Integer studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    @Transactional
    public Student createStudent(Student student) {
        if (student.getStudentId() != null) {
            throw new IllegalArgumentException("Nowy student nie powinien miec ustawionego ID");
        }
        String rawPassword = student.getPassword();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Haslo jest wymagane");
        }
        student.setPassword(passwordEncoder.encode(rawPassword));
        Set<Projekt> requestedProjekty = student.getProjekty();
        student.setProjekty(new HashSet<>());
        if (student.getRole() == null || student.getRole().isBlank()) {
            student.setRole("ROLE_USER");
        }
        Student savedStudent = studentRepository.save(student);

        if (requestedProjekty != null && !requestedProjekty.isEmpty()) {
            Set<Projekt> managedProjekty = new HashSet<>();
            for (Projekt projektRef : requestedProjekty) {
                Integer projektId = projektRef.getProjektId();
                if (projektId == null) {
                    throw new IllegalArgumentException("Projekt musi miec ustawione ID");
                }
                Projekt projekt = projektRepository.findById(projektId)
                        .orElseThrow(() -> new NotFoundException("Projekt o id=" + projektId + " nie istnieje"));
                projekt.addStudent(savedStudent);
                managedProjekty.add(projekt);
            }
            projektRepository.saveAll(managedProjekty);
        }

        return savedStudent;
    }

    @Override
    @Transactional
    public Student updateStudent(Student student) {
        if (student.getStudentId() == null) {
            throw new IllegalArgumentException("Student do aktualizacji musi miec ustawione ID");
        }

        Student existing = studentRepository.findById(student.getStudentId())
                .orElseThrow(() -> new NotFoundException(
                        "Student o id=" + student.getStudentId() + " nie istnieje"));

        existing.setImie(student.getImie());
        existing.setNazwisko(student.getNazwisko());
        existing.setNrIndeksu(student.getNrIndeksu());
        existing.setEmail(student.getEmail());
        existing.setStacjonarny(student.getStacjonarny());

        Set<Projekt> requestedProjekty = student.getProjekty() == null
                ? new HashSet<>()
                : student.getProjekty();
        Map<Integer, Projekt> desiredById = new HashMap<>();
        for (Projekt projektRef : requestedProjekty) {
            Integer projektId = projektRef.getProjektId();
            if (projektId == null) {
                throw new IllegalArgumentException("Projekt musi miec ustawione ID");
            }
            Projekt projekt = projektRepository.findById(projektId)
                    .orElseThrow(() -> new NotFoundException("Projekt o id=" + projektId + " nie istnieje"));
            desiredById.put(projektId, projekt);
        }

        Map<Integer, Projekt> existingById = new HashMap<>();
        for (Projekt projekt : existing.getProjekty()) {
            existingById.put(projekt.getProjektId(), projekt);
        }

        Set<Projekt> changedProjekty = new HashSet<>();
        for (Integer existingId : existingById.keySet()) {
            if (!desiredById.containsKey(existingId)) {
                Projekt projekt = existingById.get(existingId);
                projekt.removeStudent(existing);
                changedProjekty.add(projekt);
            }
        }

        for (Integer desiredId : desiredById.keySet()) {
            if (!existingById.containsKey(desiredId)) {
                Projekt projekt = desiredById.get(desiredId);
                projekt.addStudent(existing);
                changedProjekty.add(projekt);
            }
        }

        String rawPassword = student.getPassword();
        if (rawPassword != null && !rawPassword.isBlank()) {
            existing.setPassword(passwordEncoder.encode(rawPassword));
        }

        if (!changedProjekty.isEmpty()) {
            projektRepository.saveAll(changedProjekty);
        }

        return studentRepository.save(existing);
    }

    @Override
    public Student registerStudent(Student student, String rawPassword) {
        if (student.getStudentId() != null) {
            throw new IllegalArgumentException("Rejestracja nie powinna zawierac ID");
        }
        student.setRole("ROLE_USER");
        student.setPassword(passwordEncoder.encode(rawPassword));
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deleteStudent(Integer studentId) {
        studentRepository.findById(studentId).ifPresent(student -> {
            Set<Projekt> projekty = new HashSet<>(student.getProjekty());
            for (Projekt projekt : projekty) {
                projekt.removeStudent(student);
            }
            if (!projekty.isEmpty()) {
                projektRepository.saveAll(projekty);
            }
            studentRepository.delete(student);
        });
    }

    @Override
    public Page<Student> getStudenci(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public Page<Student> searchByEmail(String email, Pageable pageable) {
        if (email == null || email.isBlank()) {
            return studentRepository.findAll(pageable);
        }
        return studentRepository.findByEmailStartsWithIgnoreCase(email, pageable);
    }

    @Override
    public Page<Student> searchByImie(String imie, Pageable pageable) {
        if (imie == null || imie.isBlank()) {
            return studentRepository.findAll(pageable);
        }
        return studentRepository.findByImieStartsWithIgnoreCase(imie, pageable);
    }

    @Override
    public Page<Student> searchByNazwisko(String nazwisko, Pageable pageable) {
        if (nazwisko == null || nazwisko.isBlank()) {
            return studentRepository.findAll(pageable);
        }
        return studentRepository.findByNazwiskoStartsWithIgnoreCase(nazwisko, pageable);
    }

    @Override
    public Optional<Student> getByNrIndeksu(String nrIndeksu) {
        if (nrIndeksu == null || nrIndeksu.isBlank()) {
            return Optional.empty();
        }
        return studentRepository.findByNrIndeksu(nrIndeksu);
    }

    @Override
    public Optional<Student> getByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return studentRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public Optional<Student> updateSelf(String email, Student student) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        return studentRepository.findByEmailIgnoreCase(email)
                .map(existing -> {
                    existing.setImie(student.getImie());
                    existing.setNazwisko(student.getNazwisko());
                    existing.setNrIndeksu(student.getNrIndeksu());
                    existing.setEmail(student.getEmail());
                    existing.setStacjonarny(student.getStacjonarny());

                    String rawPassword = student.getPassword();
                    if (rawPassword != null && !rawPassword.isBlank()) {
                        existing.setPassword(passwordEncoder.encode(rawPassword));
                    }

                    return studentRepository.save(existing);
                });
    }
}
