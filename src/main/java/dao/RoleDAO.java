/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.*;
import model.*;
import util.DBContext;

/**
 *
 * @author Tan Vinh
 */
public class RoleDAO extends DBContext {
    
    public List<Role> getAll() {
        List<Role> list = new ArrayList<>();
        
        String sql = "SELECT RoleId, RoleName FROM Role WHERE IsActive = 1";
        
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Role r = new Role();
                r.setRoleId(rs.getInt("RoleId"));
                r.setRoleName(rs.getString("RoleName"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean existsById(int roleId) {
        
        String sql = "SELECT 1 FROM Role WHERE RoleId = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isDoctor(int roleId) {
        String sql = "SELECT 1 FROM Role WHERE RoleId = ? AND RoleName = 'Doctor'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Role> searchRolesByName(String searchTerm) {
        List<Role> list = new ArrayList<>();
        // Câu lệnh SQL search theo tên (nếu searchTerm rỗng sẽ lấy hết)
        String sql = "SELECT * FROM Role WHERE RoleName LIKE ? OR ? = ''";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            String query = "%" + searchTerm + "%";
            st.setString(1, query);
            st.setString(2, searchTerm);
            
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Role r = new Role();
                    r.setRoleId(rs.getInt("RoleId"));
                    r.setRoleName(rs.getString("RoleName"));
                    r.setIsActive(rs.getBoolean("IsActive"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public Role getRoleById(int roleId) {
        String sql = "SELECT * FROM Role WHERE RoleId = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, roleId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Role r = new Role();
                    r.setRoleId(rs.getInt("RoleId"));
                    r.setRoleName(rs.getString("RoleName"));
                    r.setIsActive(rs.getBoolean("IsActive"));
                    return r;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        
    }
    
    public boolean insert(String roleName) {
        String sql = "INSERT INTO [dbo].[Role] ([RoleName]) VALUES (?)";
        
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, roleName);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
    }
    
    public boolean update(int roleId, boolean status) {
        String sql = "UPDATE [Role] SET IsActive = ? WHERE RoleId = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setBoolean(1, status);
            st.setInt(2, roleId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void main(String[] args) {
        for (Role role : new RoleDAO().getAll()) {
            System.out.println(role.toString());
        }
    }
}
