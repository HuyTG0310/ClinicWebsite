<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<div class="container-fluid">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-stethoscope text-primary me-2"></i>
                Specialty Detail
            </h2>

            <p class="text-muted mb-0">
                View specialty information
            </p>
        </div>

        <div class="d-flex gap-2">

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

            <a href="${pageContext.request.contextPath}/admin/specialty/list"
               class="btn btn-outline-secondary">

                <i class="fa-solid fa-arrow-left me-2"></i>
                Back to list
            </a>

        </div>

    </div>



    <!-- DETAIL CARD -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-stethoscope text-primary me-2"></i>
                Specialty Information
            </h5>
        </div>

        <div class="card-body">


            <div class="row mb-3">

                <!-- ID -->
                <div class="col-md-6">

                    <label class="text-muted small">
                        Specialty ID
                    </label>

                    <div class="fw-semibold">
                        #${specialty.specialtyId}
                    </div>

                </div>


                <!-- STATUS -->
                <div class="col-md-6">

                    <label class="text-muted small">
                        Status
                    </label>

                    <div>

                        <span class="badge ${specialty.isActive ? 'bg-success' : 'bg-secondary'}">

                            <i class="fa-solid ${specialty.isActive ? 'fa-check' : 'fa-ban'} me-1"></i>

                            ${specialty.isActive ? 'Active' : 'Inactive'}

                        </span>

                    </div>

                </div>

            </div>



            <!-- NAME -->
            <div class="mb-3">

                <label class="text-muted small">
                    Name
                </label>

                <div class="fs-5 fw-bold">
                    ${specialty.name}
                </div>

            </div>



            <!-- DESCRIPTION -->
            <div>

                <label class="text-muted small">
                    Description
                </label>

                <div>

                    <c:choose>

                        <c:when test="${not empty specialty.description}">
                            ${specialty.description}
                        </c:when>

                        <c:otherwise>
                            <span class="text-muted">
                                <i class="fa-solid fa-circle-info me-1"></i>
                                No description available
                            </span>
                        </c:otherwise>

                    </c:choose>

                </div>

            </div>

        </div>

    </div>



    <!-- ===== EDIT MODAL ===== -->

    <div class="modal fade" id="editModal" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">

                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fa-solid fa-pen-to-square me-2"></i>
                        Edit Specialty
                    </h5>

                    <button type="button"
                            class="btn-close"
                            data-bs-dismiss="modal"></button>
                </div>

                <form method="post"
                      action="${pageContext.request.contextPath}/admin/specialty/detail">

                    <div class="modal-body">

                        <input type="hidden"
                               name="id"
                               value="${specialty.specialtyId}" />

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                <i class="fa-solid fa-triangle-exclamation me-2"></i>
                                ${error}
                            </div>
                        </c:if>

                        <div class="row g-3">

                            <!-- Name -->
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">
                                    Specialty Name
                                </label>

                                <input type="text"
                                       class="form-control"
                                       name="name"
                                       value="${specialty.name}">
                            </div>

                            <!-- Status -->
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">
                                    Status
                                </label>

                                <select name="isActive"
                                        class="form-select">

                                    <option value="true"
                                            ${specialty.isActive ? "selected" : ""}>
                                        Active
                                    </option>

                                    <option value="false"
                                            ${!specialty.isActive ? "selected" : ""}>
                                        Inactive
                                    </option>

                                </select>
                            </div>

                            <!-- Description -->
                            <div class="col-md-12">
                                <label class="form-label fw-semibold">
                                    Description
                                </label>

                                <textarea class="form-control"
                                          name="description"
                                          rows="3">${specialty.description}</textarea>
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

    function openEditModal(id, name, desc, active) {

        document.querySelector('input[name="id"]').value = id;
        document.querySelector('input[name="name"]').value = name;
        document.querySelector('textarea[name="description"]').value = desc;

        document.querySelector('select[name="isActive"]').value = active;

        new bootstrap.Modal(
                document.getElementById("editModal")
                ).show();

    }


// AUTO OPEN MODAL WHEN VALIDATE FAIL
    <c:if test="${openModal}">
    openEditModal(
            '${specialty.specialtyId}',
            '${specialty.name}',
            '${specialty.description}',
            '${specialty.isActive}'
            );
    </c:if>

</script>



<style>

    .card{
        border-radius:12px;
        border:none;
    }

    .card-header{
        border-bottom:1px solid rgba(0,0,0,0.08);
        border-radius:12px 12px 0 0 !important;
    }

    .badge{
        padding:0.45em 0.9em;
        font-weight:500;
        border-radius:6px;
    }

    .btn{
        border-radius:8px;
        font-weight:500;
        transition:all 0.25s ease;
    }

    .btn:hover{
        transform:translateY(-2px);
        box-shadow:0 4px 12px rgba(0,0,0,0.15);
    }

</style>