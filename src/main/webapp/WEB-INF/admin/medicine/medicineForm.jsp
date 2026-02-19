<%-- 
    Document   : medicineForm
    Created on : Feb 4, 2026, 6:38:43 PM
    Author     : TRUONGTHINHNGUYEN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h4 class="mb-4 fw-semibold text-primary">
    <i class="fa-solid fa-pills me-2"></i>
    <c:choose>
        <c:when test="${not empty medicine.medicineId}">Edit Medicine</c:when>
        <c:otherwise>Add Medicine</c:otherwise>
    </c:choose>
</h4>

<div class="card shadow-sm border-0">
    <div class="card-body">

        <c:if test="${not empty error}">
            <div class="alert alert-danger d-flex align-items-center">
                <i class="fa-solid fa-circle-exclamation me-2"></i>
                ${error}
            </div>
        </c:if>

        <form method="post">

            <c:if test="${not empty medicine.medicineId}">
                <input type="hidden" name="id" value="${medicine.medicineId}">
            </c:if>

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

            <div class="mb-3">
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
                    <option value="Effervescent tablet" ${medicine.unit == 'Effervescent tablet' ? 'selected' : ''}>Effervescent Tablet</option>
                    <option value="Capsule" ${medicine.unit == 'Capsule' ? 'selected' : ''}>Capsule</option>
                    <option value="Bag" ${medicine.unit == 'Bag' ? 'selected' : ''}>Bag</option>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label fw-semibold">Ingredients</label>
                <textarea class="form-control" name="ingredients" rows="3">${medicine.ingredients}</textarea>
            </div>

            <div class="mb-3">
                <label class="form-label fw-semibold">Usage</label>
                <textarea class="form-control" name="usage" rows="3">${medicine.usage}</textarea>
            </div>

            <div class="mb-3">
                <label class="form-label fw-semibold">Contraindication</label>
                <textarea class="form-control" name="contra">${medicine.contraindication}</textarea>
            </div>

            <c:if test="${not empty medicine.medicineId}">
                <div class="mb-4 form-check form-switch">
                    <input class="form-check-input" type="checkbox" 
                           id="isActive" name="isActive" value="true"
                           ${medicine.isActive ? 'checked' : ''}>
                    <label class="form-check-label" for="isActive">Active Status (Is being used?)</label>
                </div>
            </c:if>

            <div class="d-flex justify-content-end gap-2">
                <a href="${basePath}/medicine/list" class="btn btn-outline-secondary">
                    <i class="fa-solid fa-arrow-left me-1"></i> Cancel
                </a>

                <button type="submit" class="btn btn-primary">
                    <i class="fa-solid fa-floppy-disk me-1"></i> Save
                </button>
            </div>

        </form>
    </div>
</div>