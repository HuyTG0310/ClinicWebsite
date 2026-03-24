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

/**
 *
 * @author Truong Thinh
 */
@WebServlet(name = "DoctorQueueCallServlet", urlPatterns = {"/doctor/queue/call", "/admin/queue/call"})
public class DoctorQueueCallServlet extends HttpServlet {

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
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        request.setAttribute("basePath", basePath);

        try {
            // 1. Lấy ID của lịch hẹn được chọn
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

            AppointmentDAO appDAO = new AppointmentDAO();

            // 2. Chuyển trạng thái sang ĐANG KHÁM
            boolean success = appDAO.updateStatusToInProgress(appointmentId);

            if (success) {
                // Thành công: Đưa bác sĩ nhảy thẳng sang trang Viết Bệnh Án của bệnh nhân đó
                // (Chúng ta sẽ dùng chung URL /edit vì bản chất là vào không gian làm việc)
                response.sendRedirect(basePath + "/medical-record/edit?appointmentId=" + appointmentId);
            } else {
                // Thất bại (Do ai đó đã gọi rồi, hoặc bệnh nhân đã hủy lịch)
                request.getSession().setAttribute("error", "Không thể gọi bệnh nhân này! Có thể họ đã được gọi hoặc đã hủy lịch hẹn.");
                response.sendRedirect(basePath + "/queue/list");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Đã xảy ra lỗi hệ thống!");
            response.sendRedirect(basePath + "/queue/list");
        }
    }

}
