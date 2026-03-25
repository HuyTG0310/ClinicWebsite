package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Certification;
import util.DBContext;

public class CertificationDAO extends DBContext {

    // INSERT CERTIFICATION
    public void insertCertification(Certification c) {

        String sql = "INSERT INTO Certifications "
                + "(UserId, CertificateName, CertificateNumber, IssueDate, ExpiryDate, FilePath, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'PENDING')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getUserId());
            ps.setString(2, c.getCertificateName());
            ps.setString(3, c.getCertificateNumber());
            ps.setDate(4, c.getIssueDate());
            ps.setDate(5, c.getExpiryDate());
            ps.setString(6, c.getFilePath());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET CERTIFICATIONS BY USER
    public List<Certification> getByUser(int userId) {

        List<Certification> list = new ArrayList<>();

        String sql = "SELECT * FROM Certifications "
                + "WHERE UserId = ? "
                + "ORDER BY CreatedAt DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Certification c = new Certification();

                c.setCertificationId(rs.getInt("CertificationId"));
                c.setUserId(rs.getInt("UserId"));
                c.setCertificateName(rs.getString("CertificateName"));
                c.setCertificateNumber(rs.getString("CertificateNumber"));
                c.setIssueDate(rs.getDate("IssueDate"));
                c.setExpiryDate(rs.getDate("ExpiryDate"));
                c.setFilePath(rs.getString("FilePath"));
                c.setStatus(rs.getString("Status"));
                c.setRejectionNote(rs.getString("RejectionNote"));

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Certification getById(int id) {

        String sql = "SELECT * FROM Certifications WHERE CertificationId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Certification c = new Certification();

                c.setCertificationId(rs.getInt("CertificationId"));
                c.setUserId(rs.getInt("UserId"));
                c.setCertificateName(rs.getString("CertificateName"));
                c.setCertificateNumber(rs.getString("CertificateNumber"));
                c.setIssueDate(rs.getDate("IssueDate"));
                c.setExpiryDate(rs.getDate("ExpiryDate"));
                c.setFilePath(rs.getString("FilePath"));
                c.setStatus(rs.getString("Status"));

                return c;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // GET ALL CERTIFICATIONS (ADMIN)
    public List<Certification> getAll() {
        List<Certification> list = new ArrayList<>();

        String sql = "SELECT c.*, u.FullName, u.Phone, r.RoleName "
                + "FROM Certifications c "
                + "LEFT JOIN [User] u ON c.UserId = u.UserId "
                + "LEFT JOIN [Role] r ON u.RoleId = r.RoleId "
                + "ORDER BY c.CreatedAt DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Certification c = new Certification();
                c.setCertificationId(rs.getInt("CertificationId"));
                c.setUserId(rs.getInt("UserId"));
                c.setCertificateName(rs.getString("CertificateName"));
                c.setCertificateNumber(rs.getString("CertificateNumber"));
                c.setIssueDate(rs.getDate("IssueDate"));
                c.setExpiryDate(rs.getDate("ExpiryDate"));
                c.setFilePath(rs.getString("FilePath"));
                c.setStatus(rs.getString("Status"));
                c.setRejectionNote(rs.getString("RejectionNote"));
                c.setFullName(rs.getString("FullName"));
                c.setPhoneNumber(rs.getString("Phone"));
                c.setRoleName(rs.getString("RoleName"));

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Certification> searchCertifications(String name, String phone) {
        List<Certification> list = new ArrayList<>();
        String sql = "SELECT c.*, u.FullName, u.Phone, r.RoleName "
                + "FROM [Certifications] c "
                + "LEFT JOIN [User] u ON c.UserId = u.UserId "
                + "LEFT JOIN [Role] r ON u.RoleId = r.RoleId "
                + "WHERE (ISNULL(u.FullName, '') LIKE ? "
                + "   OR ISNULL(c.CertificateName, '') LIKE ? "
                + "   OR ISNULL(c.CertificateNumber, '') LIKE ?) ";

        if (phone != null && !phone.trim().isEmpty()) {
            sql += "AND ISNULL(u.Phone, '') LIKE ? ";
        }
        sql += " ORDER BY c.CreatedAt DESC";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String p = "%" + name + "%";
            ps.setString(1, p);
            ps.setString(2, p);
            ps.setString(3, p);
            if (phone != null && !phone.trim().isEmpty()) {
                ps.setString(4, "%" + phone + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Certification c = new Certification();
                c.setCertificationId(rs.getInt("CertificationId"));
                c.setUserId(rs.getInt("UserId"));
                c.setCertificateName(rs.getString("CertificateName"));
                c.setCertificateNumber(rs.getString("CertificateNumber"));
                c.setIssueDate(rs.getDate("IssueDate"));
                c.setExpiryDate(rs.getDate("ExpiryDate"));
                c.setFilePath(rs.getString("FilePath"));
                c.setStatus(rs.getString("Status"));
                c.setRejectionNote(rs.getString("RejectionNote"));
                c.setFullName(rs.getString("FullName"));
                c.setPhoneNumber(rs.getString("Phone")); // Lấy từ cột Phone
                c.setRoleName(rs.getString("RoleName"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // APPROVE CERTIFICATION
    public void approve(int certificationId, int adminId) {

        String sql = "UPDATE Certifications "
                + "SET Status = 'VERIFIED', "
                + "VerifiedBy = ?, "
                + "VerifiedAt = GETDATE() "
                + "WHERE CertificationId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ps.setInt(2, certificationId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // REJECT CERTIFICATION
    public void reject(int id, String reason) throws Exception {
        String sql = "UPDATE Certifications SET Status = 'REJECTED', RejectionNote = ? WHERE CertificationId = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reason);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new Exception("Không tìm thấy chứng chỉ với ID: " + id);
            }
            System.out.println("Reject thành công ID: " + id);

        } catch (Exception e) {
            e.printStackTrace();
            throw e; 
        }
    }

    // CHECK DUPLICATE CERTIFICATE NUMBER
    public boolean isCertificateNumberExist(String number) {

        String sql = "SELECT 1 FROM Certifications WHERE CertificateNumber = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, number);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateCertification(Certification c) {
        String sql = "UPDATE Certifications SET "
                + "CertificateName = ?, "
                + "CertificateNumber = ?, "
                + "IssueDate = ?, "
                + "ExpiryDate = ?, "
                + "FilePath = ? "
                + "WHERE CertificationId = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, c.getCertificateName());
            ps.setString(2, c.getCertificateNumber());
            ps.setDate(3, c.getIssueDate());
            ps.setDate(4, c.getExpiryDate());
            ps.setString(5, c.getFilePath());
            ps.setInt(6, c.getCertificationId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteCertification(int id) {
        String sql = "DELETE FROM Certifications WHERE CertificationId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
