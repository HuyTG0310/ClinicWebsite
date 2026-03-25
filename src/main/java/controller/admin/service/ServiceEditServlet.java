/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.service;

import dao.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 *
 * @author Gia Huy
 */
@WebServlet(name = "ServiceEditServlet", urlPatterns = {"/admin/service/edit", "/doctor/service/edit", "/receptionist/service/edit", "/lab/service/edit"})
public class ServiceEditServlet extends HttpServlet {

    private ServiceDAO serviceDAO = new ServiceDAO();
    private LabTestDAO labTestDAO = new LabTestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        String basePath;
        if (uri.startsWith(ctx + "/admin")) {
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            basePath = ctx + "/doctor";
        } else if (uri.startsWith(ctx + "/receptionist")) {
            basePath = ctx + "/receptionist";
        } else if (uri.startsWith(ctx + "/lab")) {
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);
        
        String idRaw = request.getParameter("serviceId");
        String name = request.getParameter("serviceName");
        String category = request.getParameter("category"); // Được lấy từ thẻ input hidden
        String priceRaw = request.getParameter("price");
        boolean isActive = request.getParameter("isActive") != null;

        int id = 0;
        try {
            id = Integer.parseInt(idRaw);
            java.math.BigDecimal price = new java.math.BigDecimal(priceRaw != null && !priceRaw.isEmpty() ? priceRaw : "0");

            model.Service s = new model.Service();
            s.setServiceId(id);
            s.setServiceName(name.trim());
            s.setCategory(category);
            s.setCurrentPrice(price);
            s.setIsActive(isActive);

            boolean success = false;

            // CHIA TH
            if ("Xét nghiệm".equals(category)) {
                int labTestId = Integer.parseInt(request.getParameter("labTestId"));
                String testCode = request.getParameter("testCode");
                int labCategoryId = Integer.parseInt(request.getParameter("labCategoryId"));

                model.LabTest labTest = new model.LabTest();
                labTest.setLabTestId(labTestId);
                labTest.setServiceId(id);
                labTest.setTestCode(testCode);
                labTest.setTestName(name.trim()); // Đồng bộ tên
                labTest.setCategoryId(labCategoryId);
                labTest.setIsActive(isActive);

                String[] paramIds = request.getParameterValues("paramId[]");
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
                        p.setParameterId(Integer.parseInt(paramIds[i])); // Phân biệt sửa (có ID) và thêm mới (ID=0)
                        p.setParameterCode(paramCodes[i].trim().toUpperCase());
                        p.setParameterName(paramNames[i].trim());
                        p.setUnit(paramUnits[i] != null ? paramUnits[i].trim() : "");

                        // DÙNG GSON ĐỂ ĐỌC CHUỖI JSON THÀNH LIST OBJECT RANGES
                        java.util.List<model.LabReferenceRange> rangesList = new java.util.ArrayList<>();
                        if (paramRangesData != null && i < paramRangesData.length && !paramRangesData[i].trim().isEmpty()) {
                            try {
                                rangesList = gson.fromJson(paramRangesData[i], listType);
                            } catch (Exception e) {
                                System.out.println("Lỗi Parse JSON Range lúc Edit: " + e.getMessage());
                            }
                        }
                        p.setReferenceRanges(rangesList);
                        paramList.add(p);
                    }
                }


                success = labTestDAO.updateFullLabTest(s, labTest, paramList);

            } else {
                // Dịch vụ bình thường (Khám bệnh, Siêu âm...)
                success = serviceDAO.update(s);
            }

            if (success) {
                request.getSession().setAttribute("success", "Update service successful!");
            } else {
                request.getSession().setAttribute("error", "Error when update Database!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Invalid input!");
        }

        response.sendRedirect(basePath + "/service/detail?id=" + id);
    }

}
