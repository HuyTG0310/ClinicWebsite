/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.role;

import dao.RoleDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author huytr
 */
@WebServlet(name = "RoleAddServlet", urlPatterns = {"/admin/role/create"})
public class RoleAddServlet extends HttpServlet {

    private RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Role Create");
        request.setAttribute("activePage", "manageRole"); // Để menu bên trái vẫn sáng ở mục Role
        request.setAttribute("contentPage", "/WEB-INF/admin/role/roleAdd.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roleName = request.getParameter("roleName");

        if (roleName != null && !roleName.trim().isEmpty()) {
            boolean success = roleDAO.insert(roleName.trim());
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/role/list?msg=addSuccess");
            } else {
                request.setAttribute("error", "Could not add role. It might already exist.");
                doGet(request, response);
            }
        } else {
            request.setAttribute("error", "Role name is required!");
            doGet(request, response);
        }
    }

}
