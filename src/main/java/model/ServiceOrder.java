
package model;

import java.sql.Date;
import java.sql.Timestamp;


public class ServiceOrder {

    private int serviceOrderId;
    private int patientId;
    private int medicalRecordId;
    private int serviceId;
    private int assignedById;
    private Integer cashierId;
    private double priceAtTime;
    private String status;
    private Timestamp paidAt;
    private String paymentMethod; // CASH hoặc BANKING

    // CÁC TRƯỜNG HIỂN THỊ THÊM (JOIN)
    private String patientName;
    private String serviceName;
    private String cashierName;
    private Date patientDOB;
    private String patientAddress;
    private int AppointmentId;

    public ServiceOrder() {
    }

    public ServiceOrder(int serviceOrderId, int patientId, int medicalRecordId, int serviceId, int assignedById, Integer cashierId, double priceAtTime, String status, Timestamp paidAt, String paymentMethod, String patientName, String serviceName, String cashierName, Date patientDOB, String patientAddress, int AppointmentId) {
        this.serviceOrderId = serviceOrderId;
        this.patientId = patientId;
        this.medicalRecordId = medicalRecordId;
        this.serviceId = serviceId;
        this.assignedById = assignedById;
        this.cashierId = cashierId;
        this.priceAtTime = priceAtTime;
        this.status = status;
        this.paidAt = paidAt;
        this.paymentMethod = paymentMethod;
        this.patientName = patientName;
        this.serviceName = serviceName;
        this.cashierName = cashierName;
        this.patientDOB = patientDOB;
        this.patientAddress = patientAddress;
        this.AppointmentId = AppointmentId;
    }

    public int getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(int serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
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

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(int assignedById) {
        this.assignedById = assignedById;
    }

    public Integer getCashierId() {
        return cashierId;
    }

    public void setCashierId(Integer cashierId) {
        this.cashierId = cashierId;
    }

    public double getPriceAtTime() {
        return priceAtTime;
    }

    public void setPriceAtTime(double priceAtTime) {
        this.priceAtTime = priceAtTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public Date getPatientDOB() {
        return patientDOB;
    }

    public void setPatientDOB(Date patientDOB) {
        this.patientDOB = patientDOB;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public int getAppointmentId() {
        return AppointmentId;
    }

    public void setAppointmentId(int AppointmentId) {
        this.AppointmentId = AppointmentId;
    }

}
