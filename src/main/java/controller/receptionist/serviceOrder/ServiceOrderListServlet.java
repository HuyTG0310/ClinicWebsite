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


@WebServlet(name = "ServiceOrderListServlet", urlPatterns = {"/receptionist/service-order/list"})
public class ServiceOrderListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }


        String dateStr = request.getParameter("date");
        String paymentMethod = request.getParameter("paymentMethod");
        String status = request.getParameter("status");


        if (dateStr == null) {
            dateStr = LocalDate.now().toString();
        }


        ServiceOrderDAO dao = new ServiceOrderDAO();
        List<ServiceOrder> orderList = dao.getServiceOrders(dateStr, paymentMethod, status);


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


        request.setAttribute("orderList", orderList);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalCash", totalCash);
        request.setAttribute("totalBanking", totalBanking);


        request.setAttribute("paramDate", dateStr);

        request.setAttribute("pageTitle", "Transaction History");
        request.setAttribute("activePage", "manageServiceOrder");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/serviceorder/orderList.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/receptionistLayout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
