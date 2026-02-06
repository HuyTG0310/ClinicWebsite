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

@WebServlet(name = "AddPatient", urlPatterns = {"/AddPatient"})
public class AddPatientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the add patient form JSP
//

        request.setAttribute("pageTitle", "Manage Patient");
        request.setAttribute("activePage", "managePatient");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/addPatient.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get form parameters
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String dateOfBirthStr = request.getParameter("dateOfBirth");
            String gender = request.getParameter("gender");
            String address = request.getParameter("address");
            String medicalHistory = request.getParameter("medicalHistory");
            String allergy = request.getParameter("allergy");

            // Validate required fields
            if (fullName == null || fullName.trim().isEmpty()
                    || phone == null || phone.trim().isEmpty()
                    || dateOfBirthStr == null || dateOfBirthStr.trim().isEmpty()
                    || gender == null || gender.trim().isEmpty()
                    || address == null || address.trim().isEmpty()) {

                request.setAttribute("error", "Please fill in all required fields!");
//                request.getRequestDispatcher("WEB-INF/patient/addPatient.jsp").forward(request, response);
                
                request.setAttribute("pageTitle", "Manage Patient");
        request.setAttribute("activePage", "managePatient");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/addPatient.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

                
                return;
            }

            fullName = fullName.trim();
            phone = phone.trim();

            // Validate full name - only Vietnamese/English letters and spaces
            if (!fullName.matches("^[a-zA-ZÀ-ỿ\\s]*$")) {
                request.setAttribute("error", "Full name can only contain Vietnamese/English letters and spaces!");
//                request.getRequestDispatcher("WEB-INF/patient/addPatient.jsp").forward(request, response);
                
                request.setAttribute("pageTitle", "Manage Patient");
        request.setAttribute("activePage", "managePatient");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/addPatient.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

                
                return;
            }

            // Validate phone number - must be exactly 10 digits starting with 0 (Vietnamese
            // standard)
            String phoneDigitsOnly = phone.replaceAll("[^0-9]", "");
            if (phoneDigitsOnly.length() != 10) {
                request.setAttribute("error",
                        "Phone number must be exactly 10 digits according to Vietnamese standard!");
//                request.getRequestDispatcher("WEB-INF/patient/addPatient.jsp").forward(request, response);
                
                request.setAttribute("pageTitle", "Manage Patient");
        request.setAttribute("activePage", "managePatient");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/addPatient.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

                
                return;
            }

            if (!phoneDigitsOnly.startsWith("0")) {
                request.setAttribute("error",
                        "Phone number must start with 0 (Vietnamese standard)!");
//                request.getRequestDispatcher("WEB-INF/patient/addPatient.jsp").forward(request, response);  
                
                
                request.setAttribute("pageTitle", "Manage Patient");
        request.setAttribute("activePage", "managePatient");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/addPatient.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

                
                return;
            }

            // Convert date string to SQL Date
            Date dateOfBirth = Date.valueOf(dateOfBirthStr);
            java.util.Date today = new java.util.Date();

            // Validate DOB - cannot be in the future
            if (dateOfBirth.getTime() > today.getTime()) {
                request.setAttribute("error", "Date of birth cannot be in the future!");
//                request.getRequestDispatcher("WEB-INF/patient/addPatient.jsp").forward(request, response);
                
                
                request.setAttribute("pageTitle", "Manage Patient");
        request.setAttribute("activePage", "managePatient");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/addPatient.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

                
                
                return;
            }

            // Check if phone number already exists
            PatientDAO patientDAO = new PatientDAO();
            if (patientDAO.isPhoneExists(phone)) {
                request.setAttribute("error", "A patient with this phone number already exists!");
//                request.getRequestDispatcher("WEB-INF/patient/addPatient.jsp").forward(request, response);
                
                
                request.setAttribute("pageTitle", "Manage Patient");
        request.setAttribute("activePage", "managePatient");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/addPatient.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

                
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
                response.sendRedirect(request.getContextPath() + "/PatientList");
            } else {
                request.setAttribute("error", "Failed to add patient. Please try again!");
//                request.getRequestDispatcher("WEB-INF/patient/addPatient.jsp").forward(request, response);

                request.setAttribute("pageTitle", "Manage Patient");
                request.setAttribute("activePage", "managePatient");
                request.setAttribute("contentPage", "/WEB-INF/receptionist/patient/addPatient.jsp");

                request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD!");
            request.getRequestDispatcher("WEB-INF/patient/addPatient.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("Error adding patient: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while adding the patient: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/patient/addPatient.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Add Patient Servlet";
    }
}
