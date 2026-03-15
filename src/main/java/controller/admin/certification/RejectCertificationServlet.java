package controller.admin.certification;

import dao.CertificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;

@WebServlet("/admin/certification/reject")
public class RejectCertificationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        User admin = (User) session.getAttribute("user");

        // kiểm tra login
        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {

            int certificationId = Integer.parseInt(
                    request.getParameter("id"));

            CertificationDAO dao = new CertificationDAO();

            dao.reject(certificationId);

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/certification/list");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/certification/list");

        }

    }

}
