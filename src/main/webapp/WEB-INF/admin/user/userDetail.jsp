

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="activeTab" value="${param.activeTab}" />
<c:set var="isProfessionalActive" value="${activeTab == 'professional'}" />
<c:set var="isBasicActive" value="${empty activeTab || activeTab != 'professional'}" />

<!-- ================= HEADER ================= -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-user text-primary me-2"></i>
            User detail
        </h2>
        <p class="text-muted mb-0">
            User account information and permissions
        </p>
    </div>

    <a href="${pageContext.request.contextPath}/admin/user/list"
       class="btn btn-outline-secondary">
        <i class="fa-solid fa-arrow-left me-1"></i>
        Back
    </a>
</div>

<!-- ================= TABS ================= -->
<ul class="nav nav-tabs mb-4">
    <li class="nav-item">
        <button class="nav-link ${isBasicActive ? 'active' : ''}"
                data-bs-toggle="tab"
                data-bs-target="#basicInfo">
            <i class="fa-solid fa-id-card me-1"></i>
            Basic info
        </button>
    </li>

    <c:if test="${user.roleName == 'Doctor'}">
        <li class="nav-item">
            <button class="nav-link ${isProfessionalActive ? 'active' : ''}"
                    data-bs-toggle="tab"
                    data-bs-target="#professionalInfo">
                <i class="fa-solid fa-stethoscope me-1"></i>
                Professional Info
            </button>
        </li>
    </c:if>
</ul>

<div class="tab-content">

    <!-- ================= BASIC INFO ================= -->
    <div class="tab-pane fade ${isBasicActive ? 'show active' : ''}"
         id="basicInfo">

        <div class="card shadow-sm mb-4">
            <div class="card-body">

                <div class="row g-4">

                    <div class="col-md-6">
                        <div class="detail-label">Username</div>
                        <div class="detail-value fw-semibold">
                            ${user.username}
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="detail-label">Fullname</div>
                        <div class="detail-value fw-semibold">
                            ${user.fullName}
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="detail-label">Role</div>
                        <div class="detail-value">
                            <span class="badge bg-light text-dark border">
                                ${user.roleName}
                            </span>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="detail-label">Email</div>
                        <div class="detail-value">
                            ${user.email}
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="detail-label">Phone</div>
                        <div class="detail-value">
                            ${user.phone}
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="detail-label">Status</div>
                        <div class="detail-value">
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

                <div class="mt-4">
                    <button class="btn btn-warning"
                            data-bs-toggle="modal"
                            data-bs-target="#editUserModal">
                        <i class="fa-solid fa-pen-to-square me-1"></i>
                        Edit
                    </button>
                </div>

            </div>
        </div>

        <!-- ================= EDIT MODAL (GIỮ LOGIC) ================= -->
        <!-- (Modal code của bạn GIỮ NGUYÊN, chỉ đổi UI input nếu muốn đồng bộ) -->
        <div class="modal fade" id="editUserModal" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content">

                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/user/edit">

                        <div class="modal-header">
                            <h5 class="modal-title">Edit user</h5>
                            <button type="button" class="btn-close"
                                    data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">

                            <input type="hidden" name="userId"
                                   value="${user.userId}" />

                            <c:if test="${not empty param.editError}">
                                <div class="alert alert-danger">
                                    ${param.editError}
                                </div>
                            </c:if>

                            <div class="row g-3">

                                <div class="col-md-6">
                                    <label>Username *</label>
                                    <input class="form-control"
                                           name="username"
                                           value="${user.username}" />
                                </div>

                                <div class="col-md-6">
                                    <label>Fullname *</label>
                                    <input class="form-control"
                                           name="fullName"
                                           value="${user.fullName}" />
                                </div>

                                <div class="col-md-6">
                                    <label>Role</label>
                                    <select name="roleId" class="form-select">
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
                                    <label>Email</label>
                                    <input class="form-control"
                                           name="email"
                                           value="${user.email}" />
                                </div>

                                <div class="col-md-6">
                                    <label>Phone</label>
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
                                Save
                            </button>
                        </div>

                    </form>
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
    </div>

    <!-- ================= PROFESSIONAL INFO ================= -->
    <c:if test="${user.roleName == 'Doctor'}">
        <div class="tab-pane fade ${isProfessionalActive ? 'show active' : ''}"
             id="professionalInfo">

            <div class="card shadow-sm">
                <div class="card-body">

                    <h5 class="mb-3">
                        <i class="fa-solid fa-stethoscope me-2 text-primary"></i>
                        Doctor's specialty
                    </h5>

                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/doctor-specialty/edit">

                        <input type="hidden" name="doctorId"
                               value="${user.userId}" />

                        <c:if test="${empty specialties}">
                            <p class="text-muted">
                                There is no list of specialties yet.
                            </p>
                        </c:if>

                        <c:forEach var="s" items="${specialties}">
                            <div class="border rounded p-3 mb-2">

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

                                <div class="form-check ms-4 mt-1">
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
                        </c:forEach>

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

        </div>
    </c:if>

</div>


<style>

    .detail-label {
        font-size: 0.85rem;
        color: #6c757d;
        margin-bottom: 4px;
    }

    .detail-value {
        font-size: 1rem;
        color: #212529;
    }

    .nav-tabs .nav-link {
        font-weight: 500;
    }

    .nav-tabs .nav-link.active {
        color: #0d6efd;
    }

</style>