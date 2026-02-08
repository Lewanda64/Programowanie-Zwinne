package com.projekt.project_rest_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import com.project.model.Projekt;
import com.project.model.Student;
import com.project.model.Zadanie;
import com.project.project_rest_api.ProjectRestApiApplication;
import com.project.repository.ProjektRepository;
import com.project.repository.StudentRepository;
import com.project.repository.ZadanieRepository;

@SpringBootTest(classes = ProjectRestApiApplication.class)
@AutoConfigureMockMvc(addFilters = false)
class RelacjaDeleteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ZadanieRepository zadanieRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void deleteProjektRemovesZadaniaAndJoinRows() throws Exception {
        Projekt projekt = projektRepository.save(new Projekt("Projekt", "Opis"));

        Zadanie zadanie = new Zadanie("Zadanie", "Opis", 1);
        zadanie.setProjekt(projekt);
        zadanieRepository.save(zadanie);

        String studentPayload = "{"
                + "\"imie\":\"Jan\","
                + "\"nazwisko\":\"Kowalski\","
                + "\"nrIndeksu\":\"S123\","
                + "\"email\":\"jan.kowalski@example.com\","
                + "\"password\":\"haslo123\","
                + "\"stacjonarny\":true,"
                + "\"projekty\":[{\"projektId\":" + projekt.getProjektId() + "}]"
                + "}";

        mockMvc.perform(post("/api/studenci")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPayload))
                .andExpect(status().isCreated());

        Integer studentId = studentRepository.findByNrIndeksu("S123")
                .map(Student::getStudentId)
                .orElseThrow();

        assertThat(countProjektStudentByProjektId(projekt.getProjektId())).isEqualTo(1);

        mockMvc.perform(delete("/api/projekty/{projektId}", projekt.getProjektId()))
                .andExpect(status().isOk());

        assertThat(zadanieRepository.findZadaniaProjektu(projekt.getProjektId())).isEmpty();
        assertThat(countProjektStudentByProjektId(projekt.getProjektId())).isZero();
        assertThat(projektRepository.findById(projekt.getProjektId())).isEmpty();
        assertThat(studentRepository.findById(studentId)).isPresent();
    }

    @Test
    void deleteStudentRemovesJoinRows() throws Exception {
        Projekt projekt = projektRepository.save(new Projekt("Projekt", "Opis"));

        String studentPayload = "{"
                + "\"imie\":\"Anna\","
                + "\"nazwisko\":\"Nowak\","
                + "\"nrIndeksu\":\"S456\","
                + "\"email\":\"anna.nowak@example.com\","
                + "\"password\":\"haslo456\","
                + "\"stacjonarny\":true,"
                + "\"projekty\":[{\"projektId\":" + projekt.getProjektId() + "}]"
                + "}";

        mockMvc.perform(post("/api/studenci")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPayload))
                .andExpect(status().isCreated());

        Optional<Student> student = studentRepository.findByNrIndeksu("S456");
        Integer studentId = student.map(Student::getStudentId).orElseThrow();

        assertThat(countProjektStudentByStudentId(studentId)).isEqualTo(1);

        mockMvc.perform(delete("/api/studenci/{studentId}", studentId))
                .andExpect(status().isOk());

        assertThat(countProjektStudentByStudentId(studentId)).isZero();
        assertThat(studentRepository.findById(studentId)).isEmpty();
        assertThat(projektRepository.findById(projekt.getProjektId())).isPresent();
    }

    private int countProjektStudentByProjektId(Integer projektId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM projekt_student WHERE projekt_id = ?",
                Integer.class,
                projektId
        );
        return count == null ? 0 : count;
    }

    private int countProjektStudentByStudentId(Integer studentId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM projekt_student WHERE student_id = ?",
                Integer.class,
                studentId
        );
        return count == null ? 0 : count;
    }
}
