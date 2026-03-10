/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.doctor.medicalRecord;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

@WebServlet(name = "MedicalRecordDetailServlet", urlPatterns = {"/doctor/medical-record/detail", "/admin/medical-record/detail"})
public class MedicalRecordDetailServlet extends HttpServlet {

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

        request.setAttribute("basePath", basePath);
        try {
            // 1. Nhận cả 2 loại ID (Chỉ 1 trong 2 sẽ có giá trị)
            String mrIdStr = request.getParameter("id"); // Từ trang List mới làm
            String appIdStr = request.getParameter("appointmentId"); // Từ trang Queue cũ

            MedicalRecordDAO mrDAO = new MedicalRecordDAO();
            MedicalRecord mr = null;

            // 2. Tìm bệnh án dựa theo ID được truyền vào
            if (mrIdStr != null && !mrIdStr.isEmpty()) {
                int mrId = Integer.parseInt(mrIdStr);
                mr = mrDAO.getRecordById(mrId); // Gọi theo MedicalRecordId
            } else if (appIdStr != null && !appIdStr.isEmpty()) {
                int appId = Integer.parseInt(appIdStr);
                mr = mrDAO.getRecordByAppointment(appId); // Gọi theo AppointmentId
            }

            if (mr == null) {
                request.getSession().setAttribute("error", "Không tìm thấy Bệnh án!");
                response.sendRedirect(request.getContextPath() + "/medical-record/list");
                return;
            }

            // ==========================================
            // 3. BỨC TƯỜNG LỬA BẢO MẬT (PHÂN QUYỀN)
            // ==========================================
            model.User currentUser = (model.User) request.getSession().getAttribute("user");
            int currentUserId = currentUser.getUserId();
            boolean isAdmin = (currentUser.getRoleId() == 1);

            // Tái sử dụng lại hàm check quyền đã làm ở DAO (Bạn có thể bỏ hàm này trong MedicalRecordDAO cho chuẩn)
            boolean[] perms = mrDAO.checkPermissionDetail(mr.getMedicalRecordId(), currentUserId, isAdmin);
            boolean canView = perms[0];
            boolean canEdit = perms[1];

            if (!canView) {
                // Ai đó cố tình gõ URL lụi -> Đá văng ra ngoài!
                request.getSession().setAttribute("error", "⛔ BẢO MẬT: Bạn không có quyền xem bệnh án này!");
                response.sendRedirect(request.getContextPath() + "/medical-record/list");
                return;
            }

            // Truyền cờ canEdit sang JSP để quyết định ẨN/HIỆN nút "Sửa bệnh án"
            request.setAttribute("canEdit", canEdit);
            // ==========================================

            // 4. Lấy danh sách kết quả Xét nghiệm (Như code cũ của bạn)
            dao.LabTestDAO labDao = new dao.LabTestDAO();
            java.util.List<java.util.Map<String, Object>> consolidatedResults = labDao.getConsolidatedLabResults(mr.getMedicalRecordId());

            request.setAttribute("consolidatedResults", consolidatedResults);
            request.setAttribute("mr", mr);

            // Giữ nguyên các attribute template của bạn
            request.setAttribute("pageTitle", "Medical Record Detail");
            request.setAttribute("activePage", "manageMedicalRecord");
            request.setAttribute("contentPage", "/WEB-INF/doctor/medicalrecord/medicalRecordDetail.jsp");

            request.getRequestDispatcher(layout).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/medical-record/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
