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

/**
 *
 * @author huytr
 */
@WebServlet(name = "UserDetailServlet", urlPatterns = {"/admin/user/detail"})
public class UserDetailServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private RoleDAO roleDAO = new RoleDAO();
    private SpecialtyDAO specialtyDAO = new SpecialtyDAO();
    private DoctorSpecialtyDAO doctorSpecialtyDAO = new DoctorSpecialtyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        int id;

        try {
            id = Integer.parseInt(idRaw);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/user/list");
            return;
        }

        User user = userDAO.getUserById(id);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/admin/user/list");
            return;
        }
        
        if ("Doctor".equals(user.getRoleName())) {

            request.setAttribute("specialties", specialtyDAO.getAll());

            request.setAttribute(
                    "assignedSpecialtyIds",
                    doctorSpecialtyDAO.getSpecialtyIdsByDoctor(user.getUserId())
            );

            request.setAttribute(
                    "primarySpecialtyId",
                    doctorSpecialtyDAO.getPrimarySpecialtyId(user.getUserId())
            );
        }

        request.setAttribute("user", user);
//        request.getRequestDispatcher("/WEB-INF/admin/user/userDetail.jsp")
//                .forward(request, response);
        request.setAttribute("roles", roleDAO.getAll());

        request.setAttribute("pageTitle", "Staff Detail");
        request.setAttribute("activePage", "manageStaff");
        request.setAttribute("contentPage", "/WEB-INF/admin/user/userDetail.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
