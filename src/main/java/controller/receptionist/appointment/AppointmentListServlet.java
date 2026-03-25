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
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import model.Appointment;


/**
 *
 * @author Chi Duong
 */
@WebServlet(name = "AppointmentListServlet", urlPatterns = {"/receptionist/appointment/list", "/doctor/appointment/list", "/admin/appointment/list", "/lab/appointment/list"})
public class AppointmentListServlet extends HttpServlet {

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

        String msg = request.getParameter("msg");
        if ("success".equals(msg)) {
            request.setAttribute("successMessage", "Create appointment successfully. Patient is currently in queue");
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String keyword = request.getParameter("keyword");
        String dateStr = request.getParameter("date");
        String status = request.getParameter("status");

        if (dateStr == null) {
            dateStr = LocalDate.now().toString();
        }

        AppointmentDAO appDAO = new AppointmentDAO();
        List<Appointment> list = appDAO.getAppointmentList(keyword, dateStr, status);

        request.setAttribute("appointmentList", list);

        request.setAttribute("paramDate", dateStr);

        request.setAttribute("pageTitle", "Appointment List");
        request.setAttribute("activePage", "manageAppointment");
        request.setAttribute("contentPage", "/WEB-INF/receptionist/appointment/appointmentList.jsp");

        request.getRequestDispatcher(layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
