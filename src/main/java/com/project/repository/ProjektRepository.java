package com.project.repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.project.model.Projekt;
public interface ProjektRepository extends JpaRepository<Projekt, Integer> {
 Page<Projekt> findByNazwaContainingIgnoreCase(String nazwa, Pageable pageable);
 List<Projekt> findByNazwaContainingIgnoreCase(String nazwa);
 @Modifying
 @Query(value = "DELETE FROM projekt_student WHERE projekt_id = :projektId", nativeQuery = true)
 void deleteProjektStudentRelationsByProjektId(@Param("projektId") Integer projektId);
}
