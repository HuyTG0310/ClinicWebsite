/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lab.test;

import dao.LabTestDAO;
import dao.UserDAO;
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
@WebServlet(name = "PrintLabResultServlet", urlPatterns = {"/lab/test/print", "/doctor/test/print", "/admin/test/print"})
public class PrintLabResultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int mrId = Integer.parseInt(request.getParameter("mrId"));

            // 1. Lấy thông tin Hành chính của Bệnh án (Tên, tuổi, chẩn đoán, bác sĩ chỉ định...)
            // Lưu ý: Đổi tên hàm getRecordById sao cho khớp với hàm trong MedicalRecordDAO của bạn nhé
            dao.MedicalRecordDAO mrDao = new dao.MedicalRecordDAO();
            model.MedicalRecord mr = mrDao.getRecordById(mrId);

            if (mr == null) {
                response.getWriter().print("Medical record information not found!");
                return;
            }
            request.setAttribute("mr", mr);

            // 2. Lấy Danh sách Kết quả Xét nghiệm Tổng hợp (Tận dụng lại đúng hàm của Bác sĩ)
            dao.LabTestDAO labDao = new dao.LabTestDAO();
            List<Map<String, Object>> results = labDao.getConsolidatedLabResults(mrId);
            request.setAttribute("results", results);
                        request.setAttribute("inChargeLab", new UserDAO().getUserById(new LabTestDAO().getInChargeLabTechinicianId(mrId)).getFullName());
            // Đẩy ra trang JSP In Ấn
            request.getRequestDispatcher("/WEB-INF/lab/printLabResult.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("Lỗi hệ thống khi tải bản in!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
