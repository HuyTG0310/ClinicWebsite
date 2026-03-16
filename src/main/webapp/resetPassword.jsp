<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Reset Password</title>

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- FontAwesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">

        <!-- Google Font -->
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

        <style>

            body{
                font-family:'Poppins',sans-serif;
                background:linear-gradient(135deg,#0d6efd,#0dcaf0);
                height:100vh;
                display:flex;
                align-items:center;
                justify-content:center;
            }

            /* CARD */

            .reset-card{
                background:#ffffff;
                border-radius:20px;
                padding:45px;
                width:100%;
                max-width:480px;

                border:1px solid rgba(13,110,253,0.15);

                box-shadow:0 15px 35px rgba(13,110,253,0.2);

                animation:fadeInUp .6s ease;
            }

            /* INPUT */

            .form-control{
                border-radius:10px;
                padding:12px 15px;
                border:1px solid #dee2e6;
                font-size:15px;
            }

            .form-control:focus{
                border-color:#0d6efd;
                box-shadow:0 0 6px rgba(13,110,253,0.35);
            }

            /* INPUT ICON */

            .input-group-text{
                border-radius:10px 0 0 10px;
                background:#0d6efd;
                color:white;
                border:none;
            }

            /* BUTTON */

            .btn-primary{
                border-radius:10px;
                padding:12px;
                font-weight:500;
                transition:.3s;
            }

            .btn-primary:hover{
                transform:translateY(-2px);
                box-shadow:0 10px 20px rgba(13,110,253,0.35);
            }

            /* PASSWORD TOGGLE */

            .toggle-password{
                cursor:pointer;
            }

            /* ERROR MESSAGE */

            .password-error{
                display:none;
                font-size:14px;
            }

            /* ANIMATION */

            @keyframes fadeInUp{

                from{
                    opacity:0;
                    transform:translateY(25px);
                }

                to{
                    opacity:1;
                    transform:translateY(0);
                }

            }

        </style>
    </head>

    <body>

        <div class="reset-card">

            <h3 class="text-center fw-bold mb-3">
                <i class="fa-solid fa-lock text-primary me-2"></i>
                Create New Password
            </h3>

            <p class="text-center text-muted mb-4">
                Enter your new password below.
            </p>

            <form action="reset-password" method="post" onsubmit="return validatePassword()">

                <!-- PASSWORD -->

                <div class="mb-3">

                    <label class="form-label">New Password</label>

                    <div class="input-group">

                        <span class="input-group-text">
                            <i class="fa-solid fa-lock"></i>
                        </span>

                        <input type="password"
                               id="password"
                               name="password"
                               class="form-control"
                               placeholder="Enter new password"
                               required>

                        <span class="input-group-text toggle-password" onclick="togglePassword('password', this)">
                            <i class="fa-solid fa-eye"></i>
                        </span>

                    </div>

                </div>

                <!-- CONFIRM PASSWORD -->

                <div class="mb-3">

                    <label class="form-label">Confirm Password</label>

                    <div class="input-group">

                        <span class="input-group-text">
                            <i class="fa-solid fa-check"></i>
                        </span>

                        <input type="password"
                               id="confirmPassword"
                               class="form-control"
                               placeholder="Confirm password"
                               required>

                        <span class="input-group-text toggle-password" onclick="togglePassword('confirmPassword', this)">
                            <i class="fa-solid fa-eye"></i>
                        </span>

                    </div>

                </div>

                <!-- ERROR -->

                <div id="passwordError" class="alert alert-danger password-error">
                    Passwords do not match!
                </div>

                <!-- BUTTON -->

                <button type="submit" class="btn btn-primary w-100">
                    <i class="fa-solid fa-rotate-right me-2"></i>
                    Reset Password
                </button>

            </form>

            <div class="text-center mt-4">

                <a href="login" class="text-primary text-decoration-none">

                    <i class="fa-solid fa-arrow-left me-1"></i>
                    Back to Login

                </a>

            </div>

        </div>

        <script>

            /* SHOW HIDE PASSWORD */

            function togglePassword(id, icon) {

                let input = document.getElementById(id);
                let i = icon.querySelector("i");

                if (input.type === "password") {

                    input.type = "text";
                    i.classList.replace("fa-eye", "fa-eye-slash");

                } else {

                    input.type = "password";
                    i.classList.replace("fa-eye-slash", "fa-eye");

                }

            }

            /* VALIDATE PASSWORD */

            function validatePassword() {

                let pass = document.getElementById("password").value;
                let confirm = document.getElementById("confirmPassword").value;

                let error = document.getElementById("passwordError");

                if (pass !== confirm) {

                    error.style.display = "block";
                    return false;

                }

                error.style.display = "none";
                return true;

            }

        </script>

    </body>
</html>