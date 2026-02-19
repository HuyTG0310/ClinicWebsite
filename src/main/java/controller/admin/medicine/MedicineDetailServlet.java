/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.medicine;

import dao.MedicineDAO;
import model.Medicine;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 *
 * @author TRUONGTHINHNGUYEN
 */
@WebServlet(name = "MedicineDetailServlet", urlPatterns = {"/admin/medicine/detail", "/doctor/medicine/detail", "/receptionist/medicine/detail"})
public class MedicineDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ===== VALIDATE ID =====
        String idRaw = request.getParameter("id");
        int id;

        try {
            id = Integer.parseInt(idRaw);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // ===== LOAD DATA =====
        MedicineDAO dao = new MedicineDAO();
        Medicine medicine = dao.getById(id);

        if (medicine == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("medicine", medicine);
        request.setAttribute("pageTitle", "Medicine detail");
        request.setAttribute("activePage", "manageMedicine");

        // ===== CHỌN LAYOUT THEO URL =====
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        String layout;
        String basePath; // <--- Biến cần bổ sung

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

        // Gửi basePath sang JSP để dùng cho nút Back/Edit
        request.setAttribute("basePath", basePath);

        // ===== 5. JSP DÙNG CHUNG =====
        request.setAttribute("contentPage", "/WEB-INF/admin/medicine/medicineDetail.jsp");
        request.getRequestDispatcher(layout).forward(request, response);
    }
}
