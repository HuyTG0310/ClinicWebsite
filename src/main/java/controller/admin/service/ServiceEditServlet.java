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
import java.net.URLEncoder;
import model.*;

/**
 *
 * @author huytr
 */
@WebServlet(name = "ServiceEditServlet", urlPatterns = {"/admin/service/edit"})
public class ServiceEditServlet extends HttpServlet {

    private ServiceDAO serviceDAO = new ServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idRaw = request.getParameter("serviceId");
        String name = request.getParameter("serviceName");
        String category = request.getParameter("category");
        String priceRaw = request.getParameter("price");
        String activeRaw = request.getParameter("isActive");

        int id;

        try {
            id = Integer.parseInt(idRaw);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/service/list");
            return;
        }

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

        // ❌ nếu lỗi → quay lại detail + báo lỗi
        if (error != null) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/service/detail?id=" + id
                    + "&editError=" + URLEncoder.encode(error, "UTF-8")
            );
            return;
        }

        Service s = new Service();
        s.setServiceId(id);
        s.setServiceName(name.trim());
        s.setCategory(category);
        s.setCurrentPrice(price);
        s.setIsActive(activeRaw != null);

        serviceDAO.update(s);

        response.sendRedirect(request.getContextPath() + "/admin/service/detail?id=" + id);
    }

}
