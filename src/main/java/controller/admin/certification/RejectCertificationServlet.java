package controller.admin.certification;

import dao.CertificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import java.io.IOException;

/**
 *
 * @author Tai Loi
 */
@WebServlet("/admin/certification/reject")
public class RejectCertificationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");

        if (admin == null || !admin.getRoleName().equalsIgnoreCase("Admin")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            int certificationId = Integer.parseInt(request.getParameter("id"));
            String reason = request.getParameter("rejectionNote");

            if (reason == null || reason.trim().isEmpty()) {
                throw new Exception("Vui lòng nhập lý do từ chối");
            }

            CertificationDAO dao = new CertificationDAO();
            // Lưu ý: Bạn cần cập nhật hàm reject trong DAO để nhận thêm String reason nhé
            dao.reject(certificationId, reason.trim());

            // Trả về thành công cho AJAX
            response.getWriter().write("SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
        }
    }
}
