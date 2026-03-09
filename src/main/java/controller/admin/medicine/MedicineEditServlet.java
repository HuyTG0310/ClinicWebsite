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
@WebServlet(name = "MedicineEditServlet", urlPatterns = {"/admin/medicine/edit", "/doctor/medicine/edit", "/receptionist/medicine/edit"})
public class MedicineEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        request.setAttribute("basePath", basePath);

        // 1. Lấy dữ liệu
        String idRaw = request.getParameter("id"); // ID thường để trong hidden field
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String ingredients = request.getParameter("ingredients");
        String usage = request.getParameter("usage");
        String contra = request.getParameter("contra");
        String isActiveStr = request.getParameter("isActive");

        // 2. Validate
        if (name == null || name.trim().isEmpty()
                || unit == null || unit.trim().isEmpty()
                || ingredients == null || ingredients.trim().isEmpty()
                || usage == null || usage.trim().isEmpty()
                || contra == null || contra.trim().isEmpty()) {

            // Lỗi -> Forward lại form edit kèm thông báo
            Medicine m = new Medicine();
            m.setMedicineId(Integer.parseInt(idRaw));
            m.setMedicineName(name);
            m.setUnit(unit);
            m.setIngredients(ingredients);
            m.setUsage(usage);
            m.setContraindication(contra);
            m.setIsActive(isActiveStr != null); // Giữ trạng thái active

            request.setAttribute("medicine", m);
            request.setAttribute("error", "All fields are required!");

            request.setAttribute("pageTitle", "Edit Medicine");
            request.setAttribute("contentPage", "/WEB-INF/admin/medicine/medicineForm.jsp");
            request.getRequestDispatcher(layout).forward(request, response);
            return;
        }

        // 3. Update DB
        Medicine m = new Medicine();
        m.setMedicineId(Integer.parseInt(idRaw));
        m.setMedicineName(name);
        m.setUnit(unit);
        m.setIngredients(ingredients);
        m.setUsage(usage);
        m.setContraindication(contra);

        // Xử lý checkbox isActive (Nếu null là ko tick -> false)
        m.setIsActive(isActiveStr != null);
        MedicineDAO dao = new MedicineDAO();
        dao.update(m);
        response.sendRedirect(basePath + "/medicine/detail?id=" + m.getMedicineId() + "&msg=updateSuccess");
    }
}
