/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.service;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@WebServlet(name = "ServiceAddServlet", urlPatterns = {"/admin/service/add"})
public class ServiceAddServlet extends HttpServlet {

    ServiceDAO serviceDAO = new ServiceDAO();
    LabTestDAO labTestDAO = new LabTestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //lấy danh sách cate của dịch vụ như khám bệnh hoặc xét nghiệm
        request.setAttribute("categories", ServiceCategory.getAll());

        //lấy danh sách category của xét nghiệm như HUYẾT HỌC, SINH HÓA, ...
        LabTestCategoryDAO labCatDao = new LabTestCategoryDAO();
        request.setAttribute("labCategories", labCatDao.getAllCategories());

        request.setAttribute("pageTitle", "Add service");
        request.setAttribute("activePage", "manageService");
        request.setAttribute("contentPage", "/WEB-INF/admin/service/serviceAdd.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Lấy thông tin chung (luôn luôn có)
            String name = request.getParameter("serviceName");
            String category = request.getParameter("category");
            String priceRaw = request.getParameter("price");
            java.math.BigDecimal price = new java.math.BigDecimal(priceRaw != null && !priceRaw.isEmpty() ? priceRaw : "0");

            model.Service s = new model.Service();
            s.setServiceName(name.trim());
            s.setCategory(category);
            s.setCurrentPrice(price);

            boolean success = false;

            // CHIA TH
            if ("Xét nghiệm".equals(category)) {
                // NẾU LÀ XÉT NGHIỆM -> Đọc thêm các trường XN
                String testCode = request.getParameter("testCode");
                int labCategoryId = Integer.parseInt(request.getParameter("labCategoryId"));

                model.LabTest labTest = new model.LabTest();
                labTest.setTestCode(testCode);
                labTest.setTestName(name.trim());
                labTest.setCategoryId(labCategoryId);

                // Đọc mảng Chỉ số cơ bản
                String[] paramCodes = request.getParameterValues("paramCode[]");
                String[] paramNames = request.getParameterValues("paramName[]");
                String[] paramUnits = request.getParameterValues("paramUnit[]");
                String[] paramRangesData = request.getParameterValues("paramRanges[]");

                boolean isPanel = (paramCodes != null && paramCodes.length > 1);
                labTest.setIsPanel(isPanel);

                java.util.List<model.LabTestParameter> paramList = new java.util.ArrayList<>();
                if (paramCodes != null) {
                    Gson gson = new Gson(); 
                    java.lang.reflect.Type listType = new TypeToken<java.util.ArrayList<model.LabReferenceRange>>() {
                    }.getType();
                    for (int i = 0; i < paramCodes.length; i++) {
                        model.LabTestParameter p = new model.LabTestParameter();
                        p.setParameterCode(paramCodes[i].trim().toUpperCase());
                        p.setParameterName(paramNames[i].trim());
                        p.setUnit(paramUnits[i] != null ? paramUnits[i].trim() : "");

                        //Parse JSON bằng Gson
                        java.util.List<model.LabReferenceRange> rangesList = new java.util.ArrayList<>();
                        if (paramRangesData != null && i < paramRangesData.length && !paramRangesData[i].trim().isEmpty()) {
                            try {
                                rangesList = gson.fromJson(paramRangesData[i], listType);
                            } catch (Exception e) {
                                System.out.println("Lỗi Parse JSON Range: " + e.getMessage());
                            }
                        }
                        p.setReferenceRanges(rangesList);
                        paramList.add(p);
                    }
                }
                // Gọi Transaction Gộp
                success = labTestDAO.insertFullLabTest(s, labTest, paramList);
            } else {
                // NẾU LÀ DỊCH VỤ BÌNH THƯỜNG -> Lưu bình thường
                success = serviceDAO.insert(s);
            }

            if (success) {
                request.getSession().setAttribute("success", "Add service successful!");
                response.sendRedirect(request.getContextPath() + "/admin/service/list");
            } else {
                throw new Exception("Database Insert Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/service/add");
        }
    }

}
