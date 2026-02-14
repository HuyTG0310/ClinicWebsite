/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


public class RoomEdit {

    private int roomId;
    private String roomName;
    private int specialtyId;
    private boolean specialtyActive;
    private Integer currentDoctorId;
    private boolean isActive;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(int specialtyId) {
        this.specialtyId = specialtyId;
    }

    public boolean isSpecialtyActive() {
        return specialtyActive;
    }

    public void setSpecialtyActive(boolean specialtyActive) {
        this.specialtyActive = specialtyActive;
    }

    public Integer getCurrentDoctorId() {
        return currentDoctorId;
    }

    public void setCurrentDoctorId(Integer currentDoctorId) {
        this.currentDoctorId = currentDoctorId;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}
