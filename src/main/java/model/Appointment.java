package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Appointment {

    private int appointmentId;
    private int patientId;
    private String patientAddress;
    private Integer medicalRecordId; // Có thể null
    private int roomId;
    private Integer createdBy;
    private Timestamp appointmentTime;
    private String status; // WAITING, IN_PROGRESS, COMPLETED, CANCELLED

    // Các trường phụ để hiển thị (Join bảng)
    private String patientName;
    private String patientPhone;
    private String roomName;
    private String doctorName; // Tên bác sĩ phụ trách phòng đó
    private String specialtyName;

    private String patientGender;
    private Date patientDob;
    private String receptionistName;
    private String paymentMethod;
    private double price;

    private String paymentStatus;
    private String serviceName;

    public Appointment() {
    }

    public Appointment(int appointmentId, int patientId, String patientAddress, Integer medicalRecordId, int roomId, Integer createdBy, Timestamp appointmentTime, String status, String patientName, String patientPhone, String roomName, String doctorName, String specialtyName, String patientGender, Date patientDob, String receptionistName, String paymentMethod, double price, String paymentStatus) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientAddress = patientAddress;
        this.medicalRecordId = medicalRecordId;
        this.roomId = roomId;
        this.createdBy = createdBy;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.patientName = patientName;
        this.patientPhone = patientPhone;
        this.roomName = roomName;
        this.doctorName = doctorName;
        this.specialtyName = specialtyName;
        this.patientGender = patientGender;
        this.patientDob = patientDob;
        this.receptionistName = receptionistName;
        this.paymentMethod = paymentMethod;
        this.price = price;
        this.paymentStatus = paymentStatus;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public Integer getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(Integer medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Timestamp appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public Date getPatientDob() {
        return patientDob;
    }

    public void setPatientDob(Date patientDob) {
        this.patientDob = patientDob;
    }

    public String getReceptionistName() {
        return receptionistName;
    }

    public void setReceptionistName(String receptionistName) {
        this.receptionistName = receptionistName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
