/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.doctor.prescription;

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
@WebServlet(name = "PrintPrescriptionServlet", urlPatterns = {"/doctor/prescription/print", "/admin/prescription/print"})
public class PrintPrescriptionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

        String mrIdStr = request.getParameter("medicalRecordId");

        if (mrIdStr == null || mrIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Prescription not found");
            response.sendRedirect(basePath + "/prescription/list");
            return;
        }

        try {
            int medicalRecordId = Integer.parseInt(mrIdStr);
            PrescriptionDAO pDao = new PrescriptionDAO();
            if (!pDao.isExistPrescription(medicalRecordId)) {
                request.getSession().setAttribute("error", "Prescription not found");
                response.sendRedirect(basePath + "/prescription/list");
                return;
            }

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
            request.getSession().setAttribute("error", "Prescription not found");
            response.sendRedirect(basePath + "/prescription/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
