package controller.auth;

import dao.AccountDAO;
import util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Tai Loi
 */
@WebServlet("/send-otp")
public class SendOtpServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        System.out.println("EMAIL USER INPUT: " + email);

        AccountDAO dao = new AccountDAO();

        boolean exists = dao.checkEmailExist(email);
        System.out.println("EMAIL EXISTS: " + exists);
        if (!dao.checkEmailExist(email)) {

            request.setAttribute("error", "Email does not exist!");
            request.getRequestDispatcher("forgotPassword.jsp")
                   .forward(request, response);
            return;
        }

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        HttpSession session = request.getSession();

        session.setAttribute("otp", otp);
        session.setAttribute("resetEmail", email);

        try {
            EmailUtil.sendOTP(email, otp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("verifyOTP.jsp");
    }
}