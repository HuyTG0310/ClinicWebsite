/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.*;
import model.Privilege;
import util.DBContext;

/**
 *
 * @author huytr
 */
public class PrivilegeDAO extends DBContext {

    public List<String> getPrivilegeCodesByUserId(int userId) {
        List<String> privileges = new ArrayList<>();

        String sql = "            SELECT DISTINCT p.PrivilegeCode\n"
                + "            FROM [User] u\n"
                + "            JOIN Role r ON u.RoleId = r.RoleId\n"
                + "            JOIN RolePrivilege rp ON r.RoleId = rp.RoleId\n"
                + "            JOIN Privilege p ON rp.PrivilegeId = p.PrivilegeId\n"
                + "            WHERE u.UserId = ? AND u.IsActive = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                privileges.add(rs.getString("PrivilegeCode"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return privileges;
    }

    // 1. Lấy danh sách ID các quyền mà Role này ĐANG CÓ (để tick sẵn trên UI)
    public List<Integer> getPrivilegeIdsByRole(int roleId) {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT PrivilegeId FROM RolePrivilege WHERE RoleId = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, roleId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getInt("PrivilegeId"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Privilege> getAll() {
        List<Privilege> list = new ArrayList<>();
        String sql = "SELECT * FROM Privilege";
        try (PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Privilege p = new Privilege();
                p.setPrivilegeId(rs.getInt("PrivilegeId"));
                p.setPrivilegeCode(rs.getString("PrivilegeCode"));
                p.setDescription(rs.getString("Description"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateRolePrivileges(int roleId, String[] privilegeIds) {
        String deleteSql = "DELETE FROM RolePrivilege WHERE RoleId = ?";
        String insertSql = "INSERT INTO RolePrivilege (RoleId, PrivilegeId) VALUES (?, ?)";

        try {
            // Tắt auto commit để quản lý Transaction thủ công
            conn.setAutoCommit(false);

            // BƯỚC 1: Xóa sạch quyền cũ
            try (PreparedStatement psDelete = conn.prepareStatement(deleteSql)) {
                psDelete.setInt(1, roleId);
                psDelete.executeUpdate();
            }

            // BƯỚC 2: Thêm quyền mới (Nếu có chọn)
            if (privilegeIds != null && privilegeIds.length > 0) {
                try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                    for (String pId : privilegeIds) {
                        psInsert.setInt(1, roleId);
                        psInsert.setInt(2, Integer.parseInt(pId));
                        psInsert.addBatch(); // Gom lại chạy 1 lần cho nhanh
                    }
                    psInsert.executeBatch();
                }
            }

            // BƯỚC 3: Chốt đơn (Commit)
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); // Có lỗi thì hoàn tác
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true); // Bật lại chế độ mặc định
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        PrivilegeDAO dao = new PrivilegeDAO();

        for (String s : dao.getPrivilegeCodesByUserId(2)) {
            System.out.println(s);
        }
//        System.out.println();
    }
}
