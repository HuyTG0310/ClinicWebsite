<%-- 
    Document   : medicineDetail
    Created on : Feb 4, 2026, 3:35:08 PM
    Author     : TRUONGTHINHNGUYEN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div class="container-fluid">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-capsules text-primary me-2"></i>
                Medicine Detail
            </h2>
            <p class="text-muted mb-0">
                View detailed information of medicine
            </p>
        </div>

        <div class="d-flex gap-2">

            <c:if test="${hasMedicineEdit}">
                <button class="btn btn-warning"
                        data-bs-toggle="modal"
                        data-bs-target="#editMedicineModal">
                    <i class="fa-solid fa-pen-to-square me-2"></i>Edit
                </button>
            </c:if>

            <a href="${basePath}/medicine/list"
               class="btn btn-outline-secondary">
                <i class="fa-solid fa-arrow-left me-2"></i>Back to list
            </a>

        </div>

    </div>



    <!-- DETAIL CARD -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-capsules text-primary me-2"></i>
                Medicine Information
            </h5>
        </div>

        <div class="card-body">


            <!-- ID + STATUS -->
            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">Medicine ID</label>
                    <div class="fw-semibold">
                        #${medicine.medicineId}
                    </div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Status</label>
                    <div>
                        <span class="badge ${medicine.isActive ? 'bg-success' : 'bg-secondary'}">
                            ${medicine.isActive ? "Active" : "Inactive"}
                        </span>
                    </div>
                </div>

            </div>


            <!-- NAME -->
            <div class="mb-3">
                <label class="text-muted small">Medicine Name</label>
                <div class="fs-5 fw-medium">
                    ${medicine.medicineName}
                </div>
            </div>


            <!-- UNIT -->
            <div class="mb-3">
                <label class="text-muted small">Unit</label>
                <div>
                    <span class="badge bg-light text-dark border">
                        ${medicine.unit}
                    </span>
                </div>
            </div>


            <!-- INGREDIENT -->
            <div class="mb-3">
                <label class="text-muted small">Ingredients</label>
                <div>
                    <c:choose>
                        <c:when test="${not empty medicine.ingredients}">
                            ${medicine.ingredients}
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted">
                                <i class="fa-solid fa-circle-info me-1"></i>
                                No ingredient information
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>


            <!-- USAGE -->
            <div class="mb-3">
                <label class="text-muted small">Usage</label>
                <div>
                    <c:choose>
                        <c:when test="${not empty medicine.usage}">
                            ${medicine.usage}
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted">
                                <i class="fa-solid fa-circle-info me-1"></i>
                                No usage instructions
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>


            <!-- CONTRAINDICATION -->
            <div class="mb-3">
                <label class="text-muted small">Contraindication</label>
                <div>
                    <c:choose>
                        <c:when test="${not empty medicine.contraindication}">
                            ${medicine.contraindication}
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted">
                                <i class="fa-solid fa-circle-info me-1"></i>
                                No contraindication information
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </div>
    </div>



    <!-- EDIT POPUP -->
    <c:set var="editError" value="${param.editError}" />

    <div class="modal fade" id="editMedicineModal" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">

                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fa-solid fa-pen-to-square me-2"></i>
                        Edit Medicine
                    </h5>
                    <button type="button"
                            class="btn-close"
                            data-bs-dismiss="modal"></button>
                </div>

                <form method="post"
                      action="${basePath}/medicine/edit">

                    <div class="modal-body">

                        <input type="hidden"
                               name="id"
                               value="${medicine.medicineId}" />

                        <c:if test="${not empty editError}">
                            <div class="alert alert-danger">
                                <i class="fa-solid fa-triangle-exclamation me-2"></i>
                                ${editError}
                            </div>
                        </c:if>

                        <div class="row g-3">

                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Medicine Name</label>
                                <input type="text"
                                       name="name"
                                       value="${medicine.medicineName}"
                                       class="form-control">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold">
                                    Unit <span class="text-danger">*</span>
                                </label>

                                <select name="unit" class="form-select" required>
                                    <option value="">-- Select Unit --</option>
                                    <option value="Viên" ${medicine.unit == 'Viên' ? 'selected' : ''}>Viên</option>
                                    <option value="Ống" ${medicine.unit == 'Ống' ? 'selected' : ''}>Ống</option>
                                    <option value="Lọ" ${medicine.unit == 'Lọ' ? 'selected' : ''}>Lọ</option>
                                    <option value="Gói" ${medicine.unit == 'Gói' ? 'selected' : ''}>Gói</option>
                                </select>
                            </div>

                            <div class="col-md-12">
                                <label class="form-label fw-semibold">Ingredients</label>
                                <textarea name="ingredients"
                                          class="form-control"
                                          rows="2">${medicine.ingredients}</textarea>
                            </div>

                            <div class="col-md-12">
                                <label class="form-label fw-semibold">Usage</label>
                                <textarea name="usage"
                                          class="form-control"
                                          rows="2">${medicine.usage}</textarea>
                            </div>

                            <div class="col-md-12">
                                <label class="form-label fw-semibold">Contraindication</label>
                                <textarea name="contra"
                                          class="form-control"
                                          rows="2">${medicine.contraindication}</textarea>
                            </div>

                            <div class="col-md-6 d-flex align-items-center">
                                <div class="form-check form-switch mt-4">
                                    <input class="form-check-input"
                                           type="checkbox"
                                           name="isActive"
                                           <c:if test="${medicine.isActive}">checked</c:if>>
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
                                    class="btn btn-primary px-4">
                                <i class="fa-solid fa-save me-1"></i>
                                Save
                            </button>
                        </div>

                    </form>
                </div>
            </div>
        </div>



        <script>
            window.onload = function () {
        <c:if test="${showEditPopup}">
                document.getElementById("editPopup").style.display = "flex";
        </c:if>
            };
    </script>


    <c:if test="${not empty editError}">
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const modalEl = document.getElementById("editMedicineModal");
                if (modalEl) {
                    const modal = new bootstrap.Modal(modalEl);
                    modal.show();
                }
            });
        </script>
    </c:if>



