
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
}
