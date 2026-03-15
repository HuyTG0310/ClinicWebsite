/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Map;

/**
 *
 * @author huytr
 */
public class TestResult {

    private int medicalRecordId;

    private int patientId;

    private String patientName;

    private String gender;

    private int age;

    private String doctorName;

    private int totalTests;

    private int completedTests;

    private String progress;

    private boolean isFullyCompleted;

    private String[] orderTestIds;

    private String[] paramIds;

    private int technicianId;

    private Map<String, String[]> parameterMap;

    public TestResult() {
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    public int getCompletedTests() {
        return completedTests;
    }

    public void setCompletedTests(int completedTests) {
        this.completedTests = completedTests;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public boolean isIsFullyCompleted() {
        return isFullyCompleted;
    }

    public void setIsFullyCompleted(boolean isFullyCompleted) {
        this.isFullyCompleted = isFullyCompleted;
    }

    public String[] getOrderTestIds() {
        return orderTestIds;
    }

    public void setOrderTestIds(String[] orderTestIds) {
        this.orderTestIds = orderTestIds;
    }

    public String[] getParamIds() {
        return paramIds;
    }

    public void setParamIds(String[] paramIds) {
        this.paramIds = paramIds;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

}
