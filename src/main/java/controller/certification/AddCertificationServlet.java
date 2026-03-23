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

@WebServlet({
    "/doctor/certification/add",
    "/receptionist/certification/add",
    "/lab/certification/add"
})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class AddCertificationServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "D:/ClinicData/uploads/certifications";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String layout;
        String basePath;

        if (uri.startsWith(ctx + "/admin")) {
            layout = "/WEB-INF/layout/adminLayout.jsp";
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            layout = "/WEB-INF/layout/doctorLayout.jsp";
            basePath = ctx + "/doctor";
        } else if (uri.startsWith(ctx + "/receptionist")) {
            layout = "/WEB-INF/layout/receptionistLayout.jsp";
            basePath = ctx + "/receptionist";
        } else if (uri.startsWith(ctx + "/lab")) {
            layout = "/WEB-INF/layout/labLayout.jsp";
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath); // Gửi sang JSP để dùng

        request.setAttribute("activePage", "myCertification");

        request.setAttribute("pageTitle", "Add Certification");

        request.setAttribute("contentPage", "/WEB-INF/certification/addCertification.jsp");

        request.getRequestDispatcher(layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        String layout;
        String basePath;
        if (uri.startsWith(ctx + "/admin")) {
            layout = "/WEB-INF/layout/adminLayout.jsp";
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            layout = "/WEB-INF/layout/doctorLayout.jsp";
            basePath = ctx + "/doctor";
        } else if (uri.startsWith(ctx + "/receptionist")) {
            layout = "/WEB-INF/layout/receptionistLayout.jsp";
            basePath = ctx + "/receptionist";
        } else if (uri.startsWith(ctx + "/lab")) {
            layout = "/WEB-INF/layout/labLayout.jsp";
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        request.setAttribute("basePath", basePath); // Gửi sang JSP để dùng
        try {
            // =========================
            // 1. VALIDATE INPUT
            // =========================
            String name = request.getParameter("certificateName");
            String number = request.getParameter("certificateNumber");
            String issueDateStr = request.getParameter("issueDate");
            String expiryDateStr = request.getParameter("expiryDate");

            if (name == null || name.trim().isEmpty()
                    || number == null || number.trim().isEmpty()
                    || issueDateStr == null || expiryDateStr == null) {
                throw new Exception("Vui lòng nhập đầy đủ thông tin");
            }
            if (!issueDateStr.matches("^\\d{4}-\\d{2}-\\d{2}$")
                    || !expiryDateStr.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                throw new Exception("Ngày không hợp lệ (định dạng yyyy-MM-dd)");
            }
            Date issueDate = Date.valueOf(issueDateStr);
            Date expiryDate = Date.valueOf(expiryDateStr);
            Date today = new Date(System.currentTimeMillis());
            if (expiryDate.before(today)) {
                throw new Exception("Chứng chỉ đã hết hạn");
            }

            if (issueDate.after(today)) {
                throw new Exception("Chứng chỉ chưa được cấp");
            }

            if (expiryDate.before(issueDate)) {
                throw new Exception("Ngày hết hạn phải sau ngày cấp");
            }

            CertificationDAO dao = new CertificationDAO();

            if (dao.isCertificateNumberExist(number)) {
                throw new Exception("Số chứng chỉ đã tồn tại");
            }

            // =========================
            // 2. HANDLE FILE UPLOAD
            // =========================
            Part filePart = request.getPart("certificateFile");

            if (filePart == null || filePart.getSize() == 0) {
                throw new Exception("Vui lòng chọn file chứng chỉ");
            }

            // Validate content type
            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")
                    && !contentType.equals("application/pdf")) {
                throw new Exception("Chỉ chấp nhận file ảnh hoặc PDF");
            }

            // Tạo folder nếu chưa có
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Lấy extension
            String originalFileName = Paths.get(filePart.getSubmittedFileName())
                    .getFileName().toString();

            String extension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                extension = originalFileName.substring(i);
            }

            // Tạo tên file unique
            String uniqueFileName = UUID.randomUUID().toString() + extension;

            // Lưu file
            String filePath = UPLOAD_DIR + File.separator + uniqueFileName;
            filePart.write(filePath);

            // =========================
            // 3. SAVE DATABASE
            // =========================
            Certification c = new Certification();
            c.setUserId(user.getUserId());
            c.setCertificateName(name.trim());
            c.setCertificateNumber(number.trim());
            c.setIssueDate(issueDate);
            c.setExpiryDate(expiryDate);
            c.setFilePath(uniqueFileName); // chỉ lưu tên file
            c.setStatus("PENDING");

            dao.insertCertification(c);


            // 4. REDIRECT THEO ROLE
            response.sendRedirect(basePath + "/certification/my");

        } catch (Exception e) {
            e.printStackTrace();

            request.setAttribute("error", e.getMessage());
            request.setAttribute("certificateName", request.getParameter("certificateName"));
            request.setAttribute("certificateNumber", request.getParameter("certificateNumber"));
            request.setAttribute("issueDate", request.getParameter("issueDate"));
            request.setAttribute("expiryDate", request.getParameter("expiryDate"));

            request.setAttribute("activePage", "myCertification");

            request.setAttribute("pageTitle", "Add Certification");

            request.setAttribute("contentPage", "/WEB-INF/certification/addCertification.jsp");

            request.getRequestDispatcher(layout).forward(request, response);
        }
    }
}
