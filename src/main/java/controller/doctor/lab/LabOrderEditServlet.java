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
@WebServlet(name = "LabOrderEditServlet", urlPatterns = {"/doctor/lab-order/edit", "/admin/lab-order/edit"})
public class LabOrderEditServlet extends HttpServlet {

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

        // Bảo mật: Trục xuất nếu chưa đăng nhập
        if (doctor == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String appIdStr = request.getParameter("appointmentId");

        try {
            int appointmentId = Integer.parseInt(appIdStr);
            int medicalRecordId = Integer.parseInt(request.getParameter("medicalRecordId"));
            int patientId = Integer.parseInt(request.getParameter("patientId"));
            int batchId = Integer.parseInt(request.getParameter("batchId")); // 🔥 Lấy thêm BatchId để biết đang sửa Lô nào

            // Lấy danh sách các dịch vụ Bác sĩ mới tick (Có thể bị NULL nếu bác sĩ gỡ tick hết)
            String[] labTestIds = request.getParameterValues("labTestIds");

            // Gọi DAO xử lý logic Thêm/Bớt siêu phức tạp
            dao.LabTestDAO labDAO = new dao.LabTestDAO();
            boolean success = labDAO.editLabOrders(batchId, patientId, medicalRecordId, doctor.getUserId(), labTestIds);

            // Thông báo kết quả
            if (success) {
                if (labTestIds == null || labTestIds.length == 0) {
                    session.setAttribute("success", "Đã gỡ toàn bộ chỉ định. Lô xét nghiệm đã tự động được hủy!");
                } else {
                    session.setAttribute("success", "Cập nhật Lô xét nghiệm thành công! Hóa đơn đã được điều chỉnh.");
                }
            } else {
                session.setAttribute("error", "Không thể cập nhật! Lô này có thể đã được thu tiền hoặc đang xử lý.");
            }

            // Quay xe về lại đúng trang Bệnh án
            response.sendRedirect(basePath + "/medical-record/edit?appointmentId=" + appointmentId);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Dữ liệu gửi lên không hợp lệ!");
            if (appIdStr != null && !appIdStr.isEmpty()) {
                response.sendRedirect(basePath + "/medical-record/edit?appointmentId=" + appIdStr);
            } else {
                response.sendRedirect(basePath + "/queue/list");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi hệ thống khi cập nhật phiếu!");
            response.sendRedirect(basePath + "/queue/list");
        }
    }
}
