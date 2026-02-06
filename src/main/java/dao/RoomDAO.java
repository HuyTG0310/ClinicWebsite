package dao;

import model.Room;
import util.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO extends DBContext {

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT [RoomId], [RoomName], [SpecialtyId], [CurrentDoctorId], [IsActive] " +
                      "FROM [DB_03_02].[dbo].[Room]";

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all rooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    public Room getRoomById(int roomId) {
        String query = "SELECT [RoomId], [RoomName], [SpecialtyId], [CurrentDoctorId], [IsActive] " +
                      "FROM [DB_03_02].[dbo].[Room] WHERE [RoomId] = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRoom(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting room by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean addRoom(Room room) {
    String query = "INSERT INTO [DB_03_02].[dbo].[Room] ([RoomName], [SpecialtyId], [CurrentDoctorId], [IsActive]) " +
                  "VALUES (?, ?, ?, ?)";

    System.out.println("\n========== RoomDAO.addRoom() ==========");
    System.out.println("SQL: " + query);
    System.out.println("Room Name: " + room.getRoomName());
    System.out.println("Specialty ID: " + room.getSpecialtyId());
    System.out.println("Current Doctor ID: " + room.getCurrentDoctorId());
    System.out.println("Is Active: " + room.getIsActive());

    try (PreparedStatement ps = conn.prepareStatement(query)) {
        System.out.println("PreparedStatement created");
        
        ps.setString(1, room.getRoomName());
        System.out.println("Set param 1 (RoomName): " + room.getRoomName());
        
        setNullableInt(ps, 2, room.getSpecialtyId());
        System.out.println("Set param 2 (SpecialtyId): " + room.getSpecialtyId());
        
        setNullableInt(ps, 3, room.getCurrentDoctorId());
        System.out.println("Set param 3 (CurrentDoctorId): " + room.getCurrentDoctorId());
        
        ps.setBoolean(4, room.getIsActive() != null ? room.getIsActive() : true);
        System.out.println("Set param 4 (IsActive): " + (room.getIsActive() != null ? room.getIsActive() : true));

        System.out.println("Executing update...");
        int rowsAffected = ps.executeUpdate();
        System.out.println("Rows affected: " + rowsAffected);
        System.out.println("========================================\n");
        
        return rowsAffected > 0;
        
    } catch (SQLException e) {
        System.out.println("\n=== SQL ERROR in addRoom() ===");
        System.out.println("Error Code: " + e.getErrorCode());
        System.out.println("SQL State: " + e.getSQLState());
        System.out.println("Message: " + e.getMessage());
        System.out.println("===============================\n");
        e.printStackTrace();
        return false;
    }
}

    public boolean updateRoom(Room room) {
        String query = "UPDATE [DB_03_02].[dbo].[Room] SET [RoomName] = ?, [SpecialtyId] = ?, " +
                      "[CurrentDoctorId] = ?, [IsActive] = ? WHERE [RoomId] = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, room.getRoomName());
            setNullableInt(ps, 2, room.getSpecialtyId());
            setNullableInt(ps, 3, room.getCurrentDoctorId());
            ps.setBoolean(4, room.getIsActive() != null ? room.getIsActive() : true);
            ps.setInt(5, room.getRoomId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoom(int roomId) {
        String query = "DELETE FROM [DB_03_02].[dbo].[Room] WHERE [RoomId] = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Room> searchRoomByName(String keyword) {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT [RoomId], [RoomName], [SpecialtyId], [CurrentDoctorId], [IsActive] " +
                      "FROM [DB_03_02].[dbo].[Room] WHERE [RoomName] LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRoom(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching rooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    public boolean isRoomNameExists(String roomName, Integer excludeRoomId) {
        String query = excludeRoomId != null 
            ? "SELECT COUNT(*) FROM [DB_03_02].[dbo].[Room] WHERE [RoomName] = ? AND [RoomId] != ?"
            : "SELECT COUNT(*) FROM [DB_03_02].[dbo].[Room] WHERE [RoomName] = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, roomName);
            if (excludeRoomId != null) {
                ps.setInt(2, excludeRoomId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking room name existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("RoomId"));
        room.setRoomName(rs.getString("RoomName"));
        
        Object specialtyId = rs.getObject("SpecialtyId");
        if (specialtyId != null) {
            room.setSpecialtyId(rs.getInt("SpecialtyId"));
        }

        Object currentDoctorId = rs.getObject("CurrentDoctorId");
        if (currentDoctorId != null) {
            room.setCurrentDoctorId(rs.getInt("CurrentDoctorId"));
        }

        room.setIsActive(rs.getBoolean("IsActive"));
        return room;
    }

    private void setNullableInt(PreparedStatement ps, int parameterIndex, Integer value) throws SQLException {
        if (value != null) {
            ps.setInt(parameterIndex, value);
        } else {
            ps.setNull(parameterIndex, java.sql.Types.INTEGER);
        }
    }
}