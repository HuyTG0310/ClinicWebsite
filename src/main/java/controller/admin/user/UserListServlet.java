/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.user;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import model.*;


/**
 *
 * @author Tan Vinh
 */
@WebServlet(name = "UserListServlet", urlPatterns = {"/admin/user/list"})
public class UserListServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String roleIdRaw = request.getParameter("roleId");
        String statusRaw = request.getParameter("status");

        boolean hasSearch
                = (keyword != null && !keyword.trim().isEmpty())
                || (roleIdRaw != null && !roleIdRaw.isEmpty())
                || (statusRaw != null && !statusRaw.isEmpty());

        List<User> users;

        if (!hasSearch) {
            // 👉 KHÔNG search → lấy toàn bộ
            users = userDAO.getAll();
        } else {
            Integer roleId = null;
            Boolean isActive = null;

            try {
                if (roleIdRaw != null && !roleIdRaw.isEmpty()) {
                    roleId = Integer.parseInt(roleIdRaw);
                }
                if (statusRaw != null && !statusRaw.isEmpty()) {
                    isActive = statusRaw.equals("1");
                }
            } catch (Exception e) {
                // ignore
            }

            users = userDAO.search(keyword, roleId, isActive);
        }

        request.setAttribute("users", users);
        request.setAttribute("roles", roleDAO.getAll());

        request.setAttribute("pageTitle", "Staff List");
        request.setAttribute("activePage", "manageStaff");
        request.setAttribute("contentPage", "/WEB-INF/admin/user/userList.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
