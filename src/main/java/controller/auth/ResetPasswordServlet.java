package controller.auth;

import dao.AccountDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String password = request.getParameter("password");

        HttpSession session = request.getSession();

        String email = (String) session.getAttribute("resetEmail");

        AccountDAO dao = new AccountDAO();

        dao.updatePassword(email, password);

        session.removeAttribute("otp");

        response.sendRedirect("login?reset=success");
    }
}