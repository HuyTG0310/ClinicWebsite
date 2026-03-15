<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Verify OTP</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
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

            .verify-card{
                background:white;
                border-radius:20px;
                padding:45px;
                width:100%;
                max-width:480px;
                box-shadow:0 20px 40px rgba(0,0,0,0.15);
            }

            .otp-input{
                width:50px;
                height:55px;
                font-size:22px;
                text-align:center;
                border-radius:10px;
                border:1px solid #ccc;
            }

            .otp-input:focus{
                border-color:#0d6efd;
                box-shadow:0 0 5px rgba(13,110,253,0.5);
                outline:none;
            }

            .otp-group{
                display:flex;
                justify-content:space-between;
                gap:10px;
            }

            .btn-primary{
                border-radius:10px;
                padding:12px;
                font-weight:500;
            }

        </style>

    </head>

    <body>

        <div class="verify-card">

            <h3 class="text-center fw-bold mb-3">
                <i class="fa-solid fa-shield-halved text-primary me-2"></i>
                Verify OTP
            </h3>

            <p class="text-center text-muted mb-4">
                Enter the 6-digit code sent to your email
            </p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger text-center">
                    ${error}
                </div>
            </c:if>

            <form action="verify-otp" method="post" onsubmit="combineOTP()">

                <div class="otp-group mb-4">

                    <input type="text" maxlength="1" class="otp-input" id="otp1">
                    <input type="text" maxlength="1" class="otp-input" id="otp2">
                    <input type="text" maxlength="1" class="otp-input" id="otp3">
                    <input type="text" maxlength="1" class="otp-input" id="otp4">
                    <input type="text" maxlength="1" class="otp-input" id="otp5">
                    <input type="text" maxlength="1" class="otp-input" id="otp6">

                </div>

                <input type="hidden" name="otp" id="otp">

                <button class="btn btn-primary w-100">
                    <i class="fa-solid fa-check me-2"></i>
                    Verify OTP
                </button>

            </form>

            <div class="text-center mt-4">
                <a href="login" class="text-primary">
                    <i class="fa-solid fa-arrow-left me-1"></i>
                    Back to Login
                </a>
            </div>

        </div>

        <script>

            const inputs = document.querySelectorAll(".otp-input");

            inputs.forEach((input, index) => {

                input.addEventListener("input", () => {

                    if (input.value.length === 1 && index < 5) {
                        inputs[index + 1].focus();
                    }

                });

                input.addEventListener("keydown", (e) => {

                    if (e.key === "Backspace" && input.value === "" && index > 0) {
                        inputs[index - 1].focus();
                    }

                });

            });

            function combineOTP() {

                let otp = "";

                inputs.forEach(i => {
                    otp += i.value;
                });

                document.getElementById("otp").value = otp;

            }

        </script>

    </body>
</html>