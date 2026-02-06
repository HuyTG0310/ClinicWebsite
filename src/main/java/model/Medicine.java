/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author TRUONGTHINHNGUYEN
 */
public class Medicine {
    private int medicineId;
    private String medicineName;
    private String unit;
    private String ingredients;
    private String usage;
    private String contraindication;
    private boolean isActive;

    public Medicine() {
    }

    public Medicine(int medicineId, String medicineName, String unit, String ingredients, String usage, String contraindication, boolean isActive) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.unit = unit;
        this.ingredients = ingredients;
        this.usage = usage;
        this.contraindication = contraindication;
        this.isActive = isActive;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getContraindication() {
        return contraindication;
    }

    public void setContraindication(String contraindication) {
        this.contraindication = contraindication;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
}
