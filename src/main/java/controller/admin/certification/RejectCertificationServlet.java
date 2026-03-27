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
                throw new Exception("Please enter reject reason");
            }

            CertificationDAO dao = new CertificationDAO();
            dao.reject(certificationId, reason.trim());
            response.getWriter().write("SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
        }
    }
}
