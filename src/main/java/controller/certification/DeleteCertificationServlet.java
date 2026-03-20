package controller.certification;

import dao.CertificationDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import model.Certification;
import model.User;

@WebServlet({
    "/doctor/certification/delete",
    "/receptionist/certification/delete",
    "/lab/certification/delete",
    "/admin/certification/delete"
})
public class DeleteCertificationServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "D:/ClinicData/uploads/certifications";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 🔥 LẤY SEARCH PARAM
        String searchName = request.getParameter("searchName");
        String searchPhone = request.getParameter("searchPhone");

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            CertificationDAO dao = new CertificationDAO();
            Certification c = dao.getById(id);

            if (c == null) {
                throw new Exception("Không tìm thấy chứng chỉ");
            }

            boolean isAdmin = "Admin".equalsIgnoreCase(user.getRoleName());

            if (isAdmin) {
                if ("PENDING".equalsIgnoreCase(c.getStatus())) {
                    throw new Exception("Admin không được xóa chứng chỉ đang chờ duyệt!");
                }
            } else {
                if (!"PENDING".equalsIgnoreCase(c.getStatus())) {
                    throw new Exception("Bạn không thể xóa chứng chỉ đã qua xử lý!");
                }
            }

            // 🔥 XÓA FILE
            if (c.getFilePath() != null) {
                File file = new File(UPLOAD_DIR, c.getFilePath());
                if (file.exists()) {
                    file.delete();
                }
            }

            // 🔥 XÓA DB
            dao.deleteCertification(id);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔥 GIỮ SEARCH SAU DELETE
        if (searchName != null && !searchName.trim().isEmpty()) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/certification/search?searchName="
                    + URLEncoder.encode(searchName, "UTF-8")
                    + "&searchPhone="
                    + URLEncoder.encode(searchPhone != null ? searchPhone : "", "UTF-8")
            );
            return;
        }

        // 🔥 KHÔNG SEARCH → redirect như cũ
        String referer = request.getHeader("referer");

// Nếu đang ở trang search → quay lại đúng trang đó
        if (referer != null && referer.contains("/admin/certification/search")) {
            response.sendRedirect(referer);
            return;
        }

// 🔥 KHÔNG PHẢI SEARCH → xử lý như cũ
        redirectByRole(user, request, response);
    }

    private void redirectByRole(User user, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String role = user.getRoleName();
        String path = "/admin/certification/list";

        if ("Doctor".equalsIgnoreCase(role)) {
            path = "/doctor/certification/my";
        } else if ("Receptionist".equalsIgnoreCase(role)) {
            path = "/receptionist/certification/my";
        } else if ("Lab technician".equalsIgnoreCase(role)) {
            path = "/lab/certification/my";
        }

        response.sendRedirect(request.getContextPath() + path);
    }
}
