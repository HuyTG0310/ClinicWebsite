/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lab.test;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author huytr
 */
@WebServlet(name = "LabResultDetailServlet", urlPatterns = {"/lab/lab-test/detail", "/admin/lab-test/detail"})
public class LabResultDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        
        try {
            int mrId = Integer.parseInt(request.getParameter("mrId"));
            dao.LabTestDAO labDao = new dao.LabTestDAO();

            java.util.List<java.util.Map<String, Object>> tests = labDao.getTestsForProcessing(mrId);
            request.setAttribute("tests", tests);
            request.setAttribute("mrId", mrId);

            // 🔥 CỜ BÁO HIỆU CHO JSP: ĐÂY LÀ CHẾ ĐỘ CHỈ XEM (READ-ONLY)
            request.setAttribute("isViewMode", true);

            dao.MedicalRecordDAO mrDao = new dao.MedicalRecordDAO();
            model.MedicalRecord mr = mrDao.getRecordById(mrId);
            request.setAttribute("mr", mr); // Ném qua JSP

            request.setAttribute("pageTitle", "Result Detail");
            request.setAttribute("activePage", "manageTest");

            // Dùng chung file JSP Nhập liệu, nhờ cờ isViewMode để khóa Form lại
            request.setAttribute("contentPage", "/WEB-INF/lab/labTestEdit.jsp");

            request.getRequestDispatcher(layout).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(basePath + "/lab-queue/list");
        }
    }
}
