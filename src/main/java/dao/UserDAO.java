package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;
import util.DBContext;

public class UserDAO extends DBContext {

    // Lấy thông tin user theo ID (View Profile Detail)
    public User getUserById(int userId) {
        String sql = "SELECT UserId, Username, PasswordHash, FullName, Phone, Email, RoleId, IsActive "
                + "FROM [User] "
                + "WHERE UserId = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserId"));
                u.setUsername(rs.getString("Username"));
                u.setPasswordHash(rs.getString("PasswordHash"));
                u.setFullName(rs.getString("FullName"));
                u.setPhone(rs.getString("Phone"));
                u.setEmail(rs.getString("Email"));
                u.setRoleId(rs.getInt("RoleId"));
                u.setActive(rs.getBoolean("IsActive"));
                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update profile (FullName, Phone, Email)
    public boolean updateProfile(int userId, String fullName, String phone, String email) {
        String sql = "UPDATE [User] "
                + "SET FullName = ?, Phone = ?, Email = ? "
                + "WHERE UserId = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setInt(4, userId);

            int row = ps.executeUpdate();
            return row > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Check old password trước khi đổi mật khẩu
    public boolean checkOldPassword(int userId, String oldPassword) {
        String sql = "SELECT * FROM [User] WHERE UserId = ? AND PasswordHash = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setString(2, oldPassword);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Change password
    public boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE [User] SET PasswordHash = ? WHERE UserId = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            int row = ps.executeUpdate();
            return row > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}