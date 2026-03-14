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

/**
 *
 * @author huytr
 */
@WebServlet(name = "LabCheckinServlet", urlPatterns = {"/lab/test/checkin"})
public class LabCheckinServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int mrId = Integer.parseInt(request.getParameter("mrId"));
            dao.LabTestDAO labDao = new dao.LabTestDAO();

            java.util.List<java.util.Map<String, Object>> orderedServices = labDao.getOrderedServicesForCheckin(mrId);
            request.setAttribute("orderedServices", orderedServices);

            dao.MedicalRecordDAO mrDao = new dao.MedicalRecordDAO();
            model.MedicalRecord mr = mrDao.getRecordById(mrId);
            request.setAttribute("mr", mr); // Ném qua JSP

            request.setAttribute("mrId", mrId);
            request.setAttribute("pageTitle", "Sample Check");
            request.setAttribute("activePage", "manageTest");
            request.setAttribute("contentPage", "/WEB-INF/lab/labTestCheckin.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/labLayout.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/lab/queue/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int mrId = Integer.parseInt(request.getParameter("mrId"));
            String action = request.getParameter("action");
            dao.LabTestDAO dao = new dao.LabTestDAO();

            if ("ACCEPT_ALL".equals(action)) {
                String[] testIds = request.getParameterValues("labOrderTestIds");
                if (testIds != null) {
                    for (String idStr : testIds) {
                        dao.updateLabTestStatus(Integer.parseInt(idStr), "ACCEPTED", null);
                    }
                }
                request.getSession().setAttribute("success", "Samples received! Please enter results.");
            } else if ("REJECT_SINGLE".equals(action)) {
                int rejectTestId = Integer.parseInt(request.getParameter("rejectTestId"));
                String rejectReason = request.getParameter("rejectReason");
                dao.updateLabTestStatus(rejectTestId, "REJECTED", rejectReason);
                request.getSession().setAttribute("success", "Rejected service successfully.");
            }

            // Xử lý Check-in xong -> Chuyển thẳng sang trang Nhập liệu (Edit)
            response.sendRedirect(request.getContextPath() + "/lab/test/edit?mrId=" + mrId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/lab/queue/list");
        }
    }
}
