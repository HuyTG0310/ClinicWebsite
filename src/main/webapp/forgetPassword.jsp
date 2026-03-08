<%-- 
    Document   : forgetPassword
    Created on : Feb 12, 2026, 2:47:43 PM
    Author     : huytr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Forget Password</title>

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">

        <!-- Google Font -->
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

        <style>
            body {
                font-family: 'Poppins', sans-serif;
                background: linear-gradient(135deg, #0d6efd, #0dcaf0);
                height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .forgot-card {
                background: white;
                border-radius: 20px;
                padding: 45px;
                width: 100%;
                max-width: 480px;
                box-shadow: 0 20px 40px rgba(0,0,0,0.15);
                animation: fadeInUp 0.8s ease;
            }

            .form-control {
                border-radius: 10px;
                padding: 12px 15px;
            }

            .btn-primary {
                border-radius: 10px;
                padding: 12px;
                font-weight: 500;
                transition: 0.3s;
            }

            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 20px rgba(13,110,253,0.3);
            }

            .back-link {
                font-size: 14px;
                text-decoration: none;
            }

            .back-link:hover {
                text-decoration: underline;
            }

            @keyframes fadeInUp {
                from {
                    opacity: 0;
                    transform: translateY(30px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
        </style>
    </head>

    <body>

        <div class="forgot-card">

            <h3 class="text-center fw-bold mb-3">
                <i class="fa-solid fa-key text-primary me-2"></i>
                Reset Password
            </h3>

            <p class="text-center text-muted mb-4">
                Enter your registered email address.  
                We will send you instructions to reset your password.
            </p>

            <!-- Error -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger text-center">
                    <i class="fa-solid fa-circle-exclamation me-2"></i>
                    ${error}
                </div>
            </c:if>

            <!-- Success -->
            <c:if test="${not empty success}">
                <div class="alert alert-success text-center">
                    <i class="fa-solid fa-circle-check me-2"></i>
                    ${success}
                </div>
            </c:if>

           <form action="${pageContext.request.contextPath}/forget-password" method="post">

                <div class="mb-4">
                    <label class="form-label">Email Address</label>
                    <div class="input-group">
                        <span class="input-group-text bg-primary text-white">
                            <i class="fa-solid fa-envelope"></i>
                        </span>
                        <input type="email" name="email" class="form-control"
                               placeholder="Enter your email" required>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-100">
                    <i class="fa-solid fa-paper-plane me-2"></i>
                    Send Reset Link
                </button>

            </form>

            <div class="text-center mt-4">
                <a href="login" class="back-link text-primary">
                    <i class="fa-solid fa-arrow-left me-1"></i>
                    Back to Login
                </a>
            </div>

        </div>

    </body>
</html>
