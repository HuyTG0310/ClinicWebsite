<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<div class="container-fluid">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h4 class="fw-bold text-primary mb-0">
                <i class="fa-solid fa-circle-info me-2"></i>
                Specialty Detail
            </h4>
            <small class="text-muted">View specialty information</small>
        </div>

        <a href="${pageContext.request.contextPath}/admin/specialty/list"
           class="btn btn-outline-secondary">

            <i class="fa-solid fa-arrow-left me-2"></i>
            Back
        </a>

    </div>


    <!-- DETAIL CARD -->
    <div class="card border-0 shadow-sm">

        <div class="card-body">

            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">ID</label>
                    <div class="fw-semibold">#${specialty.specialtyId}</div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Status</label>

                    <div>
                        <span class="badge ${specialty.isActive ? 'bg-success-subtle text-success border' : 'bg-secondary-subtle text-secondary border'}">

                            <i class="fa-solid ${specialty.isActive ? 'fa-check' : 'fa-ban'} me-1"></i>

                            ${specialty.isActive ? 'Active' : 'Inactive'}
                        </span>
                    </div>

                </div>

            </div>


            <div class="mb-3">
                <label class="text-muted small">Name</label>
                <div class="fw-medium fs-5">${specialty.name}</div>
            </div>

            <div>
                <label class="text-muted small">Description</label>
                <div class="text-muted">
                    ${specialty.description}
                </div>
            </div>

        </div>

    </div>


    <!-- ACTION BUTTON -->
    <div class="mt-4">

        <button class="btn btn-warning shadow-sm"
                onclick="openEditModal(
                                '${specialty.specialtyId}',
                                '${specialty.name}',
                                '${specialty.description}',
                                '${specialty.isActive}'
                                )">

            <i class="fa-solid fa-pen-to-square me-2"></i>
            Edit
        </button>

    </div>
    <!-- ===== EDIT MODAL ===== -->

    <div class="modal fade" id="editModal" tabindex="-1">

        <div class="modal-dialog">

            <form class="modal-content"
                  method="post"
                  action="${pageContext.request.contextPath}/admin/specialty/detail">

                <div class="modal-header">

                    <h5 class="modal-title">
                        <i class="fa-solid fa-pen me-2 text-primary"></i>
                        Edit Specialty
                    </h5>

                    <button type="button"
                            class="btn-close"
                            data-bs-dismiss="modal"></button>

                </div>

                <div class="modal-body">
                    <!-- VALIDATE ERROR -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">
                            ${error}
                        </div>
                    </c:if>
                    <input type="hidden"
                           name="id"
                           value="${specialty.specialtyId}">

                    <div class="mb-3">
                        <label class="form-label">Name</label>

                        <input type="text"
                               class="form-control"
                               name="name"
                               value="${specialty.name}">
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Description</label>

                        <textarea class="form-control"
                                  name="description"
                                  rows="4">${specialty.description}</textarea>
                    </div>

                    <!-- 🔥 QUAN TRỌNG -->
                    <div class="mb-4">

                        <label class="form-label fw-semibold text-muted">
                            <i class="fa-solid fa-toggle-on me-2 text-primary"></i>
                            Status
                        </label>

                        <select name="isActive" class="form-select shadow-sm">

                            <option value="true"
                                    ${specialty.isActive ? "selected" : ""}>
                                🟢 Active
                            </option>

                            <option value="false"
                                    ${!specialty.isActive ? "selected" : ""}>
                                🔴 Inactive
                            </option>

                        </select>

                    </div>

                </div>

                <div class="modal-footer">

                    <button class="btn btn-primary">
                        <i class="fa-solid fa-floppy-disk me-2"></i>
                        Save
                    </button>

                    <button type="button"
                            class="btn btn-secondary"
                            data-bs-dismiss="modal">
                        Cancel
                    </button>

                </div>

            </form>

        </div>

    </div>
    <script>
        function openEditModal(id, name, desc, active) {

            document.querySelector('input[name="id"]').value = id;
            document.querySelector('input[name="name"]').value = name;
            document.querySelector('textarea[name="description"]').value = desc;

            // FIX CHÍNH
            document.querySelector('select[name="isActive"]').value = active;

            new bootstrap.Modal(
                    document.getElementById("editModal")
                    ).show();
        }

// 🔥 AUTO OPEN MODAL WHEN VALIDATE FAIL
        <c:if test="${openModal}">
        openEditModal();
        </c:if>
    </script>
</div>
