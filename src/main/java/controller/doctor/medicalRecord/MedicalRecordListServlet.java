/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.doctor.medicalRecord;

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


@WebServlet(name = "MedicalRecordListServlet", urlPatterns = {"/doctor/medical-record/list", "/admin/medical-record/list"})
public class MedicalRecordListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int currentUserId = currentUser.getUserId();
        //ĐỔI LẠI THEO THỨ TỰ MÀ MN IMPORTT
        boolean isAdmin = (currentUser.getRoleId() == 1);
        
        String searchKeyword = request.getParameter("searchKeyword");
        String searchDate = request.getParameter("searchDate");

        MedicalRecordDAO dao = new MedicalRecordDAO();
        java.util.List<java.util.Map<String, Object>> recordList;

        boolean hasKeyword = searchKeyword != null && !searchKeyword.trim().isEmpty();
        boolean hasDate = searchDate != null && !searchDate.trim().isEmpty();

// 2. PHÂN LUỒNG LOGIC: Nếu có lọc thì gọi Search, nếu không thì gọi GetAll
        if (hasKeyword || hasDate) {
            recordList = dao.searchMedicalRecords(searchKeyword, searchDate, currentUserId);
        } else {
            recordList = dao.getAllMedicalRecords(currentUserId);
        }
        // BỘ NÃO PHÂN QUYỀN
        for (Map<String, Object> map : recordList) {
            boolean isOwner = (boolean) map.get("isOwner");
            boolean isPastCaregiver = (boolean) map.get("isPastCaregiver");
            boolean isSameSpecialty = (boolean) map.get("isSameSpecialty");

            boolean canView = false;
            boolean canEdit = false;

            if (isAdmin || isOwner) {
                canView = true;
                canEdit = true;
            } else if (isPastCaregiver || isSameSpecialty) {
                canView = true;
                canEdit = false;
            }

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

        request.setAttribute("recordList", recordList);
        request.setAttribute("searchKeyword", searchKeyword);

        request.setAttribute("pageTitle", "Medical Record List");
        request.setAttribute("activePage", "manageMedicalRecord");
        request.setAttribute("contentPage", "/WEB-INF/doctor/medicalrecord/medicalRecordList.jsp");

        request.getRequestDispatcher(layout).forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
