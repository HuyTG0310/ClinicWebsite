package controller.certification;

import dao.CertificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;

@WebServlet("/certification/my")
public class MyCertificationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        CertificationDAO dao = new CertificationDAO();

        request.setAttribute("list",
                dao.getByUser(user.getUserId()));

        request.getRequestDispatcher(
                "/WEB-INF/doctor/certification/myCertification.jsp")
                .forward(request, response);
    }
}
