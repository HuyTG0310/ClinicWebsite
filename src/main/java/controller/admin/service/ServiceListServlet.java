/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.service;

import dao.ServiceDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import model.*;


@WebServlet(name = "ServiceListServlet", urlPatterns = {"/admin/service/list", "/receptionist/service/list", "/doctor/service/list", "/lab/service/list"})
public class ServiceListServlet extends HttpServlet {

    private ServiceDAO serviceDAO = new ServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        String layout;
        String basePath;
        if (uri.startsWith(ctx + "/admin")) {
            layout = "/WEB-INF/layout/adminLayout.jsp";
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            layout = "/WEB-INF/layout/doctorLayout.jsp";
            basePath = ctx + "/doctor";
        } else if (uri.startsWith(ctx + "/receptionist")) {
            layout = "/WEB-INF/layout/receptionistLayout.jsp";
            basePath = ctx + "/receptionist";
        } else if (uri.startsWith(ctx + "/lab")) {
            layout = "/WEB-INF/layout/labLayout.jsp";
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);
        
        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");

        List<Service> services;

        if ((keyword != null && !keyword.isEmpty())
                || (status != null && !status.isEmpty())) {
            services = serviceDAO.search(keyword, status);
        } else {
            services = serviceDAO.getAll();
        }

        request.setAttribute("services", services);
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);

        request.setAttribute("pageTitle", "Service List");
        request.setAttribute("activePage", "manageService");
        request.setAttribute("contentPage", "/WEB-INF/admin/service/serviceList.jsp");

        request.getRequestDispatcher(layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
