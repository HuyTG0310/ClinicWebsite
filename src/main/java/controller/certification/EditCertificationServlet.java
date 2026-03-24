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
import java.time.LocalDate;
import java.util.UUID;

/**
 *
 * @author Tai Loi
 */
@WebServlet({
    "/doctor/certification/edit",
    "/receptionist/certification/edit",
    "/lab/certification/edit",
    "/admin/certification/edit"
})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class EditCertificationServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "D:/ClinicData/uploads/certifications";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Thiết lập tiếng Việt cho phản hồi
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
            return;
        }

        CertificationDAO dao = new CertificationDAO();
        Certification c = null;

        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                throw new Exception("ID không hợp lệ");
            }

            int id = Integer.parseInt(idParam);
            c = dao.getById(id);

            if (c == null) {
                throw new Exception("Chứng chỉ không tồn tại trong hệ thống");
            }

            // Kiểm tra quyền sửa (Chỉ Admin hoặc chứng chỉ PENDING mới được sửa)
            boolean isAdmin = user.getRoleName().equalsIgnoreCase("Admin");
            if (!isAdmin && !"PENDING".equalsIgnoreCase(c.getStatus())) {
                throw new Exception("Chứng chỉ đã được xử lý, bạn không có quyền chỉnh sửa");
            }

            // ===== LẤY DỮ LIỆU =====
            String name = request.getParameter("certificateName");
            String number = request.getParameter("certificateNumber");
            String issueDateStr = request.getParameter("issueDate");
            String expiryDateStr = request.getParameter("expiryDate");

            // ===== VALIDATE TRỐNG =====
            if (name == null || name.trim().isEmpty()
                    || number == null || number.trim().isEmpty()
                    || issueDateStr == null || issueDateStr.trim().isEmpty()
                    || expiryDateStr == null || expiryDateStr.trim().isEmpty()) {
                throw new Exception("Vui lòng nhập đầy đủ thông tin bắt buộc");
            }

            // ===== VALIDATE ĐỘ DÀI =====
            if (name.length() > 255) {
                throw new Exception("Tên chứng chỉ quá dài (tối đa 255 ký tự)");
            }
            if (number.length() > 100) {
                throw new Exception("Số chứng chỉ quá dài (tối đa 100 ký tự)");
            }

            // ===== VALIDATE NGÀY THÁNG (Dùng LocalDate để chính xác tuyệt đối) =====
            Date issueDate = Date.valueOf(issueDateStr);
            Date expiryDate = Date.valueOf(expiryDateStr);

            LocalDate today = LocalDate.now();
            LocalDate ldIssue = issueDate.toLocalDate();
            LocalDate ldExpiry = expiryDate.toLocalDate();

            if (ldIssue.isAfter(today)) {
                throw new Exception("Ngày cấp không được vượt quá ngày hiện tại");
            }

            if(ldExpiry.isBefore(today)){
                throw new Exception("Chứng chỉ đã hết hạn");
            }
            
            if (ldExpiry.isBefore(ldIssue)) {
                throw new Exception("Ngày hết hạn phải sau ngày cấp chứng chỉ");
            }

            // ===== KIỂM TRA TRÙNG SỐ CHỨNG CHỈ =====
            if (dao.isCertificateNumberExist(number)
                    && !number.equals(c.getCertificateNumber())) {
                throw new Exception("Số chứng chỉ này đã tồn tại trên hệ thống");
            }

            // ===== CẬP NHẬT THÔNG TIN =====
            c.setCertificateName(name.trim());
            c.setCertificateNumber(number.trim());
            c.setIssueDate(issueDate);
            c.setExpiryDate(expiryDate);

            // ===== XỬ LÝ FILE (Nếu người dùng chọn file mới) =====
            Part filePart = request.getPart("certificateFile");
            if (filePart != null && filePart.getSize() > 0) {

                String contentType = filePart.getContentType();
                if (!contentType.startsWith("image/") && !contentType.equals("application/pdf")) {
                    throw new Exception("Chỉ chấp nhận định dạng file ảnh hoặc PDF");
                }

                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Xóa file cũ để tiết kiệm bộ nhớ
                if (c.getFilePath() != null) {
                    File oldFile = new File(UPLOAD_DIR, c.getFilePath());
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                String original = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String ext = original.contains(".") ? original.substring(original.lastIndexOf(".")) : "";
                String fileName = UUID.randomUUID().toString() + ext;

                filePart.write(UPLOAD_DIR + File.separator + fileName);
                c.setFilePath(fileName);
            }

            // ===== LƯU VÀO DATABASE =====
            dao.updateCertification(c);

            // TRẢ VỀ TÍN HIỆU THÀNH CÔNG CHO AJAX
            response.getWriter().write("SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            // TRẢ VỀ MÃ LỖI 400 VÀ NỘI DUNG LỖI ĐỂ MODAL HIỂN THỊ
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
        }
    }

    // Giữ lại hàm này nếu bạn có dùng trong doGet hoặc các xử lý khác
    private void forwardByRole(User user, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = user.getRoleName().toLowerCase();
        String page = "/WEB-INF/doctor/myCertification.jsp";
        if (role.contains("receptionist")) {
            page = "/WEB-INF/receptionist/myCertification.jsp";
        } else if (role.contains("lab")) {
            page = "/WEB-INF/lab/myCertification.jsp";
        } else if (role.contains("admin")) {
            page = "/WEB-INF/admin/certification/list.jsp";
        }
        request.getRequestDispatcher(page).forward(request, response);
    }
}
