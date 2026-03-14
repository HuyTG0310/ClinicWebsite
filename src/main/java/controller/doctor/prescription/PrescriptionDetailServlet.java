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
 * @author huytr
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
            // 1. Lấy ID của Bệnh án từ URL (ví dụ: ?id=105)
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
                // Có Bác sĩ nào cố tình gõ URL để xem lén -> Đá văng ra ngoài!
                request.getSession().setAttribute("error", "You have no permission to view this prescription!");
                response.sendRedirect(basePath + "/prescription/list");
                return;
            }

            // 3. Lấy 3 luồng dữ liệu thần thánh
            Map<String, Object> recordInfo = pDao.getMedicalRecordDetail(medicalRecordId);
            List<Map<String, Object>> prescribedMedicines = pDao.getPrescribedMedicines(medicalRecordId);
            List<Medicine> allMedicines = mDao.getAllActiveMedicines(); // HÀM CŨ CỦA BẠN ĐÂY!

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
            PrescriptionDAO dao = new PrescriptionDAO();

            // 2. Gọi hàm lưu vào Database
            boolean success = dao.savePrescription(medicalRecordId, medicineIds, quantities, dosages, notes);

            if (success) {
                request.getSession().setAttribute("success", "Update prescription successfully!");
            } else {
                request.getSession().setAttribute("error", "Update error.");
            }

            // 3. Quay trở lại trang chi tiết của chính hồ sơ đó
            response.sendRedirect(basePath + "/prescription/detail?id=" + medicalRecordId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(basePath + "/prescription/list?error=UpdateFailed");
        }

    }

}
