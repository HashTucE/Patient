package com.mediscreen.patient.controller;

import com.mediscreen.library.dto.PatientDto;
import com.mediscreen.patient.exception.PatientNotFoundException;
import com.mediscreen.patient.service.PatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {


    @Mock
    private PatientService patientService;
    @InjectMocks
    private PatientController patientController;


    @Test
    @DisplayName("Should found equality")
    void findPatientByIdTest() throws PatientNotFoundException {

        // Arrange
        int id = 1;
        PatientDto patient = new PatientDto();
        when(patientService.findPatientById(id)).thenReturn(patient);

        // Act
        ResponseEntity<PatientDto> response = patientController.findPatientById(id);

        // Assert
        assertEquals(patient, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(patientService).findPatientById(id);
    }


    @Test
    void testFindPatientByIdNotFound() throws PatientNotFoundException {

        // Arrange
        int id = 123;
        when(patientService.findPatientById(id)).thenThrow(PatientNotFoundException.class);

        // Act
        ResponseEntity<PatientDto> response = patientController.findPatientById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    @DisplayName("Should found equality")
    void findPatientByFamilyNameTest() throws PatientNotFoundException {

        // Arrange
        String familyName = "Smith";
        PatientDto patient = new PatientDto();
        when(patientService.findPatientByFamilyName(familyName)).thenReturn(patient);

        // Act
        ResponseEntity<PatientDto> response = patientController.findPatientByFamilyName(familyName);

        // Assert
        assertEquals(patient, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(patientService).findPatientByFamilyName(familyName);
    }


    @Test
    @DisplayName("Should throw PatientNotFoundException")
    void findPatientByFamilyNameNegativeTest() throws PatientNotFoundException {

        // Arrange
        String familyName = "Smith";
        when(patientService.findPatientByFamilyName(familyName)).thenThrow(PatientNotFoundException.class);

        // Act
        // Assert
        assertThrows(PatientNotFoundException.class, () -> patientController.findPatientByFamilyName(familyName));
        verify(patientService).findPatientByFamilyName(familyName);
    }


    @Test
    @DisplayName("Should assert equality")
    void getAllPatientsTest() {

        // Arrange
        List<PatientDto> patients = Collections.singletonList(new PatientDto());
        when(patientService.getAllPatients()).thenReturn(patients);

        // Act
        ResponseEntity<List<PatientDto>> response = patientController.getAllPatients();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patients, response.getBody());
    }


    @Test
    @DisplayName("Should found equality")
    void validatePatientTest() {

        // Arrange
        PatientDto patientDto = new PatientDto();
        PatientDto patient = new PatientDto();

        // Act
        when(patientService.createPatient(patientDto)).thenReturn(patient);
        ResponseEntity<PatientDto> response = patientController.validatePatient(patientDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
    }


    @Test
    @DisplayName("Should return no content when no patients exist")
    void getAllPatientsNegativeTest() {

        // Arrange
        when(patientService.getAllPatients()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<PatientDto>> response = patientController.getAllPatients();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(patientService).getAllPatients();
    }


    @Test
    @DisplayName("Should found equality")
    void updatePatientTest() throws PatientNotFoundException {

        // Arrange
        int id = 1;
        PatientDto patientDto = new PatientDto();
        PatientDto patient = new PatientDto();

        // Act
        when(patientService.updatePatient(id, patientDto)).thenReturn(patient);
        ResponseEntity<PatientDto> response = patientController.updatePatient(id, patientDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
        verify(patientService, times(1)).updatePatient(id, patientDto);
    }


    @Test
    @DisplayName("Should throw PatientNotFoundException")
    void updatePatientNegativeTest() throws PatientNotFoundException {

        // Arrange
        int id = 1;
        PatientDto patientDto = new PatientDto();
        when(patientService.updatePatient(id, patientDto)).thenThrow(new PatientNotFoundException("Patient not found"));

        // Act
        // Assert
        assertThrows(PatientNotFoundException.class, () -> patientController.updatePatient(id, patientDto));
        verify(patientService, times(1)).updatePatient(id, patientDto);
    }


    @Test
    @DisplayName("Should found equality")
    void deletePatientTest() throws PatientNotFoundException {

        // Arrange
        int id = 1;
        doNothing().when(patientService).deletePatient(id);

        // Act
        ResponseEntity<Void> response = patientController.deletePatient(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(patientService, times(1)).deletePatient(id);
    }


    @Test
    @DisplayName("Should throw PatientNotFoundException")
    void deletePatientNegativeTest() throws PatientNotFoundException {

        // Arrange
        int id = 1;
        doThrow(new PatientNotFoundException("Patient not found")).when(patientService).deletePatient(id);

        // Act
        // Assert
        assertThrows(PatientNotFoundException.class, () -> patientController.deletePatient(id));
        verify(patientService, times(1)).deletePatient(id);
    }
}
