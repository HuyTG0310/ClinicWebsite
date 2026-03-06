/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.receptionist.serviceOrder;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.*;
import java.util.*;
import model.*;

/**
 *
 * @author huytr
 */
@WebServlet(name = "ServiceOrderListServlet", urlPatterns = {"/receptionist/service-order/list", "/doctor/service-order/list", "/admin/service-order/list"})
public class ServiceOrderListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String layout;
        String basePath;

        if (uri.startsWith(ctx + "/admin")) {
            layout = "/WEB-INF/layout/adminLayout.jsp";
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            layout = "/WEB-INF/layout/doctorLayout.jsp";
            basePath = ctx + "/doctor";
        } else if (uri.startsWith(ctx + "/receptionist")) {
            layout = "/WEB-INF/layout/receptionistLayout.jsp";
            basePath = ctx + "/receptionist";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 1. Lấy tham số bộ lọc
        String dateStr = request.getParameter("date");
        String paymentMethod = request.getParameter("paymentMethod");
        String status = request.getParameter("status");

        // Mặc định luôn hiển thị doanh thu của NGÀY HÔM NAY
        if (dateStr == null) {
            dateStr = LocalDate.now().toString();
        }

        // 2. Lấy dữ liệu từ DAO
        ServiceOrderDAO dao = new ServiceOrderDAO();
        List<ServiceOrder> orderList = dao.getServiceOrders(dateStr, paymentMethod, status);

        // 3. Tính toán Thống kê nhanh (Summing up)
        double totalRevenue = 0;
        double totalCash = 0;
        double totalBanking = 0;

        for (ServiceOrder order : orderList) {
            if ("PAID".equals(order.getStatus())) {
                totalRevenue += order.getPriceAtTime();
                if ("CASH".equals(order.getPaymentMethod())) {
                    totalCash += order.getPriceAtTime();
                } else if ("BANKING".equals(order.getPaymentMethod())) {
                    totalBanking += order.getPriceAtTime();
                }
            }
        }

        // 4. Gửi dữ liệu ra JSP
        request.setAttribute("orderList", orderList);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalCash", totalCash);
        request.setAttribute("totalBanking", totalBanking);

        // Giữ lại tham số lọc để hiển thị trên form
        request.setAttribute("paramDate", dateStr);

        request.setAttribute("pageTitle", "Transaction History");
        request.setAttribute("activePage", "manageServiceOrder");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/serviceorder/orderList.jsp");

        request.getRequestDispatcher(layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
