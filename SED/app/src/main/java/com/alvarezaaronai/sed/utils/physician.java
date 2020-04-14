package com.alvarezaaronai.sed.utils;

import java.util.List;

public class physician {

    public physician(String _physician_id, List<patient> _patients, String _last_name, String _physician_image, String _first_name){
        physician_id = _physician_id;
        patients = _patients;
        last_name = _last_name;
        physician_image = _physician_image;
        first_name = _first_name;
    }

    private String physician_id;
    private List<patient> patients;
    private String last_name;
    private String physician_image;
    private String first_name;

    public String getPhysician_id() {
        return this.physician_id;
    }

    public void setPhysician_id(String id) {
        this.physician_id = id;
    }

    public List<patient> getPatients() {
        return this.patients;
    }

    public void setPatients(List<patient> patients) {
        this.patients = patients;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhysician_image() {
        return this.physician_image;
    }

    public void setPhysician_image(String image) {
        this.physician_image = image;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    @Override
    public String toString(){
        return ("\nphysician_id: " + physician_id + "\n\npatients: " + patients + "\n\nlast_name: " + last_name + "\nphysician_image: " + physician_image + "\nfirst_name: " + first_name);
    }

}
