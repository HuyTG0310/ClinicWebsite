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

    public void updateMedicalRecord(model.MedicalRecord mr, int appointmentId, String nextStatus) {
        String sqlMR = "UPDATE MedicalRecord SET Symptom = ?, PhysicalExam = ?, DoctorNotes = ?, "
                + "Diagnosis = ?, TreatmentPlan = ?, BloodPressure = ?, HeartRate = ?, "
                + "Temperature = ?, Weight = ?, FollowUpDate = ? "
                + "WHERE MedicalRecordId = ?";

        String sqlApp = "UPDATE Appointment SET Status = ? WHERE AppointmentId = ?";

        java.sql.Connection conn = null;
        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Update Bệnh án
            java.sql.PreparedStatement stMR = conn.prepareStatement(sqlMR);
            stMR.setString(1, mr.getSymptom());
            stMR.setString(2, mr.getPhysicalExam());
            stMR.setString(3, mr.getDoctorNotes());
            stMR.setString(4, mr.getDiagnosis());
            stMR.setString(5, mr.getTreatmentPlan());
            stMR.setString(6, mr.getBloodPressure());
            // Xử lý Integer/Double có thể null
            if (mr.getHeartRate() != null) {
                stMR.setInt(7, mr.getHeartRate());
            } else {
                stMR.setNull(7, java.sql.Types.INTEGER);
            }
            if (mr.getTemperature() != null) {
                stMR.setDouble(8, mr.getTemperature());
            } else {
                stMR.setNull(8, java.sql.Types.DOUBLE);
            }
            if (mr.getWeight() != null) {
                stMR.setDouble(9, mr.getWeight());
            } else {
                stMR.setNull(9, java.sql.Types.DOUBLE);
            }
            // Xử lý Ngày tái khám
            if (mr.getFollowUpDate() != null) {
                stMR.setDate(10, new java.sql.Date(mr.getFollowUpDate().getTime()));
            } else {
                stMR.setNull(10, java.sql.Types.DATE);
            }

            stMR.setInt(11, mr.getMedicalRecordId()); // UPDATE dựa trên ID truyền vào
            stMR.executeUpdate();

            // 2. Update Trạng thái Lịch hẹn
            java.sql.PreparedStatement stApp = conn.prepareStatement(sqlApp);
            stApp.setString(1, nextStatus);
            stApp.setInt(2, appointmentId);
            stApp.executeUpdate();

            conn.commit(); // Chốt Transaction
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public int saveMedicalRecord(MedicalRecord mr, int appointmentId, String nextStatus) {
        Connection conn = null;
        PreparedStatement stRecord = null;
        PreparedStatement stPres = null;
        PreparedStatement stApp = null;

        try {
            conn = new DBContext().conn; // Nhớ dùng getConnection()
            conn.setAutoCommit(false);

            // 1. LƯU BỆNH ÁN
            String sqlRecord = "INSERT INTO MedicalRecord "
                    + "(PatientId, ResponsibleDoctorId, Temperature, BloodPressure, HeartRate, "
                    + "Weight, Height, Symptom, PhysicalExam, Diagnosis, TreatmentPlan, "
                    + "DoctorNotes, FollowUpDate, FollowUpStatus, CompletedAt) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";

            stRecord = conn.prepareStatement(sqlRecord, Statement.RETURN_GENERATED_KEYS);
            stRecord.setInt(1, mr.getPatientId());
            stRecord.setInt(2, mr.getResponsibleDoctorId());
            if (mr.getTemperature() != null) {
                stRecord.setDouble(3, mr.getTemperature());
            } else {
                stRecord.setNull(3, java.sql.Types.DECIMAL);
            }
            stRecord.setString(4, mr.getBloodPressure());
            if (mr.getHeartRate() != null) {
                stRecord.setInt(5, mr.getHeartRate());
            } else {
                stRecord.setNull(5, java.sql.Types.INTEGER);
            }
            if (mr.getWeight() != null) {
                stRecord.setDouble(6, mr.getWeight());
            } else {
                stRecord.setNull(6, java.sql.Types.DECIMAL);
            }
            if (mr.getHeight() != null) {
                stRecord.setDouble(7, mr.getHeight());
            } else {
                stRecord.setNull(7, java.sql.Types.DECIMAL);
            }

            stRecord.setString(8, mr.getSymptom());
            stRecord.setString(9, mr.getPhysicalExam());
            stRecord.setString(10, mr.getDiagnosis());
            stRecord.setString(11, mr.getTreatmentPlan());
            stRecord.setString(12, mr.getDoctorNotes());
            stRecord.setDate(13, mr.getFollowUpDate());
            stRecord.setString(14, mr.getFollowUpStatus());

            if (stRecord.executeUpdate() == 0) {
                conn.rollback();
                return -1;
            }

            // 🔥 LẤY ID CỦA BỆNH ÁN VỪA LƯU
            int newMedicalRecordId = -1;
            try (ResultSet generatedKeys = stRecord.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newMedicalRecordId = generatedKeys.getInt(1);
                } else {
                    conn.rollback();
                    return -1;
                }
            }

            // 2. LƯU DANH SÁCH THUỐC (NẾU CÓ)
            if (mr.getPrescriptions() != null && !mr.getPrescriptions().isEmpty()) {
                String sqlPres = "INSERT INTO Prescription (MedicalRecordId, MedicineId, Quantity, Dosage, Note) VALUES (?, ?, ?, ?, ?)";
                stPres = conn.prepareStatement(sqlPres);
                for (model.Prescription p : mr.getPrescriptions()) {
                    stPres.setInt(1, newMedicalRecordId);
                    stPres.setInt(2, p.getMedicineId());
                    stPres.setInt(3, p.getQuantity());
                    stPres.setString(4, p.getDosage());
                    stPres.setString(5, p.getNote());
                    stPres.addBatch();
                }
                stPres.executeBatch();
            }

            // 3. CHỐT CA KHÁM VỚI TRẠNG THÁI ĐỘNG
            String sqlApp = "UPDATE Appointment SET Status = ?, MedicalRecordId = ? WHERE AppointmentId = ?";
            stApp = conn.prepareStatement(sqlApp);
            stApp.setString(1, nextStatus);
            stApp.setInt(2, newMedicalRecordId); // 🔥 BỔ SUNG DÒNG NÀY ĐỂ LIÊN KẾT
            stApp.setInt(3, appointmentId);

            if (stApp.executeUpdate() == 0) {
                conn.rollback();
                return -1;
            }

            conn.commit();
            return newMedicalRecordId; // Trả về ID thay vì true

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
            }
            return -1;
        } finally {
            try {
                if (stApp != null) {
                    stApp.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stPres != null) {
                    stPres.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stRecord != null) {
                    stRecord.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public model.MedicalRecord getRecordById(int medicalRecordId) {
        model.MedicalRecord mr = null;
        java.sql.Connection conn = null;

        String sql = "SELECT m.*, p.FullName AS PatientName, p.Gender, p.DateOfBirth, p.Address, p.Phone, u.FullName AS DoctorName "
                + "FROM MedicalRecord m "
                + "JOIN Patient p ON m.PatientId = p.PatientId "
                + "JOIN [User] u ON m.ResponsibleDoctorId = u.UserId "
                + "WHERE m.MedicalRecordId = ?";

        try {
            conn = new DBContext().conn;
            try (java.sql.PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, medicalRecordId);
                try (java.sql.ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        mr = new model.MedicalRecord();

                        // 1. Map thông tin Bệnh án & Bệnh nhân
                        mr.setMedicalRecordId(rs.getInt("MedicalRecordId"));
                        mr.setPatientId(rs.getInt("PatientId"));
                        mr.setResponsibleDoctorId(rs.getInt("ResponsibleDoctorId"));
                        mr.setSymptom(rs.getString("Symptom"));
                        mr.setPhysicalExam(rs.getString("PhysicalExam"));
                        mr.setDiagnosis(rs.getString("Diagnosis"));
                        mr.setTreatmentPlan(rs.getString("TreatmentPlan"));
                        mr.setDoctorNotes(rs.getString("DoctorNotes"));
                        mr.setBloodPressure(rs.getString("BloodPressure"));
                        mr.setHeartRate(rs.getObject("HeartRate") != null ? rs.getInt("HeartRate") : null);
                        mr.setTemperature(rs.getObject("Temperature") != null ? rs.getDouble("Temperature") : null);
                        mr.setWeight(rs.getObject("Weight") != null ? rs.getDouble("Weight") : null);
                        mr.setHeight(rs.getObject("Height") != null ? rs.getDouble("Height") : null);
                        mr.setCompletedAt(rs.getTimestamp("CompletedAt"));
                        mr.setFollowUpDate(rs.getDate("FollowUpDate"));
                        mr.setFollowUpStatus(rs.getString("FollowUpStatus"));

                        mr.setPatientName(rs.getString("PatientName"));
                        mr.setPatientGender(rs.getString("Gender"));
                        mr.setPatientDob(rs.getDate("DateOfBirth"));
                        mr.setPatientPhone(rs.getString("Phone"));
                        mr.setPatientAddress(rs.getString("Address"));
                        mr.setDoctorName(rs.getString("DoctorName"));
                    }
                }
            }

            // 2. Nếu tìm thấy Bệnh án -> Quét luôn Đơn thuốc đính kèm
            if (mr != null) {
                String sqlPres = "SELECT p.*, m.MedicineName, m.Unit "
                        + "FROM Prescription p "
                        + "JOIN Medicine m ON p.MedicineId = m.MedicineId "
                        + "WHERE p.MedicalRecordId = ?";
                try (java.sql.PreparedStatement stPres = conn.prepareStatement(sqlPres)) {
                    stPres.setInt(1, mr.getMedicalRecordId());
                    try (java.sql.ResultSet rsPres = stPres.executeQuery()) {
                        java.util.List<model.Prescription> presList = new java.util.ArrayList<>();
                        while (rsPres.next()) {
                            model.Prescription p = new model.Prescription();
                            p.setPrescriptionId(rsPres.getInt("PrescriptionId"));
                            p.setMedicineId(rsPres.getInt("MedicineId"));
                            p.setQuantity(rsPres.getInt("Quantity"));
                            p.setDosage(rsPres.getString("Dosage"));
                            p.setNote(rsPres.getString("Note"));
                            p.setMedicineName(rsPres.getString("MedicineName"));
                            p.setUnit(rsPres.getString("Unit"));
                            presList.add(p);
                        }
                        mr.setPrescriptions(presList);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
        return mr;
    }

    public boolean[] checkPermissionDetail(int medicalRecordId, int currentUserId, boolean isAdmin) {
        if (isAdmin) {
            return new boolean[]{true, true}; // Admin trùm cuối
        }
        boolean[] perms = new boolean[]{false, false};
        String sql = "SELECT "
                + "CASE WHEN mr.ResponsibleDoctorId = ? THEN 1 ELSE 0 END AS IsOwner, "
                + "CASE WHEN EXISTS (SELECT 1 FROM MedicalRecord mr_sub WHERE mr_sub.PatientId = mr.PatientId AND mr_sub.ResponsibleDoctorId = ?) THEN 1 ELSE 0 END AS IsPastCaregiver, "
                + "CASE WHEN EXISTS (SELECT 1 FROM DoctorSpecialty ds1 JOIN DoctorSpecialty ds2 ON ds1.SpecialtyId = ds2.SpecialtyId WHERE ds1.UserId = ? AND ds2.UserId = mr.ResponsibleDoctorId) THEN 1 ELSE 0 END AS IsSameSpecialty "
                + "FROM MedicalRecord mr WHERE mr.MedicalRecordId = ?";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, currentUserId);
            st.setInt(2, currentUserId);
            st.setInt(3, currentUserId);
            st.setInt(4, medicalRecordId);

            try (java.sql.ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    boolean isOwner = rs.getInt("IsOwner") == 1;
                    boolean isPastCaregiver = rs.getInt("IsPastCaregiver") == 1;
                    boolean isSameSpecialty = rs.getInt("IsSameSpecialty") == 1;

                    if (isOwner) {
                        perms[0] = true;
                        perms[1] = true; // canView = true, canEdit = true
                    } else if (isPastCaregiver || isSameSpecialty) {
                        perms[0] = true;
                        perms[1] = false; // canView = true, canEdit = false
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return perms;
    }

}
