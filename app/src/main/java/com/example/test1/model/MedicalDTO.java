package com.example.test1.model;

public class MedicalDTO {
    private String name;
    private String address;
    private double distance;

    // Default constructor
    public MedicalDTO() {
    }

    // Parameterized constructor
    public MedicalDTO(String name, String address, double distance) {
        this.name = name;
        this.address = address;
        this.distance = distance;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter and Setter for distance
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "MedicalDTO{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", distance=" + distance +
                '}';
    }
}
