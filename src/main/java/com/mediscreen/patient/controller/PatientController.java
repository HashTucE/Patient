package com.mediscreen.patient.controller;


import com.mediscreen.library.dto.PatientDto;
import com.mediscreen.patient.exception.PatientNotFoundException;
import com.mediscreen.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {


    private final PatientService patientService;


    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }



    private static final Logger log = LogManager.getLogger(PatientController.class);



    /**
     * Find patient by id response entity.
     *
     * @param id the id
     * @return the response entity
     * @throws PatientNotFoundException the patient not found exception
     */
    @GetMapping("/find/{id}")
    @Operation(summary = "Find a patient by ID", description = "Returns the patient with the given ID")
    @ApiResponse(responseCode = "200", description = "Patient found")
    @ApiResponse(responseCode = "404", description = "Patient not found")
    public ResponseEntity<PatientDto> findPatientById(@PathVariable int id) {
        try {
            log.debug("Find patient by id request received, id: {}", id);
            PatientDto patient = patientService.findPatientById(id);
            log.debug("Find patient by id request processed, patient: {}", patient);
            return ResponseEntity.ok(patient);
        } catch (PatientNotFoundException ex) {
            log.error("PatientNotFoundException: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /**
     * Find patient by family name response entity.
     *
     * @param familyName of the patient
     * @return the response entity
     * @throws PatientNotFoundException the patient not found exception
     */
    @GetMapping("/finder/{familyName}")
    @Operation(summary = "Find a patient by family name", description = "Returns the patient with the given family name")
    @ApiResponse(responseCode = "200", description = "Patient found")
    @ApiResponse(responseCode = "404", description = "Patient not found")
    public ResponseEntity<PatientDto> findPatientByFamilyName(@PathVariable String familyName) throws PatientNotFoundException {

        log.debug("Find patient by family name request received, family name: {}", familyName);
        PatientDto patient = patientService.findPatientByFamilyName(familyName);
        log.debug("Find patient by family name request processed, patient: {}", patient);
        return ResponseEntity.ok(patient);
    }


    /**
     * Get all patients response entity.
     *
     * @return the response entity
     */
    @GetMapping("/list")
    @Operation(summary = "Get all patients", description = "Returns a list of all patients")
    @ApiResponse(responseCode = "200", description = "List of patients")
    @ApiResponse(responseCode = "204", description = "No patients found")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patientList = patientService.getAllPatients();

        if (patientList.isEmpty()) {
            log.debug("Get all patients request processed, no patients found");
            return ResponseEntity.noContent().build();
        } else {
            log.debug("Get all patients request processed, patients count: {}", patientList.size());
            return ResponseEntity.ok(patientList);
        }
    }


    /**
     * Validate patient response entity.
     *
     * @param patientDto the patient dto
     * @return the response entity
     */
    @PostMapping("/validate")
    @Operation(summary = "Validate a patient", description = "Validates and creates a new patient")
    @ApiResponse(responseCode = "200", description = "Patient created")
    @ApiResponse(responseCode = "400", description = "Invalid patient details")
    public ResponseEntity<PatientDto> validatePatient(@Valid @RequestBody PatientDto patientDto) {

        log.debug("Validate patient request received, patient: {}", patientDto);
        PatientDto patient = patientService.createPatient(patientDto);
        log.debug("Validate patient request processed, patient: {}", patient);
        return ResponseEntity.ok(patient);
    }


    /**
     * Update patient response entity.
     *
     * @param id the id
     * @param patientDto the patient dto
     * @return the response entity
     * @throws PatientNotFoundException the patient not found exception
     */
    @PostMapping("/update/{id}")
    @Operation(summary = "Update a patient", description = "Updates an existing patient")
    @ApiResponse(responseCode = "200", description = "Patient updated")
    @ApiResponse(responseCode = "404", description = "Patient not found")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable int id, @RequestBody PatientDto patientDto) throws PatientNotFoundException {

        log.debug("Update patient request received, id: {}, patient: {}", id, patientDto);
        PatientDto patient = patientService.updatePatient(id, patientDto);
        log.debug("Update patient request processed, patient: {}", patient);
        return ResponseEntity.ok(patient);
    }


    /**

     Delete a patient by id.
     @param id The id of the patient to delete.
     @return An HTTP 204 (No Content) response if the patient was successfully deleted, or an HTTP 404 (Not Found) response if the patient could not be found.
     @throws PatientNotFoundException if the patient could not be found.
     */
    @PostMapping("/delete/{id}")
    @Operation(summary = "Delete a patient", description = "Deletes an existing patient by ID")
    @ApiResponse(responseCode = "204", description = "Patient deleted")
    @ApiResponse(responseCode = "404", description = "Patient not found")
    public ResponseEntity<Void> deletePatient(@PathVariable int id) throws PatientNotFoundException {

        patientService.deletePatient(id);
        log.debug("Deleted patient with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}

