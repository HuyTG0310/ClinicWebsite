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

        // Lấy thông tin mới nhất từ Database
        UserDAO dao = new UserDAO();
//<<<<<<< HEAD
        User updatedUser = dao.getUserById(user.getUserId());
        request.setAttribute("user", updatedUser);

        // XỬ LÝ DYNAMIC LAYOUT VÀ TITLE THEO ROLE
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
//=======
//        User userr = dao.getUserById(user.getUserId());
//        
//        request.setAttribute("basePath", basePath);
//        request.setAttribute("user", userr);
//        request.setAttribute("pageTitle", "Profile Detail");
//        request.setAttribute("activePage", "profile");
//        request.setAttribute("contentPage", "/WEB-INF/profile/profileDetail.jsp");
//
//        request.getRequestDispatcher(layout).forward(request, response);
//>>>>>>> e69fd48a9d4e1c25401d242ea75c1869f56faf3b
    }
}
