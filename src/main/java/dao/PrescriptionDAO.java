/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

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

        
        return list;
    }
}
