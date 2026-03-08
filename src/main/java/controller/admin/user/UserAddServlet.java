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
import model.*;
import util.EmailUtil;

@WebServlet(name = "UserAddServlet", urlPatterns = {"/admin/user/add"})
public class UserAddServlet extends HttpServlet {

    private RoleDAO roleDAO = new RoleDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("roles", roleDAO.getAll());

        request.setAttribute("pageTitle", "Staff Detail");
        request.setAttribute("activePage", "manageStaff");
        request.setAttribute("contentPage", "/WEB-INF/admin/user/userAdd.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String roleIdRaw = request.getParameter("roleId");
        String activeRaw = request.getParameter("isActive");

        String error = null;
        int roleId = -1;

        if (username == null || username.trim().isEmpty()) {
            error = "Username không được để trống";
        } else if (userDAO.existsByUsername(username.trim())) {
            error = "Username đã tồn tại";
        }

        if (error == null) {
            if (fullName == null || fullName.trim().isEmpty()) {
                error = "Họ tên không được để trống";
            }
        }

        if (error == null) {
            try {
                roleId = Integer.parseInt(roleIdRaw);
                if (!roleDAO.existsById(roleId)) {
                    error = "Vai trò không hợp lệ";
                }
            } catch (Exception e) {
                error = "Vai trò không hợp lệ";
            }
        }

        if (error == null && phone != null && !phone.trim().isEmpty()) {

            phone = phone.trim();

            if (!phone.matches("\\d{9,11}")) {
                error = "Số điện thoại không hợp lệ";
            } else if (userDAO.existsByPhone(phone)) {
                error = "Số điện thoại đã tồn tại";
            }
        }

        if (error == null && email != null && !email.trim().isEmpty()) {

            email = email.trim();

            if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$")) {
                error = "Email không hợp lệ";
            } else if (userDAO.existsByEmail(email)) {
                error = "Email đã tồn tại";
            }
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("roles", roleDAO.getAll());

            request.setAttribute("username", username);
            request.setAttribute("fullName", fullName);
            request.setAttribute("phone", phone);
            request.setAttribute("email", email);
            request.setAttribute("roleId", roleIdRaw);
            request.setAttribute("isActive", activeRaw);
            request.setAttribute("pageTitle", "Staff Detail");
            request.setAttribute("activePage", "manageStaff");
            request.setAttribute("contentPage", "/WEB-INF/admin/user/userAdd.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
            return;
        }

        User u = new User();
        u.setUsername(username.trim());
        u.setFullName(fullName.trim());
        u.setPhone(phone);
        u.setEmail(email);
        u.setRoleId(roleId);
        u.setIsActive(activeRaw != null);

//        userDAO.insert(u);
//
//        response.sendRedirect(request.getContextPath() + "/admin/user/list");
        boolean success = userDAO.insert(u);

        if (success) {

            if (email != null && !email.isEmpty()) {
                EmailUtil.sendAccount(email, username);
            }

            response.sendRedirect(request.getContextPath() + "/admin/user/list");

        } else {

            request.setAttribute("error", "Cannot create user");
            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp")
                    .forward(request, response);
        }
    }
}
