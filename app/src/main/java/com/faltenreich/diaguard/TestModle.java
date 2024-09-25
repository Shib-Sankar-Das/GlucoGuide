package com.faltenreich.diaguard;

public class TestModle {

    private String LabID, LabName, address, contact;

    public TestModle(String labID, String labName, String address, String contact) {
        this.LabID = labID;
        this.LabName = labName;
        this.address = address;
        this.contact = contact;
    }

    public String getLabID() {return LabID;}

    public void setLabID(String labID) {LabID = labID;}

    public String getLabName() {
        return LabName;
    }

    public void setLabName(String labName) {
        LabName = labName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
