/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.api;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Tai Loi
 */
@WebServlet(name = "CheckPaymentStatusAPI", urlPatterns = {"/api/check-payment"})
public class CheckPaymentStatusAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (java.io.PrintWriter out = response.getWriter()) {

            // 1. 2 LOẠI ID TỪ JAVASCRIPT GỬI LÊN
            String mrIdRaw = request.getParameter("mrId");
            String soIdRaw = request.getParameter("soId");

            int mrId = (mrIdRaw != null && !mrIdRaw.isEmpty()) ? Integer.parseInt(mrIdRaw) : 0;
            int soId = (soIdRaw != null && !soIdRaw.isEmpty()) ? Integer.parseInt(soIdRaw) : 0;

            // 2. TẠO KEY ĐỊNH DANH DUY NHẤT
            // Nếu có Bệnh án -> Key là "BA68", Nếu Khám lẻ -> Key là "PK101"
            String alertKey = (mrId > 0) ? "BA" + mrId : "PK" + soId;

            // 3. KIỂM TRA CẢNH BÁO TỪ WEBHOOK
            Double alertAmount = SePayWebhookServlet.paymentAlerts.remove(alertKey);

            if (alertAmount != null) {
                if (alertAmount < 0) {
                    out.print("{\"status\": \"UNDERPAID\", \"received\": " + Math.abs(alertAmount) + "}");
                    return;
                } else if (alertAmount > 0) {
                    out.print("{\"status\": \"OVERPAID\", \"excess\": " + alertAmount + "}");
                    return;
                }
            }

            // 4. KIỂM TRA DATABASE
            dao.ServiceOrderDAO dao = new dao.ServiceOrderDAO();
            boolean isPaid = false;

            if (mrId > 0) {
                isPaid = dao.isOrderPaid(mrId);
            } else if (soId > 0) {
                // Check đúng 1 Phiếu khám ban đầu
                model.ServiceOrder order = dao.getServiceOrderById(soId);
                if (order != null && "PAID".equals(order.getStatus())) {
                    isPaid = true;
                }
            }

            // Trả kết quả về cho UI
            out.print("{\"status\": \"" + (isPaid ? "PAID" : "UNPAID") + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
