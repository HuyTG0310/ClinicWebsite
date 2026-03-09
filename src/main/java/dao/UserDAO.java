package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import model.*;
import util.DBContext;

public class UserDAO extends DBContext {

    // Lấy thông tin user theo ID (View Profile Detail)
    public User getUserById(int userId) {
        String sql = "SELECT u.UserId, u.Username, u.PasswordHash ,u.FullName, u.Phone, u.Email,\n"
                + "               u.IsActive, r.RoleId, r.RoleName\n"
                + "        FROM [User] u\n"
                + "        JOIN Role r ON u.RoleId = r.RoleId\n"
                + "        WHERE u.UserId = ?";

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
                u.setIsActive(rs.getBoolean("IsActive"));
                u.setRoleId(rs.getInt("RoleId"));
                u.setRoleName(rs.getString("RoleName"));
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

    public List<User> getAll() {
        List<User> list = new ArrayList<>();

        String sql = "        SELECT u.UserId, u.Username, u.FullName, u.Phone, u.Email,\n"
                + "               u.IsActive, r.RoleId, r.RoleName, r.IsActive AS RoleActive\n"
                + "        FROM [User] u\n"
                + "        JOIN Role r ON u.RoleId = r.RoleId\n"
                + "        ORDER BY u.UserId";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserId"));
                u.setUsername(rs.getString("Username"));
                u.setFullName(rs.getString("FullName"));
                u.setPhone(rs.getString("Phone"));
                u.setEmail(rs.getString("Email"));
                u.setRoleId(rs.getInt("RoleId"));
                u.setRoleName(rs.getString("RoleName"));
                u.setIsActive(rs.getBoolean("IsActive"));
                u.setRoleActive(rs.getBoolean("RoleActive"));
                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(User u) {

        String sql = "INSERT INTO [User]\n"
                + "(Username, PasswordHash, FullName, Phone, Email, RoleId, IsActive)\n"
                + "VALUES (?, ?, ?, ?, ?, ?, 1)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, "123"); // 👈 password giả, KHÔNG hash
            ps.setString(3, u.getFullName());
            ps.setString(4, u.getPhone());
            ps.setString(5, u.getEmail());
            ps.setInt(6, u.getRoleId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(User u) {

        String sql = "        UPDATE [User]\n"
                + "        SET Username = ?, FullName = ?, Phone = ?, Email = ?,\n"
                + "            RoleId = ?, IsActive = ?\n"
                + "        WHERE UserId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getFullName());
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getEmail());
            ps.setInt(5, u.getRoleId());
            ps.setBoolean(6, u.isIsActive());
            ps.setInt(7, u.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> search(String keyword, Integer roleId, Boolean isActive) {

        List<User> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("        SELECT u.UserId, u.Username, u.FullName, u.Phone, u.Email,\n"
                + "               u.IsActive, r.RoleId, r.RoleName, r.IsActive AS RoleActive\n"
                + "        FROM [User] u\n"
                + "        JOIN Role r ON u.RoleId = r.RoleId\n"
                + "        WHERE 1 = 1");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (u.Username LIKE ? OR u.FullName LIKE ?)");
            String kw = "%" + keyword.trim() + "%";
            params.add(kw);
            params.add(kw);
        }

        if (roleId != null) {
            sql.append(" AND u.RoleId = ?");
            params.add(roleId);
        }

        if (isActive != null) {
            sql.append(" AND u.IsActive = ?");
            params.add(isActive);
        }

        sql.append(" ORDER BY u.UserId");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserId"));
                u.setUsername(rs.getString("Username"));
                u.setFullName(rs.getString("FullName"));
                u.setPhone(rs.getString("Phone"));
                u.setEmail(rs.getString("Email"));
                u.setRoleId(rs.getInt("RoleId"));
                u.setRoleName(rs.getString("RoleName"));
                u.setIsActive(rs.getBoolean("IsActive"));
                u.setRoleActive(rs.getBoolean("RoleActive"));
                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean existsByUsername(String username) {

        String sql = "SELECT 1 FROM [User] WHERE Username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByPhone(String phone) {

        String sql = "SELECT 1 FROM [User] WHERE Phone = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByEmail(String email) {

        String sql = "SELECT 1 FROM [User] WHERE Email = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByUsernameExceptId(String username, int userId) {

        String sql = "SELECT 1 FROM [User] WHERE Username = ? AND UserId <> ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByPhoneExceptId(String phone, int userId) {

        String sql = "SELECT 1 FROM [User] WHERE Phone = ? AND UserId <> ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByEmailExceptId(String email, int userId) {

        String sql = "SELECT 1 FROM [User] WHERE Email = ? AND UserId <> ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getDoctorsBySpecialty(int specialtyId) {
        List<User> list = new ArrayList<>();

        String sql = "        SELECT DISTINCT u.UserId, u.FullName\n"
                + "        FROM [User] u\n"
                + "        JOIN Role r ON u.RoleId = r.RoleId\n"
                + "        JOIN DoctorSpecialty ds ON u.UserId = ds.UserId\n"
                + "        WHERE r.RoleName = 'Doctor'\n"
                + "          AND ds.SpecialtyId = ?\n"
                + "          AND u.IsActive = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, specialtyId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserId"));
                u.setFullName(rs.getString("FullName"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean isDoctorInSpecialty(int doctorId, int specialtyId) {
        String sql = "SELECT 1\n"
                + "        FROM DoctorSpecialty\n"
                + "        WHERE UserId = ? AND SpecialtyId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, specialtyId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User login(String username, String password) {
        // Join với bảng Role để lấy RoleName phục vụ cho AuthorizationFilter
        String sql = "SELECT u.*, r.RoleName "
                + "FROM [User] u "
                + "JOIN Role r ON u.RoleId = r.RoleId "
                + "WHERE u.Username = ? AND u.PasswordHash = ? AND u.IsActive = 1";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, password);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("UserId"));
                    u.setUsername(rs.getString("Username"));
                    u.setFullName(rs.getString("FullName"));
                    u.setEmail(rs.getString("Email"));
                    u.setRoleId(rs.getInt("RoleId"));
                    u.setRoleName(rs.getString("RoleName")); // Rất quan trọng!
                    u.setIsActive(rs.getBoolean("IsActive"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.out.println("Login DAO Error: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new UserDAO().login("duongnc", "123").toString());
    }

}
