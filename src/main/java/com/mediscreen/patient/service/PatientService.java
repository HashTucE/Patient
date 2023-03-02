package com.mediscreen.patient.service;

import com.mediscreen.library.dto.PatientDto;
import com.mediscreen.patient.exception.PatientNotFoundException;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.repository.PatientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public PatientService(PatientRepository patientRepository, ModelMapper modelMapper) {
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
    }

    private static final Logger log = LogManager.getLogger(PatientService.class);



    /**
     Find a patient by id.
     @param id The id of the patient to retrieve.
     @return A {@link PatientDto} representing the patient with the given id, or an HTTP 404 (Not Found) response if the patient could not be found.
     @throws PatientNotFoundException if the patient could not be found.
     */
    public PatientDto findPatientById(int id) throws PatientNotFoundException {

        log.debug("Finding patient with id: {}", id);
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));

        log.debug("Found patient: {}", patient);
        return modelMapper.map(patient, PatientDto.class);
    }


    /**
     Find a patient by familyName.
     @param familyName of the patient to retrieve.
     @return A {@link PatientDto} representing the patient with the given familyName, or an HTTP 404 (Not Found) response if the patient could not be found.
     @throws PatientNotFoundException if the patient could not be found.
     */
    public PatientDto findPatientByFamilyName(String familyName) throws PatientNotFoundException {
        log.debug("Finding patient with family name: {}", familyName);
        Patient patient = patientRepository.findByFamilyName(familyName).orElseThrow(() -> new PatientNotFoundException(familyName));

        log.debug("Found patient: {}", patient);
        return modelMapper.map(patient, PatientDto.class);
    }


    /**
     Get all patients.
     @return A list of {@link PatientDto} representing all patients.
     */
    public List<PatientDto> getAllPatients() {

        log.debug("Getting all patients");
        List<Patient> patients = patientRepository.findAll();

        log.debug("Found patients: {}", patients);
        return patients.stream()
                .map(patient -> modelMapper.map(patient, PatientDto.class))
                .collect(Collectors.toList());
    }


    /**
     Create a new patient.
     @param patientDto A {@link PatientDto} representing the patient to create.
     @return A {@link PatientDto} representing the created patient.
     */
    public PatientDto createPatient(PatientDto patientDto) {
        log.debug("Creating patient: {}", patientDto);
        Patient patient = modelMapper.map(patientDto, Patient.class);
        patient = patientRepository.save(patient);
        log.debug("Created patient: {}", patient);
        return modelMapper.map(patient, PatientDto.class);
    }


    /**
     Update an existing patient.
     @param id The id of the patient to update.
     @param patientDto A {@link PatientDto} representing the updated patient information.
     @return A {@link PatientDto} representing the updated patient.
     @throws PatientNotFoundException if the patient could not be found.
     */
    public PatientDto updatePatient(int id, PatientDto patientDto) throws PatientNotFoundException {

        log.debug("Updating patient with id: {} and data: {}", id, patientDto);
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        patient.setLastName(patientDto.getLastName());
        patient.setFirstName(patientDto.getFirstName());
        patient.setAddress(patientDto.getAddress());
        patient.setPhone(patientDto.getPhone());
        patient.setBirthdate(patientDto.getBirthdate());
        patient.setSex(patientDto.getSex());
        patient = patientRepository.save(patient);

        log.debug("Updated patient: {}", patient);
        return modelMapper.map(patient, PatientDto.class);
    }


    /**
     Delete a patient by id.
     @param id the id of the patient to delete
     @throws PatientNotFoundException if the patient with the given id does not exist
     */
    public void deletePatient(int id) throws PatientNotFoundException {

        log.debug("Processing delete patient request for id: {}", id);
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));

        patientRepository.delete(patient);
        log.debug("Patient with id: {} deleted successfully", id);
    }
}
