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
 * @author Gia Huy
 */

@WebServlet(name = "ServiceDetailServlet", urlPatterns = {"/admin/service/detail", "/doctor/service/detail", "/receptionist/service/detail", "/lab/service/detail"})
public class ServiceDetailServlet extends HttpServlet {

    private ServiceDAO serviceDAO = new ServiceDAO();
    private LabTestDAO labTestDAO = new LabTestDAO();
    private LabTestCategoryDAO labCateDAO = new LabTestCategoryDAO();

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
        
        String idRaw = request.getParameter("id");
        if (idRaw == null) {
            response.sendRedirect(basePath + "/service/list");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);
            Service service = serviceDAO.getById(id);

            if (service == null) {
                request.getSession().setAttribute("error", "Service not found!");
                response.sendRedirect(basePath + "/service/list");
                return;
            }

            request.setAttribute("service", service);

            if ("Xét nghiệm".equals(service.getCategory())) {
                LabTest labTest = labTestDAO.getLabTestByServiceId(id);
                request.setAttribute("labTest", labTest);
                request.setAttribute("labCategories", labCateDAO.getAllCategories());
            }

            request.setAttribute("categories", ServiceCategory.getAll());
            request.setAttribute("pageTitle", "Service Detail");
            request.setAttribute("activePage", "manageService");
            request.setAttribute("contentPage", "/WEB-INF/admin/service/serviceDetail.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(basePath + "/service/list");
        }

        request.getRequestDispatcher(layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
