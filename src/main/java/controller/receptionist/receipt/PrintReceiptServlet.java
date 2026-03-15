/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.receptionist.receipt;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import model.*;

@WebServlet(name = "PrintReceiptServlet", urlPatterns = {"/receptionist/receipt/print", "/doctor/receipt/print", "/admin/receipt/print"})
public class PrintReceiptServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String mrIdRaw = request.getParameter("mrId");
            String soIdRaw = request.getParameter("soId");
            String patientIdRaw = request.getParameter("patientId");
            String timeStr = request.getParameter("time");
            String appIdRaw = request.getParameter("appId");
            int appId = (appIdRaw != null && !appIdRaw.isEmpty()) ? Integer.parseInt(appIdRaw) : 0;

            int mrId = (mrIdRaw != null && !mrIdRaw.isEmpty()) ? Integer.parseInt(mrIdRaw) : 0;
            int soId = (soIdRaw != null && !soIdRaw.isEmpty()) ? Integer.parseInt(soIdRaw) : 0;
            int patientId = (patientIdRaw != null && !patientIdRaw.isEmpty()) ? Integer.parseInt(patientIdRaw) : 0;

            String formattedTime = "";
            if (timeStr != null && !timeStr.trim().isEmpty()) {
                long timeInMillis = Long.parseLong(timeStr);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formattedTime = sdf.format(new java.util.Date(timeInMillis));
            }

            dao.ServiceOrderDAO soDao = new dao.ServiceOrderDAO();
            dao.PatientDAO patientDao = new dao.PatientDAO();

            if (mrId > 0) {
                List<Map<String, Object>> details = soDao.getServiceDetailsByMrId(mrId, patientId, "PAID", formattedTime);

                double totalAmount = 0;
                for (Map<String, Object> item : details) {
                    totalAmount += (Double) item.get("price");
                }

                String amountInWords = util.MoneyConvertUtil.convertToVietnameseWords((long) totalAmount);

                request.setAttribute("details", details);
                request.setAttribute("totalAmount", totalAmount);
                request.setAttribute("amountInWords", amountInWords);
                request.setAttribute("patient", patientDao.getPatientById(patientId));
                request.setAttribute("mrId", mrId);

                if (!details.isEmpty()) {
                    request.setAttribute("paidAt", details.get(0).get("paidAt"));
                    request.setAttribute("cashierName", details.get(0).get("cashierName"));
                    request.setAttribute("paymentMethod", details.get(0).get("paymentMethod"));
                }

                request.getRequestDispatcher("/WEB-INF/receptionist/serviceorder/printLabReceipt.jsp").forward(request, response);

            } else if (soId > 0) {
                model.ServiceOrder order = soDao.getServiceOrderById(soId);

                if (order != null) {
                    dao.AppointmentDAO appDao = new dao.AppointmentDAO();
                    model.Appointment app = appDao.getAppointmentDetailById(order.getAppointmentId());

                    if (app != null) {
                        request.setAttribute("app", app); // Đóng gói cục app
                        String amountInWords = util.MoneyConvertUtil.convertToVietnameseWords((long) app.getPrice());
                        request.setAttribute("amountInWords", amountInWords);
                        request.getRequestDispatcher("/WEB-INF/receptionist/serviceorder/printExamReceipt.jsp").forward(request, response);
                    } else {
                        response.getWriter().print("Không tìm thấy thông tin Lịch Khám!");
                    }
                } else {
                    response.getWriter().print("Không tìm thấy hóa đơn Khám bệnh!");
                }
            } else if (appId > 0) {
                dao.AppointmentDAO appDao = new AppointmentDAO();
                Appointment app = appDao.getAppointmentDetailById(appId);

                if (app != null) {
                    request.setAttribute("app", app);
                    String amountInWords = util.MoneyConvertUtil.convertToVietnameseWords((long) app.getPrice());
                    request.setAttribute("amountInWords", amountInWords);
                    request.getRequestDispatcher("/WEB-INF/receptionist/serviceorder/printExamReceipt.jsp").forward(request, response);
                } else {
                    response.getWriter().print("Không tìm thấy thông tin Lịch Khám!");
                }
            } else {
                response.getWriter().print("Lỗi: Không xác định được loại biên lai!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("Lỗi tải biên lai: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
