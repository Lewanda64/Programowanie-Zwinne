package com.projekt.project_rest_api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.project.project_rest_api.ProjectRestApiApplication;

@SpringBootTest(classes = ProjectRestApiApplication.class)
@AutoConfigureMockMvc(addFilters = false)
class ZadanieCreateValidationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createZadanieWithoutProjektReturnsBadRequest() throws Exception {
        String payload = "{\"nazwa\":\"Zadanie\",\"kolejnosc\":1}";

        mockMvc.perform(post("/api/zadania")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Projekt jest wymagany"));
    }

    @Test
    void createZadanieWithUnknownProjektReturnsNotFound() throws Exception {
        String payload = "{\"nazwa\":\"Zadanie\",\"kolejnosc\":1,\"projekt\":{\"projektId\":999999}}";

        mockMvc.perform(post("/api/zadania")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Projekt o id=999999 nie istnieje"));
    }
}
