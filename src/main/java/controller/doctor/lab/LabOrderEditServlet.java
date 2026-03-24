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
 * @author Gia Huy
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

        // Bảo mật
        if (doctor == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String appIdStr = request.getParameter("appointmentId");

        try {
            int appointmentId = Integer.parseInt(appIdStr);
            int medicalRecordId = Integer.parseInt(request.getParameter("medicalRecordId"));
            int patientId = Integer.parseInt(request.getParameter("patientId"));
            int batchId = Integer.parseInt(request.getParameter("batchId")); //  Lấy thêm BatchId để biết đang sửa Lô nào

            // Lấy danh sách các dịch vụ Bác sĩ mới tick (Có thể bị NULL nếu bác sĩ gỡ tick hết)
            String[] labTestIds = request.getParameterValues("labTestIds");

            model.LabTestBatch batchUpdate = new model.LabTestBatch();
            batchUpdate.setBatchId(batchId); // Nhét BatchId vào để biết sửa Lô nào
            batchUpdate.setMedicalRecordId(medicalRecordId);
            batchUpdate.setPatientId(patientId);
            batchUpdate.setCreatedByDoctorId(doctor.getUserId());

            java.util.List<Integer> testIdList = new java.util.ArrayList<>();
            if (labTestIds != null) {
                for (String idStr : labTestIds) {
                    testIdList.add(Integer.parseInt(idStr));
                }
            }
            // Set List vào Model (Nếu bác sĩ gỡ tick hết thì list này empty)
            batchUpdate.setTestIds(testIdList);

            // Gọi DAO xử lý logic Thêm/Bớt
            dao.LabTestDAO labDAO = new dao.LabTestDAO();

            boolean success = labDAO.editLabOrders(batchUpdate);
            // Thông báo kết quả
            if (success) {
                if (labTestIds == null || labTestIds.length == 0) {
                    session.setAttribute("success", "Cancel lab test batch successful!");
                } else {
                    session.setAttribute("success", "Update lab test batch successful!");
                }
            } else {
                session.setAttribute("error", "Error update lab test batch!");
            }

            response.sendRedirect(basePath + "/medical-record/edit?appointmentId=" + appointmentId);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Invalid input!");
            if (appIdStr != null && !appIdStr.isEmpty()) {
                response.sendRedirect(basePath + "/medical-record/edit?appointmentId=" + appIdStr);
            } else {
                response.sendRedirect(basePath + "/queue/list");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "System error!");
            response.sendRedirect(basePath + "/queue/list");
        }
    }
}
