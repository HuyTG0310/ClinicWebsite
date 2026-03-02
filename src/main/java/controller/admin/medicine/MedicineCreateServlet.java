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
@WebServlet(name = "MedicineCreateServlet", urlPatterns = {"/admin/medicine/create", "/doctor/medicine/create", "/receptionist/medicine/create"})
public class MedicineCreateServlet extends HttpServlet {

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

        request.setAttribute("basePath", basePath); // Gửi sang JSP để dùng
        request.setAttribute("pageTitle", "Add medicine");
        request.setAttribute("activePage", "manageMedicine");

        request.setAttribute("contentPage", "/WEB-INF/admin/medicine/medicineForm.jsp");

        request.getRequestDispatcher(layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

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

        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String ingredients = request.getParameter("ingredients");
        String usage = request.getParameter("usage");
        String contra = request.getParameter("contra");

        if (name == null || name.trim().isEmpty()
                || unit == null || unit.trim().isEmpty()
                || ingredients == null || ingredients.trim().isEmpty()
                || usage == null || usage.trim().isEmpty()
                || contra == null || contra.trim().isEmpty()) {

            // Tạo object Medicine để giữ lại dữ liệu user đã nhập
            Medicine m = new Medicine();
            m.setMedicineName(name);
            m.setUnit(unit);
            m.setIngredients(ingredients);
            m.setUsage(usage);
            m.setContraindication(contra);

            // Gửi lại dữ liệu + thông báo lỗi về JSP
            request.setAttribute("medicine", m);
            request.setAttribute("error", "All fields are required. Please fill in all information.");

            request.setAttribute("basePath", basePath);
            request.setAttribute("pageTitle", "Add medicine");
            request.setAttribute("activePage", "manageMedicine");
            request.setAttribute("contentPage", "/WEB-INF/admin/medicine/medicineForm.jsp");

            request.getRequestDispatcher(layout).forward(request, response);
            return;
        }

        Medicine m = new Medicine();
        m.setMedicineName(name);
        m.setUnit(unit);
        m.setIngredients(ingredients);
        m.setUsage(usage);
        m.setContraindication(contra);

        MedicineDAO dao = new MedicineDAO();
        dao.insert(m);

        response.sendRedirect(basePath + "/medicine/list");
    }
}
