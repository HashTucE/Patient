package com.mediscreen.patient.service;

import com.mediscreen.library.dto.PatientDto;
import com.mediscreen.patient.exception.PatientNotFoundException;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.repository.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {


    @Mock
    private PatientRepository patientRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private PatientService patientService;


    @Test
    @DisplayName("Should Return PatientDto")
    public void findPatientByIdTest() throws PatientNotFoundException {

        // Arrange
        int id = 1;
        Patient patient = new Patient("Doe", "John");
        patient.setId(id);
        PatientDto patientDto = new PatientDto("Doe", "John");
        patientDto.setId(id);
        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(modelMapper.map(patient, PatientDto.class)).thenReturn(patientDto);

        // Act
        PatientDto result = patientService.findPatientById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Doe", result.getLastName());
        assertEquals("John", result.getFirstName());
    }


    @Test
    @DisplayName("Should Throw Patient Not Found Exception")
    public void findPatientByIdNegativeTest() {

        // Arrange
        int id = 1;
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(PatientNotFoundException.class, () -> {
            patientService.findPatientById(id);
        }, "Patient does not exist with id : 1");
    }


    @Test
    @DisplayName("Should found same name")
    public void findPatientByFamilyName() throws PatientNotFoundException {

        // Arrange
        Patient patient = new Patient("Doe", "John");
        String name = patient.getLastName();
        PatientDto patientDto = new PatientDto("Doe", "John");
        when(patientRepository.findByFamilyName(name)).thenReturn(Optional.of(patient));
        when(modelMapper.map(patient, PatientDto.class)).thenReturn(patientDto);

        // Act
        patientDto = patientService.findPatientByFamilyName(name);

        // Assert
        assertEquals(name, patientDto.getLastName());
    }


    @Test
    @DisplayName("Should Throw PatientNotFoundException")
    public void findPatientByFamilyNameNegativeTest() {

        // Arrange
        String familyName = "Johnson";

        // Act & Assert
        assertThrows(PatientNotFoundException.class, () -> {
            patientService.findPatientByFamilyName(familyName);
        });
    }


    @Test
    @DisplayName("should return all patients")
    void getAllPatientsTest() {

        // Arrange
        Patient patient1 = new Patient("Doe", "John");
        Patient patient2 = new Patient("Smith", "Jane");
        PatientDto patientDto1 = new PatientDto("Doe", "John");
        PatientDto patientDto2 = new PatientDto("Smith", "Jane");

        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient1, patient2));
        when(modelMapper.map(patient1, PatientDto.class)).thenReturn(patientDto1);
        when(modelMapper.map(patient2, PatientDto.class)).thenReturn(patientDto2);

        // Act
        List<PatientDto> result = patientService.getAllPatients();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(patientRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(Patient.class), eq(PatientDto.class));
    }


    @Test
    @DisplayName("should return created patient")
    void createPatientTest() {

        // Arrange
        PatientDto patientDto = new PatientDto("John", "Doe");
        Patient patient = new Patient("John", "Doe");
        when(patientRepository.save(patient)).thenReturn(patient);
        when(modelMapper.map(patientDto, Patient.class)).thenReturn(patient);
        when(modelMapper.map(patient, PatientDto.class)).thenReturn(patientDto);

        // Act
        PatientDto result = patientService.createPatient(patientDto);

        // Assert
        assertEquals(patientDto, result);
        verify(patientRepository, times(1)).save(patient);
        verify(modelMapper, times(1)).map(patientDto, Patient.class);
        verify(modelMapper, times(1)).map(patient, PatientDto.class);
    }


    @Test
    @DisplayName("should return updated patient")
    void updatePatientTest() throws PatientNotFoundException {

        // Arrange
        int id = 1;
        PatientDto patientDto = new PatientDto("John", "Doe");
        Patient patient = new Patient("John", "Doe");
        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);
        when(modelMapper.map(patient, PatientDto.class)).thenReturn(patientDto);

        // Act
        PatientDto result = patientService.updatePatient(id, patientDto);

        // Assert
        assertEquals(patientDto, result);
        verify(patientRepository, times(1)).findById(id);
        verify(patientRepository, times(1)).save(patient);
        verify(modelMapper, times(1)).map(patient, PatientDto.class);
    }


    @Test
    @DisplayName("should throw PatientNotFoundException")
    void updatePatientNegativeTest() {

        // Arrange
        int id = 1;
        PatientDto patientDto = new PatientDto("John", "Doe");

        // Act
        // Assert
        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(id, patientDto));
        verify(patientRepository, times(1)).findById(id);
        verify(patientRepository, times(0)).save(any());
        verify(modelMapper, times(0)).map(any(), any());
    }


    @Test
    @DisplayName("should call patient repository")
    public void deletePatient() throws PatientNotFoundException {

        // Arrange
        int patientId = 1;
        Patient patient = new Patient();
        patient.setId(patientId);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // Act
        patientService.deletePatient(patientId);

        // Assert
        verify(patientRepository, times(1)).findById(patientId);
        verify(patientRepository, times(1)).delete(patient);
    }


    @Test
    @DisplayName("should throw PatientNotFoundException")
    public void deletePatientNegativeTest() {

        // Arrange
        int patientId = 1;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(patientId));
        verify(patientRepository, times(1)).findById(patientId);
        verify(patientRepository, never()).delete(any(Patient.class));
    }
}
