package controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/verify-otp")
public class VerifyOtpServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String inputOtp = request.getParameter("otp");

        HttpSession session = request.getSession();

        String otp = (String) session.getAttribute("otp");

        if (inputOtp.equals(otp)) {

            response.sendRedirect("resetPassword.jsp");

        } else {

            request.setAttribute("error", "OTP incorrect!");
            request.getRequestDispatcher("verifyOtp.jsp")
                   .forward(request, response);

        }
    }
}