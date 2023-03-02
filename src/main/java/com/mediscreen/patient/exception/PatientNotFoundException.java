package com.mediscreen.patient.exception;

public class PatientNotFoundException extends Exception {


    public PatientNotFoundException(int id) {

        super(" Patient does not exist with id : " + id);
    }

    public PatientNotFoundException(String familyName) {

        super(" Patient does not exist with name : " + familyName);
    }
}

