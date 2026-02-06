package controller.profile;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.User;

@WebServlet(name = "ProfileUpdateServlet", urlPatterns = {"/profile/update"})
public class ProfileUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));

            String fullName = request.getParameter("fullName").trim();
            String phone = request.getParameter("phone").trim();
            String email = request.getParameter("email").trim();

            
            if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$")) {
                request.setAttribute("error", "Email must contain '@' and end with .com");

                UserDAO dao = new UserDAO();
                User user = dao.getUserById(userId);
                request.setAttribute("user", user);
                request.setAttribute("openModal", true);

                request.getRequestDispatcher("/WEB-INF/profile/profileDetail.jsp")
                       .forward(request, response);
                return;
            }

            
            UserDAO dao = new UserDAO();
            dao.updateProfile(userId, fullName, phone, email);

            response.sendRedirect(request.getContextPath() + "/profile/view");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        }
    }
}