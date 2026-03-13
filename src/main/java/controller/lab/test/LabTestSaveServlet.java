/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.lab.test;

import dao.LabTestDAO;
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
@WebServlet(name = "LabTestSaveServlet", urlPatterns = {"/lab/test/save"})
public class LabTestSaveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int medicalRecordId = Integer.parseInt(request.getParameter("medicalRecordId"));

            String[] orderTestIds = request.getParameterValues("orderTestId");
            String[] paramIds = request.getParameterValues("paramId");

            LabTestDAO dao = new LabTestDAO();
            boolean success = dao.saveLabResults(medicalRecordId, orderTestIds, paramIds, request.getParameterMap());

            if (success) {
                request.getSession().setAttribute("success", "Đã lưu kết quả xét nghiệm");
            } else {
                request.getSession().setAttribute("error", "Có lỗi xảy ra khi lưu kết quả");
            }
            response.sendRedirect(request.getContextPath() + "/lab/test/detail?mrId=" + medicalRecordId);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/lab/queue/list");
        }
    }

}
