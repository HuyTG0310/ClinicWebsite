/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.doctor.prescription;

import dao.PrescriptionDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import model.User;

/**
 *
 * @author Truong Thinh
 */
@WebServlet(name = "PrescriptionListServlet", urlPatterns = {"/doctor/prescription/list", "/admin/prescription/list"})
public class PrescriptionListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy thông tin User đang đăng nhập từ Session
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        // Chặn nếu chưa đăng nhập 
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int currentUserId = currentUser.getUserId();
        // ĐỔI LẠI ROLE ID TÙY THỨ TỰ IMPORT TRONG DB CỦA MN
        boolean isAdmin = (currentUser.getRoleId() == 1);
        
        // 2. Nhận tham số tìm kiếm từ JSP
        String searchKeyword = request.getParameter("searchKeyword");
        String searchDate = request.getParameter("searchDate"); // 🔥 Hứng thêm Ngày

        PrescriptionDAO dao = new PrescriptionDAO();
        List<Map<String, Object>> rawList;

        boolean hasKeyword = searchKeyword != null && !searchKeyword.trim().isEmpty();
        boolean hasDate = searchDate != null && !searchDate.trim().isEmpty();

        if (hasKeyword || hasDate) {
            rawList = dao.searchPrescriptions(searchKeyword, searchDate, currentUserId);
        } else {
            rawList = dao.getAllPrescriptions(currentUserId);
        }

        for (Map<String, Object> map : rawList) {

            boolean isOwner = (boolean) map.get("isOwner");
            boolean isPastCaregiver = (boolean) map.get("isPastCaregiver");
            boolean isSameSpecialty = (boolean) map.get("isSameSpecialty");

            boolean canView = false;
            boolean canEdit = false;

            // Áp dụng phân quyền:
            if (isAdmin || isOwner) {
                // Admin HOẶC Người trực tiếp kê đơn -> Toàn quyền!
                canView = true;
                canEdit = true;
            } else if (isPastCaregiver || isSameSpecialty) {
                // Từng khám cho BN này HOẶC Chung chuyên khoa -> Chỉ cho Xem
                canView = true;
                canEdit = false;
            }
            // Các trường hợp còn lại (Khác khoa + Không liên quan) -> (false)

            // Gắn quyền đã chốt vào lại map để đưa sang JSP
            map.put("canView", canView);
            map.put("canEdit", canEdit);
        }

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


        request.setAttribute("prescriptionList", rawList);
        request.setAttribute("searchKeyword", searchKeyword);


        request.setAttribute("pageTitle", "Manage prescription");
        request.setAttribute("activePage", "managePrescription");
        request.setAttribute("contentPage", "/WEB-INF/doctor/prescription/prescription-list.jsp");

        request.getRequestDispatcher(layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
