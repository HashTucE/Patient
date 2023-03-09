package com.mediscreen.patient.controller;

import com.mediscreen.library.dto.PatientDto;
import com.mediscreen.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class PatientCurlController {


    private final PatientService patientService;

    public PatientCurlController(PatientService patientService) {
        this.patientService = patientService;
    }

    private static final Logger log = LogManager.getLogger(PatientController.class);


    /**
     * Endpoint for adding a new patient
     * @param family patient's family name
     * @param given patient's given name
     * @param dob patient's date of birth
     * @param sex patient's sex
     * @param address patient's address
     * @param phone patient's phone number
     * @return HTTP response with status OK and message "Patient added"
     */
    @PostMapping("/patient/add")
    @Operation(summary = "Add a new patient", description = "Endpoint for adding a new patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Parameters({
            @Parameter(name = "family", description = "Patient's family name", required = true, example = "Doe"),
            @Parameter(name = "given", description = "Patient's given name", required = true, example = "John"),
            @Parameter(name = "dob", description = "Patient's date of birth", required = true, example = "2000-01-01"),
            @Parameter(name = "sex", description = "Patient's sex", required = true, example = "M"),
            @Parameter(name = "address", description = "Patient's address", required = true, example = "123 Main St."),
            @Parameter(name = "phone", description = "Patient's phone number", required = true, example = "555-1234")
    })
    public ResponseEntity<String> addPatient(@RequestParam("family") String family,
                                             @RequestParam("given") String given,
                                             @RequestParam("dob") LocalDate dob,
                                             @RequestParam("sex") String sex,
                                             @RequestParam("address") String address,
                                             @RequestParam("phone") String phone) {

        log.debug("Received request to add a patient: family: {}, given: {}, dob: {}, sex: {}, address: {}, phone: {}",
                family, given, dob, sex, address, phone);

        PatientDto patient = new PatientDto(family, given, dob, sex, address, phone);
        patientService.createPatient(patient);

        log.debug("Successfully added patient: {}", patient);
        return ResponseEntity.ok("Patient added");
    }
}
