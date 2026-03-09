/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Specialty;
import util.DBContext;

public class SpecialtyDAO extends DBContext {
    
    // VIEW LIST
    public List<Specialty> getAll() {
        List<Specialty> list = new ArrayList<>();
        String sql = "SELECT * FROM Specialty";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Specialty s = new Specialty();
                s.setSpecialtyId(rs.getInt("SpecialtyId"));
                s.setName(rs.getString("SpecialtyName"));
                s.setDescription(rs.getString("Description"));
                s.setIsActive(rs.getBoolean("IsActive"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    
    // VIEW LIST FOR ADD ROOM
    public List<Specialty> getAllActiveRoom() {
        List<Specialty> list = new ArrayList<>();
        String sql = "SELECT * FROM Specialty WHERE isActive = 1";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Specialty s = new Specialty();
                s.setSpecialtyId(rs.getInt("SpecialtyId"));
                s.setName(rs.getString("SpecialtyName"));
                s.setDescription(rs.getString("Description"));
                s.setIsActive(rs.getBoolean("IsActive"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    

    // VIEW DETAIL
    public Specialty getById(int id) {
        String sql = "SELECT * FROM Specialty WHERE SpecialtyId = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Specialty s = new Specialty();
                s.setSpecialtyId(rs.getInt("SpecialtyId"));
                s.setName(rs.getString("SpecialtyName"));
                s.setDescription(rs.getString("Description"));
                s.setIsActive(rs.getBoolean("IsActive"));
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ADD
    public void insert(Specialty s) {
        String sql = "INSERT INTO Specialty(SpecialtyName, Description, IsActive) VALUES (?, ?, 1)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getName());
            ps.setString(2, s.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // EDIT
    public void update(Specialty s) {
        String sql = "UPDATE Specialty SET SpecialtyName = ?, Description = ?, IsActive=? WHERE SpecialtyId = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getName());
            ps.setString(2, s.getDescription());
            ps.setBoolean(3, s.isIsActive());
            ps.setInt(4, s.getSpecialtyId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// check name đã tồn tại (dùng cho ADD)

    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM Specialty WHERE LOWER(SpecialtyName) = LOWER(?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByNameExceptId(String name, int id) {
        String sql = "SELECT 1 FROM Specialty "
                + "WHERE LOWER(SpecialtyName) = LOWER(?) "
                + "AND SpecialtyId <> ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // SEARCH
    public List<Specialty> searchByName(String keyword) {
        List<Specialty> list = new ArrayList<>();
        String sql = "SELECT * FROM Specialty WHERE IsActive = 1 AND SpecialtyName LIKE ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Specialty s = new Specialty();
                s.setSpecialtyId(rs.getInt("SpecialtyId"));
                s.setName(rs.getString("SpecialtyName"));
                s.setDescription(rs.getString("Description"));
                s.setIsActive(rs.getBoolean("IsActive"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
