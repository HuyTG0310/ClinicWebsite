package controller.patient;

import java.io.IOException;

import dao.PatientDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Chi Duong
 */
@WebServlet(name = "DeletePatient", urlPatterns = {"/admin/patient/delete", "/doctor/patient/delete", "/receptionist/patient/delete",  "/lab/patient/delete"})
public class DeletePatientServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String basePath;

        if (uri.startsWith(ctx + "/admin")) {
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            basePath = ctx + "/doctor";
        } else if (uri.startsWith(ctx + "/receptionist")) {
            basePath = ctx + "/receptionist";
        } else if (uri.startsWith(ctx + "/lab")) {
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);

        try {
            String patientIdStr = request.getParameter("patientId");
            String confirmDelete = request.getParameter("confirmDelete");

            if (patientIdStr == null || patientIdStr.trim().isEmpty()) {
                response.sendRedirect(basePath + "/patient/list");
                return;
            }

            int patientId = Integer.parseInt(patientIdStr);

            // Check if delete is confirmed
            if (confirmDelete == null || !confirmDelete.equals("yes")) {
                response.sendRedirect(basePath + "/patient/detail?id=" + patientId);
                return;
            }

            PatientDAO patientDAO = new PatientDAO();
            boolean isDeleted = patientDAO.deletePatient(patientId);

            if (isDeleted) {
                response.sendRedirect(basePath + "/patient/list");
            } else {
                response.sendRedirect(basePath + "/patient/detail?id=" + patientId + "&error=Failed to delete patient");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(basePath + "/patient/list");
        } catch (Exception e) {
            System.out.println("Error deleting patient: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(basePath + "/patient/list");
        }
    }

}
