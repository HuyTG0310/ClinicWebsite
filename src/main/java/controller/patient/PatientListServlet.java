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

/**
 *
 * @author Chi Duong
 */
@WebServlet(name = "PatientList", urlPatterns = {"/admin/patient/list", "/doctor/patient/list", "/receptionist/patient/list", "/lab/patient/list"})
public class PatientListServlet extends HttpServlet {

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
        } else if (uri.startsWith(ctx + "/lab")) {
            layout = "/WEB-INF/layout/labLayout.jsp";
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);

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
        request.setAttribute("pageTitle", "Patient List");
        request.setAttribute("activePage", "managePatient");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/patientList.jsp");

        request.getRequestDispatcher(layout).forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
