/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.service;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import model.*;

/**
 *
 * @author huytr
 */
@WebServlet(name = "ServiceAddServlet", urlPatterns = {"/admin/service/add"})
public class ServiceAddServlet extends HttpServlet {

    ServiceDAO serviceDAO = new ServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categories", ServiceCategory.getAll());
        request.setAttribute("pageTitle", "Add service");
        request.setAttribute("activePage", "manageService");
        request.setAttribute("contentPage", "/WEB-INF/admin/service/serviceAdd.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

//        request.getRequestDispatcher("/WEB-INF/admin/service/serviceAdd.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("serviceName");
        String category = request.getParameter("category");
        String priceRaw = request.getParameter("price");

        String error = null;
        BigDecimal price = null;

        if (name == null || name.trim().isEmpty()) {
            error = "Tên dịch vụ không được để trống";
        } else {
            try {
                price = new BigDecimal(priceRaw);
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    error = "Giá phải lớn hơn 0";
                }
            } catch (Exception e) {
                error = "Giá không hợp lệ";
            }
        }

        if (!ServiceCategory.getAll().contains(category)) {
            error = "Nhóm dịch vụ không hợp lệ";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("serviceName", name);
            request.setAttribute("category", category);
            request.setAttribute("price", priceRaw);

            request.setAttribute("categories", ServiceCategory.getAll());
            request.setAttribute("pageTitle", "Add service");
            request.setAttribute("activePage", "manageService");
            request.setAttribute("contentPage", "/WEB-INF/admin/service/serviceAdd.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

//            request.getRequestDispatcher("/WEB-INF/admin/service/serviceAdd.jsp").forward(request, response);
            return;
        }

        Service s = new Service();
        s.setServiceName(name.trim());
        s.setCategory(category);
        s.setCurrentPrice(price);

        boolean success = serviceDAO.insert(s);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/service/list");
        } else {
            request.setAttribute("error", "Thêm dịch vụ thất bại");

            request.setAttribute("categories", ServiceCategory.getAll());
            request.setAttribute("pageTitle", "Add service");
            request.setAttribute("activePage", "manageService");
            request.setAttribute("contentPage", "/WEB-INF/admin/service/serviceAdd.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
        }
    }

}
