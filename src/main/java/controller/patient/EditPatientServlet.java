package controller.patient;

import java.io.IOException;
import java.sql.Date;

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
@WebServlet(name = "EditPatient", urlPatterns = {"/admin/patient/edit", "/doctor/patient/edit", "/receptionist/patient/edit", "/lab/patient/edit"})
public class EditPatientServlet extends HttpServlet {

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
        } else if (uri.startsWith(ctx + "/receptionist")) {
            basePath = ctx + "/receptionist";
        } else if (uri.startsWith(ctx + "/lab")) {
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            String patientIdStr = request.getParameter("id");

            if (patientIdStr == null || patientIdStr.trim().isEmpty()) {
                response.sendRedirect(basePath + "/patient/list");
                return;
            }

            int patientId = Integer.parseInt(patientIdStr);
            PatientDAO patientDAO = new PatientDAO();
            Patient patient = patientDAO.getPatientById(patientId);

            if (patient == null) {
                request.setAttribute("error", "Patient not found!");
                response.sendRedirect(basePath + "/patient/list");
                return;
            }

            request.setAttribute("patient", patient);
            request.getRequestDispatcher("WEB-INF/patient/editPatient.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(basePath + "/patient/list");
        } catch (Exception e) {
            System.out.println("Error loading edit form: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(basePath + "/patient/list");
        }
    }

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
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String dateOfBirthStr = request.getParameter("dateOfBirth");
            String gender = request.getParameter("gender");
            String address = request.getParameter("address");
            String medicalHistory = request.getParameter("medicalHistory");
            String allergy = request.getParameter("allergy");

            // Validate required fields
            if (patientIdStr == null || patientIdStr.trim().isEmpty()
                    || fullName == null || fullName.trim().isEmpty()
                    || phone == null || phone.trim().isEmpty()
                    || dateOfBirthStr == null || dateOfBirthStr.trim().isEmpty()
                    || gender == null || gender.trim().isEmpty()
                    || address == null || address.trim().isEmpty()) {

                request.setAttribute("error", "Please fill in all required fields!");
                doGet(request, response);
                return;
            }

            int patientId = Integer.parseInt(patientIdStr);
            fullName = fullName.trim();
            phone = phone.trim();

            // Validate full name - only Vietnamese/English letters and spaces
            if (!fullName.matches("^[a-zA-ZÀ-ỿ\\s]*$")) {
                request.setAttribute("error", "Patient names must contain only Vietnamese/English letters and spaces!");
                request.setAttribute("patient", new PatientDAO().getPatientById(patientId));
                request.getRequestDispatcher(basePath + "/WEB-INF/receptionist/patient/viewPatient.jsp").forward(request, response);
                return;
            }

            // Validate phone number - must be exactly 10 digits starting with 0 (Vietnamese
            // standard)
            String phoneDigitsOnly = phone.replaceAll("[^0-9]", "");
            if (phoneDigitsOnly.length() != 10) {
                request.setAttribute("error",
                        "Phone numbers must have exactly 10 digits, according to Vietnamese standards!");
                request.setAttribute("patient", new PatientDAO().getPatientById(patientId));
                request.getRequestDispatcher(basePath + "/WEB-INF/receptionist/patient/viewPatient.jsp").forward(request, response);
                return;
            }

            if (!phoneDigitsOnly.startsWith("0")) {
                request.setAttribute("error", "Phone number must start with 0 (Vietnamese standard)!");
                request.setAttribute("patient", new PatientDAO().getPatientById(patientId));
                request.getRequestDispatcher(basePath + "/WEB-INF/receptionist/patient/viewPatient.jsp").forward(request, response);
                return;
            }

            // Convert date string to SQL Date
            Date dateOfBirth = Date.valueOf(dateOfBirthStr);
            java.util.Date today = new java.util.Date();

            // Validate DOB - cannot be in the future
            if (dateOfBirth.getTime() > today.getTime()) {
                request.setAttribute("error", "A birth date cannot be a date in the future!");
                request.setAttribute("patient", new PatientDAO().getPatientById(patientId));
                request.getRequestDispatcher(basePath + "/WEB-INF/receptionist/patient/viewPatient.jsp").forward(request, response);
                return;
            }

            // Get existing patient
            PatientDAO patientDAO = new PatientDAO();
            Patient patient = patientDAO.getPatientById(patientId);

            if (patient == null) {
                request.setAttribute("error", "No patient found!");
                response.sendRedirect(basePath + "/patient/list");
                return;
            }

            // Check if phone number already exists (excluding current patient)
            if (patientDAO.isPhoneExists(phone, patientId)) {
                request.setAttribute("error", "This phone number already exists in the system!");
                request.setAttribute("patient", patient);
                request.getRequestDispatcher(basePath + "/patient/viewPatient.jsp").forward(request, response);
                return;
            }

            // Update patient information
            patient.setFullName(fullName);
            patient.setPhone(phone);
            patient.setDateOfBirth(dateOfBirth);
            patient.setGender(gender);
            patient.setAddress(address.trim());
            patient.setMedicalHistory(medicalHistory != null ? medicalHistory.trim() : "");
            patient.setAllergy(allergy != null ? allergy.trim() : "");

            boolean isUpdated = patientDAO.updatePatient(patient);

            if (isUpdated) {
                response.sendRedirect(basePath + "/patient/detail?id=" + patientId);
            } else {
                request.setAttribute("error", "Patient update: Treatment failed. Please try again!");
                request.setAttribute("patient", patient);
                request.getRequestDispatcher(basePath + "/WEB-INF/receptionist/patient/viewPatient.jsp").forward(request, response);
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD!");

            try {
                doGet(request, response);

            } catch (ServletException | IOException ex) {
                ex.printStackTrace();

            }

        } catch (Exception e) {
            System.out.println("Patient update error: " + e.getMessage());

            e.printStackTrace();

            request.setAttribute("error", "An error occurred while updating the patient!");

            try {
                doGet(request, response);

            } catch (ServletException | IOException ex) {
                ex.printStackTrace();

            }
        }

    }

}
