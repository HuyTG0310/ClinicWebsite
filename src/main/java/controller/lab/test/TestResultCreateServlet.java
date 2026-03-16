/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lab.test;

import dao.LabTestDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TestResult;

/**
 *
 * @author huytr
 */
@WebServlet(name = "LabTestSaveServlet", urlPatterns = {"/lab/lab-test/save", "/admin/lab-test/save"})
public class TestResultCreateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String basePath;

        if (uri.startsWith(ctx + "/admin")) {
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/lab")) {
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);

        try {
            int medicalRecordId = Integer.parseInt(request.getParameter("medicalRecordId"));

            String[] orderTestIds = request.getParameterValues("orderTestId");
            String[] paramIds = request.getParameterValues("paramId");

            model.User currentUser = (model.User) request.getSession().getAttribute("user");
            int technicianId = currentUser.getUserId();

            TestResult testResult = new TestResult();
            testResult.setMedicalRecordId(medicalRecordId);
            testResult.setOrderTestIds(orderTestIds);
            testResult.setParamIds(paramIds);
            testResult.setTechnicianId(technicianId);
            testResult.setParameterMap(request.getParameterMap());

            LabTestDAO dao = new LabTestDAO();
//            boolean success = dao.saveLabResults(medicalRecordId, orderTestIds, paramIds, request.getParameterMap(), technicianId);
            boolean success = dao.saveLabResults(testResult);

            if (success) {
                request.getSession().setAttribute("success", "Saved result successfully!");
            } else {
                request.getSession().setAttribute("error", "Save result error");
            }
            response.sendRedirect(basePath + "/lab-test/detail?mrId=" + medicalRecordId);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Invalid input!");
            response.sendRedirect(basePath + "/lab-queue/list");
        }
    }

}
