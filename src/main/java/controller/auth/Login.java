/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.auth;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import dao.*;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Tai Loi
 */
@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            redirectByUserRole(response, request, user);
            return;
        }
        // Hiện trang login
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userParam = request.getParameter("username");
        String passParam = request.getParameter("password");

        UserDAO dao = new UserDAO();
        User user = dao.login(userParam, passParam);

        if (user != null) {
            // 1. Lưu user vào Session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // 2. Điều hướng dựa trên Role
            String role = user.getRoleName();
            String ctx = request.getContextPath();

            if ("ADMIN".equalsIgnoreCase(role)) {
                response.sendRedirect(ctx + "/admin/role/list");
            } else if ("DOCTOR".equalsIgnoreCase(role)) {
                response.sendRedirect(ctx + "/doctor/queue/list");
            } else if ("RECEPTIONIST".equalsIgnoreCase(role)) {
                response.sendRedirect(ctx + "/receptionist/appointment/list");
            } else if ("LAB TECHNICIAN".equalsIgnoreCase(role)) {
                response.sendRedirect(ctx + "/lab/lab-queue/list");
            } else {
                // Các role khác nếu có
                response.sendRedirect(ctx + "/index.jsp");
            }
        } else {
            // 3. Thất bại: Gửi thông báo lỗi về trang login
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng, hoặc tài khoản bị khóa!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void redirectByUserRole(HttpServletResponse response, HttpServletRequest request, User user) throws IOException {
        String role = user.getRoleName();
        if ("ADMIN".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else if ("DOCTOR".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/doctor/dashboard");
        } else if ("RECEPTIONIST".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/receptionist/dashboard");
        } else if ("LAB TECHNICIAN".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/lab/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/home"); // Hoặc trang mặc định
        }
    }
}
