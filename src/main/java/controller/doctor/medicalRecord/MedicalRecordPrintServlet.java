/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.doctor.medicalRecord;

import dao.MedicalRecordDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.MedicalRecord;
import model.User;


/**
 *
 * @author Truong Thinh
 */
@WebServlet(name = "MedicalRecordPrintServlet", urlPatterns = {"/doctor/medical-record/print", "/admin/medical-record/print"})
public class MedicalRecordPrintServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String basePath;

        if (uri.startsWith(ctx + "/admin")) {
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            basePath = ctx + "/doctor";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            String mrIdStr = request.getParameter("id");
            String appIdStr = request.getParameter("appointmentId");

            MedicalRecordDAO mrDAO = new MedicalRecordDAO();
            MedicalRecord mr = null;

            if (mrIdStr != null && !mrIdStr.isEmpty()) {
                mr = mrDAO.getRecordById(Integer.parseInt(mrIdStr));
            } else if (appIdStr != null && !appIdStr.isEmpty()) {
                mr = mrDAO.getRecordByAppointment(Integer.parseInt(appIdStr));
            }

            if (mr == null) {
                request.getSession().setAttribute("error", "Medical record not found!");
                response.sendRedirect(basePath + "/medical-record/list");
                return;
            }

            int currentUserId = currentUser.getUserId();
            boolean isAdmin = (currentUser.getRoleId() == 1);

            boolean[] perms = mrDAO.checkPermissionDetail(mr.getMedicalRecordId(), currentUserId, isAdmin);
            boolean canView = perms[0];

            if (!canView) {
                request.getSession().setAttribute("error", "SECURITY: You HAVE NO PERMISSION to print this medical record!");
                response.sendRedirect(basePath + "/medical-record/list");
                return;
            }

            dao.LabTestDAO labDao = new dao.LabTestDAO();
            java.util.List<java.util.Map<String, Object>> consolidatedResults = labDao.getConsolidatedLabResults(mr.getMedicalRecordId());

            request.setAttribute("consolidatedResults", consolidatedResults);
            request.setAttribute("mr", mr);

            request.getRequestDispatcher("/WEB-INF/doctor/medicalrecord/medicalRecordPrint.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login"); // Hoặc trang lỗi 500
        }
    }
}
