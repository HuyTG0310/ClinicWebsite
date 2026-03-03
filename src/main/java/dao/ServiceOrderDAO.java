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

}
