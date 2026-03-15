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
import java.net.URLEncoder;
import model.*;


@WebServlet(name = "UserEditServlet", urlPatterns = {"/admin/user/edit"})
public class UserEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    private UserDAO userDAO = new UserDAO();
    private RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idRaw = request.getParameter("userId");
        String username = request.getParameter("username");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String roleIdRaw = request.getParameter("roleId");
        String activeRaw = request.getParameter("isActive");

        int userId, roleId;
        String error = null;

        try {
            userId = Integer.parseInt(idRaw);
            roleId = Integer.parseInt(roleIdRaw);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/user/list");
            return;
        }

        User oldUser = userDAO.getUserById(userId);
        if (oldUser == null) {
            response.sendRedirect(request.getContextPath() + "/admin/user/list");
            return;
        }

        // 1️⃣ Username
        if (username == null || username.trim().isEmpty()) {
            error = "Username không được để trống";
        } else if (userDAO.existsByUsernameExceptId(username.trim(), userId)) {
            error = "Username đã tồn tại";
        }

        // 2️⃣ Full name
        if (error == null && (fullName == null || fullName.trim().isEmpty())) {
            error = "Họ tên không được để trống";
        }

        // 3️⃣ Role
        if (error == null && !roleDAO.existsById(roleId)) {
            error = "Vai trò không hợp lệ";
        }

        // 4️⃣ Phone
        if (error == null && phone != null && !phone.trim().isEmpty()) {
            phone = phone.trim();
            if (!phone.matches("\\d{9,11}")) {
                error = "Số điện thoại không hợp lệ";
            } else if (userDAO.existsByPhoneExceptId(phone, userId)) {
                error = "Số điện thoại đã tồn tại";
            }
        }

        // 5️⃣ Email
        if (error == null && email != null && !email.trim().isEmpty()) {
            email = email.trim();
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                error = "Email không hợp lệ";
            } else if (userDAO.existsByEmailExceptId(email, userId)) {
                error = "Email đã tồn tại";
            }
        }

        if (error != null) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/user/detail?id=" + userId
                    + "&editError=" + URLEncoder.encode(error, "UTF-8")
            );
            return;
        }

        boolean newIsActive = (activeRaw != null);
        boolean oldIsActive = oldUser.isIsActive();

        boolean wasDoctor = roleDAO.isDoctor(oldUser.getRoleId());
        boolean isDoctor = roleDAO.isDoctor(roleId);

        // Doctor inactive hoặc đổi role → gỡ khỏi room
        if (wasDoctor && (!isDoctor || !newIsActive)) {
            RoomDAO roomDAO = new RoomDAO();
            roomDAO.removeDoctorFromRooms(userId);
        }

        User u = new User();
        u.setUserId(userId);
        u.setUsername(username.trim());
        u.setFullName(fullName.trim());
        u.setPhone(phone);
        u.setEmail(email);
        u.setRoleId(roleId);
        u.setIsActive(newIsActive);

        userDAO.update(u);

        response.sendRedirect(request.getContextPath() + "/admin/user/detail?id=" + userId);
    }
}
