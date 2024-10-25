package com.example.test1.model;

import java.io.Serializable;

public class PatientModel implements Serializable {
    private String patientEmail;
    private String patientName;
    private String patientContactNo;
    private int patientAge;
    private String patientGender;
    private String patientBloodGroup;
    private double lat;
    private double lang;

    public PatientModel() {
    }

    public PatientModel(String patientEmail, String patientName, String patientContactNo, int patientAge, String patientGender, String patientBloodGroup, double lat, double lang) {
        this.patientEmail = patientEmail;
        this.patientName = patientName;
        this.patientContactNo = patientContactNo;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.patientBloodGroup = patientBloodGroup;
        this.lat = lat;
        this.lang = lang;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientContactNo() {
        return patientContactNo;
    }

    public void setPatientContactNo(String patientContactNo) {
        this.patientContactNo = patientContactNo;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientBloodGroup() {
        return patientBloodGroup;
    }

    public void setPatientBloodGroup(String patientBloodGroup) {
        this.patientBloodGroup = patientBloodGroup;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }
}