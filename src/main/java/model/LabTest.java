/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 *
 * @author huytr
 */
public class LabTest {

    private int labTestId;
    private int serviceId;
    private String testCode;
    private String testName;
    private int categoryId;
    private String categoryName; // Lấy từ bảng LabTestCategory
    private boolean isPanel;
    private double currentPrice; // Lấy từ bảng Service
    private int sortOrder;
    private boolean isActive;
    private List<LabTestParameter> parameters;

    public LabTest() {
    }

    public LabTest(int labTestId, int serviceId, String testCode, String testName, int categoryId, String categoryName, boolean isPanel, double currentPrice, int sortOrder, boolean isActive, List<LabTestParameter> parameters) {
        this.labTestId = labTestId;
        this.serviceId = serviceId;
        this.testCode = testCode;
        this.testName = testName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isPanel = isPanel;
        this.currentPrice = currentPrice;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.parameters = parameters;
    }

    public int getLabTestId() {
        return labTestId;
    }

    public void setLabTestId(int labTestId) {
        this.labTestId = labTestId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isIsPanel() {
        return isPanel;
    }

    public void setIsPanel(boolean isPanel) {
        this.isPanel = isPanel;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<LabTestParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<LabTestParameter> parameters) {
        this.parameters = parameters;
    }

}
