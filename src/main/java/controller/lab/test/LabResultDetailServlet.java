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
@WebServlet(name = "LabResultDetailServlet", urlPatterns = {"/lab/test/detail"})
public class LabResultDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

            request.setAttribute("pageTitle", "Chi Tiết Kết Quả Xét Nghiệm");
            request.setAttribute("activePage", "manageTest");

            // Dùng chung file JSP Nhập liệu, nhờ cờ isViewMode để khóa Form lại
            request.setAttribute("contentPage", "/WEB-INF/lab/labTestEdit.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/labLayout.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/lab/queue/list");
        }
    }
}
