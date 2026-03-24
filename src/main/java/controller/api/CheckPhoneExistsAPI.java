package controller.api;

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
@WebServlet(name = "CheckPhoneExistsAPI", urlPatterns = { "/api/checkPhoneExists" })
public class CheckPhoneExistsAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            String phone = request.getParameter("phone");
            String excludePatientIdStr = request.getParameter("excludePatientId");

            if (phone == null || phone.trim().isEmpty()) {
                response.getWriter().write("{\"exists\": false}");
                return;
            }

            phone = phone.trim();

            // Validate phone format - must be exactly 10 digits starting with 0
            String phoneDigitsOnly = phone.replaceAll("[^0-9]", "");
            if (phoneDigitsOnly.length() != 10 || !phoneDigitsOnly.startsWith("0")) {
                response.getWriter().write("{\"exists\": false, \"error\": \"Invalid phone format\"}");
                return;
            }

            PatientDAO patientDAO = new PatientDAO();

            boolean exists;
            if (excludePatientIdStr != null && !excludePatientIdStr.trim().isEmpty()) {
                try {
                    int excludePatientId = Integer.parseInt(excludePatientIdStr);
                    exists = patientDAO.isPhoneExists(phone, excludePatientId);
                } catch (NumberFormatException e) {
                    exists = patientDAO.isPhoneExists(phone);
                }
            } else {
                exists = patientDAO.isPhoneExists(phone);
            }

            response.getWriter().write("{\"exists\": " + exists + "}");

        } catch (Exception e) {
            System.out.println("Error checking phone: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"exists\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
