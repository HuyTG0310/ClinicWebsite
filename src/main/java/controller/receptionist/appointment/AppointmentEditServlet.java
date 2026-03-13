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
import java.util.*;
import model.*;

@WebServlet(name = "AppointmentEditServlet", urlPatterns = {"/receptionist/appointment/edit", "/doctor/appointment/edit", "/admin/appointment/edit"})
public class AppointmentEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);
        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            String action = request.getParameter("action");

            dao.AppointmentDAO appDAO = new dao.AppointmentDAO();

            if ("editRoom".equals(action)) {
                int roomId = Integer.parseInt(request.getParameter("roomId"));
                boolean success = appDAO.updateAppointmentRoom(appointmentId, roomId);

                if (success) {
                    request.getSession().setAttribute("success", "Changed room successfully!");
                } else {
                    request.getSession().setAttribute("error", "Change room error!");
                }
            } else if ("cancel".equals(action)) {
                boolean success = appDAO.cancelAppointment(appointmentId);

                if (success) {
                    request.getSession().setAttribute("success", "Cancel appointment successfully!");
                } else {
                    request.getSession().setAttribute("error", "Cancel error!");
                }
            }

            response.sendRedirect(basePath + "/appointment/detail?id=" + appointmentId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }

}
