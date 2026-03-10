/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.doctor.lab;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author huytr
 */
@WebServlet(name = "LabOrderCancelServlet", urlPatterns = {"/doctor/lab-order/cancel", "/admin/lab-order/cancel"})
public class LabOrderCancelServlet extends HttpServlet {

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

        HttpSession session = request.getSession();
        // Redirect về lại bệnh án
        String appointmentId = request.getParameter("appointmentId");

        try {
            int batchId = Integer.parseInt(request.getParameter("batchId"));

            dao.LabTestDAO dao = new dao.LabTestDAO();
            boolean success = dao.cancelLabBatch(batchId);

            if (success) {
                session.setAttribute("success", "Đã hủy Lô xét nghiệm thành công! Hóa đơn đã được thu hồi.");
            } else {
                session.setAttribute("error", "Không thể hủy! Lô này đã được thanh toán hoặc đang xử lý.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Dữ liệu không hợp lệ.");
        }

        response.sendRedirect(basePath + "/medical-record/edit?appointmentId=" + appointmentId);
    }
}
