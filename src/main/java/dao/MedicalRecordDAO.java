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
public class MedicalRecordDAO extends DBContext {

    private final String BASE_MR_SQL = "SELECT "
            + "mr.MedicalRecordId, p.PatientId, p.FullName AS PatientName, p.Gender, "
            + "(YEAR(GETDATE()) - YEAR(p.DateOfBirth)) AS Age, "
            + "u.FullName AS DoctorName, mr.Symptom, mr.Diagnosis, mr.CompletedAt, "
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
            + "JOIN Appointment a ON a.MedicalRecordId = mr.MedicalRecordId "
            + "WHERE a.Status = 'COMPLETED' ";

    // =========================================================================
    // 1. HÀM LẤY TẤT CẢ (GET ALL)
    // =========================================================================
    public java.util.List<java.util.Map<String, Object>> getAllMedicalRecords(int currentUserId) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String sql = BASE_MR_SQL + "ORDER BY mr.MedicalRecordId DESC";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, currentUserId);
            st.setInt(2, currentUserId);
            st.setInt(3, currentUserId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRecord(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================================================================
    // 2. HÀM TÌM KIẾM (SEARCH THEO TỪ KHÓA & NGÀY)
    // =========================================================================
    public java.util.List<java.util.Map<String, Object>> searchMedicalRecords(String keyword, String dateStr, int currentUserId) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_MR_SQL);

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasDate = dateStr != null && !dateStr.trim().isEmpty();

        // Nối thêm điều kiện Tìm kiếm theo Từ khóa (Tên / ID)
        if (hasKeyword) {
            sql.append("AND (p.FullName LIKE ? OR CAST(mr.MedicalRecordId AS VARCHAR) = ?) ");
        }
        // Nối thêm điều kiện Tìm kiếm theo Ngày (Ép kiểu CompletedAt về DATE để so sánh)
        if (hasDate) {
            sql.append("AND CAST(mr.CompletedAt AS DATE) = ? ");
        }

        sql.append("ORDER BY mr.MedicalRecordId DESC");

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql.toString())) {
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
                    list.add(mapResultSetToRecord(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private java.util.Map<String, Object> mapResultSetToRecord(java.sql.ResultSet rs) throws Exception {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("medicalRecordId", rs.getInt("MedicalRecordId"));
        map.put("patientName", rs.getString("PatientName"));
        map.put("gender", rs.getString("Gender"));
        map.put("age", rs.getInt("Age"));
        map.put("doctorName", rs.getString("DoctorName"));
        map.put("symptom", rs.getString("Symptom"));
        map.put("diagnosis", rs.getString("Diagnosis"));
        map.put("completedAt", rs.getTimestamp("CompletedAt"));
        map.put("isOwner", rs.getInt("IsOwner") == 1);
        map.put("isPastCaregiver", rs.getInt("IsPastCaregiver") == 1);
        map.put("isSameSpecialty", rs.getInt("IsSameSpecialty") == 1);
        return map;
    }
}
