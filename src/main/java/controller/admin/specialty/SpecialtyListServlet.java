/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.admin.specialty;

import dao.SpecialtyDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Specialty;

/**
 *
 * @author ADMIN
 */
@WebServlet("/admin/specialty/list")
public class SpecialtyListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        SpecialtyDAO dao = new SpecialtyDAO();
        List<Specialty> list;

        if (keyword == null || keyword.trim().isEmpty()) {
            // 🔹 không nhập hoặc chỉ space → list all
            list = dao.getAll();
        } else {
            // 🔹 có keyword → search
            list = dao.searchByName(keyword.trim());
        }

        request.setAttribute("list", list);
//        request.getRequestDispatcher("/WEB-INF/admin/specialty/listSpecialty.jsp")
//                .forward(request, response);

        request.setAttribute("pageTitle", "Manage speiclaty");
        request.setAttribute("activePage", "manageSpecialty");
        request.setAttribute("contentPage", "/WEB-INF/admin/specialty/listSpecialty.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
    }

}
