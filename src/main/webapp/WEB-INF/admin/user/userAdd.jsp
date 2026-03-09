<%--  
    Document   : userAdd
    Created on : Feb 7, 2026
    Author     : huytr
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid">

    <!-- ================= HEADER ================= -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-user-plus text-primary me-2"></i>
                Add User
            </h2>

            <p class="text-muted mb-0">
                Create a new user account in the system
            </p>
        </div>

        <a href="${pageContext.request.contextPath}/admin/user/list"
           class="btn btn-outline-secondary">

            <i class="fa-solid fa-arrow-left me-2"></i>
            Back to list

        </a>

    </div>



    <!-- ================= ERROR ================= -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger shadow-sm">

            <i class="fa-solid fa-exclamation-triangle me-2"></i>
            ${error}

        </div>
    </c:if>



    <!-- ================= FORM CARD ================= -->
    <div class="card shadow-sm">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">

                <i class="fa-solid fa-user text-primary me-2"></i>
                User Information

            </h5>

        </div>



        <div class="card-body">

            <form method="post">

                <div class="row g-4">

                    <!-- USERNAME -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Username <span class="text-danger">*</span>
                        </label>

                        <input type="text"
                               name="username"
                               value="${username}"
                               class="form-control"
                               placeholder="Enter username"
                               required>

                    </div>



                    <!-- FULL NAME -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Full Name <span class="text-danger">*</span>
                        </label>

                        <input type="text"
                               name="fullName"
                               value="${fullName}"
                               class="form-control"
                               placeholder="Enter full name"
                               required>

                    </div>



                    <!-- PHONE -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Phone
                        </label>

                        <input type="text"
                               name="phone"
                               value="${phone}"
                               class="form-control"
                               placeholder="Enter phone number">

                    </div>



                    <!-- EMAIL -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Email
                        </label>

                        <input type="email"
                               name="email"
                               value="${email}"
                               class="form-control"
                               placeholder="example@email.com">

                    </div>



                    <!-- ROLE -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Role <span class="text-danger">*</span>
                        </label>

                        <select name="roleId" class="form-select">

                            <c:forEach var="r" items="${roles}">

                                <option value="${r.roleId}"
                                        <c:if test="${roleId == r.roleId}">selected</c:if>>

                                        ${r.roleName}

                                </option>

                            </c:forEach>

                        </select>

                    </div>


                </div>



                <!-- ================= ACTION BUTTONS ================= -->

                <div class="d-flex justify-content-end gap-2 mt-4">

                    <a href="${pageContext.request.contextPath}/admin/user/list"
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