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
 * @author Tan Vinh
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
            list = dao.getAll();
        } else {
            list = dao.searchByName(keyword.trim());
        }

        request.setAttribute("list", list);
        request.setAttribute("pageTitle", "Specialty List");
        request.setAttribute("activePage", "manageSpecialty");
        request.setAttribute("contentPage", "/WEB-INF/admin/specialty/listSpecialty.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
    }

}
