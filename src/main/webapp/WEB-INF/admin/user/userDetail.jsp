

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="activeTab" value="${param.activeTab}" />
<c:set var="isProfessionalActive" value="${activeTab == 'professional'}" />
<c:set var="isBasicActive" value="${empty activeTab || activeTab != 'professional'}" />



<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="activeTab" value="${param.activeTab}" />
<c:set var="isProfessionalActive" value="${activeTab == 'professional'}" />
<c:set var="isBasicActive" value="${empty activeTab || activeTab != 'professional'}" />

<div class="container-fluid">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-user text-primary me-2"></i>
                Staff Detail
            </h2>
            <p class="text-muted mb-0">
                User account information and permissions
            </p>
        </div>

        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/admin/user/list"
               class="btn btn-outline-secondary">
                <i class="fa-solid fa-arrow-left me-2"></i>
                Back to list
            </a>

            <button class="btn btn-warning"
                    data-bs-toggle="modal"
                    data-bs-target="#editUserModal">
                <i class="fa-solid fa-pen-to-square me-2"></i>
                Edit
            </button>
        </div>

    </div>



    <!-- BASIC INFORMATION -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-id-card text-primary me-2"></i>
                Basic Information
            </h5>
        </div>

        <div class="card-body">

            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">Username</label>
                    <div class="fw-semibold">${user.username}</div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Fullname</label>
                    <div class="fw-semibold">${user.fullName}</div>
                </div>

            </div>


            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">Role</label>
                    <div>
                        <span class="badge bg-light text-dark border">
                            ${user.roleName}
                        </span>
                    </div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Status</label>
                    <div>

                        <c:choose>

                            <c:when test="${user.isActive}">
                                <span class="badge bg-success">
                                    <i class="fa-solid fa-check-circle me-1"></i>
                                    Active
                                </span>
                            </c:when>

                            <c:otherwise>
                                <span class="badge bg-danger">
                                    <i class="fa-solid fa-times-circle me-1"></i>
                                    Inactive
                                </span>
                            </c:otherwise>

                        </c:choose>

                    </div>
                </div>

            </div>


            <div class="row">

                <div class="col-md-6">
                    <label class="text-muted small">Email</label>
                    <div>${user.email}</div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Phone</label>
                    <div>${user.phone}</div>
                </div>

            </div>

        </div>

    </div>



    <!-- DOCTOR SPECIALTY -->
    <c:if test="${user.roleName == 'Doctor'}">

        <div class="card shadow-sm mb-4">

            <div class="card-header bg-white py-3">
                <h5 class="mb-0">
                    <i class="fa-solid fa-stethoscope text-primary me-2"></i>
                    Doctor Specialty
                </h5>
            </div>

            <div class="card-body">

                <form method="post"
                      action="${pageContext.request.contextPath}/admin/doctor-specialty/edit">

                    <input type="hidden"
                           name="doctorId"
                           value="${user.userId}" />

                    <div class="row">

                        <c:forEach var="s" items="${specialties}">

                            <div class="col-md-6 mb-3">

                                <div class="border rounded p-3">

                                    <div class="form-check">

                                        <input class="form-check-input"
                                               type="checkbox"
                                               id="spec_${s.specialtyId}"
                                               name="specialtyIds"
                                               value="${s.specialtyId}"
                                               <c:if test="${assignedSpecialtyIds.contains(s.specialtyId)}">
                                                   checked
                                               </c:if>>

                                        <label class="form-check-label fw-semibold"
                                               for="spec_${s.specialtyId}">
                                            ${s.name}
                                        </label>

                                    </div>


                                    <div class="form-check ms-4 mt-2">

                                        <input class="form-check-input"
                                               type="radio"
                                               id="primary_${s.specialtyId}"
                                               name="primarySpecialtyId"
                                               value="${s.specialtyId}"
                                               <c:if test="${primarySpecialtyId == s.specialtyId}">
                                                   checked
                                               </c:if>>

                                        <label class="form-check-label text-muted"
                                               for="primary_${s.specialtyId}">
                                            Primary specialty
                                        </label>

                                    </div>

                                </div>

                            </div>

                        </c:forEach>

                    </div>


                    <c:if test="${not empty param.specError}">
                        <div class="alert alert-danger mt-3">
                            <i class="fa-solid fa-exclamation-triangle me-2"></i>
                            ${param.specError}
                        </div>
                    </c:if>


                    <button type="submit"
                            class="btn btn-primary mt-3">
                        <i class="fa-solid fa-save me-1"></i>
                        Save specialty
                    </button>

                </form>

            </div>

        </div>

    </c:if>



    <!-- ================= EDIT USER MODAL ================= -->

    <div class="modal fade" id="editUserModal" tabindex="-1">

        <div class="modal-dialog modal-lg modal-dialog-centered">

            <div class="modal-content">

                <form method="post"
                      action="${pageContext.request.contextPath}/admin/user/edit">

                    <div class="modal-header">

                        <h5 class="modal-title">
                            <i class="fa-solid fa-pen-to-square me-2"></i>
                            Edit User
                        </h5>

                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"></button>

                    </div>


                    <div class="modal-body">

                        <input type="hidden"
                               name="userId"
                               value="${user.userId}" />

                        <c:if test="${not empty param.editError}">
                            <div class="alert alert-danger">
                                ${param.editError}
                            </div>
                        </c:if>


                        <div class="row g-3">

                            <div class="col-md-6">
                                <label class="form-label fw-semibold">
                                    Username
                                </label>

                                <input class="form-control"
                                       name="username"
                                       value="${user.username}" />
                            </div>


                            <div class="col-md-6">
                                <label class="form-label fw-semibold">
                                    Fullname
                                </label>

                                <input class="form-control"
                                       name="fullName"
                                       value="${user.fullName}" />
                            </div>


                            <div class="col-md-6">
                                <label class="form-label fw-semibold">
                                    Role
                                </label>

                                <select name="roleId"
                                        class="form-select">

                                    <c:forEach var="r" items="${roles}">
                                        <option value="${r.roleId}"
                                                <c:if test="${r.roleId == user.roleId}">
                                                    selected
                                                </c:if>>
                                            ${r.roleName}
                                        </option>
                                    </c:forEach>

                                </select>

                            </div>


                            <div class="col-md-6">
                                <label class="form-label fw-semibold">
                                    Email
                                </label>

                                <input class="form-control"
                                       name="email"
                                       value="${user.email}" />
                            </div>


                            <div class="col-md-6">
                                <label class="form-label fw-semibold">
                                    Phone
                                </label>

                                <input class="form-control"
                                       name="phone"
                                       value="${user.phone}" />
                            </div>


                            <div class="col-md-6 d-flex align-items-center">

                                <div class="form-check form-switch mt-4">

                                    <input class="form-check-input"
                                           type="checkbox"
                                           name="isActive"
                                           <c:if test="${user.isActive}">
                                               checked
                                           </c:if>>

                                    <label class="form-check-label">
                                        Active
                                    </label>

                                </div>

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
                                class="btn btn-primary">
                            <i class="fa-solid fa-save me-1"></i>
                            Save
                        </button>

                    </div>

                </form>

            </div>

        </div>

    </div>
</div>


<c:if test="${not empty param.editError}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const modal = new bootstrap.Modal(
                    document.getElementById("editUserModal")
                    );
            modal.show();
        });
    </script>
</c:if>    