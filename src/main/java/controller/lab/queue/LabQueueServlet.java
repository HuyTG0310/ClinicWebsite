/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lab.queue;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *
 * @author huytr
 */
@WebServlet(name = "LabQueueServlet", urlPatterns = {"/lab/queue/list"})
public class LabQueueServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("search");
        String status = request.getParameter("status");

        if (status == null) {
            status = "PENDING";
        }

        LabTestDAO dao = new LabTestDAO();

        List<Map<String, Object>> queue = dao.getLabQueue(search, status);

        request.setAttribute("queue", queue);

        request.setAttribute("pageTitle", "Manage Test");
        request.setAttribute("activePage", "manageTest");
        request.setAttribute("contentPage", "/WEB-INF/lab/queue.jsp");
        request.getRequestDispatcher("/WEB-INF/layout/labLayout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
