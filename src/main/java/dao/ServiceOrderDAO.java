package dao;

import java.sql.*;
import java.util.*;
import model.*;
import util.DBContext;

public class ServiceOrderDAO extends DBContext {

    public List<ServiceOrder> getServiceOrders(String dateStr, String paymentMethod, String status) {
        List<ServiceOrder> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT "
                + "so.MedicalRecordId, "
                + "MAX(so.ServiceOrderId) AS ServiceOrderId, "
                + "so.PatientId, "
                + "p.FullName AS PatientName, "
                + "so.Status, "
                + "so.PaymentMethod, "
                + "so.PaidAt, "
                + "MAX(so.OrderTime) AS OrderTime, "
                + "MAX(u.FullName) AS CashierName, "
                + "COUNT(so.ServiceOrderId) AS TotalServices, "
                + "SUM(so.PriceAtTime) AS TotalAmount "
                + "FROM ServiceOrder so "
                + "JOIN Patient p ON so.PatientId = p.PatientId "
                + "LEFT JOIN [User] u ON so.CashierId = u.UserId "
                + "WHERE 1=1 "
        );

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND so.Status = ? ");
        }

        if (dateStr != null && !dateStr.trim().isEmpty()) {
            sql.append("AND (CAST(so.PaidAt AS DATE) = ? OR CAST(so.OrderTime AS DATE) = ?) ");
        }

        if (paymentMethod != null && !paymentMethod.trim().isEmpty()) {
            sql.append("AND so.PaymentMethod = ? ");
        }

        sql.append("GROUP BY so.MedicalRecordId, so.PatientId, p.FullName, so.Status, so.PaymentMethod, so.PaidAt, ");
        sql.append("CASE WHEN so.MedicalRecordId IS NULL THEN so.ServiceOrderId ELSE 0 END ");

        sql.append("ORDER BY MAX(so.OrderTime) DESC");

        try (Connection conn = new DBContext().conn; PreparedStatement st = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (status != null && !status.trim().isEmpty()) {
                st.setString(index++, status);
            }
            if (dateStr != null && !dateStr.trim().isEmpty()) {
                st.setString(index++, dateStr);
                st.setString(index++, dateStr);
            }
            if (paymentMethod != null && !paymentMethod.trim().isEmpty()) {
                st.setString(index++, paymentMethod);
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ServiceOrder order = new ServiceOrder();

                int mrId = rs.getInt("MedicalRecordId");
                int soId = rs.getInt("ServiceOrderId");
                order.setMedicalRecordId(mrId);
                order.setServiceOrderId(soId);

                if (mrId > 0) {

                    int count = rs.getInt("TotalServices");
                    order.setServiceName("Gói Cận Lâm Sàng (" + count + " chỉ định)");
                } else {
                    // Nếu là phí khám ban đầu
                    order.setServiceName("Khám bệnh lâm sàng");
                }

                order.setPriceAtTime(rs.getDouble("TotalAmount"));
                order.setStatus(rs.getString("Status"));
                order.setPaidAt(rs.getTimestamp("PaidAt"));
                order.setPaymentMethod(rs.getString("PaymentMethod"));
                order.setPatientName(rs.getString("PatientName"));
                order.setCashierName(rs.getString("CashierName"));
                order.setPatientId(rs.getInt("PatientId"));
                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //hàm lấy chi tiết danh sách dịch vụ theo bệnh án và trạng thái
    public java.util.List<java.util.Map<String, Object>> getServiceDetailsByMrId(int medicalRecordId, int patientId, String status, String formattedTime) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT s.ServiceName, so.PriceAtTime, "
                + "so.PaymentMethod, so.PaidAt, u.FullName AS CashierName "
                + "FROM ServiceOrder so "
                + "JOIN Service s ON so.ServiceId = s.ServiceId "
                + "LEFT JOIN [User] u ON so.CashierId = u.UserId " // Join để lấy tên người thu tiền
                + "WHERE so.Status = ?");

        if (medicalRecordId > 0) {
            sql.append(" AND so.MedicalRecordId = ? ");
        } else {
            // Nếu chưa có Bệnh án -> Tìm hóa đơn của Bệnh nhân này nhưng chưa gắn Bệnh án
            sql.append(" AND so.MedicalRecordId IS NULL AND so.PatientId = ? ");
        }

        // So sánh chính xác thời điểm thu tiền
        if ("PAID".equals(status) && formattedTime != null && !formattedTime.isEmpty()) {
            sql.append(" AND CONVERT(VARCHAR(19), so.PaidAt, 120) = ? ");
        } else {
            sql.append(" AND so.PaidAt IS NULL ");
        }

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1; // Quản lý số thứ tự của dấu ?

            st.setString(paramIndex++, status); // Gắn trạng thái vào đây

            if (medicalRecordId > 0) {
                st.setInt(paramIndex++, medicalRecordId);
            } else {
                st.setInt(paramIndex++, patientId);
            }

            if ("PAID".equals(status) && formattedTime != null && !formattedTime.isEmpty()) {
                st.setString(paramIndex++, formattedTime);
            }

            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> item = new java.util.HashMap<>();
                    item.put("serviceName", rs.getString("ServiceName"));
                    item.put("price", rs.getDouble("PriceAtTime"));

                    // 🔥 HỨNG THÊM DỮ LIỆU ĐỂ IN BIÊN LAI
                    item.put("paymentMethod", rs.getString("PaymentMethod"));
                    item.put("paidAt", rs.getTimestamp("PaidAt"));
                    item.put("cashierName", rs.getString("CashierName") != null ? rs.getString("CashierName") : "Hệ thống");

                    list.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ServiceOrder getServiceOrderById(int soId) {
        // JOIN với bảng Patient để lấy Tên bệnh nhân hiển thị lên UI
        String sql = "SELECT so.*, p.FullName AS PatientName "
                + "FROM ServiceOrder so "
                + "JOIN Patient p ON so.PatientId = p.PatientId "
                + "WHERE so.ServiceOrderId = ?";

        try (Connection conn = new DBContext().conn; PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, soId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    model.ServiceOrder order = new model.ServiceOrder();

                    order.setServiceOrderId(rs.getInt("ServiceOrderId"));
                    order.setPatientId(rs.getInt("PatientId"));

                    // Xử lý MedicalRecordId có thể NULL
                    if (rs.getObject("MedicalRecordId") != null) {
                        order.setMedicalRecordId(rs.getInt("MedicalRecordId"));
                    }

                    order.setServiceId(rs.getInt("ServiceId"));
                    order.setPriceAtTime(rs.getDouble("PriceAtTime"));
                    order.setStatus(rs.getString("Status"));
                    order.setPaymentMethod(rs.getString("PaymentMethod"));
                    order.setPaidAt(rs.getTimestamp("PaidAt"));
                    order.setAppointmentId(rs.getInt("AppointmentId"));
                    order.setPatientName(rs.getString("PatientName"));

                    return order;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //hàm xác nhận thu tiền cho toàn bộ dịch vụ của 1 ca khám
    public boolean checkoutServiceOrders(int medicalRecordId, int cashierId, String paymentMethod) {

        // 1. Cập nhật tất cả các record có cùng MedicalRecordId và đang UNPAID thành PAID
        String sqlService = "UPDATE ServiceOrder "
                + "SET Status = 'PAID', CashierId = ?, PaymentMethod = ?, PaidAt = GETDATE() "
                + "WHERE MedicalRecordId = ? AND Status = 'UNPAID'";

        // 2. Cập nhật Lô xét nghiệm từ CREATED -> IN_PROGRESS để báo cho phòng Lab biết đã thu tiền
        String sqlBatch = "UPDATE LabTestBatch "
                + "SET Status = 'IN_PROGRESS' "
                + "WHERE MedicalRecordId = ? AND Status = 'CREATED'";

        java.sql.Connection conn = null;
        java.sql.PreparedStatement stService = null;
        java.sql.PreparedStatement stBatch = null;

        try {
            // Lưu ý: Nếu DBContext của bạn dùng getConnection() thì đổi lại nhé
            conn = new DBContext().conn;
            conn.setAutoCommit(false); // Bật Transaction: Đảm bảo Kế toán và Lab đồng bộ 100%

            // Bước 1: Thu tiền (Cập nhật bảng ServiceOrder)
            stService = conn.prepareStatement(sqlService);
            stService.setInt(1, cashierId);
            stService.setString(2, paymentMethod);
            stService.setInt(3, medicalRecordId);

            int rowsAffected = stService.executeUpdate();

            // Bước 2: Bật cờ cho phòng Lab (Nếu có thu tiền thành công)
            if (rowsAffected > 0) {
                stBatch = conn.prepareStatement(sqlBatch);
                stBatch.setInt(1, medicalRecordId);
                stBatch.executeUpdate();
            }

            conn.commit(); // Chốt giao dịch! Lưu cả 2 bảng.
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("Lỗi thu tiền: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // Nếu có lỗi thì quay xe, không lưu gì cả
                }
            } catch (Exception ex) {
            }
        } finally {
            // Đóng tài nguyên sạch sẽ
            try {
                if (stService != null) {
                    stService.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stBatch != null) {
                    stBatch.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    //hàm dùng cho khách hàng khi trả tiền khám bằng QR mà đổi ý thành tiền mặt
    public boolean checkoutSingleServiceOrder(int soId, int cashierId, String paymentMethod) {
        // Chỉ Update khi nó đang UNPAID để tránh bug chốt 2 lần
        String sql = "UPDATE ServiceOrder "
                + "SET Status = 'PAID', CashierId = ?, PaidAt = GETDATE(), PaymentMethod = ? "
                + "WHERE ServiceOrderId = ? AND Status = 'UNPAID'";

        try (Connection conn = new DBContext().conn; PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cashierId);
            ps.setString(2, paymentMethod); // Cập nhật lại thành CASH
            ps.setInt(3, soId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //hàm check xem bệnh án này đã được thu tiền xong chưa
    public boolean isOrderPaid(int medicalRecordId) {
        String sql = "SELECT COUNT(*) AS UnpaidCount FROM ServiceOrder WHERE MedicalRecordId = ? AND Status = 'UNPAID'";
        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, medicalRecordId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    // Nếu số lượng UNPAID = 0, tức là đã PAID toàn bộ!
                    return rs.getInt("UnpaidCount") == 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getUnpaidTotalAmount(int medicalRecordId) {
        String sql = "SELECT SUM(PriceAtTime) AS Total FROM ServiceOrder WHERE MedicalRecordId = ? AND Status = 'UNPAID'";
        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, medicalRecordId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Không nợ đồng nào
    }
}
