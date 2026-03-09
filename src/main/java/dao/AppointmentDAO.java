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

        // Câu SQL lấy toàn bộ thông tin chi tiết
        String sql = "SELECT a.AppointmentId, a.PatientId, a.AppointmentTime, a.Status, a.MedicalRecordId, \n"
                + "       p.FullName AS PatientName, p.Phone AS PatientPhone, p.Gender AS PatientGender, p.DateOfBirth AS PatientDob, p.Address AS PatientAddress, \n"
                + "       r.RoomName, s.SpecialtyName, -- 🔥 Thêm s.SpecialtyName\n"
                + "       uDoc.FullName AS DoctorName, \n"
                + "       uRec.FullName AS ReceptionistName, \n"
                + "       so.PriceAtTime, so.PaymentMethod, \n"
                + "       srv.ServiceName "
                + "FROM Appointment a \n"
                + "JOIN Patient p ON a.PatientId = p.PatientId \n"
                + "JOIN Room r ON a.RoomId = r.RoomId \n"
                + "JOIN Specialty s ON r.SpecialtyId = s.SpecialtyId -- 🔥 Thêm JOIN bảng Specialty\n"
                + "LEFT JOIN [User] uDoc ON r.CurrentDoctorId = uDoc.UserId \n"
                + "LEFT JOIN [User] uRec ON a.CreatedBy = uRec.UserId \n"
                + "LEFT JOIN ServiceOrder so ON so.AppointmentId = a.AppointmentId \n"
                + "LEFT JOIN Service srv ON so.ServiceId = srv.ServiceId \n"
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

                // Patient info
                app.setPatientId(rs.getInt("PatientId"));
                app.setPatientName(rs.getString("PatientName"));
                app.setPatientPhone(rs.getString("PatientPhone"));
                app.setPatientGender(rs.getString("PatientGender"));
                app.setPatientDob(rs.getDate("PatientDob"));
                // Nếu bạn có PatientAddress thì set thêm, không thì bỏ qua

                app.setSpecialtyName(rs.getString("SpecialtyName"));
                app.setPatientAddress(rs.getString("PatientAddress"));

                // Room & Staff
                app.setRoomName(rs.getString("RoomName"));
                app.setDoctorName(rs.getString("DoctorName") != null ? rs.getString("DoctorName") : "N/A");
                app.setReceptionistName(rs.getString("ReceptionistName"));

                // Billing info (Từ ServiceOrder)
                app.setPrice(rs.getDouble("PriceAtTime"));
                app.setPaymentMethod(rs.getString("PaymentMethod") != null ? rs.getString("PaymentMethod") : "CASH");
                app.setServiceName(rs.getString("ServiceName"));
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
        int generatedServiceOrderId = -1; // Biến lưu ID hóa đơn mới

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false); // BẮT ĐẦU TRANSACTION

            // ==========================================
            // BƯỚC 1: TẠO LỊCH HẸN (APPOINTMENT) VÀ LẤY ID CỦA NÓ
            // ==========================================
            String sqlApp = "INSERT INTO Appointment (PatientId, RoomId, CreatedBy, Status) VALUES (?, ?, ?, 'WAITING')";

            // 🔥 Yêu cầu SQL trả về ID của Lịch khám
            stApp = conn.prepareStatement(sqlApp, PreparedStatement.RETURN_GENERATED_KEYS);
            stApp.setInt(1, patientId);
            stApp.setInt(2, roomId);
            stApp.setInt(3, receptionistId);

            if (stApp.executeUpdate() == 0) {
                conn.rollback();
                return -1;
            }

            // Lấy ID Lịch khám vừa tạo
            int generatedAppId = -1;
            rsApp = stApp.getGeneratedKeys();
            if (rsApp.next()) {
                generatedAppId = rsApp.getInt(1);
            }

            // ==========================================
            // BƯỚC 2: TẠO SERVICE ORDER (GẮN CÁI AppointmentId VÀO)
            // ==========================================
            int clinicalExamServiceId = 1; // ID dịch vụ khám lâm sàng (Check lại DB của bạn)

            double examPrice = new ServiceDAO().getById(1).getCurrentPrice().doubleValue();     // Giá tiền khám

            // Xác định Status và PaidAt dựa trên PaymentMethod
            String status = "CASH".equals(paymentMethod) ? "PAID" : "UNPAID";
            String paidAtFragment = "CASH".equals(paymentMethod) ? "GETDATE()" : "NULL";

            // Có tổng cộng 8 dấu chấm hỏi (?)
            String sqlOrder = "INSERT INTO ServiceOrder "
                    + "(PatientId, AppointmentId, MedicalRecordId, ServiceId, AssignedById, CashierId, PriceAtTime, Status, PaidAt, PaymentMethod) "
                    + "VALUES (?, ?, NULL, ?, ?, ?, ?, ?, " + paidAtFragment + ", ?)";

            stOrder = conn.prepareStatement(sqlOrder, PreparedStatement.RETURN_GENERATED_KEYS);

            // Gán giá trị cẩn thận theo ĐÚNG THỨ TỰ các dấu ?
            stOrder.setInt(1, patientId);             // ?: PatientId
            stOrder.setInt(2, generatedAppId);        // ?: AppointmentId (Móc nối quan trọng nhất)
            stOrder.setInt(3, clinicalExamServiceId); // ?: ServiceId
            stOrder.setInt(4, receptionistId);        // ?: AssignedById (Lễ tân chỉ định)

            // ?: CashierId (Người thu tiền: Nếu CASH thì là Lễ tân, BANKING thì để NULL)
            if ("CASH".equals(paymentMethod)) {
                stOrder.setInt(5, receptionistId);
            } else {
                stOrder.setNull(5, java.sql.Types.INTEGER);
            }

            stOrder.setDouble(6, examPrice);          // ?: PriceAtTime
            stOrder.setString(7, status);             // ?: Status
            stOrder.setString(8, paymentMethod);      // ?: PaymentMethod

            if (stOrder.executeUpdate() == 0) {
                conn.rollback();
                return -1;
            }

            // Lấy ID của ServiceOrder vừa tạo ra
            rsOrder = stOrder.getGeneratedKeys();
            if (rsOrder.next()) {
                generatedServiceOrderId = rsOrder.getInt(1);
            }

            // THÀNH CÔNG -> CHỐT ĐƠN
            conn.commit();
            return generatedServiceOrderId; // Trả về ID Hóa đơn

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
            // Đóng kết nối...
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

    public boolean updateAppointmentRoom(int appointmentId, int newRoomId) {
        String sql = "UPDATE Appointment SET RoomId = ? WHERE AppointmentId = ? AND Status = 'WAITING'";
        try (Connection conn = new DBContext().conn; PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, newRoomId);
            st.setInt(2, appointmentId);

            return st.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cancelAppointment(int appointmentId) {
        Connection conn = null;
        PreparedStatement stApp = null;
        PreparedStatement stOrder = null;

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false);

            String sqlApp = "UPDATE Appointment SET Status = 'CANCELLED' WHERE AppointmentId = ? AND Status = 'WAITING'";
            stApp = conn.prepareStatement(sqlApp);
            stApp.setInt(1, appointmentId);

            if (stApp.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }

            String sqlOrder = "UPDATE ServiceOrder SET Status = 'CANCELLED' WHERE AppointmentId = ?";

            stOrder = conn.prepareStatement(sqlOrder);
            stOrder.setInt(1, appointmentId);

            stOrder.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
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
