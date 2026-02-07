package com.project.repository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.model.Student;
public interface StudentRepository extends JpaRepository<Student, Integer> {
 Optional<Student> findByNrIndeksu(String nrIndeksu);
 Page<Student> findByNrIndeksuStartsWith(String nrIndeksu, Pageable pageable);
 Page<Student> findByEmailStartsWithIgnoreCase(String email, Pageable pageable);
 Page<Student> findByImieStartsWithIgnoreCase(String imie, Pageable pageable);
 Page<Student> findByNazwiskoStartsWithIgnoreCase(String nazwisko, Pageable pageable);
}
