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

/**
 *
 * @author Gia Huy
 */
@WebServlet(name = "PrintLabOrderServlet", urlPatterns = {"/doctor/lab-order/print", "/admin/lab-order/print"})
public class PrintLabOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String batchIdStr = request.getParameter("batchId");
        if (batchIdStr == null || batchIdStr.isEmpty()) {
            response.getWriter().print("Không tìm thấy mã phiếu chỉ định!");
            return;
        }

        try {
            int batchId = Integer.parseInt(batchIdStr);

            dao.LabTestDAO testDAO = new dao.LabTestDAO();
            dao.AppointmentDAO appDAO = new dao.AppointmentDAO();

            java.util.List<model.LabTest> printList = testDAO.getLabTestsByBatchId(batchId);

            double totalAmount = 0;
            for (model.LabTest t : printList) {
                totalAmount += t.getCurrentPrice();
            }

            // Gửi dữ liệu ra Giao diện In
            request.setAttribute("batchId", batchId);
            request.setAttribute("printList", printList);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("currentDate", new java.util.Date());
            java.util.Map<String, String> info = testDAO.getPrintInvoiceInfo(batchId);
            request.setAttribute("info", info);
            request.getRequestDispatcher("/WEB-INF/doctor/medicalrecord/printLabOrder.jsp").forward(request, response);

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
