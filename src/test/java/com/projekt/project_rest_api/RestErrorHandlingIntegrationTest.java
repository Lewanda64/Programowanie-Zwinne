package com.projekt.project_rest_api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class RestErrorHandlingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void notFoundReturnsApiErrorJson() throws Exception {
        mockMvc.perform(get("/api/projekty/{projektId}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/projekty/999999"));
    }

    @Test
    void validationErrorReturnsFieldErrors() throws Exception {
        mockMvc.perform(post("/api/projekty")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors[0].field").exists())
                .andExpect(jsonPath("$.path").value("/api/projekty"));
    }
}
