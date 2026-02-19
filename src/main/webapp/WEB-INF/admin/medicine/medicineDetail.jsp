<%-- 
    Document   : medicineDetail
    Created on : Feb 4, 2026, 3:35:08 PM
    Author     : TRUONGTHINHNGUYEN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>



<h4 class="mb-4 fw-semibold text-primary">
    <i class="fa-solid fa-circle-info me-2"></i> Medicine Detail
</h4>

<!-- DETAIL CARD -->
<div class="card shadow-sm border-0 mb-4">
    <div class="card-body">

        <div class="row g-3">

            <!-- MEDICINE ID -->
            <div class="col-12">
                <label class="form-label fw-semibold">Medicine ID</label>
                <input type="text"
                       class="form-control"
                       value="${medicine.medicineId}"
                       readonly>
            </div>


            <!-- MEDICINE NAME -->
            <div class="col-12">
                <label class="form-label fw-semibold">Medicine Name</label>
                <input type="text"
                       class="form-control"
                       value="${medicine.medicineName}"
                       readonly>
            </div>

            <!-- UNIT -->
            <div class="col-12">
                <label class="form-label fw-semibold">Unit</label>
                <input type="text"
                       class="form-control"
                       value="${medicine.unit}"
                       readonly>
            </div>

            <!-- INGREDIENTS -->
            <div class="col-12">
                <label class="form-label fw-semibold">Ingredients</label>
                <textarea class="form-control"
                          rows="3"
                          readonly>${medicine.ingredients}</textarea>
            </div>

            <!-- USAGE -->
            <div class="col-12">
                <label class="form-label fw-semibold">Usage</label>
                <textarea class="form-control"
                          rows="3"
                          readonly>${medicine.usage}</textarea>
            </div>

            <!-- CONTRAINDICATION -->
            <div class="col-12">
                <label class="form-label fw-semibold">Contraindication</label>
                <textarea class="form-control"
                          rows="3"
                          readonly>${medicine.contraindication}</textarea>
            </div>

            <!-- STATUS -->
            <div class="col-12">
                <label class="form-label fw-semibold">Status</label><br>
                <span class="badge ${medicine.isActive ? 'bg-success' : 'bg-secondary'}">
                    ${medicine.isActive ? "Active" : "Inactive"}
                </span>
            </div>

        </div>

        <!-- ACTION -->
        <div class="d-flex justify-content-end gap-2 mt-4">
            <a href="${basePath}/medicine/list"
               class="btn btn-outline-secondary">
                <i class="fa-solid fa-arrow-left me-1"></i> Back
            </a>


            <c:if test="${hasMedicineEdit}">
                <button class="btn btn-warning"
                        data-bs-toggle="modal"
                        data-bs-target="#editMedicineModal">
                    <i class="fa-solid fa-pen-to-square me-1"></i> Edit
                </button>
            </c:if>



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

                    <!-- ID (readonly) -->
                    <input type="hidden" name="id"
                           value="${medicine.medicineId}" />

                    <!-- ERROR -->
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
                                <option value="Tablet" ${medicine.unit == 'Tablet' ? 'selected' : ''}>Tablet</option>
                                <option value="Blister" ${medicine.unit == 'Blister' ? 'selected' : ''}>Blister</option>
                                <option value="Box" ${medicine.unit == 'Box' ? 'selected' : ''}>Box</option>
                                <option value="Bottle" ${medicine.unit == 'Bottle' ? 'selected' : ''}>Bottle</option>
                                <option value="Vial" ${medicine.unit == 'Vial' ? 'selected' : ''}>Vial</option>
                                <option value="Tube" ${medicine.unit == 'Tube' ? 'selected' : ''}>Tube</option>
                                <option value="Sachet" ${medicine.unit == 'Sachet' ? 'selected' : ''}>Sachet</option>
                                <option value="Effervescent tablet" ${medicine.unit == 'Effervescent tablet' ? 'selected' : ''}>
                                    Effervescent Tablet
                                </option>
                                <option value="capsule" ${medicine.unit == 'capsule' ? 'selected' : ''}>Capsule</option>
                                <option value="bag" ${medicine.unit == 'bag' ? 'selected' : ''}>Bag</option>
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


<style>
    .btn {
        border-radius: 8px;
        font-weight: 500;
        transition: all 0.25s ease;
    }

    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
</style>
