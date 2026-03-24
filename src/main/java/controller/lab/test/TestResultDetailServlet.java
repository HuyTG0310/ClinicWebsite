/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lab.test;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TestResult;
import model.TestResultDetail;

/**
 *
 * @author Gia Huy
 */
@WebServlet(name = "LabResultDetailServlet", urlPatterns = {"/lab/lab-test/detail", "/admin/lab-test/detail"})
public class TestResultDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        String layout;
        String basePath;

        if (uri.startsWith(ctx + "/admin")) {
            layout = "/WEB-INF/layout/adminLayout.jsp";
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/lab")) {
            layout = "/WEB-INF/layout/labLayout.jsp";
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);

        try {
            int mrId = Integer.parseInt(request.getParameter("mrId"));
            dao.LabTestDAO labDao = new dao.LabTestDAO();

            java.util.List<TestResultDetail> tests = labDao.getTestsForProcessing(mrId);
            if (tests.isEmpty()) {
                request.getSession().setAttribute("error", "Test result detail not found!");
                response.sendRedirect(basePath + "/lab-queue/list");
                return;
            }
            
            request.setAttribute("tests", tests);
            request.setAttribute("mrId", mrId);

            //CHẾ ĐỘ CHỈ XEM (READ-ONLY)
            request.setAttribute("isViewMode", true);

            dao.MedicalRecordDAO mrDao = new dao.MedicalRecordDAO();
            model.MedicalRecord mr = mrDao.getRecordById(mrId);
            request.setAttribute("mr", mr); // Ném qua JSP

            request.setAttribute("pageTitle", "Result Detail");
            request.setAttribute("activePage", "manageTest");

            request.setAttribute("contentPage", "/WEB-INF/lab/labTestEdit.jsp");

            request.getRequestDispatcher(layout).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(basePath + "/lab-queue/list");
        }
    }
}
