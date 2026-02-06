package controller.patient;

import java.io.IOException;

import dao.PatientDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Patient;

@WebServlet(name = "ViewPatient", urlPatterns = {"/ViewPatient"})
public class ViewPatientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String patientIdStr = request.getParameter("id");

            if (patientIdStr == null || patientIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/PatientList");
                return;
            }

            int patientId = Integer.parseInt(patientIdStr);
            PatientDAO patientDAO = new PatientDAO();
            Patient patient = patientDAO.getPatientById(patientId);

            if (patient == null) {
                request.setAttribute("error", "Patient not found!");
                request.getRequestDispatcher("WEB-INF/patient/patientList.jsp").forward(request, response);
                return;
            }

            request.setAttribute("patient", patient);
//            request.getRequestDispatcher("WEB-INF/patient/viewPatient.jsp").forward(request, response);

            request.setAttribute("pageTitle", "Manage Patient");
            request.setAttribute("activePage", "managePatient");
            request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/viewPatient.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
            
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/PatientList");
        } catch (Exception e) {
            System.out.println("Error viewing patient: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while retrieving patient details!");
            try {
                request.getRequestDispatcher("WEB-INF/patient/patientList.jsp").forward(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }

     @Override
    public String getServletInfo() {
        return "View Patient Details Servlet";
    }
}
