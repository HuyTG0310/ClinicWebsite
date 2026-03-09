<%-- 
    Document   : medicineForm
    Created on : Feb 4, 2026, 6:38:43 PM
    Author     : TRUONGTHINHNGUYEN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="container-fluid">

    <!-- ================= HEADER ================= -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-pills text-primary me-2"></i>

                <c:choose>
                    <c:when test="${not empty medicine.medicineId}">
                        Edit Medicine
                    </c:when>
                    <c:otherwise>
                        Add Medicine
                    </c:otherwise>
                </c:choose>

            </h2>

            <p class="text-muted mb-0">
                Manage medicine information in the system
            </p>
        </div>

        <a href="${basePath}/medicine/list"
           class="btn btn-outline-secondary">

            <i class="fa-solid fa-arrow-left me-2"></i>
            Back to list

        </a>

    </div>



    <!-- ================= ERROR ================= -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger shadow-sm">
            <i class="fa-solid fa-circle-exclamation me-2"></i>
            ${error}
        </div>
    </c:if>



    <!-- ================= FORM CARD ================= -->
    <div class="card shadow-sm">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">

                <i class="fa-solid fa-capsules text-primary me-2"></i>
                Medicine Information

            </h5>

        </div>



        <div class="card-body">

            <form method="post">

                <c:if test="${not empty medicine.medicineId}">
                    <input type="hidden"
                           name="id"
                           value="${medicine.medicineId}">
                </c:if>



                <div class="row g-4">

                    <!-- MEDICINE NAME -->
                    <div class="col-md-6">

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
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Unit <span class="text-danger">*</span>
                        </label>

                        <select name="unit"
                                class="form-select"
                                required>

                            <option value="">-- Select Unit --</option>

                            <option value="Viên" ${medicine.unit == 'Viên' ? 'selected' : ''}>Viên</option>
                            <option value="Ống" ${medicine.unit == 'Ống' ? 'selected' : ''}>Ống</option>
                            <option value="Lọ" ${medicine.unit == 'Lọ' ? 'selected' : ''}>Lọ</option>
                            <option value="Gói" ${medicine.unit == 'Gói' ? 'selected' : ''}>Gói</option>
                        </select>

                    </div>



                    <!-- INGREDIENTS -->
                    <div class="col-12">

                        <label class="form-label fw-semibold">
                            Ingredients
                        </label>

                        <textarea class="form-control"
                                  name="ingredients"
                                  rows="3">${medicine.ingredients}</textarea>

                    </div>



                    <!-- USAGE -->
                    <div class="col-12">

                        <label class="form-label fw-semibold">
                            Usage
                        </label>

                        <textarea class="form-control"
                                  name="usage"
                                  rows="3">${medicine.usage}</textarea>

                    </div>



                    <!-- CONTRAINDICATION -->
                    <div class="col-12">

                        <label class="form-label fw-semibold">
                            Contraindication
                        </label>

                        <textarea class="form-control"
                                  name="contra"
                                  rows="3">${medicine.contraindication}</textarea>

                    </div>



                    <!-- ACTIVE -->
                    <c:if test="${not empty medicine.medicineId}">

                        <div class="col-12">

                            <div class="form-check form-switch">

                                <input class="form-check-input"
                                       type="checkbox"
                                       id="isActive"
                                       name="isActive"
                                       value="true"
                                       ${medicine.isActive ? 'checked' : ''}>

                                <label class="form-check-label fw-semibold"
                                       for="isActive">

                                    Active Status (Is being used?)

                                </label>

                            </div>

                        </div>

                    </c:if>


                </div>



                <!-- ACTION BUTTONS -->
                <div class="d-flex justify-content-end gap-2 mt-4">

                    <a href="${basePath}/medicine/list"
                       class="btn btn-outline-secondary">

                        <i class="fa-solid fa-arrow-left me-1"></i>
                        Cancel

                    </a>


                    <button type="submit"
                            class="btn btn-primary">

                        <i class="fa-solid fa-floppy-disk me-1"></i>
                        Save

                    </button>

                </div>

            </form>

        </div>

    </div>

</div>