package com.project.repository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.project.model.Student;
public interface StudentRepository extends JpaRepository<Student, Integer> {
 Optional<Student> findByNrIndeksu(String nrIndeksu);
 Optional<Student> findByEmailIgnoreCase(String email);
 Page<Student> findByNrIndeksuStartsWith(String nrIndeksu, Pageable pageable);
 Page<Student> findByEmailStartsWithIgnoreCase(String email, Pageable pageable);
 Page<Student> findByImieStartsWithIgnoreCase(String imie, Pageable pageable);
 Page<Student> findByNazwiskoStartsWithIgnoreCase(String nazwisko, Pageable pageable);
 @Modifying
 @Query(value = "DELETE FROM projekt_student WHERE student_id = :studentId", nativeQuery = true)
 void deleteProjektStudentRelationsByStudentId(@Param("studentId") Integer studentId);
}
