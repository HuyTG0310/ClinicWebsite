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

@WebServlet(name = "ProfileViewServlet", urlPatterns = {"/doctor/profile/view", "/receptionist/profile/view", "/lab/profile/view"})
public class ProfileViewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        String layout;
        String basePath;
        
        if (uri.startsWith(ctx + "/lab")) {
            layout = "/WEB-INF/layout/labLayout.jsp";
            basePath = ctx + "/lab";
        } else if (uri.startsWith(ctx + "/doctor")) {
            layout = "/WEB-INF/layout/doctorLayout.jsp";
            basePath = ctx + "/doctor";
        } else if (uri.startsWith(ctx + "/receptionist")) {
            layout = "/WEB-INF/layout/receptionistLayout.jsp";
            basePath = ctx + "/receptionist";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        UserDAO dao = new UserDAO();
        User userr = dao.getUserById(user.getUserId());
        
        request.setAttribute("basePath", basePath);
        request.setAttribute("user", userr);
        request.setAttribute("pageTitle", "Profile Detail");
        request.setAttribute("activePage", "profile");
        request.setAttribute("contentPage", "/WEB-INF/profile/profileDetail.jsp");

        request.getRequestDispatcher(layout).forward(request, response);
    }
}
