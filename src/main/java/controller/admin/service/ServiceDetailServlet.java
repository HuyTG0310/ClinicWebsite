/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.service;

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
@WebServlet(name = "ServiceDetailServlet", urlPatterns = {"/admin/service/detail"})
public class ServiceDetailServlet extends HttpServlet {

    private ServiceDAO serviceDAO = new ServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idRaw = request.getParameter("id");

        if (idRaw == null) {
            response.sendRedirect(request.getContextPath() + "/admin/service/list");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idRaw);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/service/list");
            return;
        }

        Service service = serviceDAO.getById(id);

        if (service == null) {
            response.sendRedirect(request.getContextPath() + "/admin/service/list");
            return;
        }

        request.setAttribute("service", service);
//        request.getRequestDispatcher("/WEB-INF/admin/service/serviceDetail.jsp").forward(request, response);
        
        request.setAttribute("categories", ServiceCategory.getAll());
        request.setAttribute("pageTitle", "Add service");
        request.setAttribute("activePage", "manageService");
        request.setAttribute("contentPage", "/WEB-INF/admin/service/serviceDetail.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
