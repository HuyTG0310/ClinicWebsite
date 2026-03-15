<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="container-fluid mb-5">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-user text-primary me-2"></i>
                Profile Detail
            </h2>

            <p class="text-muted mb-0">
                View and manage your account information
            </p>
        </div>


        <div class="d-flex gap-2">
            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/profile/change-password?userId=${user.userId}">

                <i class="fa-solid fa-lock me-2"></i>
                Change Password

            </a>

            <button class="btn btn-warning"
                    data-bs-toggle="modal"
                    data-bs-target="#updateModal">

                <i class="fa-solid fa-pen-to-square me-2"></i>
                Edit

            </button>
        </div>
    </div>


    <!-- PROFILE INFORMATION -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-id-card text-primary me-2"></i>
                Account Information
            </h5>
        </div>

        <div class="card-body">

            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">User ID</label>
                    <div class="fw-semibold">${user.userId}</div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Username</label>
                    <div class="fw-semibold">${user.username}</div>
                </div>

            </div>



            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">Full Name</label>
                    <div class="fw-semibold">${user.fullName}</div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Role</label>
                    <div>
                        <span class="badge bg-light text-dark border">
                            ${user.roleName}
                        </span>
                    </div>
                </div>

            </div>



            <div class="row">

                <div class="col-md-6">
                    <label class="text-muted small">Phone</label>
                    <div class="fw-semibold">${user.phone}</div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Email</label>
                    <div class="fw-semibold">${user.email}</div>
                </div>

            </div>

        </div>
    </div>

</div>



<!-- MODAL OVERLAY -->



<!-- UPDATE MODAL --><div class="modal fade" id="updateModal" tabindex="-1">

    <div class="modal-dialog modal-lg modal-dialog-centered">

        <div class="modal-content">

            <div class="modal-header">

                <h5 class="modal-title">
                    <i class="fa-solid fa-user-pen me-2"></i>
                    Update Profile
                </h5>

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"></button>

            </div>


            <form action="${pageContext.request.contextPath}/profile/update" method="post">

                <div class="modal-body">

                    <input type="hidden" name="userId" value="${user.userId}" />

                    <div class="row g-3">

                        <!-- Username -->
                        <div class="col-md-6">

                            <label class="form-label fw-semibold">
                                Username
                            </label>

                            <input type="text"
                                   class="form-control"
                                   value="${user.username}"
                                   readonly>

                        </div>


                        <!-- Full Name -->
                        <div class="col-md-6">

                            <label class="form-label fw-semibold">
                                Full Name
                            </label>

                            <input
                                type="text"
                                name="fullName"
                                class="form-control"
                                value="${user.fullName}"
                                required
                                pattern="^([A-ZÀ-Ỹ][a-zà-ỹ]+)(\s[A-ZÀ-Ỹ][a-zà-ỹ]+)*$"
                                title="Each word must start with a capital letter and contain only letters">

                        </div>


                        <!-- Phone -->
                        <div class="col-md-6">

                            <label class="form-label fw-semibold">
                                Phone
                            </label>

                            <input
                                type="text"
                                name="phone"
                                class="form-control"
                                value="${user.phone}"
                                required
                                pattern="^0\d{9}$"
                                title="Phone number must contain exactly 10 digits and start with 0">

                        </div>


                        <!-- Email -->
                        <div class="col-md-6">

                            <label class="form-label fw-semibold">
                                Email
                            </label>

                            <input
                                type="email"
                                name="email"
                                class="form-control"
                                value="${user.email}"
                                required
                                pattern="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.com$"
                                title="Email must contain '@' and end with .com"
                                oninput="this.value=this.value.trim()">

                        </div>

                    </div>

                </div>


                <div class="modal-footer">

                    <button type="button"
                            class="btn btn-outline-secondary"
                            data-bs-dismiss="modal">

                        Cancel

                    </button>

                    <button type="submit"
                            class="btn btn-primary px-4"
                            id="saveBtn">

                        <i class="fa-solid fa-save me-1"></i>
                        Save

                    </button>

                </div>

            </form>

        </div>

    </div>

</div>



