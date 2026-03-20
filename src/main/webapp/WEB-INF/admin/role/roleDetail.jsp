<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container-fluid">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-user-shield text-primary me-2"></i>
                Role  privilege
            </h2>
            <p class="text-muted mb-0">
                Manage role information and privileges
            </p>
        </div>

        <div class="d-flex gap-2">
            <a href="list" class="btn btn-outline-secondary">
                <i class="fa-solid fa-arrow-left me-2"></i>
                Back to list
            </a>

            <button type="button"
                    class="btn btn-warning px-4"
                    data-bs-toggle="modal"
                    data-bs-target="#editRoleModal">

                <i class="fa-solid fa-pen-to-square me-2"></i>
                Edit
            </button>
        </div>

    </div>


    <!-- SUCCESS MESSAGE -->
    <c:if test="${param.msg eq 'success'}">

        <div class="alert alert-success alert-dismissible fade show shadow-sm">

            <i class="fa-solid fa-check-circle me-2"></i>
            Update role successfully!

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert"></button>

        </div>

    </c:if>



    <div class="row g-4">

        <!-- ROLE INFORMATION -->
        <div class="col-lg-3">

            <div class="card shadow-sm h-100">

                <div class="card-header bg-white">

                    <h5 class="mb-0">
                        <i class="fa-solid fa-id-badge text-primary me-2"></i>
                        Role Information
                    </h5>

                </div>

                <div class="card-body text-center py-4">

                    <div class="mb-3">
                        <i class="fa-solid fa-user-shield fa-3x text-primary opacity-25"></i>
                    </div>

                    <h5 class="fw-bold mb-1">
                        ${role.roleName}
                    </h5>

                    <p class="text-muted small mb-3">
                        ID: #${role.roleId}
                    </p>


                    <span class="text-muted small d-block mb-2">
                        Current Status
                    </span>

                    <c:choose>

                        <c:when test="${role.isActive}">

                            <span class="badge bg-success-subtle text-success px-3 py-2">
                                <i class="fa-solid fa-circle-check me-1"></i>
                                Active
                            </span>

                        </c:when>

                        <c:otherwise>

                            <span class="badge bg-danger-subtle text-danger px-3 py-2">
                                <i class="fa-solid fa-ban me-1"></i>
                                Inactive
                            </span>

                        </c:otherwise>

                    </c:choose>

                </div>

            </div>

        </div>



        <!-- PRIVILEGES -->
        <div class="col-lg-9">

            <div class="card shadow-sm h-100">

                <div class="card-header bg-white d-flex justify-content-between align-items-center">

                    <h5 class="mb-0">
                        <i class="fa-solid fa-key text-primary me-2"></i>
                        Granted Privileges
                    </h5>

                    <span class="badge bg-primary-subtle text-primary border">
                        ${currentPrivilegeIds.size()} Privileges
                    </span>

                </div>


                <div class="card-body p-0">

                    <div class="table-responsive"
                         style="max-height:420px; overflow-y:auto;">

                        <table class="table table-hover align-middle mb-0">

                            <thead class="table-light">

                                <tr>
                                    <th width="35%">
                                        Privilege Code
                                    </th>

                                    <th>
                                        Description
                                    </th>

                                </tr>

                            </thead>


                            <tbody>

                                <c:if test="${empty currentPrivilegeIds}">

                                    <tr>

                                        <td colspan="3"
                                            class="text-center text-muted py-5">

                                            <i class="fa-solid fa-box-open fa-2x mb-2 opacity-50"></i>

                                            <p class="mb-0">
                                                No privileges granted for this role yet.
                                            </p>

                                        </td>

                                    </tr>

                                </c:if>


                                <c:forEach var="p" items="${allPrivileges}">

                                    <c:if test="${currentPrivilegeIds.contains(p.privilegeId)}">

                                        <tr>
                                            <td>
                                                <div class="text-primary">
                                                    ${p.privilegeCode}
                                                </div>
                                            </td>

                                            <td>${p.description}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <div class="modal fade" id="editRoleModal" tabindex="-1" data-bs-backdrop="static">

        <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">

            <div class="modal-content">

                <div class="modal-header">

                    <h5 class="modal-title">
                        <i class="fa-solid fa-pen-to-square me-2"></i>
                        Edit Role: ${role.roleName}
                    </h5>

                    <button type="button"
                            class="btn-close"
                            data-bs-dismiss="modal"></button>

                </div>


                <form action="detail" method="post">

                    <input type="hidden"
                           name="roleId"
                           value="${role.roleId}">


                    <div class="modal-body">

                        <!-- ROLE STATUS -->

                        <div class="card border-0 shadow-sm mb-4">

                            <div class="card-body d-flex justify-content-between align-items-center">

                                <div>

                                    <h6 class="fw-bold mb-1">
                                        Role Status
                                    </h6>

                                    <small class="text-muted">
                                        Activate or deactivate this role system-wide
                                    </small>

                                </div>


                                <div class="form-check form-switch fs-5">

                                    <input class="form-check-input"
                                           type="checkbox"
                                           name="isActive"
                                           id="roleStatusEdit"
                                           value="true"
                                           ${role.isActive ? 'checked' : ''}>

                                    <label class="form-check-label ms-2">
                                        Active
                                    </label>

                                </div>

                            </div>

                        </div>



                        <!-- PRIVILEGE SELECT -->

                        <div class="card border-0 shadow-sm">

                            <div class="card-header bg-white">

                                <h6 class="fw-bold mb-0">
                                    <i class="fa-solid fa-list-check me-2"></i>
                                    Select Privileges
                                </h6>

                            </div>


                            <div class="table-responsive"
                                 style="max-height:350px; overflow-y:auto;">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th style="width:60px" class="text-center">
                                                <input class="form-check-input"
                                                       type="checkbox"
                                                       id="checkAll">
                                            </th>

                                            <th>Privilege Code</th>

                                            <th>Description</th>
                                        </tr>
                                    </thead>

                                    <tbody>

                                        <c:forEach var="p" items="${allPrivileges}">
                                            <tr>
                                                <td class="text-center">
                                                    <input class="form-check-input privilege-checkbox"
                                                           type="checkbox"
                                                           name="privilegeIds"
                                                           value="${p.privilegeId}"
                                                           ${currentPrivilegeIds.contains(p.privilegeId) ? 'checked' : ''}>
                                                </td>

                                                <td>
                                                    <div class="text-primary">
                                                        ${p.privilegeCode}
                                                    </div>
                                                </td>

                                                <td>${p.description}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
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
                                class="btn btn-primary px-4">
                            <i class="fa-solid fa-save me-1"></i>
                            Save
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<script>
    // Xử lý nút Check All trong Modal
    document.getElementById('checkAll').addEventListener('change', function () {
        var checkboxes = document.querySelectorAll('.privilege-checkbox');
        for (var checkbox of checkboxes) {
            checkbox.checked = this.checked;
        }
    });

    // Tiện ích nhỏ: Đổi chữ Active/Inactive khi click toggle switch
    document.getElementById('roleStatusEdit').addEventListener('change', function () {
        let label = document.querySelector('label[for="roleStatusEdit"]');
        if (this.checked) {
            label.textContent = "Active";
            label.classList.remove("text-danger");
            label.classList.add("text-success");
        } else {
            label.textContent = "Inactive";
            label.classList.remove("text-success");
            label.classList.add("text-danger");
        }
    });
</script>