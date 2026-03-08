package controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/verify-otp")
public class VerifyOTP extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userOtp = request.getParameter("otp");

        HttpSession session = request.getSession();
        String otp = (String) session.getAttribute("otp");

        if(otp != null && otp.equals(userOtp)){

            response.sendRedirect("resetPassword.jsp");

        }else{

            request.setAttribute("error","Invalid OTP");
            request.getRequestDispatcher("verifyOTP.jsp").forward(request,response);

        }
    }
}