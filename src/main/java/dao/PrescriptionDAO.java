/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.DBContext;

/**
 *
 * @author TRUONGTHINHNGUYEN
 */
public class PrescriptionDAO extends DBContext {

    // =========================================================================
    // 🔥 KHUNG SQL GỐC (Tránh lặp code)
    // =========================================================================
    private final String BASE_PR_SQL = "SELECT "
            + "mr.MedicalRecordId, p.PatientId, p.FullName AS PatientName, p.Gender, "
            + "(YEAR(GETDATE()) - YEAR(p.DateOfBirth)) AS Age, "
            + "u.FullName AS DoctorName, mr.Diagnosis, mr.CompletedAt, "
            + "COUNT(pr.PrescriptionId) AS TotalMedicines, "
            + "CASE WHEN mr.ResponsibleDoctorId = ? THEN 1 ELSE 0 END AS IsOwner, "
            + "CASE WHEN EXISTS (SELECT 1 FROM MedicalRecord mr_sub WHERE mr_sub.PatientId = mr.PatientId AND mr_sub.ResponsibleDoctorId = ?) THEN 1 ELSE 0 END AS IsPastCaregiver, "
            + "CASE WHEN EXISTS ("
            + "    SELECT 1 FROM DoctorSpecialty ds1 "
            + "    JOIN DoctorSpecialty ds2 ON ds1.SpecialtyId = ds2.SpecialtyId "
            + "    WHERE ds1.UserId = ? AND ds2.UserId = mr.ResponsibleDoctorId"
            + ") THEN 1 ELSE 0 END AS IsSameSpecialty "
            + "FROM MedicalRecord mr "
            + "JOIN Patient p ON mr.PatientId = p.PatientId "
            + "JOIN [User] u ON mr.ResponsibleDoctorId = u.UserId "
            + "JOIN Prescription pr ON mr.MedicalRecordId = pr.MedicalRecordId "
            + "WHERE 1=1 ";

    private final String GROUP_BY_SQL = " GROUP BY mr.MedicalRecordId, mr.PatientId, p.PatientId, p.FullName, p.Gender, p.DateOfBirth, u.FullName, mr.Diagnosis, mr.CompletedAt, mr.ResponsibleDoctorId ";

    // =========================================================================
    // 1. HÀM LẤY TẤT CẢ (GET ALL)
    // =========================================================================
    public java.util.List<java.util.Map<String, Object>> getAllPrescriptions(int currentUserId) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String sql = BASE_PR_SQL + GROUP_BY_SQL + "ORDER BY mr.MedicalRecordId DESC";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, currentUserId);
            st.setInt(2, currentUserId);
            st.setInt(3, currentUserId);

            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPrescription(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean isExistPrescription(int mrId) {
        String sql = "SELECT * FROM Prescription WHERE MedicalRecordId = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, mrId);
            
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private java.util.Map<String, Object> mapResultSetToPrescription(java.sql.ResultSet rs) throws Exception {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("medicalRecordId", rs.getInt("MedicalRecordId"));
        map.put("patientName", rs.getString("PatientName"));
        map.put("gender", rs.getString("Gender"));
        map.put("age", rs.getInt("Age"));
        map.put("doctorName", rs.getString("DoctorName"));
        map.put("diagnosis", rs.getString("Diagnosis"));
        map.put("completedAt", rs.getTimestamp("CompletedAt"));
        map.put("totalMedicines", rs.getInt("TotalMedicines"));

        // Rút 3 cờ phân quyền
        map.put("isOwner", rs.getInt("IsOwner") == 1);
        map.put("isPastCaregiver", rs.getInt("IsPastCaregiver") == 1);
        map.put("isSameSpecialty", rs.getInt("IsSameSpecialty") == 1);
        return map;
    }

    // 1. Lấy thông tin chung của Bệnh án & Bệnh nhân
    public java.util.Map<String, Object> getMedicalRecordDetail(int medicalRecordId) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        String sql = "SELECT mr.MedicalRecordId, p.FullName AS PatientName, p.Gender, "
                + "(YEAR(GETDATE()) - YEAR(p.DateOfBirth)) AS Age, "
                + "mr.Weight, mr.Height, mr.Diagnosis, mr.Symptom, "
                + "u.FullName AS DoctorName, mr.CompletedAt "
                + "FROM MedicalRecord mr "
                + "JOIN Patient p ON mr.PatientId = p.PatientId "
                + "JOIN [User] u ON mr.ResponsibleDoctorId = u.UserId "
                + "WHERE mr.MedicalRecordId = ?";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, medicalRecordId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    map.put("medicalRecordId", rs.getInt("MedicalRecordId"));
                    map.put("patientName", rs.getString("PatientName"));
                    map.put("gender", rs.getString("Gender"));
                    map.put("age", rs.getInt("Age"));
                    map.put("weight", rs.getObject("Weight")); // Dùng getObject đề phòng NULL
                    map.put("height", rs.getObject("Height"));
                    map.put("diagnosis", rs.getString("Diagnosis"));
                    map.put("symptom", rs.getString("Symptom"));
                    map.put("doctorName", rs.getString("DoctorName"));
                    map.put("completedAt", rs.getTimestamp("CompletedAt"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public java.util.List<java.util.Map<String, Object>> searchPrescriptions(String keyword, String dateStr, int currentUserId) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_PR_SQL);

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasDate = dateStr != null && !dateStr.trim().isEmpty();

        // Thêm điều kiện lọc Từ khóa
        if (hasKeyword) {
            sql.append("AND (p.FullName LIKE ? OR CAST(mr.MedicalRecordId AS VARCHAR) = ?) ");
        }

        // Thêm điều kiện lọc Ngày
        if (hasDate) {
            sql.append("AND CAST(mr.CompletedAt AS DATE) = ? ");
        }

        // Ráp Group By và Order By vào cuối
        sql.append(GROUP_BY_SQL);
        sql.append("ORDER BY mr.MedicalRecordId DESC");

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql.toString())) {

            // Truyền User ID cho 3 logic phân quyền
            st.setInt(1, currentUserId);
            st.setInt(2, currentUserId);
            st.setInt(3, currentUserId);

            int paramIndex = 4;
            if (hasKeyword) {
                st.setString(paramIndex++, "%" + keyword.trim() + "%");
                st.setString(paramIndex++, keyword.trim());
            }
            if (hasDate) {
                st.setString(paramIndex++, dateStr.trim());
            }

            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPrescription(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public java.util.List<java.util.Map<String, Object>> getPrescribedMedicines(int medicalRecordId) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String sql = "SELECT pr.PrescriptionId, pr.MedicineId, m.MedicineName, m.Unit, "
                + "pr.Quantity, pr.Dosage, pr.Note, m.[Usage], m.Ingredients, m.Contraindication "
                + "FROM Prescription pr "
                + "JOIN Medicine m ON pr.MedicineId = m.MedicineId "
                + "WHERE pr.MedicalRecordId = ?";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, medicalRecordId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("prescriptionId", rs.getInt("PrescriptionId"));
                    map.put("medicineId", rs.getInt("MedicineId"));
                    map.put("medicineName", rs.getString("MedicineName"));
                    map.put("unit", rs.getString("Unit"));
                    map.put("quantity", rs.getInt("Quantity"));
                    map.put("dosage", rs.getString("Dosage"));
                    map.put("note", rs.getString("Note"));
                    map.put("usage", rs.getString("Usage")); // Cách dùng mặc định của thuốc
                    map.put("ingredients", rs.getString("Ingredients"));
                    map.put("contraindication", rs.getString("Contraindication"));
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    

    public boolean savePrescription(int medicalRecordId, String[] medicineIds, String[] quantities, String[] dosages, String[] notes) {
        java.sql.Connection conn = null;
        String sqlDelete = "DELETE FROM Prescription WHERE MedicalRecordId = ?";
        String sqlInsert = "INSERT INTO Prescription (MedicalRecordId, MedicineId, Quantity, Dosage, Note) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false); // Bắt đầu Transaction để an toàn dữ liệu

            // 1. Xóa các dòng thuốc cũ của hồ sơ này
            try (java.sql.PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
                psDel.setInt(1, medicalRecordId);
                psDel.executeUpdate();
            }

            // 2. Chèn danh sách thuốc mới vào
            if (medicineIds != null && medicineIds.length > 0) {
                try (java.sql.PreparedStatement psIns = conn.prepareStatement(sqlInsert)) {
                    for (int i = 0; i < medicineIds.length; i++) {
                        if (medicineIds[i] == null || medicineIds[i].isEmpty()) {
                            continue;
                        }

                        psIns.setInt(1, medicalRecordId);
                        psIns.setInt(2, Integer.parseInt(medicineIds[i]));
                        psIns.setInt(3, Integer.parseInt(quantities[i]));
                        psIns.setString(4, dosages[i]);
                        psIns.setString(5, notes[i]);
                        psIns.addBatch(); // Dùng Batch để thực thi nhanh
                    }
                    psIns.executeBatch();
                }
            }

            conn.commit(); // Chốt đơn!
            return true;
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (java.sql.SQLException ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (java.sql.SQLException e) {
            }
        }
    }
}
