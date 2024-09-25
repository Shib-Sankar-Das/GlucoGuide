package com.faltenreich.diaguard;

import java.io.Serializable;

public class AppointmentModel implements Serializable {
    private String labName;
    private String labAddress;
    private String labPhoneNumber;
    private String testName;
    private String testImage;
    private String description;
    private String prerequisites;
    private int price;
    private String date;
    private String time;
    private String appointmentId;

    public AppointmentModel() {
        // Default constructor required for calls to DataSnapshot.getValue(AppointmentModel.class)
    }

    public AppointmentModel(String labName, String labAddress, String labPhoneNumber, String testName, String testImage, String description, String prerequisites, int price, String date, String time, String appointmentId) {
        this.labName = labName;
        this.labAddress = labAddress;
        this.labPhoneNumber = labPhoneNumber;
        this.testName = testName;
        this.testImage = testImage;
        this.description = description;
        this.prerequisites = prerequisites;
        this.price = price;
        this.date = date;
        this.time = time;
        this.appointmentId = appointmentId;
    }

    // Getters and Setters
    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getLabAddress() {
        return labAddress;
    }

    public void setLabAddress(String labAddress) {
        this.labAddress = labAddress;
    }

    public String getLabPhoneNumber() {
        return labPhoneNumber;
    }

    public void setLabPhoneNumber(String labPhoneNumber) {
        this.labPhoneNumber = labPhoneNumber;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTestImage() {
        return testImage;
    }

    public void setTestImage(String testImage) {
        this.testImage = testImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    // Other getters and setters...
}