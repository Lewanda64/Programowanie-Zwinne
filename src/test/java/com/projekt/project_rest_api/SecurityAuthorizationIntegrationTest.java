package com.projekt.project_rest_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.project.model.Projekt;
import com.project.model.Student;
import com.project.project_rest_api.ProjectRestApiApplication;
import com.project.repository.ProjektRepository;
import com.project.repository.StudentRepository;

@SpringBootTest(classes = ProjectRestApiApplication.class)
@AutoConfigureMockMvc
class SecurityAuthorizationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @WithMockUser(roles = "USER")
    void userCanReadButCannotModifyProjects() throws Exception {
        mockMvc.perform(get("/api/projekty"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/projekty")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nazwa\":\"Projekt\",\"opis\":\"Opis\"}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/projekty/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nazwa\":\"Projekt\",\"opis\":\"Opis\"}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/projekty/{id}", 999))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCanReadButCannotModifyTasks() throws Exception {
        mockMvc.perform(get("/api/zadania"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/zadania")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nazwa\":\"Zadanie\",\"kolejnosc\":1,\"projekt\":{\"projektId\":1}}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/zadania/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nazwa\":\"Zadanie\",\"kolejnosc\":1,\"projekt\":{\"projektId\":1}}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/zadania/{id}", 999))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanModifyProjectsAndTasks() throws Exception {
        Projekt projekt = new Projekt("Projekt", "Opis");
        projekt.setDataOddania(LocalDate.now().plusDays(10));
        projekt = projektRepository.save(projekt);

        mockMvc.perform(post("/api/projekty")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nazwa\":\"Projekt Admin\",\"opis\":\"Opis\"}"))
                .andExpect(status().isCreated());

        assertNotForbidden(mockMvc.perform(put("/api/projekty/{id}", projekt.getProjektId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nazwa\":\"Projekt Admin\",\"opis\":\"Opis\"}"))
                .andReturn());

        assertNotForbidden(mockMvc.perform(delete("/api/projekty/{id}", projekt.getProjektId()))
                .andReturn());

        Projekt zadanieProjekt = projektRepository.save(new Projekt("Projekt Zadanie", "Opis"));

        mockMvc.perform(post("/api/zadania")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nazwa\":\"Zadanie Admin\",\"kolejnosc\":1,\"projekt\":{\"projektId\":"
                        + zadanieProjekt.getProjektId() + "}}"))
                .andExpect(status().isCreated());

        assertNotForbidden(mockMvc.perform(put("/api/zadania/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nazwa\":\"Zadanie Admin\",\"kolejnosc\":1,\"projekt\":{\"projektId\":"
                        + zadanieProjekt.getProjektId() + "}}"))
                .andReturn());

        assertNotForbidden(mockMvc.perform(delete("/api/zadania/{id}", 999))
                .andReturn());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void userCanAccessMeEndpoint() throws Exception {
        Student student = new Student("Jan", "Kowalski", "S999", "user@example.com", true);
        studentRepository.save(student);

        mockMvc.perform(get("/api/studenci/me"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCannotListStudents() throws Exception {
        mockMvc.perform(get("/api/studenci"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanListStudents() throws Exception {
        mockMvc.perform(get("/api/studenci"))
                .andExpect(status().isOk());
    }

    private void assertNotForbidden(MvcResult result) {
        assertThat(result.getResponse().getStatus()).isNotEqualTo(403);
    }
}
