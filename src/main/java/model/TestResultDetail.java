/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author huytr
 */
public class TestResultDetail {

    private int batchId;
    private String categoryName;
    private int labOrderTestId;
    private String status;
    private String rejectReason;
    private String testName;
    private int parameterId;
    private String parameterName;
    private String unit;

    private String normalRange;
    private boolean isNumeric;
    private Object refMin;
    private Object refMax;
    private Object resultId;
    private String resultValue;
    private boolean isAbnormal;
    private String flag;

    

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getLabOrderTestId() {
        return labOrderTestId;
    }

    public void setLabOrderTestId(int labOrderTestId) {
        this.labOrderTestId = labOrderTestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getParameterId() {
        return parameterId;
    }

    public void setParameterId(int parameterId) {
        this.parameterId = parameterId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNormalRange() {
        return normalRange;
    }

    public void setNormalRange(String normalRange) {
        this.normalRange = normalRange;
    }

    public boolean isIsNumeric() {
        return isNumeric;
    }

    public void setIsNumeric(boolean isNumeric) {
        this.isNumeric = isNumeric;
    }

    public Object getRefMin() {
        return refMin;
    }

    public void setRefMin(Object refMin) {
        this.refMin = refMin;
    }

    public Object getRefMax() {
        return refMax;
    }

    public void setRefMax(Object refMax) {
        this.refMax = refMax;
    }

    public Object getResultId() {
        return resultId;
    }

    public void setResultId(Object resultId) {
        this.resultId = resultId;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public boolean isIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(boolean isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
