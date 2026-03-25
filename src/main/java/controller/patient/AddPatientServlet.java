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
@WebServlet(name = "AddPatient", urlPatterns = {"/admin/patient/create", "/doctor/patient/create", "/receptionist/patient/create", "/lab/patient/create"})
public class AddPatientServlet extends HttpServlet {

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
        sendData(request, response, layout);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

        try {
            // Get form parameters
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String dateOfBirthStr = request.getParameter("dateOfBirth");
            String gender = request.getParameter("gender");
            String address = request.getParameter("address");
            String medicalHistory = request.getParameter("medicalHistory");
            String allergy = request.getParameter("allergy");

            Patient p = new Patient();
            p.setFullName(fullName);
            p.setPhone(phone);
            p.setDateOfBirth(Date.valueOf(dateOfBirthStr));
            p.setGender(gender);
            p.setAddress(address);
            p.setMedicalHistory(medicalHistory);
            p.setAllergy(allergy);

            // Validate required fields
            if (fullName == null || fullName.trim().isEmpty()
                    || phone == null || phone.trim().isEmpty()
                    || dateOfBirthStr == null || dateOfBirthStr.trim().isEmpty()
                    || gender == null || gender.trim().isEmpty()
                    || address == null || address.trim().isEmpty()) {

                request.setAttribute("error", "Please fill in all required fields!");
                request.setAttribute("patient", p);
                sendData(request, response, layout);
                return;
            }

            fullName = fullName.trim();
            phone = phone.trim();

            // Validate full name - only Vietnamese/English letters and spaces
            if (!fullName.matches("^[a-zA-ZÀ-ỿ\\s]*$")) {
                request.setAttribute("error", "Full name can only contain Vietnamese/English letters and spaces!");
                request.setAttribute("patient", p);
                sendData(request, response, layout);

                return;
            }

            // Validate phone number - must be exactly 10 digits starting with 0 (Vietnamese
            // standard)
            String phoneDigitsOnly = phone.replaceAll("[^0-9]", "");
            if (phoneDigitsOnly.length() != 10) {
                request.setAttribute("error",
                        "Phone number must be exactly 10 digits according to Vietnamese standard!");
                request.setAttribute("patient", p);
                sendData(request, response, layout);

                return;
            }

            if (!phoneDigitsOnly.startsWith("0")) {
                request.setAttribute("error",
                        "Phone number must start with 0 (Vietnamese standard)!");
                request.setAttribute("patient", p);
                sendData(request, response, layout);

                return;
            }

            // Convert date string to SQL Date
            Date dateOfBirth = Date.valueOf(dateOfBirthStr);
            java.util.Date today = new java.util.Date();

            // Validate DOB - cannot be in the future
            if (dateOfBirth.getTime() > today.getTime()) {
                request.setAttribute("error", "Date of birth cannot be in the future!");
                request.setAttribute("patient", p);
                sendData(request, response, layout);

                return;
            }

            // Check if phone number already exists
            PatientDAO patientDAO = new PatientDAO();
            if (patientDAO.isPhoneExists(phone)) {
                request.setAttribute("error", "A patient with this phone number already exists!");
                request.setAttribute("patient", p);
                sendData(request, response, layout);

                return;
            }

            // Create new Patient object
            Patient newPatient = new Patient();
            newPatient.setFullName(fullName);
            newPatient.setPhone(phone);
            newPatient.setDateOfBirth(dateOfBirth);
            newPatient.setGender(gender);
            newPatient.setAddress(address.trim());
            newPatient.setMedicalHistory(medicalHistory != null ? medicalHistory.trim() : "");
            newPatient.setAllergy(allergy != null ? allergy.trim() : "");

            // Add patient to database
            boolean isAdded = patientDAO.addPatient(newPatient);

            if (isAdded) {
                request.setAttribute("success", "Patient added successfully!");
                response.sendRedirect(basePath + "/patient/list");
            } else {
                request.setAttribute("error", "Failed to add patient. Please try again!");
                request.setAttribute("patient", p);
                sendData(request, response, layout);

            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD!");
            sendData(request, response, layout);
        } catch (Exception e) {
            System.out.println("Error adding patient: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while adding the patient: " + e.getMessage());
            sendData(request, response, layout);
        }
    }

    public void sendData(HttpServletRequest request, HttpServletResponse response, String layout) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Manage Patient");
        request.setAttribute("activePage", "managePatient");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/addPatient.jsp");

        request.getRequestDispatcher(layout).forward(request, response);
    }
}
