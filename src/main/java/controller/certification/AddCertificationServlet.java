package controller.certification;

import dao.CertificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Certification;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.UUID;

@WebServlet("/certification/add")
@MultipartConfig
public class AddCertificationServlet extends HttpServlet {

    // mở trang add certification
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher(
                "/WEB-INF/doctor/certification/addCertification.jsp")
                .forward(request, response);
    }

    // submit form upload certification
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {

            // lấy dữ liệu form
            String name = request.getParameter("certificateName");
            String number = request.getParameter("certificateNumber");

            String issueDateStr = request.getParameter("issueDate");
            String expiryDateStr = request.getParameter("expiryDate");

            if (name == null || number == null || issueDateStr == null || expiryDateStr == null) {
                throw new Exception("Invalid form data");
            }

            Date issueDate = Date.valueOf(issueDateStr);
            Date expiryDate = Date.valueOf(expiryDateStr);

            // lấy file upload
            Part filePart = request.getPart("certificateFile");

            if (filePart == null || filePart.getSize() == 0) {
                throw new Exception("File not uploaded");
            }

            String originalFileName = Paths.get(filePart.getSubmittedFileName())
                    .getFileName().toString();

            // tạo filename unique để tránh trùng
            String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

            // đường dẫn upload
            String uploadPath = getServletContext()
                    .getRealPath("/uploads/certifications");

            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String filePath = uploadPath + File.separator + uniqueFileName;

            filePart.write(filePath);

            // lưu path vào DB
            String dbFilePath = "uploads/certifications/" + uniqueFileName;

            // tạo object certification
            Certification c = new Certification();

            c.setUserId(user.getUserId());
            c.setCertificateName(name);
            c.setCertificateNumber(number);
            c.setIssueDate(issueDate);
            c.setExpiryDate(expiryDate);
            c.setFilePath(dbFilePath);
            c.setStatus("PENDING");

            CertificationDAO dao = new CertificationDAO();

            dao.insertCertification(c);

            // redirect về danh sách certification
            response.sendRedirect(request.getContextPath() + "/certification/my");

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute("error", "Upload certification failed!");

            request.getRequestDispatcher(
                    "/WEB-INF/doctor/certification/addCertification.jsp")
                    .forward(request, response);
        }
    }
}
