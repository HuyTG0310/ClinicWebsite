package model;

import java.sql.Date;

/**
 * Patient Entity Class
 * 
 * @author ClinicWebsite
 */
public class Patient {
    private int patientId;
    private String fullName;
    private String phone;
    private Date dateOfBirth;
    private String gender;
    private String address;
    private String medicalHistory;
    private String allergy;
    private Date createdAt;

    // Constructor không tham số
    public Patient() {
    }

    // Constructor đầy đủ tham số
    public Patient(int patientId, String fullName, String phone, Date dateOfBirth,
            String gender, String address, String medicalHistory, String allergy, Date createdAt) {
        this.patientId = patientId;
        this.fullName = fullName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.medicalHistory = medicalHistory;
        this.allergy = allergy;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                ", allergy='" + allergy + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
