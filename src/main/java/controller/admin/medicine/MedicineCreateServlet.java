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
@WebServlet(name = "MedicineCreateServlet", urlPatterns = {"/admin/medicine/create"})
public class MedicineCreateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("pageTitle", "Add medicine");
        request.setAttribute("activePage", "manageMedicine");
        request.setAttribute("contentPage", "/WEB-INF/admin/medicine/medicineForm.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
//        // Mở form thêm mới
//        request.getRequestDispatcher("/WEB-INF/admin/medicine/medicineForm.jsp")
//                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* 
           1. LẤY DỮ LIỆU TỪ FORM
      */
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String ingredients = request.getParameter("ingredients");
        String usage = request.getParameter("usage");
        String contra = request.getParameter("contra");

        /*
           2. VALIDATE RỖNG (PHẦN MỚI THÊM)
           */
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
            request.setAttribute("error",
                    "All fields are required. Please fill in all information.");

            // Forward lại form create (KHÔNG insert DB)
            request.setAttribute("pageTitle", "Add medicine");
            request.setAttribute("activePage", "manageMedicine");
            request.setAttribute("contentPage", "/WEB-INF/admin/medicine/medicineForm.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
            return; // ⚠️ BẮT BUỘC: dừng xử lý tại đây
        }

        /* 
           3. DỮ LIỆU HỢP LỆ → INSERT DB
          */
        Medicine m = new Medicine();
        m.setMedicineName(name);
        m.setUnit(unit);
        m.setIngredients(ingredients);
        m.setUsage(usage);
        m.setContraindication(contra);
        MedicineDAO dao = new MedicineDAO();
        dao.insert(m);

        /* 
           4. REDIRECT VỀ DANH SÁCH
           */
        response.sendRedirect(request.getContextPath() + "/admin/medicine");
    }
}
