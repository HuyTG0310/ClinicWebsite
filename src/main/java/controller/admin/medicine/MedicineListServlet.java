/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.medicine;

import dao.MedicineDAO;
import model.Medicine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author TRUONGTHINHNGUYEN
 */
@WebServlet(name = "MedicineListServlet", urlPatterns = {"/admin/medicine"})
public class MedicineListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");

        MedicineDAO dao = new MedicineDAO();
        List<Medicine> list;

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasStatus = status != null && !status.equals("all");

        if (hasKeyword || hasStatus) {
            if (keyword == null) {
                keyword = "";
            }
            if (status == null) {
                status = "all";
            }
            list = dao.search(keyword, status);
        } else {
            list = dao.getAll();
        }

        // 2. Gọi DAO
        // 3. Đẩy dữ liệu sang JSP
        request.setAttribute("list", list);
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);

        // 4. Forward sang view     
        request.setAttribute("pageTitle", "Manage medicine");
        request.setAttribute("activePage", "manageMedicine");
        request.setAttribute("contentPage", "/WEB-INF/admin/medicine/medicineList.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
