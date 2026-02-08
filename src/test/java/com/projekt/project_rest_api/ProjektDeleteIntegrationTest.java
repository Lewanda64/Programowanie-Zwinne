package com.projekt.project_rest_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.project_rest_api.ProjectRestApiApplication;
import com.project.repository.ProjektRepository;
import com.project.repository.ZadanieRepository;

@SpringBootTest(classes = ProjectRestApiApplication.class)
@AutoConfigureMockMvc
class ProjektDeleteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private ZadanieRepository zadanieRepository;

    @Test
    void deleteProjektWithTasksRemovesTasksFirst() throws Exception {
        Projekt projekt = new Projekt("Projekt testowy", "Opis testowy");
        projekt = projektRepository.save(projekt);

        Zadanie zadanie = new Zadanie("Zadanie testowe", "Opis zadania", 1);
        zadanie.setProjekt(projekt);
        zadanieRepository.save(zadanie);

        mockMvc.perform(delete("/api/projekty/{projektId}", projekt.getProjektId())
                .header("Authorization", basicAuthHeader()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projekty/{projektId}", projekt.getProjektId())
                .header("Authorization", basicAuthHeader()))
                .andExpect(status().isNotFound());

        assertThat(zadanieRepository.findZadaniaProjektu(projekt.getProjektId())).isEmpty();
        assertThat(projektRepository.findById(projekt.getProjektId())).isEmpty();
    }

    private static String basicAuthHeader() {
        String credentials = "admin:admin";
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encoded;
    }
}
