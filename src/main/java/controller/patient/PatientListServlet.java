package controller.patient;

import java.io.IOException;
import java.util.List;

import dao.PatientDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Patient;

@WebServlet(name = "PatientList", urlPatterns = {"/PatientList"})
public class PatientListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String searchKeyword = request.getParameter("searchKeyword");
        List<Patient> patientList;
        PatientDAO patientDAO = new PatientDAO();

        // If search parameter exists, search by keyword, otherwise get all patients
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            patientList = patientDAO.searchPatientByMultiFields(searchKeyword.trim());
            request.setAttribute("searchKeyword", searchKeyword);
        } else {
            patientList = patientDAO.getAllPatients();
        }

        // Set the patient list as request attribute to pass to JSP
        request.setAttribute("patientList", patientList);

        // Forward to the JSP page
//        request.getRequestDispatcher("WEB-INF/patient/patientList.jsp").forward(request, response);
        
        request.setAttribute("pageTitle", "Manage Patient");
            request.setAttribute("activePage", "managePatient");
            request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/patientList.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
            
            
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
