package dao;

import java.sql.*;
import java.util.*;
import model.*;
import util.DBContext;

public class ServiceOrderDAO extends DBContext {

    public List<model.ServiceOrder> getServiceOrders(String dateStr, String paymentMethod, String status) {
        List<model.ServiceOrder> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();

        // Dùng kỹ thuật CTE (WITH) để tạo 3 khối dữ liệu (Sổ cái kế toán)
        sql.append("WITH BaseData AS ( ");

        // =========================================================================
        // KHỐI 1: CÁC HÓA ĐƠN CHƯA THU TIỀN (UNPAID hoặc Hủy trước khi thu)
        // =========================================================================
        sql.append("  SELECT so.MedicalRecordId, MAX(so.ServiceOrderId) AS ServiceOrderId, so.PatientId, p.FullName AS PatientName, ");
        sql.append("         so.Status AS DBStatus, so.PaymentMethod, so.PaidAt, MAX(so.OrderTime) AS OrderTime, ");
        sql.append("         MAX(u.FullName) AS CashierName, COUNT(so.ServiceOrderId) AS TotalServices, SUM(so.PriceAtTime) AS TotalAmount ");
        sql.append("  FROM ServiceOrder so ");
        sql.append("  JOIN Patient p ON so.PatientId = p.PatientId LEFT JOIN [User] u ON so.CashierId = u.UserId ");
        sql.append("  WHERE so.PaidAt IS NULL ");
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            sql.append(" AND CAST(so.OrderTime AS DATE) = ? ");
        }
        if (paymentMethod != null && !paymentMethod.trim().isEmpty()) {
            sql.append(" AND so.PaymentMethod = ? ");
        }
        sql.append("  GROUP BY so.MedicalRecordId, so.PatientId, p.FullName, so.Status, so.PaymentMethod, so.PaidAt, CASE WHEN so.MedicalRecordId IS NULL THEN so.ServiceOrderId ELSE 0 END ");

        sql.append("  UNION ALL ");

        // =========================================================================
        // KHỐI 2: BIÊN LAI GỐC ĐÃ THU TIỀN (Gộp tất cả PAID và CANCELLED để ra 160k)
        // =========================================================================
        sql.append("  SELECT so.MedicalRecordId, MAX(so.ServiceOrderId) AS ServiceOrderId, so.PatientId, p.FullName AS PatientName, ");
        sql.append("         'PAID' AS DBStatus, so.PaymentMethod, so.PaidAt, MAX(so.OrderTime) AS OrderTime, ");
        sql.append("         MAX(u.FullName) AS CashierName, COUNT(so.ServiceOrderId) AS TotalServices, SUM(so.PriceAtTime) AS TotalAmount ");
        sql.append("  FROM ServiceOrder so ");
        sql.append("  JOIN Patient p ON so.PatientId = p.PatientId LEFT JOIN [User] u ON so.CashierId = u.UserId ");
        sql.append("  WHERE so.PaidAt IS NOT NULL ");
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            sql.append(" AND CAST(so.PaidAt AS DATE) = ? ");
        }
        if (paymentMethod != null && !paymentMethod.trim().isEmpty()) {
            sql.append(" AND so.PaymentMethod = ? ");
        }
        sql.append("  GROUP BY so.MedicalRecordId, so.PatientId, p.FullName, so.PaymentMethod, so.PaidAt, CASE WHEN so.MedicalRecordId IS NULL THEN so.ServiceOrderId ELSE 0 END ");

        sql.append("  UNION ALL ");

        // =========================================================================
        // KHỐI 3: PHIẾU HOÀN TIỀN (Chỉ bóc tách phần 60k bị CANCELLED ra thành 1 dòng)
        // =========================================================================
        sql.append("  SELECT so.MedicalRecordId, MAX(so.ServiceOrderId) AS ServiceOrderId, so.PatientId, p.FullName AS PatientName, ");
        sql.append("         'REFUNDED' AS DBStatus, so.PaymentMethod, so.PaidAt, MAX(so.OrderTime) AS OrderTime, ");
        sql.append("         MAX(u.FullName) AS CashierName, COUNT(so.ServiceOrderId) AS TotalServices, SUM(so.PriceAtTime) AS TotalAmount ");
        sql.append("  FROM ServiceOrder so ");
        sql.append("  JOIN Patient p ON so.PatientId = p.PatientId LEFT JOIN [User] u ON so.CashierId = u.UserId ");
        sql.append("  WHERE so.PaidAt IS NOT NULL AND so.Status = 'CANCELLED' ");
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            sql.append(" AND CAST(so.PaidAt AS DATE) = ? ");
        }
        if (paymentMethod != null && !paymentMethod.trim().isEmpty()) {
            sql.append(" AND so.PaymentMethod = ? ");
        }
        sql.append("  GROUP BY so.MedicalRecordId, so.PatientId, p.FullName, so.PaymentMethod, so.PaidAt, CASE WHEN so.MedicalRecordId IS NULL THEN so.ServiceOrderId ELSE 0 END ");

        sql.append(") ");

        // =========================================================================
        // TỔNG HỢP VÀ LỌC THEO STATUS Ở BÊN NGOÀI CÙNG
        // =========================================================================
        sql.append("SELECT * FROM BaseData WHERE 1=1 ");

        if (status != null && !status.trim().isEmpty() && !status.equals("ALL")) {
            sql.append("AND DBStatus = ? ");
        }

        sql.append("ORDER BY OrderTime DESC, DBStatus DESC"); // Xếp PAID trước CANCELLED nếu trùng giờ

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql.toString())) {

            int index = 1;

            // Truyền parameter cho 3 khối UNION (Lặp 3 lần cực kỳ gọn gàng)
            for (int i = 0; i < 3; i++) {
                if (dateStr != null && !dateStr.trim().isEmpty()) {
                    st.setString(index++, dateStr);
                }
                if (paymentMethod != null && !paymentMethod.trim().isEmpty()) {
                    st.setString(index++, paymentMethod);
                }
            }

            // Truyền parameter cho bộ lọc Status ngoài cùng
            if (status != null && !status.trim().isEmpty() && !status.equals("ALL")) {
                st.setString(index++, status);
            }

            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    model.ServiceOrder order = new model.ServiceOrder();

                    int mrId = rs.getInt("MedicalRecordId");
                    int soId = rs.getInt("ServiceOrderId");

                    order.setMedicalRecordId(mrId); // Nếu null thì tự động lấy số 0
                    order.setServiceOrderId(soId);

                    if (mrId > 0) {
                        int count = rs.getInt("TotalServices");
                        order.setServiceName("Gói Cận Lâm Sàng (" + count + " chỉ định)");
                    } else {
                        order.setServiceName(new ServiceDAO().getById(1).getServiceName());
                    }

                    String dbStatus = rs.getString("DBStatus");
                    String finalStatus = (status != null && !status.trim().isEmpty() && !status.equals("ALL"))
                            ? status : dbStatus;

                    order.setPriceAtTime(rs.getDouble("TotalAmount"));
                    order.setStatus(finalStatus != null ? finalStatus.trim() : "");
                    order.setPaidAt(rs.getTimestamp("PaidAt"));
                    order.setPaymentMethod(rs.getString("PaymentMethod"));
                    order.setPatientName(rs.getString("PatientName"));
                    order.setCashierName(rs.getString("CashierName"));
                    order.setPatientId(rs.getInt("PatientId"));

                    list.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //hàm lấy chi tiết danh sách dịch vụ theo bệnh án và trạng thái
    public java.util.List<java.util.Map<String, Object>> getServiceDetailsByMrId(int medicalRecordId, int patientId, String status, String formattedTime) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();

        // Lấy thêm so.Status AS CurrentStatus để biết món nào bị hủy
        StringBuilder sql = new StringBuilder("SELECT s.ServiceName, so.PriceAtTime, "
                + "so.PaymentMethod, so.PaidAt, u.FullName AS CashierName, so.Status AS CurrentStatus "
                + "FROM ServiceOrder so "
                + "JOIN Service s ON so.ServiceId = s.ServiceId "
                + "LEFT JOIN [User] u ON so.CashierId = u.UserId "
                + "WHERE 1=1 ");

        if (medicalRecordId > 0) {
            sql.append(" AND so.MedicalRecordId = ? ");
        } else {
            sql.append(" AND so.MedicalRecordId IS NULL AND so.PatientId = ? ");
        }

        // ====================================================================
        // 🔥 PHÂN NHÁNH NGHIỆP VỤ KẾ TOÁN (ĐỒNG BỘ 100% VỚI DANH SÁCH)
        // ====================================================================
        if ("UNPAID".equals(status)) {
            // 1. CHỜ THU TIỀN
            sql.append(" AND so.Status = 'UNPAID' AND so.PaidAt IS NULL ");

        } else if ("PAID".equals(status) && formattedTime != null && !formattedTime.isEmpty()) {
            // 2. BIÊN LAI GỐC (Lấy cả món sống và món chết để giữ nguyên tổng tiền 160k)
            sql.append(" AND (so.Status = 'PAID' OR so.Status = 'CANCELLED') ");
            sql.append(" AND CONVERT(VARCHAR(19), so.PaidAt, 120) = ? ");

        } else if ("REFUNDED".equals(status)) {
            // 3. CHI TIẾT HOÀN TIỀN (Đã đóng tiền nhưng bị Lab/Bác sĩ hủy)
            sql.append(" AND so.Status = 'CANCELLED' AND so.PaidAt IS NOT NULL ");
            if (formattedTime != null && !formattedTime.isEmpty()) {
                sql.append(" AND CONVERT(VARCHAR(19), so.PaidAt, 120) = ? ");
            }

        } else if ("CANCELLED".equals(status)) {
            // 4. CHI TIẾT HỦY TRẮNG (Bị hủy khi chưa đóng 1 đồng nào)
            sql.append(" AND so.Status = 'CANCELLED' AND so.PaidAt IS NULL ");
        }

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (medicalRecordId > 0) {
                st.setInt(paramIndex++, medicalRecordId);
            } else {
                st.setInt(paramIndex++, patientId);
            }

            // Chỉ nạp biến thời gian nếu là nhóm có thu tiền (PAID hoặc REFUNDED)
            if (("PAID".equals(status) || "REFUNDED".equals(status)) && formattedTime != null && !formattedTime.isEmpty()) {
                st.setString(paramIndex++, formattedTime);
            }

            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> item = new java.util.HashMap<>();
                    item.put("serviceName", rs.getString("ServiceName"));
                    item.put("price", rs.getDouble("PriceAtTime"));
                    item.put("paymentMethod", rs.getString("PaymentMethod"));
                    item.put("paidAt", rs.getTimestamp("PaidAt"));
                    item.put("cashierName", rs.getString("CashierName") != null ? rs.getString("CashierName") : "Hệ thống");

                    // Bỏ thêm trạng thái hiện tại vào túi xách để mang ra UI
                    item.put("currentStatus", rs.getString("CurrentStatus"));

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
