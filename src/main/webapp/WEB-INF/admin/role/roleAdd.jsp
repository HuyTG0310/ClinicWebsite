<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container-fluid">

    <!-- ================= HEADER ================= -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-user-shield text-primary me-2"></i>
                Add Role
            </h2>

            <p class="text-muted mb-0">
                Create a new system role to manage user permissions
            </p>
        </div>

        <a href="${pageContext.request.contextPath}/admin/role/list"
           class="btn btn-outline-secondary">

            <i class="fa-solid fa-arrow-left me-2"></i>
            Back to list

        </a>

    </div>



    <!-- ================= ERROR ================= -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger shadow-sm">

            <i class="fa-solid fa-circle-exclamation me-2"></i>
            ${error}

        </div>
    </c:if>



    <!-- ================= CARD ================= -->
    <div class="card shadow-sm">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">

                <i class="fa-solid fa-id-card text-primary me-2"></i>
                Role Information

            </h5>

        </div>



        <div class="card-body">

            <form action="${pageContext.request.contextPath}/admin/role/create"
                  method="POST">

                <div class="row g-4">

                    <!-- ROLE NAME -->
                    <div class="col-md-6">

                        <label for="roleName"
                               class="form-label fw-semibold">

                            Role Name <span class="text-danger">*</span>

                        </label>

                        <input type="text"
                               class="form-control"
                               id="roleName"
                               name="roleName"
                               placeholder="Example: Accountant, Nurse..."
                               required>

                        <div class="form-text">
                            Role name should be unique and easy to distinguish
                        </div>

                    </div>

                </div>



                <!-- ================= ACTION BUTTONS ================= -->

                <div class="d-flex justify-content-end gap-2 mt-4">

                    <a href="${pageContext.request.contextPath}/admin/role/list"
                       class="btn btn-outline-secondary">

                        Cancel

                    </a>

                    <button type="submit"
                            class="btn btn-primary px-4">

                        <i class="fa-solid fa-save me-1"></i>
                        Save

                    </button>

                </div>


            </form>

        </div>

    </div>

</div>