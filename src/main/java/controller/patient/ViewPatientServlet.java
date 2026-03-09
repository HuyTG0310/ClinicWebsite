package controller.patient;

import java.io.IOException;

import dao.PatientDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Patient;

@WebServlet(name = "ViewPatient", urlPatterns = {"/admin/patient/detail", "/doctor/patient/detail", "/receptionist/patient/detail"})
public class ViewPatientServlet extends HttpServlet {

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
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);

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

            request.setAttribute("pageTitle", "Patient Detail");
            request.setAttribute("activePage", "managePatient");
            request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/viewPatient.jsp");

            request.getRequestDispatcher(layout).forward(request, response);

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
