package model;

public class PrescriptionItem {

    private String medicineName;
    private String unit;
    private int quantity;
    private String dosage; // Cách dùng (Sáng 1, Chiều 1...)
    private String note;   // Ghi chú thêm

    public PrescriptionItem() {
    }

    // --- Getters & Setters ---
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
}
