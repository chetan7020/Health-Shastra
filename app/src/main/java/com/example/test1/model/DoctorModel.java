package com.example.test1.model;

import java.io.Serializable;
import java.util.List;

public class DoctorModel implements Serializable {
    private String docName;
    private String docMobileNumber;
    private String docEmail;
    private List<Integer> timeSlot;
    private List<String> docSpecializations;

    private double docLat;
    private double docLang;

    private int experience;
    private int totalBookedSlots;
    private int totalRevenue;
    private int charges;

    public DoctorModel() {
    }

    public DoctorModel(String docName, String docMobileNumber, String docEmail, List<Integer> timeSlot, List<String> docSpecializations, double docLat, double docLang, int experience, int totalBookedSlots, int totalRevenue, int charges) {
        this.docName = docName;
        this.docMobileNumber = docMobileNumber;
        this.docEmail = docEmail;
        this.timeSlot = timeSlot;
        this.docSpecializations = docSpecializations;
        this.docLat = docLat;
        this.docLang = docLang;
        this.experience = experience;
        this.totalBookedSlots = totalBookedSlots;
        this.totalRevenue = totalRevenue;
        this.charges = charges;
    }

    public List<Integer> getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(List<Integer> timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocMobileNumber() {
        return docMobileNumber;
    }

    public void setDocMobileNumber(String docMobileNumber) {
        this.docMobileNumber = docMobileNumber;
    }

    public String getDocEmail() {
        return docEmail;
    }

    public void setDocEmail(String docEmail) {
        this.docEmail = docEmail;
    }

    public List<String> getDocSpecializations() {
        return docSpecializations;
    }

    public void setDocSpecializations(List<String> docSpecializations) {
        this.docSpecializations = docSpecializations;
    }

    public double getDocLat() {
        return docLat;
    }

    public void setDocLat(double docLat) {
        this.docLat = docLat;
    }

    public double getDocLang() {
        return docLang;
    }

    public void setDocLang(double docLang) {
        this.docLang = docLang;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getTotalBookedSlots() {
        return totalBookedSlots;
    }

    public void setTotalBookedSlots(int totalBookedSlots) {
        this.totalBookedSlots = totalBookedSlots;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(int totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }
}
