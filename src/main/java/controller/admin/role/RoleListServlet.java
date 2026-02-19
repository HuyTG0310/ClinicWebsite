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
import java.util.List;
import model.Role;

/**
 *
 * @author huytr
 */
@WebServlet(name = "RoleListServlet", urlPatterns = {"/admin/role/list"})
public class RoleListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Lấy từ khóa search từ thanh địa chỉ (URL)
        String search = request.getParameter("search");
        if (search == null) {
            search = "";
        }

        // 2. Gọi DAO để lấy dữ liệu
        RoleDAO dao = new RoleDAO();
        List<Role> roles = dao.searchRolesByName(search);

        // 3. Đẩy dữ liệu lên JSP
        request.setAttribute("roles", roles);
        request.setAttribute("searchValue", search); // Giữ lại từ khóa trên ô input sau khi search
        request.setAttribute("pageTitle", "Role management");
        request.setAttribute("activePage", "manageRole");

        // Load vào layout Admin của bạn
        request.setAttribute("contentPage", "/WEB-INF/admin/role/roleList.jsp");
        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
