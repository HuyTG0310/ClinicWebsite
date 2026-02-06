package controller.profile;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.User;

@WebServlet(name = "ProfileViewServlet", urlPatterns = {"/profile/view"})
public class ProfileViewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = 1;

        UserDAO dao = new UserDAO();
        User user = dao.getUserById(userId);

        request.setAttribute("user", user);
//        request.getRequestDispatcher("/WEB-INF/profile/profileDetail.jsp").forward(request, response);

        request.setAttribute("pageTitle", "Doctor Dashboard");
        request.setAttribute("activePage", "profile");
        request.setAttribute("contentPage", "/WEB-INF/profile/profileDetail.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/doctorLayout.jsp").forward(request, response);
    }
}
