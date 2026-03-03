/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.*;
import model.*;
import util.DBContext;

public class AppointmentDAO extends DBContext {

    public List<Appointment> getAppointmentList(String keyword, String dateStr, String status) {
        List<Appointment> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.AppointmentId, a.AppointmentTime, a.Status, ");
        sql.append("       p.FullName AS PatientName, p.Phone AS PatientPhone, ");
        sql.append("       r.RoomName, ");
        sql.append("       u.FullName AS DoctorName, ");
        sql.append("       so.Status AS PaymentStatus, ");
        sql.append("       so.PaymentMethod ");
        sql.append("FROM Appointment a ");
        sql.append("JOIN Patient p ON a.PatientId = p.PatientId ");
        sql.append("JOIN Room r ON a.RoomId = r.RoomId ");
        sql.append("LEFT JOIN [User] u ON r.CurrentDoctorId = u.UserId "); // Có thể phòng chưa có BS
        sql.append("LEFT JOIN ServiceOrder so ON a.AppointmentId = so.AppointmentId ");
        sql.append("WHERE 1=1 ");

        // 1. Lọc theo Keyword (Tên hoặc SĐT)
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (p.FullName LIKE ? OR p.Phone LIKE ?) ");
        }

        // 2. Lọc theo Ngày (Mặc định là hôm nay nếu null)
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            sql.append("AND CAST(a.AppointmentTime AS DATE) = ? ");
        }

        // 3. Lọc theo Trạng thái
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND a.Status = ? ");
        }

        // Sắp xếp: Mới nhất lên đầu
        sql.append("ORDER BY a.AppointmentTime DESC");

        try (Connection conn = new DBContext().conn; PreparedStatement st = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                st.setString(index++, "%" + keyword + "%");
                st.setString(index++, "%" + keyword + "%");
            }

            if (dateStr != null && !dateStr.trim().isEmpty()) {
                st.setString(index++, dateStr); // Format yyyy-MM-dd từ input type="date"
            }

            if (status != null && !status.trim().isEmpty()) {
                st.setString(index++, status);
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Appointment app = new Appointment();
                app.setAppointmentId(rs.getInt("AppointmentId"));
                app.setAppointmentTime(rs.getTimestamp("AppointmentTime"));
                app.setStatus(rs.getString("Status"));

                app.setPatientName(rs.getString("PatientName"));
                app.setPatientPhone(rs.getString("PatientPhone"));
                app.setRoomName(rs.getString("RoomName"));
                app.setDoctorName(rs.getString("DoctorName"));
                app.setPaymentMethod(rs.getString("PaymentMethod"));
                app.setPaymentStatus(rs.getString("PaymentStatus"));

                if (app.getDoctorName() == null) {
                    app.setDoctorName("N/A");
                }

                list.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Appointment getAppointmentDetailById(int appointmentId) {
        Appointment app = null;

        String sql = "SELECT a.AppointmentId, a.PatientId, a.AppointmentTime, a.Status, a.MedicalRecordId, \n"
                + "       p.FullName AS PatientName, p.Phone AS PatientPhone, p.Gender AS PatientGender, p.DateOfBirth AS PatientDob, p.Address AS PatientAddress, \n"
                + "       r.RoomName, s.SpecialtyName, -- 🔥 Thêm s.SpecialtyName\n"
                + "       uDoc.FullName AS DoctorName, \n"
                + "       uRec.FullName AS ReceptionistName, \n"
                + "       so.PriceAtTime, so.PaymentMethod \n"
                + "FROM Appointment a \n"
                + "JOIN Patient p ON a.PatientId = p.PatientId \n"
                + "JOIN Room r ON a.RoomId = r.RoomId \n"
                + "JOIN Specialty s ON r.SpecialtyId = s.SpecialtyId -- 🔥 Thêm JOIN bảng Specialty\n"
                + "LEFT JOIN [User] uDoc ON r.CurrentDoctorId = uDoc.UserId \n"
                + "LEFT JOIN [User] uRec ON a.CreatedBy = uRec.UserId \n"
                + "LEFT JOIN ServiceOrder so ON so.AppointmentId = a.AppointmentId \n"
                + "WHERE a.AppointmentId = ?";

        try (Connection conn = new DBContext().conn; PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, appointmentId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                app = new Appointment();
                app.setAppointmentId(rs.getInt("AppointmentId"));
                app.setAppointmentTime(rs.getTimestamp("AppointmentTime"));
                app.setStatus(rs.getString("Status"));
                app.setMedicalRecordId(rs.getInt("MedicalRecordId"));

                app.setPatientId(rs.getInt("PatientId"));
                app.setPatientName(rs.getString("PatientName"));
                app.setPatientPhone(rs.getString("PatientPhone"));
                app.setPatientGender(rs.getString("PatientGender"));
                app.setPatientDob(rs.getDate("PatientDob"));

                app.setSpecialtyName(rs.getString("SpecialtyName"));
                app.setPatientAddress(rs.getString("PatientAddress"));

                app.setRoomName(rs.getString("RoomName"));
                app.setDoctorName(rs.getString("DoctorName") != null ? rs.getString("DoctorName") : "N/A");
                app.setReceptionistName(rs.getString("ReceptionistName"));

                app.setPrice(rs.getDouble("PriceAtTime"));
                app.setPaymentMethod(rs.getString("PaymentMethod") != null ? rs.getString("PaymentMethod") : "CASH");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return app;
    }

    public int createAppointment(int patientId, int roomId, int receptionistId, String paymentMethod) {
        Connection conn = null;
        PreparedStatement stApp = null;
        PreparedStatement stOrder = null;
        ResultSet rsApp = null;
        ResultSet rsOrder = null;
        int generatedServiceOrderId = -1;

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false);
            String sqlApp = "INSERT INTO Appointment (PatientId, RoomId, CreatedBy, Status) VALUES (?, ?, ?, 'WAITING')";

            stApp = conn.prepareStatement(sqlApp, PreparedStatement.RETURN_GENERATED_KEYS);
            stApp.setInt(1, patientId);
            stApp.setInt(2, roomId);
            stApp.setInt(3, receptionistId);

            if (stApp.executeUpdate() == 0) {
                conn.rollback();
                return -1;
            }

            int generatedAppId = -1;
            rsApp = stApp.getGeneratedKeys();
            if (rsApp.next()) {
                generatedAppId = rsApp.getInt(1);
            }

            int clinicalExamServiceId = 1;
            double examPrice = 200000;

            String status = "CASH".equals(paymentMethod) ? "PAID" : "UNPAID";
            String paidAtFragment = "CASH".equals(paymentMethod) ? "GETDATE()" : "NULL";

            String sqlOrder = "INSERT INTO ServiceOrder "
                    + "(PatientId, AppointmentId, MedicalRecordId, ServiceId, AssignedById, CashierId, PriceAtTime, Status, PaidAt, PaymentMethod) "
                    + "VALUES (?, ?, NULL, ?, ?, ?, ?, ?, " + paidAtFragment + ", ?)";

            stOrder = conn.prepareStatement(sqlOrder, PreparedStatement.RETURN_GENERATED_KEYS);

            stOrder.setInt(1, patientId);
            stOrder.setInt(2, generatedAppId);
            stOrder.setInt(3, clinicalExamServiceId);
            stOrder.setInt(4, receptionistId);

            if ("CASH".equals(paymentMethod)) {
                stOrder.setInt(5, receptionistId);
            } else {
                stOrder.setNull(5, java.sql.Types.INTEGER);
            }

            stOrder.setDouble(6, examPrice);
            stOrder.setString(7, status);
            stOrder.setString(8, paymentMethod);

            if (stOrder.executeUpdate() == 0) {
                conn.rollback();
                return -1;
            }

            rsOrder = stOrder.getGeneratedKeys();
            if (rsOrder.next()) {
                generatedServiceOrderId = rsOrder.getInt(1);
            }

            conn.commit();
            return generatedServiceOrderId;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1;
        } finally {
            try {
                if (rsApp != null) {
                    rsApp.close();
                }
            } catch (Exception e) {
            }
            try {
                if (rsOrder != null) {
                    rsOrder.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stOrder != null) {
                    stOrder.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stApp != null) {
                    stApp.close();
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

}
