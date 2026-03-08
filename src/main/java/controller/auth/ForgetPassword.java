package controller.auth;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import util.EmailUtil;

@WebServlet("/forget-password")
public class ForgetPassword extends HttpServlet {

    private String generateOTP() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("forgetPassword.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        UserDAO dao = new UserDAO();
        User user = dao.getUserByEmail(email);

        if (user == null) {
            request.setAttribute("error", "Email does not exist!");
            request.getRequestDispatcher("forgetPassword.jsp").forward(request, response);
            return;
        }

        String otp = generateOTP();

        HttpSession session = request.getSession();
        session.setAttribute("otp", otp);
        session.setAttribute("resetEmail", email);

        EmailUtil.sendOTP(email, otp);

        response.sendRedirect("verifyOTP.jsp");
    }
}
