package com.faltenreich.diaguard;

public class DoctorModle {

    private String DoctorName, DocSpeciality, DocAddress, DocHospital, DocEmail, DocContact;
    private boolean DocStatus;


    public DoctorModle(String doctorName, String docSpeciality, String docAddress, String docHospital, String docEmail, String docContact, boolean docStatus) {
        DoctorName = doctorName;
        DocSpeciality = docSpeciality;
        DocAddress = docAddress;
        DocHospital = docHospital;
        DocEmail = docEmail;
        DocContact = docContact;
        DocStatus = docStatus;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getDocSpeciality() { return DocSpeciality; }

    public void setDocSpeciality(String docSpeciality) { DocSpeciality = docSpeciality; }

    public String getDocAddress() { return DocAddress; }

    public void setDocAddress(String docAddress) {
        DocAddress = docAddress;
    }

    public String getDocHospital() {
        return DocHospital;
    }

    public void setDocHospital(String docHospital) {
        DocHospital = docHospital;
    }

    public String getDocEmail() {
        return DocEmail;
    }

    public void setDocEmail(String docEmail) {
        DocEmail = docEmail;
    }

    public String getDocContact() {
        return DocContact;
    }

    public void setDocContact(String docContact) {
        DocContact = docContact;
    }

    public boolean isDocStatus() {
        return DocStatus;
    }

    public void setDocStatus(boolean docStatus) {
        DocStatus = docStatus;
    }
}
