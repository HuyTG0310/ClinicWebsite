package dao;

import model.*;
import util.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RoomDAO extends DBContext {

    public List<RoomView> getAllView() {
        List<RoomView> list = new ArrayList<>();

        String sql = "        SELECT r.RoomId, r.RoomName, r.IsActive,\n"
                + "               s.SpecialtyName, s.SpecialtyId, \n"
                + "               u.FullName AS DoctorName\n"
                + "        FROM Room r\n"
                + "        JOIN Specialty s ON r.SpecialtyId = s.SpecialtyId\n"
                + "        LEFT JOIN [User] u ON r.CurrentDoctorId = u.UserId";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RoomView rv = new RoomView();
                rv.setRoomId(rs.getInt("RoomId"));
                rv.setRoomName(rs.getString("RoomName"));
                rv.setSpecialtyName(rs.getString("SpecialtyName"));
                rv.setDoctorName(rs.getString("DoctorName"));
                rv.setSpecialtyId(rs.getInt("SpecialtyId"));
                rv.setIsActive(rs.getBoolean("IsActive"));
                list.add(rv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Room getRoomById(int roomId) {
        String query = "SELECT [RoomId], [RoomName], [SpecialtyId], [CurrentDoctorId], [IsActive] "
                + "FROM Room WHERE [RoomId] = ?";

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
        String query = "INSERT INTO Room ([RoomName], [SpecialtyId], [CurrentDoctorId], [IsActive]) "
                + "VALUES (?, ?, ?, ?)";

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
        String query = "UPDATE Room SET [RoomName] = ?, [SpecialtyId] = ?, "
                + "[CurrentDoctorId] = ?, [IsActive] = ? WHERE [RoomId] = ?";

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
        String query = "DELETE FROM Room WHERE [RoomId] = ?";

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
        String query = "SELECT [RoomId], [RoomName], [SpecialtyId], [CurrentDoctorId], [IsActive] "
                + "FROM Room WHERE [RoomName] LIKE ?";

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

    public List<RoomView> searchRoomViewByName(String keyword) {
        List<RoomView> list = new ArrayList<>();

        String sql = "        SELECT r.RoomId, r.RoomName, r.IsActive,\n"
                + "               s.SpecialtyName,\n"
                + "               u.FullName AS DoctorName\n"
                + "        FROM Room r\n"
                + "        JOIN Specialty s ON r.SpecialtyId = s.SpecialtyId\n"
                + "        LEFT JOIN [User] u ON r.CurrentDoctorId = u.UserId\n"
                + "        WHERE r.RoomName LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RoomView rv = new RoomView();
                rv.setRoomId(rs.getInt("RoomId"));
                rv.setRoomName(rs.getString("RoomName"));
                rv.setSpecialtyName(rs.getString("SpecialtyName"));
                rv.setDoctorName(rs.getString("DoctorName"));
                rv.setIsActive(rs.getBoolean("IsActive"));
                list.add(rv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean isRoomNameExists(String roomName, Integer excludeRoomId) {
        String query = excludeRoomId != null
                ? "SELECT COUNT(*) FROM Room WHERE [RoomName] = ? AND [RoomId] != ?"
                : "SELECT COUNT(*) FROM Room WHERE [RoomName] = ?";

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

    public RoomView getRoomDetail(int roomId) {
        String sql = "        SELECT r.RoomId, r.RoomName, r.IsActive,\n"
                + "               s.SpecialtyName,\n"
                + "               u.FullName AS DoctorName\n"
                + "        FROM Room r\n"
                + "        JOIN Specialty s ON r.SpecialtyId = s.SpecialtyId\n"
                + "        LEFT JOIN [User] u ON r.CurrentDoctorId = u.UserId\n"
                + "        WHERE r.RoomId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                RoomView rv = new RoomView();
                rv.setRoomId(rs.getInt("RoomId"));
                rv.setRoomName(rs.getString("RoomName"));
                rv.setSpecialtyName(rs.getString("SpecialtyName"));
                rv.setDoctorName(rs.getString("DoctorName"));
                rv.setIsActive(rs.getBoolean("IsActive"));
                return rv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void inactiveRoomsBySpecialty(int specialtyId) {
        String sql = "        UPDATE Room\n"
                + "        SET IsActive = 0\n"
                + "        WHERE SpecialtyId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, specialtyId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inactivating rooms by specialty: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeDoctorFromRooms(int doctorId) {
        String sql = "UPDATE Room SET CurrentDoctorId = NULL WHERE CurrentDoctorId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RoomEdit getRoomForEdit(int roomId) {
        String sql = "        SELECT \n"
                + "            r.RoomId,\n"
                + "            r.RoomName,\n"
                + "            r.SpecialtyId,\n"
                + "            r.CurrentDoctorId,\n"
                + "            r.IsActive AS RoomActive,\n"
                + "            s.IsActive AS SpecialtyActive\n"
                + "        FROM Room r\n"
                + "        JOIN Specialty s ON r.SpecialtyId = s.SpecialtyId\n"
                + "        WHERE r.RoomId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RoomEdit dto = new RoomEdit();
                    dto.setRoomId(rs.getInt("RoomId"));
                    dto.setRoomName(rs.getString("RoomName"));
                    dto.setSpecialtyId(rs.getInt("SpecialtyId"));
                    dto.setCurrentDoctorId(
                            (Integer) rs.getObject("CurrentDoctorId")
                    );
                    dto.setIsActive(rs.getBoolean("RoomActive"));
                    dto.setSpecialtyActive(rs.getBoolean("SpecialtyActive"));
                    return dto;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        RoomDAO dao = new RoomDAO();
        for (RoomView roomView : dao.getAllView()) {
            System.out.println(roomView);
        }
    }

}
