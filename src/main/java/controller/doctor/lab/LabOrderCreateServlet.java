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
@WebServlet(name = "LabOrderCreateServlet", urlPatterns = {"/doctor/lab-order/create", "/admin/lab-order/create"})
public class LabOrderCreateServlet extends HttpServlet {

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

        HttpSession session = request.getSession(false);
        model.User doctor = (model.User) session.getAttribute("user");

        // Bảo mật: Chưa đăng nhập thì đá văng ra ngoài
        if (doctor == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String appIdStr = request.getParameter("appointmentId");

        try {
            int appointmentId = Integer.parseInt(appIdStr);
            String recordIdStr = request.getParameter("medicalRecordId");
            String patientIdStr = request.getParameter("patientId");
            String[] labTestIds = request.getParameterValues("labTestIds");

            // 1. Kiểm tra an toàn: Bác sĩ chưa lưu bệnh án hoặc chưa chọn XN
            if (recordIdStr == null || recordIdStr.isEmpty() || labTestIds == null || labTestIds.length == 0) {
                session.setAttribute("error", "Vui lòng Lưu Tạm bệnh án trước và chọn ít nhất 1 dịch vụ!");
                // Quay lại đúng cái ca bệnh đang khám
                response.sendRedirect(basePath + "/medical-record/edit?appointmentId=" + appointmentId);
                return;
            }

            int medicalRecordId = Integer.parseInt(recordIdStr);
            int patientId = Integer.parseInt(patientIdStr);

            // 2. Gọi DAO tạo Lô Xét nghiệm (Hàm này chúng ta đã viết ở LabTestDAO lúc trước)
            dao.LabTestDAO labDAO = new dao.LabTestDAO();
            boolean success = labDAO.createLabOrders(patientId, medicalRecordId, doctor.getUserId(), labTestIds);

            // 3. Xử lý kết quả và thông báo
            if (success) {
                session.setAttribute("success", "Đã tạo Phiếu Chỉ Định Cận lâm sàng thành công!");
            } else {
                session.setAttribute("error", "Lỗi hệ thống khi tạo phiếu. Vui lòng thử lại!");
            }

            // 4. QUAY XE: Bắn Bác sĩ về lại đúng trang Bệnh án đang mở
            response.sendRedirect(basePath + "/medical-record/edit?appointmentId=" + appointmentId);

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Dữ liệu gửi lên không hợp lệ!");
            // Nếu lỗi nặng (VD: mất ID lịch hẹn) thì mới đá ra ngoài Hàng chờ
            response.sendRedirect(basePath + "/queue/list");
        }
    }
}
