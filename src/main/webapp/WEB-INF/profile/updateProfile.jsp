<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Update Profile</title>

        <style>
            body {
                margin: 0;
                padding: 0;
                font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #141e30, #243b55);
                min-height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                color: #333;
            }

            .container {
                width: 650px;
                max-width: 95%;
                background: rgba(255, 255, 255, 0.12);
                backdrop-filter: blur(12px);
                border-radius: 20px;
                padding: 35px;
                box-shadow: 0px 15px 45px rgba(0, 0, 0, 0.4);
                border: 1px solid rgba(255, 255, 255, 0.2);
                animation: fadeIn 0.7s ease-in-out;
            }

            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(25px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            h2 {
                text-align: center;
                font-size: 30px;
                font-weight: 800;
                color: white;
                margin-bottom: 30px;
                letter-spacing: 2px;
                text-transform: uppercase;
            }

            .form-card {
                background: white;
                border-radius: 18px;
                padding: 28px 30px;
                box-shadow: 0px 12px 25px rgba(0, 0, 0, 0.2);
            }

            .input-group {
                margin-bottom: 18px;
            }

            label {
                font-weight: 750;
                display: block;
                margin-bottom: 8px;
                color: #333;
                font-size: 15px;
            }

            input[type="text"] {
                width: 100%;
                padding: 13px 14px;
                border-radius: 12px;
                border: 1px solid #ddd;
                font-size: 15px;
                outline: none;
                transition: 0.25s;
            }

            input[type="text"]:focus {
                border: 1px solid #00c6ff;
                box-shadow: 0px 0px 10px rgba(0, 198, 255, 0.25);
            }

            input[readonly] {
                background: #f3f3f3;
                color: #555;
                font-weight: 700;
                cursor: not-allowed;
            }

            button {
                width: 100%;
                padding: 14px;
                border: none;
                border-radius: 12px;
                font-size: 16px;
                font-weight: 850;
                cursor: pointer;
                background: linear-gradient(135deg, #00c6ff, #0072ff);
                color: white;
                transition: 0.3s ease;
                box-shadow: 0px 10px 20px rgba(0, 0, 0, 0.25);
            }

            button:hover {
                transform: translateY(-3px);
                box-shadow: 0px 14px 30px rgba(0, 0, 0, 0.35);
                opacity: 0.95;
            }

            hr {
                margin: 25px 0;
                border: none;
                height: 1px;
                background: linear-gradient(to right, transparent, rgba(255,255,255,0.7), transparent);
            }

            .back-link {
                display: inline-block;
                text-decoration: none;
                font-weight: 750;
                color: white;
                padding: 12px 18px;
                border-radius: 12px;
                background: rgba(255, 255, 255, 0.2);
                transition: 0.3s;
                text-align: center;
            }

            .back-link:hover {
                background: rgba(255, 255, 255, 0.35);
                transform: translateY(-2px);
            }

            .note {
                text-align: center;
                margin-top: 18px;
                font-size: 14px;
                color: rgba(255, 255, 255, 0.85);
                font-weight: 500;
            }

            .note span {
                font-weight: 750;
                color: white;
            }
        </style>
    </head>

    <body>

        <div class="container">

            <h2>UPDATE PROFILE</h2>

            <div class="form-card">
                <form action="${pageContext.request.contextPath}/profile/update" method="post">

                    <input type="hidden" name="userId" value="${user.userId}" />

                    <div class="input-group">
                        <label>Username:</label>
                        <input type="text" value="${user.username}" readonly />
                    </div>

                    <div class="input-group">
                        <label>Full Name:</label>
                        <input type="text" name="fullName" value="${user.fullName}" required />
                    </div>

                    <div class="input-group">
                        <label>Phone:</label>
                        <input type="text" name="phone" value="${user.phone}" />
                    </div>

                    <div class="input-group">
                        <label>Email:</label>
                        <input type="text" name="email" value="${user.email}" />
                    </div>

                    <button type="submit">💾 Save Changes</button>
                </form>
            </div>

            <br><hr><br>

            <!-- BACK TO PROFILE VIEW -->
            <a class="back-link" href="${pageContext.request.contextPath}/profile/view?userId=${user.userId}">
                ⬅ Back to Profile
            </a>

            <div class="note">
                ✨ Update your information carefully to keep your account <span>secure</span>.
            </div>

        </div>

    </body>
</html>