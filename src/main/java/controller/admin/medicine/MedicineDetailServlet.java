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

/**
 *
 * @author TRUONGTHINHNGUYEN
 */
@WebServlet(name = "MedicineDetailServlet", urlPatterns = {"/admin/medicine/detail"})
public class MedicineDetailServlet extends HttpServlet {

   

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy id
        int id = Integer.parseInt(request.getParameter("id"));

        // Lấy medicine
        MedicineDAO dao = new MedicineDAO();
        Medicine m = dao.getById(id);

        // Đẩy sang JSP
//        request.setAttribute("pageTitle", "Medicine detail");
//        request.setAttribute("activePage", "manageMedicine");
//        request.setAttribute("contentPage", "/WEB-INF/admin/medicine/medicineDetail.jsp");
//        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
        
        request.setAttribute("medicine", m);
        request.setAttribute("pageTitle", "Medicine detail");
        request.setAttribute("activePage", "manageMedicine");
        request.setAttribute("contentPage", "/WEB-INF/admin/medicine/medicineDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
//        request.getRequestDispatcher("/WEB-INF/admin/medicine/medicineDetail.jsp")
//               .forward(request, response);
    }
   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

   


}
