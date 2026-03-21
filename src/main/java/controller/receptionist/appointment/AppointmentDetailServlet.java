/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.receptionist.appointment;

import dao.AppointmentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Appointment;


@WebServlet(name = "AppointmentDetailServlet", urlPatterns = {"/receptionist/appointment/detail", "/doctor/appointment/detail", "/admin/appointment/detail", "/lab/appointment/detail"})
public class AppointmentDetailServlet extends HttpServlet {

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

        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.isEmpty()) {
            response.sendRedirect("list");
            return;
        }

        try {
            int appointmentId = Integer.parseInt(idRaw);
            AppointmentDAO dao = new AppointmentDAO();
            Appointment app = dao.getAppointmentDetailById(appointmentId);

            if (app == null) {
                request.getSession().setAttribute("error", "Appointment not found!");
                response.sendRedirect(basePath + "/appointment/list");
                return;
            }

            if (app != null && "WAITING".equals(app.getStatus())) {
                dao.RoomDAO roomDAO = new dao.RoomDAO();
                dao.SpecialtyDAO specialtyDAO = new dao.SpecialtyDAO();

                request.setAttribute("rooms", roomDAO.getAllView());
                request.setAttribute("specialties", specialtyDAO.getAll());
            }

            request.setAttribute("app", app);
            request.setAttribute("pageTitle", "Appointment Detail");
            request.setAttribute("activePage", "manageAppointment"); 
            request.setAttribute("contentPage", "/WEB-INF/receptionist/appointment/appointmentDetail.jsp");
            request.getRequestDispatcher(layout).forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
