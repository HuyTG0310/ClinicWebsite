/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.certification;

import dao.CertificationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Tai Loi
 */
@WebServlet("/admin/certification/list")
public class CertificationApprovalServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        CertificationDAO dao = new CertificationDAO();

        request.setAttribute("list", dao.getAll());
        request.setAttribute("activePage", "certificationApproval");

        request.setAttribute("contentPage",
                "/WEB-INF/admin/certification/certificationApproval.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp")
                .forward(request, response);
    }
}
