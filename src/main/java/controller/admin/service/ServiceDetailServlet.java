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
    private LabTestDAO labTestDAO = new LabTestDAO();
    private LabTestCategoryDAO labCateDAO = new LabTestCategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        if (idRaw == null) {
            response.sendRedirect(request.getContextPath() + "/admin/service/list");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);
            Service service = serviceDAO.getById(id);

            if (service == null) {
                response.sendRedirect(request.getContextPath() + "/admin/service/list");
                return;
            }

            request.setAttribute("service", service);

            if ("Xét nghiệm".equals(service.getCategory())) {
                LabTest labTest = labTestDAO.getLabTestByServiceId(id);
                request.setAttribute("labTest", labTest);
                //load nhóm đưa vào form edit
                request.setAttribute("labCategories", labCateDAO.getAllCategories());
            }

            request.setAttribute("categories", ServiceCategory.getAll());
            request.setAttribute("pageTitle", "Add service");
            request.setAttribute("activePage", "manageService");
            request.setAttribute("contentPage", "/WEB-INF/admin/service/serviceDetail.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/service/list");
        }

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
