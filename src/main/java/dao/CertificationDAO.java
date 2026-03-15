package dao;

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
                + "(UserId,CertificateName,CertificateNumber,IssueDate,ExpiryDate,FilePath,Status) "
                + "VALUES (?,?,?,?,?,?, 'PENDING')";

        try {

            PreparedStatement ps = conn.prepareStatement(sql);

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

        String sql = "SELECT * FROM Certifications WHERE UserId=? ORDER BY CreatedAt DESC";

        try {

            PreparedStatement ps = conn.prepareStatement(sql);
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

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // GET ALL CERTIFICATIONS (FOR ADMIN)
    public List<Certification> getAll() {

        List<Certification> list = new ArrayList<>();

        String sql = "SELECT * FROM Certifications ORDER BY CreatedAt DESC";

        try {

            PreparedStatement ps = conn.prepareStatement(sql);

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
                + "SET Status='VERIFIED', "
                + "VerifiedBy=?, "
                + "VerifiedAt=GETDATE() "
                + "WHERE CertificationId=?";

        try {

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, adminId);
            ps.setInt(2, certificationId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // REJECT CERTIFICATION
    public void reject(int certificationId) {

        String sql = "UPDATE Certifications SET Status='REJECTED' WHERE CertificationId=?";

        try {

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, certificationId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
