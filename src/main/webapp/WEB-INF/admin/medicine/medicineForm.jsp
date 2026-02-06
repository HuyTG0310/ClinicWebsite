<%-- 
    Document   : medicineForm
    Created on : Feb 4, 2026, 6:38:43 PM
    Author     : TRUONGTHINHNGUYEN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h4 class="mb-4 fw-semibold text-primary">
    <i class="fa-solid fa-pills me-2"></i>
    Add Medicine
</h4>

<div class="card shadow-sm border-0">
    <div class="card-body">

        <!-- ERROR MESSAGE -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger d-flex align-items-center">
                <i class="fa-solid fa-circle-exclamation me-2"></i>
                ${error}
            </div>
        </c:if>

        <form method="post">

            <!-- MEDICINE NAME -->
            <div class="mb-3">
                <label class="form-label fw-semibold">
                    Medicine Name <span class="text-danger">*</span>
                </label>
                <input type="text"
                       class="form-control"
                       name="name"
                       value="${medicine.medicineName}"
                       required>
            </div>

            <!-- UNIT -->
            <div class="mb-3">
                <label class="form-label fw-semibold">
                    Unit <span class="text-danger">*</span>
                </label>
                <select name="unit" class="form-select" required>
                    <option value="">-- Select Unit --</option>

                    <option value="tablet" ${medicine.unit == 'tablet' ? 'selected' : ''}>Tablet</option>
                    <option value="blister" ${medicine.unit == 'blister' ? 'selected' : ''}>Blister</option>
                    <option value="box" ${medicine.unit == 'box' ? 'selected' : ''}>Box</option>
                    <option value="bottle" ${medicine.unit == 'bottle' ? 'selected' : ''}>Bottle</option>
                    <option value="vial" ${medicine.unit == 'vial' ? 'selected' : ''}>Vial</option>
                    <option value="tube" ${medicine.unit == 'tube' ? 'selected' : ''}>Tube</option>
                    <option value="sachet" ${medicine.unit == 'sachet' ? 'selected' : ''}>Sachet</option>
                    <option value="effervescent tablet"
                            ${medicine.unit == 'effervescent tablet' ? 'selected' : ''}>
                        Effervescent Tablet
                    </option>
                    <option value="capsule" ${medicine.unit == 'capsule' ? 'selected' : ''}>Capsule</option>
                    <option value="bag" ${medicine.unit == 'bag' ? 'selected' : ''}>Bag</option>
                </select>
            </div>

            <!-- INGREDIENTS -->
            <div class="mb-3">
                <label class="form-label fw-semibold">Ingredients</label>
                <textarea class="form-control"
                          name="ingredients"
                          rows="3">${medicine.ingredients}</textarea>
            </div>

            <!-- USAGE -->
            <div class="mb-3">
                <label class="form-label fw-semibold">Usage</label>
                <textarea class="form-control"
                          name="usage"
                          rows="3">${medicine.usage}</textarea>
            </div>

            <!-- CONTRAINDICATION -->
            <div class="mb-4">
                <label class="form-label fw-semibold">Contraindication</label>
                <textarea class="form-control"
                          name="contra">${medicine.contraindication}</textarea>
            </div>

            <!-- ACTION BUTTONS -->
            <div class="d-flex justify-content-end gap-2">
                <a href="${pageContext.request.contextPath}/admin/medicine"
                   class="btn btn-outline-secondary">
                    <i class="fa-solid fa-arrow-left me-1"></i> Cancel
                </a>

                <button type="submit" class="btn btn-primary">
                    <i class="fa-solid fa-floppy-disk me-1"></i> Save
                </button>
            </div>

        </form>
    </div>
</div>
