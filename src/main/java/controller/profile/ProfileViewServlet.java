package controller.profile;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.User;

@WebServlet(name = "ProfileViewServlet", urlPatterns = {"/profile/view"})
public class ProfileViewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        UserDAO dao = new UserDAO();
        User userr = dao.getUserWithRoleById(user.getUserId());

        request.setAttribute("user", userr);
        request.setAttribute("pageTitle", "Doctor Dashboard");
        request.setAttribute("activePage", "profile");
        request.setAttribute("contentPage", "/WEB-INF/profile/profileDetail.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/doctorLayout.jsp").forward(request, response);
    }
}

