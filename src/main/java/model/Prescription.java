/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Prescription {

    private int prescriptionId;
    private int medicalRecordId;
    private int medicineId;
    private int quantity;
    private String dosage;
    private String note;

    // Thuộc tính mở rộng để hiển thị tên thuốc ra màn hình Detail sau này
    private String medicineName;
    private String unit;
    private String usage;
    private String ingredients;
    private String contraindication;

    public Prescription() {
    }

    public Prescription(int prescriptionId, int medicalRecordId, int medicineId, int quantity, String dosage, String note, String medicineName, String unit) {
        this.prescriptionId = prescriptionId;
        this.medicalRecordId = medicalRecordId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.dosage = dosage;
        this.note = note;
        this.medicineName = medicineName;
        this.unit = unit;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getContraindication() {
        return contraindication;
    }

    public void setContraindication(String contraindication) {
        this.contraindication = contraindication;
    }

}
