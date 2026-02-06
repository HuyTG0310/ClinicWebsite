<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Change Password</title>

        <style>
            /* =========================
               GLOBAL – CLINIC THEME
            ========================= */
            body {
                margin: 0;
                padding: 0;
                font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                background: #f4f7fb;
                min-height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                color: #212529;
            }

            /* =========================
               CONTAINER
            ========================= */
            .container {
                width: 500px;
                max-width: 92%;
                background: rgba(255, 255, 255, 0.12);
                backdrop-filter: blur(12px);
                border-radius: 20px;
                padding: 35px;
                box-shadow: 0px 15px 40px rgba(0, 0, 0, 0.35);
                border: 1px solid rgba(255, 255, 255, 0.2);
                animation: fadeIn 0.7s ease-in-out;
                margin-top: 100px;
            }


            /* =========================
               TITLE
            ========================= */
            h2 {
                text-align: center;
                font-size: 24px;
                font-weight: 700;
                color: #0d6efd;
                margin-bottom: 26px;
                letter-spacing: 1px;
                text-transform: uppercase;
            }

            /* =========================
               FORM CARD
            ========================= */
            .form-card {
                background: #f8f9fc;
                border-radius: 14px;
                padding: 24px 26px;
                border: 1px solid #dee2e6;
            }

            /* =========================
               FORM
            ========================= */
            label {
                font-weight: 600;
                display: block;
                margin-bottom: 6px;
                color: #495057;
                font-size: 14px;
            }

            .input-group {
                margin-bottom: 16px;
            }

            input[type="password"] {
                width: 100%;
                padding: 11px 12px;
                border-radius: 8px;
                border: 1px solid #ced4da;
                font-size: 14px;
            }

            input[type="password"]:focus {
                outline: none;
                border-color: #0d6efd;
                box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.15);
            }

            /* =========================
               BUTTON
            ========================= */
            button {
                width: 100%;
                padding: 12px;
                border: none;
                border-radius: 10px;
                font-size: 15px;
                font-weight: 600;
                cursor: pointer;
                background-color: #dc3545;
                color: #ffffff;
                transition: all 0.25s ease;
            }

            button:hover {
                background-color: #bb2d3b;
                transform: translateY(-2px);
            }

            /* =========================
               MESSAGE
            ========================= */
            .message-error {
                color: #dc3545;
                font-weight: 600;
                margin-top: 14px;
                text-align: center;
            }

            .message-success {
                color: #198754;
                font-weight: 600;
                margin-top: 14px;
                text-align: center;
            }

            /* =========================
               DIVIDER
            ========================= */
            hr {
                margin: 26px 0;
                border: none;
                height: 1px;
                background: #dee2e6;
            }

            /* =========================
               BACK LINK
            ========================= */
            .back-link {
                display: inline-block;
                text-decoration: none;
                font-weight: 600;
                color: #0d6efd;
                padding: 10px 16px;
                border-radius: 10px;
                background: #e7f1ff;
                transition: 0.25s;
                text-align: center;
            }

            .back-link:hover {
                background: #d0e4ff;
            }

            /* =========================
               NOTE
            ========================= */
            .note {
                text-align: center;
                margin-top: 16px;
                font-size: 14px;
                color: #6c757d;
            }

            .container {
                animation: fadeIn 0.6s ease, slideDown 0.6s ease;
            }

            @keyframes slideDown {
                from {
                    transform: translateY(-20px);
                }
                to {
                    transform: translateY(0);
                }
            }

        </style>
    </head>

    <body>

        <div class="container">

            <h2>CHANGE PASSWORD</h2>

            <div class="form-card">
                <form action="${pageContext.request.contextPath}/profile/change-password" method="post">

                    <div class="input-group">
                        <label>Old Password</label>
                        <input type="password" name="oldPassword" required />
                    </div>

                    <div class="input-group">
                        <label>New Password</label>
                        <input
                            type="password"
                            name="newPassword"
                            required
                            pattern="^\S{6,}$"
                            title="Password must be at least 6 characters long and must not contain spaces"
                            />
                    </div>

                    <div class="input-group">
                        <label>Confirm New Password</label>
                        <input type="password" name="confirmPassword" required />
                    </div>

                    <button type="submit">🔒 Change Password</button>

                    <% if (request.getAttribute("error") != null) {%>
                    <div class="message-error">
                        ⚠ <%= request.getAttribute("error")%>
                    </div>
                    <% } %>

                    <% if (request.getAttribute("msg") != null) {%>
                    <div class="message-success">
                        ✅ <%= request.getAttribute("msg")%>
                    </div>
                    <% }%>

                </form>
            </div>

            <br><hr><br>

            <a class="back-link" href="${pageContext.request.contextPath}/profile/view?userId=${user.userId}">
                ⬅ Back to Profile Detail
            </a>

            <div class="note">
                ⚡ Password must be at least 6 characters and must not contain spaces.
            </div>

        </div>

    </body>
</html>