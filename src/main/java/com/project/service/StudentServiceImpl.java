package com.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.model.Student;
import com.project.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Optional<Student> getStudent(Integer studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    public Student createStudent(Student student) {
        if (student.getStudentId() != null) {
            throw new IllegalArgumentException("Nowy student nie powinien miec ustawionego ID");
        }
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Student student) {
        if (student.getStudentId() == null) {
            throw new IllegalArgumentException("Student do aktualizacji musi miec ustawione ID");
        }

        Student existing = studentRepository.findById(student.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student o id=" + student.getStudentId() + " nie istnieje"));

        existing.setImie(student.getImie());
        existing.setNazwisko(student.getNazwisko());
        existing.setNrIndeksu(student.getNrIndeksu());
        existing.setEmail(student.getEmail());
        existing.setStacjonarny(student.getStacjonarny());

        // relacja many-to-many (jeśli przesyłasz ją w JSON i chcesz ją nadpisywać)
        existing.setProjekty(student.getProjekty());

        return studentRepository.save(existing);
    }

    @Override
    public void deleteStudent(Integer studentId) {
        studentRepository.deleteById(studentId);
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
}
