/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.api;

import com.google.gson.Gson;
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
 * @author Tan Vinh
 */
@WebServlet(name = "SpecialtyStatusApi", urlPatterns = {"/api/specialty-status"})
public class SpecialtyStatusApi extends HttpServlet {

    private SpecialtyDAO specialtyDAO = new SpecialtyDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idRaw = request.getParameter("id");
        if (idRaw == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int specialtyId = Integer.parseInt(idRaw);
        Specialty s = specialtyDAO.getById(specialtyId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (s == null) {
            response.getWriter().write("{}");
            return;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("isActive", s.isIsActive());

        response.getWriter().write(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
