package com.alvarezaaronai.sed.utils;

import java.util.List;

public class patient{

    public patient(String _last_name, String _patient_image, String _first_name, String _patient_id, String _email, String _physician_id, List<record> _records){
            last_name = _last_name;
            patient_image = _patient_image;
            first_name = _first_name;
            patient_id = _patient_id;
            email = _email;
            physician_id = _physician_id;
            records = _records;
        }

    private String last_name;
    private String patient_image;
    private String first_name;
    private String patient_id;
    private String email;
    private String physician_id;
    private List<record> records;

    public String getLast_name() {
        return this.last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPatient_image() {
        return this.patient_image;
    }

    public void setPatient_image(String image) {
        this.patient_image = image;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getPatient_id() {
        return this.patient_id;
    }

    public void setPatient_id(String id) {
        this.patient_id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhysician_id() {
        return this.physician_id;
    }

    public void setPhysician_id(String id) {
        this.physician_id = id;
    }

    public List<record> getRecords() {
        return this.records;
    }

    public void setRecords(List<record> records) {
        this.records = records;
    }

    @Override
    public String toString(){
        return "\nlast_name: " + last_name + "\npatient_image: " + patient_image + "\nfirst_name: " + first_name + "\npatient_id: " + patient_id + "\nemail: " + email + "\nphysician id: " + physician_id + "\nrecords: " + records;
    }

}
