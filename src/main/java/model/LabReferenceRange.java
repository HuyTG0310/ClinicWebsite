/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author huytr
 */
public class LabReferenceRange {

    private int rangeId;
    private int parameterId;
    private String gender; // "M", "F", "ALL"
    private int ageMinDays;
    private int ageMaxDays;
    private Double refMin;
    private Double refMax;
    private boolean isActive;

    public LabReferenceRange() {
    }

    public LabReferenceRange(int rangeId, int parameterId, String gender, int ageMinDays, int ageMaxDays, Double refMin, Double refMax, boolean isActive) {
        this.rangeId = rangeId;
        this.parameterId = parameterId;
        this.gender = gender;
        this.ageMinDays = ageMinDays;
        this.ageMaxDays = ageMaxDays;
        this.refMin = refMin;
        this.refMax = refMax;
        this.isActive = isActive;
    }

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public int getParameterId() {
        return parameterId;
    }

    public void setParameterId(int parameterId) {
        this.parameterId = parameterId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAgeMinDays() {
        return ageMinDays;
    }

    public void setAgeMinDays(int ageMinDays) {
        this.ageMinDays = ageMinDays;
    }

    public int getAgeMaxDays() {
        return ageMaxDays;
    }

    public void setAgeMaxDays(int ageMaxDays) {
        this.ageMaxDays = ageMaxDays;
    }

    public Double getRefMin() {
        return refMin;
    }

    public void setRefMin(Double refMin) {
        this.refMin = refMin;
    }

    public Double getRefMax() {
        return refMax;
    }

    public void setRefMax(Double refMax) {
        this.refMax = refMax;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
