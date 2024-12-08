package com.example.spring_demo.integratieTests;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WireMockIntegrationTest extends AbstractIntegrationTest {

    @Test
    void validate_that_when_external_api_user_is_found_user_data_is_returned() throws Exception {
        mockMvc.perform(get("/user/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test user"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.description").value("This is a test user"));
    }

    @Test
    void validate_that_when_external_api_user_is_not_found_404_not_found_is_returned() throws Exception {
        mockMvc.perform(get("/user/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
