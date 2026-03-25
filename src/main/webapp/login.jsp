<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login</title>

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
                overflow: hidden;
            }

            .login-wrapper {
                height: 100vh;
            }

            .brand-section {
                background: rgba(255,255,255,0.1);
                backdrop-filter: blur(10px);
                color: white;
                padding: 60px;
                display: flex;
                flex-direction: column;
                justify-content: center;
            }

            .brand-section h1 {
                font-weight: 700;
                font-size: 32px;
            }

            .brand-section p {
                opacity: 0.9;
            }

            .login-card {
                background: white;
                border-radius: 20px;
                padding: 40px;
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

            .forgot-link {
                font-size: 14px;
                text-decoration: none;
            }

            .forgot-link:hover {
                text-decoration: underline;
            }

            .input-group-text {
                border-radius: 10px 0 0 10px;
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

            @media(max-width: 992px){
                .brand-section{
                    display:none;
                }
            }
        </style>
    </head>

    <body>

        <div class="container-fluid login-wrapper">
            <div class="row h-100">

                <!-- Left Branding -->
                <div class="col-lg-6 brand-section">
                    <h1><i class="fa-solid fa-hospital me-2"></i>Clinic Management System</h1>
                    <p class="mt-3">
                        Secure • Modern • Professional <br>
                        Manage patients, staff, services and medicine efficiently.
                    </p>
                </div>

                <!-- Right Login Form -->
                <div class="col-lg-6 d-flex align-items-center justify-content-center">
                    <div class="login-card col-md-8 col-lg-7">

                        <h3 class="text-center mb-4 fw-bold">
                            <i class="fa-solid fa-user-doctor me-2 text-primary"></i>
                            Sign In
                        </h3>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show shadow-sm text-center" role="alert">
                                <i class="fa-solid fa-circle-exclamation me-2"></i>
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <c:if test="${param.error eq 'role_inactive'}">
                            <div class="alert alert-warning shadow-sm">
                                <i class="fa-solid fa-lock me-2"></i> 
                                Tài khoản của bạn thuộc vai trò đã bị tạm dừng.
                            </div>
                        </c:if>

                        <form action="login" method="post">

                            <!-- Username -->
                            <div class="mb-3">
                                <label class="form-label">Username</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-primary text-white">
                                        <i class="fa-solid fa-user"></i>
                                    </span>
                                    <input type="text" name="username" class="form-control" placeholder="Enter username" required>
                                </div>
                            </div>

                            <!-- Password -->
                            <div class="mb-3">
                                <label class="form-label">Password</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-primary text-white">
                                        <i class="fa-solid fa-lock"></i>
                                    </span>
                                    <input type="password" id="password" name="password" class="form-control" placeholder="Enter password" required>
                                    <span class="input-group-text" onclick="togglePassword()" style="cursor:pointer;">
                                        <i class="fa-solid fa-eye" id="toggleIcon"></i>
                                    </span>
                                </div>
                            </div>

                            <!-- Remember + Forgot -->
                            <div class="d-flex justify-content-end align-items-center mb-3">
                                <a href="forget-password" class="forgot-link text-primary">
                                    Forgot password?
                                </a>
                            </div>

                            <!-- Button -->
                            <button type="submit" class="btn btn-primary w-100">
                                <i class="fa-solid fa-right-to-bracket me-2"></i>
                                Login
                            </button>

                        </form>

                        <div class="text-center mt-4">
                            <small class="text-muted">
                                © 2026 Clinic Management System
                            </small>
                        </div>

                    </div>
                </div>

            </div>
        </div>

        <script>
            function togglePassword() {
                const password = document.getElementById("password");
                const icon = document.getElementById("toggleIcon");

                if (password.type === "password") {
                    password.type = "text";
                    icon.classList.remove("fa-eye");
                    icon.classList.add("fa-eye-slash");
                } else {
                    password.type = "password";
                    icon.classList.remove("fa-eye-slash");
                    icon.classList.add("fa-eye");
                }
            }
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>
