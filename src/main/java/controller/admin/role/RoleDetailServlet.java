/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.role;

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
 * @author huytr
 */
@WebServlet(name = "RoleDetailServlet", urlPatterns = {"/admin/role/detail"})
public class RoleDetailServlet extends HttpServlet {

    PrivilegeDAO privilegeDAO = new PrivilegeDAO();
    RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roleIdRaw = request.getParameter("roleId");
        if (roleIdRaw == null) {
            response.sendRedirect("list");
            return;
        }
        int roleId = Integer.parseInt(roleIdRaw);

        // Tận dụng logic lấy data của bạn
        List<Privilege> allPrivileges = privilegeDAO.getAll();
        List<Integer> currentPrivilegeIds = privilegeDAO.getPrivilegeIdsByRole(roleId);
        Role role = roleDAO.getRoleById(roleId);

        request.setAttribute("role", role);
        request.setAttribute("allPrivileges", allPrivileges);
        request.setAttribute("currentPrivilegeIds", currentPrivilegeIds);

        request.setAttribute("pageTitle", "Role Detail");
        request.setAttribute("activePage", "manageRole");

        // Trỏ về file JSP mới (đẹp hơn) nằm trong thư mục admin
        request.setAttribute("contentPage", "/WEB-INF/admin/role/roleDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Giữ nguyên logic xử lý lưu của bạn
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        String[] selectedPrivileges = request.getParameterValues("privilegeIds");

        String isActiveRaw = request.getParameter("isActive");
        boolean isActive = (isActiveRaw != null);

        privilegeDAO.updateRolePrivileges(roleId, selectedPrivileges);

        roleDAO.update(roleId, isActive);

        // Quay lại đúng trang detail của Role đó kèm thông báo
        response.sendRedirect("detail?roleId=" + roleId + "&msg=success");
    }

}
