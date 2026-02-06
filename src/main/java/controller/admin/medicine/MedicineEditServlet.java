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
import java.net.URLEncoder;

/**
 *
 * @author TRUONGTHINHNGUYEN
 */
@WebServlet(name = "MedicineEditServlet", urlPatterns = {"/admin/medicine/edit"})
public class MedicineEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

//            // giữ lại dữ liệu user đã nhập
//            Medicine m = new Medicine();
//            m.setMedicineId(Integer.parseInt(request.getParameter("id")));
//            m.setMedicineName(name);
//            m.setUnit(unit);
//            m.setIngredients(ingredients);
//            m.setUsage(usage);
//            m.setContraindication(contra);
//            m.setIsActive("1".equals(request.getParameter("isActive")));
//
//            request.setAttribute("medicine", m);
//            request.setAttribute("editError", "All fields are required. Please fill in all information.");
//
//            request.setAttribute("pageTitle", "Medicine Detail");
//            request.setAttribute("activePage", "manageMedicine");
//            request.setAttribute("contentPage", "/WEB-INF/admin/medicine/medicineDetail.jsp");
//            request.setAttribute("showEditPopup", true);
//            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
//            return;
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/medicine/detail?id=" + request.getParameter("id")
                    + "&editError=" + URLEncoder.encode("All fields are required. Please fill in all information.", "UTF-8")
            );
            return;
        }

        // Lấy dữ liệu form
        Medicine m = new Medicine();
        m.setMedicineId(Integer.parseInt(request.getParameter("id")));
        m.setMedicineName(request.getParameter("name"));
        m.setUnit(request.getParameter("unit"));
        m.setIngredients(request.getParameter("ingredients"));
        m.setUsage(request.getParameter("usage"));
        m.setContraindication(request.getParameter("contra"));
        String isActiveStr = request.getParameter("isActive");
        m.setIsActive(isActiveStr != null);

        // Update DB
        MedicineDAO dao = new MedicineDAO();
        dao.update(m);

        // Quay về view detail
        response.sendRedirect(
                request.getContextPath()
                + "/admin/medicine/detail?id=" + m.getMedicineId()
        );

    }

}
