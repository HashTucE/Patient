package com.mediscreen.patient.controller;

import com.mediscreen.library.dto.PatientDto;
import com.mediscreen.patient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PatientCurlControllerTest {

    @InjectMocks
    private PatientCurlController patientCurlController;
    @Mock
    private PatientService patientService;


    @Test
    void testAddPatient() {

        // Arrange
        String family = "Doe";
        String given = "John";
        LocalDate dob = LocalDate.of(1990, 1, 1);
        String sex = "M";
        String address = "a";
        String phone = "p";

        // Act
        ResponseEntity<String> response = patientCurlController.addPatient(family, given, dob, sex, address, phone);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Patient added", response.getBody());
        Mockito.verify(patientService, Mockito.times(1)).createPatient(Mockito.any(PatientDto.class));
    }
}
