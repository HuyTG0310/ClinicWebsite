package controller.patient;

import java.io.IOException;

import dao.PatientDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "DeletePatient", urlPatterns = { "/DeletePatient" })
public class DeletePatientServlet extends HttpServlet {

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String patientIdStr = request.getParameter("patientId");
            String confirmDelete = request.getParameter("confirmDelete");

            if (patientIdStr == null || patientIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/PatientList");
                return;
            }

            int patientId = Integer.parseInt(patientIdStr);

            // Check if delete is confirmed
            if (confirmDelete == null || !confirmDelete.equals("yes")) {
                response.sendRedirect(request.getContextPath() + "/ViewPatient?id=" + patientId);
                return;
            }

            PatientDAO patientDAO = new PatientDAO();
            boolean isDeleted = patientDAO.deletePatient(patientId);

            if (isDeleted) {
                response.sendRedirect(request.getContextPath() + "/PatientList");
            } else {
                response.sendRedirect(
                        request.getContextPath() + "/ViewPatient?id=" + patientId + "&error=Failed to delete patient");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/PatientList");
        } catch (Exception e) {
            System.out.println("Error deleting patient: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/PatientList");
        }
    }

   
    @Override
    public String getServletInfo() {
        return "Delete Patient Servlet";
    }
}
