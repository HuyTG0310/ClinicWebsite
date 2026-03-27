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
import java.util.*;
import model.*;

/**
 *
 * @author Truong Thinh
 */
@WebServlet(name = "PrescriptionDetailServlet", urlPatterns = {"/doctor/prescription/detail", "/admin/prescription/detail"})
public class PrescriptionDetailServlet extends HttpServlet {

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

        model.User currentUser = (model.User) request.getSession().getAttribute("user");
        int currentUserId = currentUser.getUserId();

        //ĐỔI ROLE ID CHO ADMIN TÙY THỨ TỰ IMPORT CỦA MN
        boolean isAdmin = (currentUser.getRoleId() == 1);

        try {
            // 1. Lấy ID của Bệnh án từ URL
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.sendRedirect(basePath + "/prescription/list");
                return;
            }
            int medicalRecordId = Integer.parseInt(idParam);

            // 2. Khởi tạo các DAO
            MedicalRecordDAO meDao = new MedicalRecordDAO();
            PrescriptionDAO pDao = new PrescriptionDAO();
            MedicineDAO mDao = new MedicineDAO();

            if (!pDao.isExistPrescription(medicalRecordId)) {
                request.getSession().setAttribute("error", "Prescription not found");
                response.sendRedirect(basePath + "/prescription/list");
                return;
            }

            boolean[] perms = meDao.checkPermissionDetail(medicalRecordId, currentUserId, isAdmin);
            boolean canView = perms[0];
            boolean canEdit = perms[1];

            if (!canView) {
                request.getSession().setAttribute("error", "You have no permission to view this prescription!");
                response.sendRedirect(basePath + "/prescription/list");
                return;
            }

            Map<String, Object> recordInfo = pDao.getMedicalRecordDetail(medicalRecordId);
            List<Map<String, Object>> prescribedMedicines = pDao.getPrescribedMedicines(medicalRecordId);
            List<Medicine> allMedicines = mDao.getAllActiveMedicines();

            request.setAttribute("basePath", basePath);
            request.setAttribute("record", recordInfo);
            request.setAttribute("prescribedList", prescribedMedicines);
            request.setAttribute("allMedicines", allMedicines);
            request.setAttribute("canEdit", canEdit);

            // 5. Chuyển hướng sang giao diện
            request.setAttribute("pageTitle", "Prescription detail");
            request.setAttribute("activePage", "managePrescription");
            request.setAttribute("contentPage", "/WEB-INF/doctor/prescription/prescription-detail.jsp");

            request.getRequestDispatcher(layout).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(basePath + "/prescription/list?error=InvalidRequest");
        }
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

        // 1. Đọc dữ liệu từ Form (Các mảng dữ liệu từ các ô input trùng tên)
        String mrIdStr = request.getParameter("medicalRecordId");
        String[] medicineIds = request.getParameterValues("medicineIds");
        String[] quantities = request.getParameterValues("quantities");
        String[] dosages = request.getParameterValues("dosages");
        String[] notes = request.getParameterValues("notes");

        try {
            int medicalRecordId = Integer.parseInt(mrIdStr);

            // ĐÓNG GÓI DỮ LIỆU THÀNH MODEL
            java.util.List<model.Prescription> prescriptionList = new java.util.ArrayList<>();

            if (medicineIds != null && medicineIds.length > 0) {
                for (int i = 0; i < medicineIds.length; i++) {
                    if (medicineIds[i] == null || medicineIds[i].trim().isEmpty()) {
                        continue; // Bỏ qua nếu dòng đó bị trống
                    }

                    model.Prescription p = new model.Prescription();
                    p.setMedicalRecordId(medicalRecordId);
                    p.setMedicineId(Integer.parseInt(medicineIds[i]));
                    p.setQuantity(Integer.parseInt(quantities[i]));
                    p.setDosage(dosages[i]);
                    // Tránh lỗi NullPointerException nếu note bị null
                    p.setNote(notes != null && notes.length > i ? notes[i] : "");

                    prescriptionList.add(p);
                }
            }

            PrescriptionDAO dao = new PrescriptionDAO();

            // 2. Gọi hàm lưu vào Database (Chỉ truyền ID và cái List)
            boolean success = dao.savePrescription(medicalRecordId, prescriptionList);

            if (success) {
                request.getSession().setAttribute("success", "Update prescription successfully!");
            } else {
                request.getSession().setAttribute("error", "Update error.");
            }

            // 3. Quay trở lại trang chi tiết
            response.sendRedirect(basePath + "/prescription/detail?id=" + medicalRecordId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(basePath + "/prescription/list?error=UpdateFailed");
        }

    }

}
