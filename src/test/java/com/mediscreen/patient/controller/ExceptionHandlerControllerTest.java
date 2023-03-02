package com.mediscreen.patient.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mediscreen.patient.exception.PatientNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerControllerTest {


    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;


    @Test
    public void handlePatientNotFoundExceptionTest() {

        PatientNotFoundException ex = new PatientNotFoundException("John");
        ResponseEntity<String> response = exceptionHandlerController.handlePatientNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(" Patient does not exist with name : John", response.getBody());
    }
}

