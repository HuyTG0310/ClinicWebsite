/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lab.test;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import model.TestResult;

/**
 *
 * @author Gia Huy
 */
@WebServlet(name = "LabQueueServlet", urlPatterns = {"/lab/lab-queue/list", "/admin/lab-queue/list"})
public class TestResultListServlet extends HttpServlet {

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
        } else if (uri.startsWith(ctx + "/lab")) {
            layout = "/WEB-INF/layout/labLayout.jsp";
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);

        String search = request.getParameter("search");
        String status = request.getParameter("status");

        if (status == null) {
            status = "PENDING";
        }

        LabTestDAO dao = new LabTestDAO();
        java.util.List<model.TestResult> queue;

        // Phân luồng logic gọi hàm DAO
        if (search != null && !search.trim().isEmpty()) {
            queue = dao.searchTestResults(search, status);
        } else {
            queue = dao.getAllTestResults(status);
        }

        request.setAttribute("queue", queue);

        request.setAttribute("pageTitle", "Manage Test");
        request.setAttribute("activePage", "manageTest");
        request.setAttribute("contentPage", "/WEB-INF/lab/queue.jsp");
        request.getRequestDispatcher(layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
