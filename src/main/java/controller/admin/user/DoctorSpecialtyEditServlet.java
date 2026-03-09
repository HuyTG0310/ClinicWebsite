/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.user;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;



/**
 *
 * @author huytr
 */

@WebServlet(name = "DoctorSpecialtyEditServlet", urlPatterns = {"/admin/doctor-specialty/edit"})
public class DoctorSpecialtyEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
    private DoctorSpecialtyDAO doctorSpecialtyDAO = new DoctorSpecialtyDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String doctorIdRaw = request.getParameter("doctorId");
        String[] specialtyIdsRaw = request.getParameterValues("specialtyIds");
        String primaryRaw = request.getParameter("primarySpecialtyId");

        int doctorId;

        try {
            doctorId = Integer.parseInt(doctorIdRaw);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/user/list");
            return;
        }


        User doctor = userDAO.getUserById(doctorId);
        if (doctor == null || !"Doctor".equals(doctor.getRoleName())) {
            response.sendRedirect(request.getContextPath() + "/admin/user/detail?id=" + doctorId);
            return;
        }


        if (specialtyIdsRaw == null || specialtyIdsRaw.length == 0) {
            redirectWithError(response, request, doctorId, "Pháº£i chá»n Ã­t nháº¥t má»™t chuyÃªn khoa.");
            return;
        }


        if (primaryRaw == null || primaryRaw.isEmpty()) {
            redirectWithError(response, request, doctorId, "Pháº£i chá»n má»™t chuyÃªn khoa chÃ­nh.");
            return;
        }

        int primaryId;
        try {
            primaryId = Integer.parseInt(primaryRaw);
        } catch (Exception e) {
            redirectWithError(response, request, doctorId, "ChuyÃªn khoa chÃ­nh khÃ´ng há»£p lá»‡.");
            return;
        }

        boolean primaryValid = false;
        for (String sid : specialtyIdsRaw) {
            if (Integer.parseInt(sid) == primaryId) {
                primaryValid = true;
                break;
            }
        }

        if (!primaryValid) {
            redirectWithError(response, request, doctorId, "ChuyÃªn khoa chÃ­nh pháº£i náº±m trong danh sÃ¡ch Ä‘Ã£ chá»n.");
            return;
        }


        doctorSpecialtyDAO.deleteByDoctor(doctorId);

        for (String sid : specialtyIdsRaw) {
            int specialtyId = Integer.parseInt(sid);
            boolean isPrimary = (specialtyId == primaryId);
            doctorSpecialtyDAO.insert(doctorId, specialtyId, isPrimary);
        }


        response.sendRedirect(request.getContextPath() + "/admin/user/detail?id=" + doctorId
        );
    }

    private void redirectWithError(HttpServletResponse response,
            HttpServletRequest request,
            int doctorId,
            String message) throws IOException {

        response.sendRedirect(
                request.getContextPath()
                + "/admin/user/detail?id=" + doctorId
                + "&activeTab=professional"
                + "&specError=" + java.net.URLEncoder.encode(message, "UTF-8")
        );
    }
}
