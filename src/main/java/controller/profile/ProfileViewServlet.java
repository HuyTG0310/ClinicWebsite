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

        request.setAttribute("basePath", basePath);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Lấy thông tin mới nhất từ Database
        UserDAO dao = new UserDAO();
        User updatedUser = dao.getUserById(user.getUserId());
        request.setAttribute("user", updatedUser);

        String roleName = updatedUser.getRoleName().toLowerCase();
        String layoutPath = "/WEB-INF/layout/doctorLayout.jsp"; // Mặc định
        String pageTitle = "Doctor Profile";

        if (roleName.contains("admin")) {
            layoutPath = "/WEB-INF/layout/adminLayout.jsp";
            pageTitle = "Admin Profile";
        } else if (roleName.contains("receptionist")) {
            layoutPath = "/WEB-INF/layout/receptionistLayout.jsp";
            pageTitle = "Receptionist Profile";
        } else if (roleName.contains("lab")) {
            layoutPath = "/WEB-INF/layout/labLayout.jsp";
            pageTitle = "Lab Profile";
        }

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("activePage", "profile");
        request.setAttribute("contentPage", "/WEB-INF/profile/profileDetail.jsp");

        // Đẩy sang đúng Layout của Role đó
        request.getRequestDispatcher(layoutPath).forward(request, response);
    }
}
