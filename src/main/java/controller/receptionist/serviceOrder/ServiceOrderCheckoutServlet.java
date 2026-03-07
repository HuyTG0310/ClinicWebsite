/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.receptionist.serviceOrder;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet(name = "ServiceOrderCheckoutServlet", urlPatterns = {"/receptionist/service-order/checkout", "/doctor/service-order/checkout", "/admin/service-order/checkout"})
public class ServiceOrderCheckoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String basePath;

        if (uri.startsWith(ctx + "/admin")) {
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            basePath = ctx + "/doctor";
        } else if (uri.startsWith(ctx + "/receptionist")) {
            basePath = ctx + "/receptionist";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            HttpSession session = request.getSession(false);
            User cashier = (User) session.getAttribute("user");

            //2 ID từ Form gửi lên
            String mrIdRaw = request.getParameter("medicalRecordId");
            String soIdRaw = request.getParameter("serviceOrderId");
            String paymentMethod = request.getParameter("paymentMethod");

            int mrId = (mrIdRaw != null && !mrIdRaw.isEmpty()) ? Integer.parseInt(mrIdRaw) : 0;
            int soId = (soIdRaw != null && !soIdRaw.isEmpty()) ? Integer.parseInt(soIdRaw) : 0;

            dao.ServiceOrderDAO dao = new dao.ServiceOrderDAO();
            boolean success = false;

            if (mrId > 0) {
                //xét nghiệm
                success = dao.checkoutServiceOrders(mrId, cashier.getUserId(), paymentMethod);
            } else if (soId > 0) {
                //1 phiếu khám ban đầu
                success = dao.checkoutSingleServiceOrder(soId, cashier.getUserId(), paymentMethod);
            }

            if (success) {
                session.setAttribute("success", "Đã thu tiền thành công!");
            } else {
                session.setAttribute("error", "Lỗi: Không thể thu tiền ca này.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ!");
        }

        response.sendRedirect(basePath + "/service-order/list");
    }

}
