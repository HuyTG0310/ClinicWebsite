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

@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/profile/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    private String hashPassword(String password) {
        // TODO: replace with BCrypt later
        return password;
    }

    private void forwardLayout(
            HttpServletRequest request,
            HttpServletResponse response,
            String contentPage
    ) throws ServletException, IOException {

        request.setAttribute("pageTitle", "Doctor Dashboard");
        request.setAttribute("activePage", "profile");
        request.setAttribute("contentPage", contentPage);

        request.getRequestDispatcher("/WEB-INF/layout/doctorLayout.jsp")
                .forward(request, response);
    }

    /* =========================
       GET
    ========================= */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy session hiện tại (không tạo mới)
        HttpSession session = request.getSession(false);

        // 2. Kiểm tra nếu chưa login thì đá về login ngay
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        forwardLayout(request, response, "/WEB-INF/profile/changePassword.jsp");
    }

    /* =========================
       POST
    ========================= */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = user.getUserId();

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        /* ===== VALIDATION ===== */
        // 1. Confirm password
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Confirm password does not match!");
            forwardLayout(request, response, "/WEB-INF/profile/changePassword.jsp");
            return;
        }

        // 2. New password != old password
        if (oldPassword.equals(newPassword)) {
            request.setAttribute(
                    "error",
                    "New password must be different from old password."
            );
            forwardLayout(request, response, "/WEB-INF/profile/changePassword.jsp");
            return;
        }

        // 3. Format: >=6 chars, no space
        if (!newPassword.matches("^\\S{3,}$")) {
            request.setAttribute(
                    "error",
                    "New password must be at least 6 characters long and must not contain spaces."
            );
            forwardLayout(request, response, "/WEB-INF/profile/changePassword.jsp");
            return;
        }

        UserDAO dao = new UserDAO();

        // 4. Old password correct
        boolean oldCorrect = dao.checkOldPassword(
                userId,
                hashPassword(oldPassword)
        );

        if (!oldCorrect) {
            request.setAttribute("error", "Old password is incorrect!");
            forwardLayout(request, response, "/WEB-INF/profile/changePassword.jsp");
            return;
        }

        /* ===== UPDATE ===== */
        boolean success = dao.changePassword(
                userId,
                hashPassword(newPassword)
        );

        if (success) {
            request.setAttribute("msg", "Change password successfully!");
        } else {
            request.setAttribute("error", "Change password failed!");
        }

        forwardLayout(request, response, "/WEB-INF/profile/changePassword.jsp");
    }
}
