package com.mediscreen.patient.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.library.dto.PatientDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PatientIT {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Order(3)
    @DisplayName("Should return patient when id exist")
    void findPatientByIdTest() throws Exception {

        int id = 1;
        mockMvc.perform(get("/api/patient/find/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthdate").value("2000-01-01"));
    }


    @Test
    @Order(4)
    @DisplayName("Should return patient when name exist")
    void findPatientByFamilyNameTest() throws Exception {

        String familyName = "Doe";
        mockMvc.perform(get("/api/patient/finder/{familyName}", familyName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value(familyName))
                .andExpect(jsonPath("$.birthdate").value("2000-01-01"));
    }


    @Test
    @Order(5)
    @DisplayName("Should return list of patients when exist")
    void getAllPatientsTest() throws Exception {

        mockMvc.perform(get("/api/patient/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].firstName").value("John"))
                .andExpect(jsonPath("$.[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.[0].birthdate").value("2000-01-01"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].firstName").value("John"))
                .andExpect(jsonPath("$.[1].lastName").value("Smith"))
                .andExpect(jsonPath("$.[1].birthdate").value("2000-01-01"));
    }


    @Test
    @Order(2)
    @DisplayName("Should return patient when valid data")
    public void validatePatientTest() throws Exception {

        LocalDate birthdate = LocalDate.of(2000, 01, 01);
        PatientDto patientDto = new PatientDto("Smith", "John", birthdate, "M", "a", "p");

        mockMvc.perform(post("/api/patient/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }


    @Test
    @Order(6)
    @DisplayName("Should update patient when valid data")
    public void updatePatientTest() throws Exception {

        LocalDate birthdate = LocalDate.of(2000, 01, 01);
        PatientDto patientDto = new PatientDto("Smith", "John", birthdate, "M", "a", "p");

        mockMvc.perform(post("/api/patient/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }


    @Test
    @Order(7)
    @DisplayName("Should delete patient when id exist")
    public void deletePatientTest() throws Exception {

        mockMvc.perform(post("/api/patient/delete/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    @Order(1)
    @DisplayName("Should add patient when valid data")
    public void AddPatientTest() throws Exception {

        mockMvc.perform(post("/patient/add")
                        .param("family", "Doe")
                        .param("given", "John")
                        .param("dob", "2000-01-01")
                        .param("sex", "M")
                        .param("address", "a")
                        .param("phone", "p"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient added"));
    }
}
