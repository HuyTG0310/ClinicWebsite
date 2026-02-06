package model;

/**
 * Room Entity Class
 * 
 * @author ClinicWebsite
 */
public class Room {
    private int roomId;
    private String roomName;
    private Integer specialtyId;
    private Integer currentDoctorId;
    private Boolean isActive;

    // Constructor không tham số
    public Room() {
    }

    // Constructor đầy đủ tham số
    public Room(int roomId, String roomName, Integer specialtyId, Integer currentDoctorId, Boolean isActive) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.specialtyId = specialtyId;
        this.currentDoctorId = currentDoctorId;
        this.isActive = isActive;
    }

    // Getters and Setters
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

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    public Integer getCurrentDoctorId() {
        return currentDoctorId;
    }

    public void setCurrentDoctorId(Integer currentDoctorId) {
        this.currentDoctorId = currentDoctorId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", specialtyId=" + specialtyId +
                ", currentDoctorId=" + currentDoctorId +
                ", isActive=" + isActive +
                '}';
    }
}
