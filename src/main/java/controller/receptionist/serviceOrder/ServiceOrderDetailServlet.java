/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.receptionist.serviceOrder;

import dao.PatientDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *
 * @author huytr
 */
@WebServlet(name = "ServiceOrderDetailServlet", urlPatterns = {"/receptionist/service-order/detail", "/doctor/service-order/detail", "/admin/service-order/detail", "/lab/service-order/detail"})
public class ServiceOrderDetailServlet extends HttpServlet {

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

        try {
            // 1. Lấy tham số (Bắt lỗi an toàn vì có thể 1 trong 2 cái bị null)
            String mrIdRaw = request.getParameter("mrId");
            String soIdRaw = request.getParameter("soId");

            int mrId = (mrIdRaw != null && !mrIdRaw.isEmpty()) ? Integer.parseInt(mrIdRaw) : 0;
            int soId = (soIdRaw != null && !soIdRaw.isEmpty()) ? Integer.parseInt(soIdRaw) : 0;

            String patientIdRaw = request.getParameter("patientId");
            int patientId = (patientIdRaw != null && !patientIdRaw.isEmpty()) ? Integer.parseInt(patientIdRaw) : 0;

            String status = request.getParameter("status");
            if (status == null || status.isEmpty()) {
                status = "UNPAID"; // Backup an toàn
            }

            String timeStr = request.getParameter("time");
            String formattedTime = "";
            if (timeStr != null && !timeStr.trim().isEmpty()) {
                long timeInMillis = Long.parseLong(timeStr);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formattedTime = sdf.format(new java.util.Date(timeInMillis));
            }

            // Các DAO cần thiết
            dao.ServiceOrderDAO dao = new dao.ServiceOrderDAO();
            dao.PatientDAO patientDao = new dao.PatientDAO();

            List<Map<String, Object>> details = new java.util.ArrayList<>();
            model.Patient patientInfo = null;

            // Các biến dành riêng cho Hóa đơn đã thanh toán
            java.util.Date paidAt = null;
            String cashierName = "Hệ thống";

            // ==============================================================
            // 2. PHÂN NHÁNH LOGIC TÌM KIẾM THEO LOẠI HÓA ĐƠN
            // ==============================================================
            if (mrId > 0) {
                // NHÁNH 1: THANH TOÁN CHÙM XÉT NGHIỆM (Đã có Bệnh án)
                details = dao.getServiceDetailsByMrId(mrId, patientId, status, formattedTime);
                patientInfo = patientDao.getPatientById(patientId); // Lấy FULL thông tin Bệnh nhân

                // Trích xuất Giờ thu & Người thu từ hàm DAO đã được nâng cấp
                if (!details.isEmpty() && ("PAID".equals(status) || "REFUNDED".equals(status))) {
                    if (details.get(0).get("paidAt") != null) {
                        paidAt = (java.util.Date) details.get(0).get("paidAt");
                    }
                    if (details.get(0).get("cashierName") != null) {
                        cashierName = (String) details.get(0).get("cashierName");
                    }
                }

            } else if (soId > 0) {
                // NHÁNH 2: THANH TOÁN PHÍ KHÁM BAN ĐẦU (Chưa có Bệnh án)
                model.ServiceOrder order = dao.getServiceOrderById(soId);
                if (order != null) {
                    Map<String, Object> item = new java.util.HashMap<>();
                    item.put("serviceName", "Khám bệnh lâm sàng");
                    item.put("price", order.getPriceAtTime());
                    details.add(item);

                    patientId = order.getPatientId();
                    patientInfo = patientDao.getPatientById(patientId); // Lấy FULL thông tin Bệnh nhân

                    // Nếu đã thanh toán, móc thông tin Giờ thu & Người thu
                    if ("PAID".equals(status) || "REFUNDED".equals(status)) {
                        paidAt = order.getPaidAt();
                        // Tận dụng hàm getAppointmentDetailById siêu xịn của bạn để lôi tên Lễ tân ra
                        dao.AppointmentDAO appDao = new dao.AppointmentDAO();
                        model.Appointment app = appDao.getAppointmentDetailById(order.getAppointmentId());
                        if (app != null && app.getReceptionistName() != null) {
                            cashierName = app.getReceptionistName();
                        }
                    }
                }
            }

            // 3. Tính tổng tiền ngay trên Server cho an toàn
            double totalAmount = 0;
            for (Map<String, Object> item : details) {
                totalAmount += (Double) item.get("price");
            }

            // 4. Đóng gói toàn bộ dữ liệu (ĐÃ BỔ SUNG ĐẦY ĐỦ) ném sang JSP
            request.setAttribute("details", details);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("mrId", mrId);
            request.setAttribute("soId", soId);
            request.setAttribute("currentStatus", status);

            // Thông tin chi tiết bệnh nhân
            if (patientInfo != null) {
                request.setAttribute("patientId", patientInfo.getPatientId());
                request.setAttribute("patientName", patientInfo.getFullName());
                request.setAttribute("patientDob", patientInfo.getDateOfBirth());
                request.setAttribute("patientGender", patientInfo.getGender());
                request.setAttribute("patientPhone", patientInfo.getPhone());
                request.setAttribute("patientAddress", patientInfo.getAddress());
            }

            // Thông tin giao dịch (Giờ thu, Thu ngân)
            if ("PAID".equals(status) || "CANCELLED".equals(status)) {
                request.setAttribute("paidAt", paidAt);
                request.setAttribute("cashierName", cashierName);
            }

            // 5. Chuyển hướng sang trang Giao diện Chi tiết
            request.setAttribute("pageTitle", "Chi tiết khoản thu");
            request.setAttribute("activePage", "manageServiceOrder");
            request.setAttribute("contentPage", "/WEB-INF/receptionist/serviceorder/billingDetail.jsp");

            request.getRequestDispatcher(layout).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Không thể tải chi tiết hóa đơn!");
            response.sendRedirect(basePath + "/service-order/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
