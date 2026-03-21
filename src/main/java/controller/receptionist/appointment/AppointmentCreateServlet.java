/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.receptionist.appointment;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import model.*;

@WebServlet(name = "AppointmentCreateServlet", urlPatterns = {"/receptionist/appointment/create", "/doctor/appointment/create", "/admin/appointment/create", "/lab/appointment/create"})
public class AppointmentCreateServlet extends HttpServlet {

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

        PatientDAO patientDAO = new PatientDAO();
        List<Patient> patients = patientDAO.getAllPatients();

        RoomDAO roomDAO = new RoomDAO();
        List<RoomView> activeRooms = roomDAO.getAllActiveRoomsForAppointment();

        SpecialtyDAO specialtyDAO = new SpecialtyDAO();
        List<Specialty> specialties = specialtyDAO.getAll();
        
        
        ServiceDAO serviceDAO = new ServiceDAO();
        double servicePrice = serviceDAO.getById(1).getCurrentPrice().doubleValue();

        request.setAttribute("patients", patients);
        request.setAttribute("rooms", activeRooms);
        request.setAttribute("specialties", specialties);
        request.setAttribute("servicePrice", servicePrice);

        request.setAttribute("pageTitle", "Create Appointment");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/appointment/addAppointment.jsp");
        request.setAttribute("activePage", "manageAppointment");
        request.getRequestDispatcher(layout).forward(request, response);
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
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            int receptionistId = user.getUserId();

            String patientIdRaw = request.getParameter("patientId");
            String roomIdRaw = request.getParameter("roomId");
            String paymentMethod = request.getParameter("paymentMethod");

            if (patientIdRaw == null || roomIdRaw == null || patientIdRaw.isEmpty() || roomIdRaw.isEmpty()) {
                request.setAttribute("error", "Please choose patient and room!");
                doGet(request, response);
                return;
            }

            if (paymentMethod == null || paymentMethod.isEmpty()) {
                paymentMethod = "CASH";
            }

            int patientId = Integer.parseInt(patientIdRaw);
            int roomId = Integer.parseInt(roomIdRaw);

            AppointmentDAO appDAO = new AppointmentDAO();
            int newServiceOrderId = appDAO.createAppointment(patientId, roomId, receptionistId, paymentMethod);

            if (newServiceOrderId > 0) {
                if ("CASH".equals(paymentMethod)) {
                    response.sendRedirect(basePath + "/appointment/list?msg=success");
                } else {
                    response.sendRedirect(basePath + "/service-order/detail?soId=" + newServiceOrderId);
                }
            } else {
                request.setAttribute("error", "Erorr system: Can not create appointment!");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "ID is invalid!");
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Internal Server Error: " + e.getMessage());
        }
    }

}
