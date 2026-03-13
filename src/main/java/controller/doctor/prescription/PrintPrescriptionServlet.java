/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.doctor.prescription;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "PrintPrescriptionServlet", urlPatterns = {"/doctor/prescription/print", "/admin/prescription/print"})
public class PrintPrescriptionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mrIdStr = request.getParameter("medicalRecordId");

        if (mrIdStr == null || mrIdStr.trim().isEmpty()) {
            response.getWriter().print("<h3>Không tìm thấy Mã Bệnh Án để in đơn thuốc!</h3>");
            return;
        }

        try {
            int medicalRecordId = Integer.parseInt(mrIdStr);

            // 1. Gọi DAO lấy thông tin Bệnh nhân & Chẩn đoán
            dao.MedicalRecordDAO mrDAO = new dao.MedicalRecordDAO();
            java.util.Map<String, String> info = mrDAO.getPrescriptionPrintInfo(medicalRecordId);

            // 2. Gọi DAO lấy danh sách các loại thuốc đã kê
            dao.MedicineDAO medDAO = new dao.MedicineDAO();
            java.util.List<model.PrescriptionItem> prescriptionList = medDAO.getPrescriptionItems(medicalRecordId);

            // 3. Đẩy toàn bộ dữ liệu ra Giao diện A4
            request.setAttribute("medicalRecordId", medicalRecordId);
            request.setAttribute("info", info);
            request.setAttribute("prescriptionList", prescriptionList);
            request.setAttribute("currentDate", new java.util.Date()); // Ngày giờ in hiện tại

            // 4. Chuyển hướng ra trang JSP mẫu giấy A4
            request.getRequestDispatcher("/WEB-INF/doctor/prescription/printPrescription.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("<h3>Lỗi hệ thống khi tải dữ liệu Đơn thuốc!</h3>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
