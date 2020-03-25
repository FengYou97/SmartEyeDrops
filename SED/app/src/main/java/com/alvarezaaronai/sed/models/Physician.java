package com.alvarezaaronai.sed.models;

import java.util.ArrayList;

public class Physician {

    private int physician_id;

    private String first_name;

    private String last_name;

    private ArrayList<Patient> patients;

    private String physician_image;

    public String getPhysician_image() {
        return physician_image;
    }

    public int getPhysician_id() {
        return physician_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public ArrayList<Patient> getPatientModel() {
        return patients;
    }

}
