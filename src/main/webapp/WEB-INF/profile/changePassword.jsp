<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="container-fluid mb-5">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-lock text-primary me-2"></i>
                Change Password
            </h2>

            <p class="text-muted mb-0">
                Update your account password securely
            </p>
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

                    <form action="${pageContext.request.contextPath}/profile/change-password" method="post">

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
                   href="${pageContext.request.contextPath}/profile/view">

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
