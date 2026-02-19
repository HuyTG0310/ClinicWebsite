<%-- 
    Document   : serviceDetail
    Created on : Feb 4, 2026
    Author     : huytr
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="editError" value="${param.editError}" />

<!-- HEADER -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-notes-medical text-primary me-2"></i>
            Service detail
        </h2>
        <p class="text-muted mb-0">
            Detailed information about the service within the system.
        </p>
    </div>

    <div class="d-flex gap-2">
        <a href="${pageContext.request.contextPath}/admin/service/list"
           class="btn btn-outline-secondary">
            <i class="fa-solid fa-arrow-left me-1"></i>
            Back
        </a>

        <button class="btn btn-warning"
                data-bs-toggle="modal"
                data-bs-target="#editServiceModal">
            <i class="fa-solid fa-pen-to-square me-1"></i>
            Edit
        </button>
    </div>
</div>

<!-- DETAIL CARD -->
<div class="card shadow-sm mb-4">
    <div class="card-body">
        <div class="row g-4">

            <div class="col-md-6">
                <label class="text-muted">ID</label>
                <div class="fw-semibold">
                    ${service.serviceId}
                </div>
            </div>

            <div class="col-md-6">
                <label class="text-muted">Service name</label>
                <div class="fw-semibold">
                    ${service.serviceName}
                </div>
            </div>

            <div class="col-md-6">
                <label class="text-muted">Category</label>
                <div>
                    <span class="badge bg-light text-dark border">
                        ${service.category}
                    </span>
                </div>
            </div>

            <div class="col-md-6">
                <label class="text-muted">Price</label>
                <div class="fw-semibold">
                    ${service.currentPrice}
                </div>
            </div>

            <div class="col-md-6">
                <label class="text-muted">Status</label>
                <div>
                    <c:choose>
                        <c:when test="${service.isActive}">
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
    </div>
</div>

<!-- EDIT MODAL -->
<div class="modal fade" id="editServiceModal" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">

            <div class="modal-header">
                <h5 class="modal-title">
                    <i class="fa-solid fa-pen-to-square me-2"></i>
                    Edit service
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>

            <form method="post"
                  action="${pageContext.request.contextPath}/admin/service/edit">

                <div class="modal-body">

                    <input type="hidden" name="serviceId"
                           value="${service.serviceId}" />

                    <c:if test="${not empty editError}">
                        <div class="alert alert-danger">
                            <i class="fa-solid fa-exclamation-triangle me-2"></i>
                            ${editError}
                        </div>
                    </c:if>

                    <div class="row g-3">

                        <!-- NAME -->
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">
                                Service name
                            </label>
                            <input type="text"
                                   name="serviceName"
                                   value="${service.serviceName}"
                                   class="form-control" />
                        </div>

                        <!-- CATEGORY -->
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">
                                Category
                            </label>
                            <select name="category" class="form-select">
                                <c:forEach var="c" items="${categories}">
                                    <option value="${c}"
                                            <c:if test="${c == service.category}">selected</c:if>>
                                        ${c}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- PRICE -->
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">
                                Price
                            </label>
                            <input type="text"
                                   name="price"
                                   value="${service.currentPrice}"
                                   class="form-control" />
                        </div>

                        <!-- ACTIVE -->
                        <div class="col-md-6 d-flex align-items-center">
                            <div class="form-check form-switch mt-4">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isActive"
                                       <c:if test="${service.isActive}">checked</c:if>>
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

    <!-- AUTO OPEN MODAL IF ERROR -->
<c:if test="${not empty editError}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const modalEl = document.getElementById("editServiceModal");
            if (modalEl) {
                const editModal = new bootstrap.Modal(modalEl);
                editModal.show();
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


