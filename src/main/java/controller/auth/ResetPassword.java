package controller.auth;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPassword extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if(!newPassword.equals(confirmPassword)){
            request.setAttribute("error","Passwords do not match");
            request.getRequestDispatcher("resetPassword.jsp").forward(request,response);
            return;
        }

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("resetEmail");

        UserDAO dao = new UserDAO();
        dao.resetPassword(email,newPassword);

        session.removeAttribute("otp");
        session.removeAttribute("resetEmail");

        response.sendRedirect("login.jsp");
    }
}