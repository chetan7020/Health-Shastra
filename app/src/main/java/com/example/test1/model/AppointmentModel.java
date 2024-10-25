package com.example.test1.model;

import java.io.Serializable;

public class AppointmentModel implements Serializable {
    private String appointmentID;
    private String patientEmail;
    private String doctorEmail;
    private String doctorName;
    private String appointmentPatientName;
    private String appointmentDate;
    private String appointmentStartTime;
    private String appointmentEndTime;
    private String appointmentStatus;

    private String appointmentLink;

    public AppointmentModel() {
    }

    public AppointmentModel(String appointmentID, String patientEmail, String doctorEmail, String doctorName, String appointmentPatientName, String appointmentDate, String appointmentStartTime, String appointmentEndTime, String appointmentStatus, String appointmentLink) {
        this.appointmentID = appointmentID;
        this.patientEmail = patientEmail;
        this.doctorEmail = doctorEmail;
        this.doctorName = doctorName;
        this.appointmentPatientName = appointmentPatientName;
        this.appointmentDate = appointmentDate;
        this.appointmentStartTime = appointmentStartTime;
        this.appointmentEndTime = appointmentEndTime;
        this.appointmentStatus = appointmentStatus;
        this.appointmentLink = appointmentLink;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAppointmentPatientName() {
        return appointmentPatientName;
    }

    public void setAppointmentPatientName(String appointmentPatientName) {
        this.appointmentPatientName = appointmentPatientName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(String appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public String getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(String appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getAppointmentLink() {
        return appointmentLink;
    }

    public void setAppointmentLink(String appointmentLink) {
        this.appointmentLink = appointmentLink;
    }
}