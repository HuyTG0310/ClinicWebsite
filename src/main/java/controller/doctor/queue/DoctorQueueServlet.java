/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.doctor.queue;

import dao.*;
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
import model.*;

/**
 *
 * @author Truong Thinh
 */
@WebServlet(name = "DoctorQueueServlet", urlPatterns = {"/doctor/queue/list", "/admin/queue/list"})
public class DoctorQueueServlet extends HttpServlet {

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
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        HttpSession session = request.getSession(false);
        User doctor = (User) session.getAttribute("user");

        //ĐỔI THEO DB
        boolean isAdmin = (doctor.getRoleId() == 1);
        // 1. Lấy tham số (Thường Bác sĩ chỉ xem ngày hôm nay)
        String dateStr = request.getParameter("date");
        String status = request.getParameter("status");
        String patientName = request.getParameter("patientName");

        if (dateStr == null) {
            dateStr = LocalDate.now().toString();
        }

        // 2. Gọi DAO 
        AppointmentDAO appDAO = new AppointmentDAO();
        List<Appointment> queueList = appDAO.getAppointmentsForDoctor(doctor.getUserId(), dateStr, status, patientName, isAdmin);

        // 3. Đẩy dữ liệu ra UI
        request.setAttribute("basePath", basePath);
        request.setAttribute("queueList", queueList);
        request.setAttribute("paramDate", dateStr);
        request.setAttribute("isAdmin", isAdmin);

        // 4. Trỏ về giao diện Hàng Đợi (Queue)
        request.setAttribute("pageTitle", "Queue List");
        request.setAttribute("activePage", "myQueue"); 
        request.setAttribute("contentPage", "/WEB-INF/doctor/queue/queueList.jsp"); // Đổi thư mục thành queue

        request.getRequestDispatcher(layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
