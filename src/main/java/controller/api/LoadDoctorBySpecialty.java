/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.api;

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
import com.google.gson.Gson;

/**
 *
 * @author Chi Duong
 */
@WebServlet(name = "LoadDoctorBySpecialty", urlPatterns = {"/api/doctors-by-specialty"})
public class LoadDoctorBySpecialty extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int specialtyId = Integer.parseInt(request.getParameter("specialtyId"));
        List<User> doctors = userDAO.getDoctorsBySpecialty(specialtyId);

        response.setContentType("application/json");
        new Gson().toJson(doctors, response.getWriter());
    }

}
