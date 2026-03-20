<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="container-fluid mb-5">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-lock text-primary me-2"></i>
                Change Password
            </h2>

<<<<<<< HEAD
    /* TITLE */
    .change-password-card h2 {
        text-align: center;
        font-size: 24px;
        font-weight: 700;
        color: #0d6efd;
        margin-bottom: 26px;
        letter-spacing: 1px;
        text-transform: uppercase;
    }

    /* FORM CARD */
    .change-password-card .form-card {
        background: #f8f9fc;
        border-radius: 14px;
        padding: 24px 26px;
        border: 1px solid #dee2e6;
    }

    /* LABEL */
    .change-password-card label {
        font-weight: 600;
        display: block;
        margin-bottom: 6px;
        color: #495057;
        font-size: 14px;
    }

    .change-password-card .input-group {
        margin-bottom: 16px;
    }

    /* INPUT */
    .change-password-card input[type="password"] {
        width: 100%;
        padding: 11px 12px;
        border-radius: 8px;
        border: 1px solid #ced4da;
        font-size: 14px;
    }

    .change-password-card input[type="password"]:focus {
        outline: none;
        border-color: #0d6efd;
        box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.15);
    }

    /* BUTTON */
    .change-password-btn {
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

    .change-password-btn:hover {
        background-color: #bb2d3b;
        transform: translateY(-2px);
    }

    /* MESSAGE */
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

    /* DIVIDER */
    .change-password-card hr {
        margin: 26px 0;
        border: none;
        height: 1px;
        background: #dee2e6;
    }

    /* BACK LINK */
    .change-password-card .back-link {
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

    .change-password-card .back-link:hover {
        background: #d0e4ff;
    }

    /* NOTE */
    .change-password-card .note {
        text-align: center;
        margin-top: 16px;
        font-size: 14px;
        color: #6c757d;
    }

    /* ANIMATION */
    @keyframes slideDown {
        from {
            transform: translateY(-20px);
        }
        to {
            transform: translateY(0);
        }
    }
</style>


<div class="change-password-page">

    <div class="change-password-card">

        <h2>Change Password</h2>

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
                        pattern="^\S{3,}$"
                        title="Password must be at least 6 characters long and must not contain spaces"
                        />
                </div>

                <div class="input-group">
                    <label>Confirm New Password</label>
                    <input type="password" name="confirmPassword" required />
                </div>

                <button type="submit" class="change-password-btn">
                    🔒 Change Password
                </button>

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

        <hr>

        <div class="text-center">
            <a class="back-link"
               href="${pageContext.request.contextPath}/profile/view">
                ⬅ Back to Profile Detail
            </a>
        </div>

        <div class="note">
            ⚡ Password must be at least 6 characters and must not contain spaces.
=======
            <p class="text-muted mb-0">
                Update your account password securely
            </p>
>>>>>>> e69fd48a9d4e1c25401d242ea75c1869f56faf3b
        </div>

    </div>



    <!-- CENTERED CARD -->
    <div class="row justify-content-center">

        <div class="col-md-6 col-lg-5">

            <div class="card shadow-sm">

                <div class="card-header bg-white py-3">
                    <h5 class="mb-0">
                        <i class="fa-solid fa-key text-primary me-2"></i>
                        Security Information
                    </h5>
                </div>



                <div class="card-body">

                    <form action="${basePath}/profile/change-password" method="post">

                        <div class="mb-3">

                            <label class="form-label fw-semibold">
                                Old Password
                            </label>

                            <input type="password"
                                   class="form-control"
                                   name="oldPassword"
                                   required />

                        </div>



                        <div class="mb-3">

                            <label class="form-label fw-semibold">
                                New Password
                            </label>

                            <input type="password"
                                   class="form-control"
                                   name="newPassword"
                                   required
                                   pattern="^\S{6,}$"
                                   title="Password must be at least 6 characters long and must not contain spaces" />

                        </div>



                        <div class="mb-4">

                            <label class="form-label fw-semibold">
                                Confirm New Password
                            </label>

                            <input type="password"
                                   class="form-control"
                                   name="confirmPassword"
                                   required />

                        </div>



                        <button type="submit"
                                class="btn btn-primary w-100">

                            <i class="fa-solid fa-lock me-2"></i>
                            Change Password

                        </button>



                        <% if (request.getAttribute("error") != null) {%>

                        <div class="alert alert-danger mt-3 mb-0">

                            <i class="fa-solid fa-triangle-exclamation me-2"></i>
                            <%= request.getAttribute("error")%>

                        </div>

                        <% } %>



                        <% if (request.getAttribute("msg") != null) {%>

                        <div class="alert alert-success mt-3 mb-0">

                            <i class="fa-solid fa-circle-check me-2"></i>
                            <%= request.getAttribute("msg")%>

                        </div>

                        <% }%>


                    </form>

                </div>

            </div>



            <!-- BACK BUTTON -->
            <div class="text-center mt-3">

                <a class="btn btn-outline-secondary"
                   href="${basePath}/profile/view">

                    <i class="fa-solid fa-arrow-left me-2"></i>
                    Back to Profile

                </a>

            </div>



            <p class="text-muted text-center small mt-2">
                Password must be at least 6 characters and must not contain spaces.
            </p>


        </div>
    </div>

</div>
