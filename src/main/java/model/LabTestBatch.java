/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class LabTestBatch {

    // 1. CÁC BIẾN CHÍNH
    private int batchId;
    private int patientId;
    private int medicalRecordId;
    private int createdByDoctorId;
    private String status;
    private Date createdAt; 
    private List<Integer> testIds;
    private Timestamp orderTime;

    // 2. CÁC BIẾN MỞ RỘNG
    private String doctorName;         // Tên bác sĩ chỉ định
    private List<String> testNames;    // Danh sách tên các xét nghiệm trong Lô này (VD: [Hồng cầu, Bạch cầu, ALT])
    private String patientName;
    private String gender;
    private int age;

    // --- Constructor ---
    public LabTestBatch() {
    }

    public LabTestBatch(int batchId, int patientId, int medicalRecordId, int createdByDoctorId, String status) {
        this.batchId = batchId;
        this.patientId = patientId;
        this.medicalRecordId = medicalRecordId;
        this.createdByDoctorId = createdByDoctorId;
        this.status = status;
    }

    // --- Getters & Setters ---
    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public int getCreatedByDoctorId() {
        return createdByDoctorId;
    }

    public void setCreatedByDoctorId(int createdByDoctorId) {
        this.createdByDoctorId = createdByDoctorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public List<String> getTestNames() {
        return testNames;
    }

    public void setTestNames(List<String> testNames) {
        this.testNames = testNames;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Integer> getTestIds() {
        return testIds;
    }

    public void setTestIds(List<Integer> testIds) {
        this.testIds = testIds;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

}
