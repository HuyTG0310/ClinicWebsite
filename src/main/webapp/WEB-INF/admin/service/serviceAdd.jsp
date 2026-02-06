<%-- 
    Document   : serviceAdd
    Created on : Feb 4, 2026
    Author     : huytr
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- HEADER -->
<div class="mb-4">
    <h2 class="mb-1">
        <i class="fa-solid fa-plus-circle text-primary me-2"></i>
        Add service
    </h2>
    <p class="text-muted mb-0">
        Enter the information to create a new service in the system.
    </p>
</div>

<!-- ERROR MESSAGE -->
<c:if test="${not empty error}">
    <div class="alert alert-danger shadow-sm">
        <i class="fa-solid fa-exclamation-triangle me-2"></i>
        ${error}
    </div>
</c:if>

<!-- FORM -->
<div class="card shadow-sm">
    <div class="card-body">

        <form method="post" action="">
            <div class="row g-4">

                <!-- SERVICE NAME -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">
                        Service name <span class="text-danger">*</span>
                    </label>
                    <input type="text"
                           name="serviceName"
                           value="${serviceName}"
                           class="form-control"
                           placeholder="Enter service name..." />
                </div>

                <!-- CATEGORY -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">
                        Category
                    </label>
                    <select name="category" class="form-select">
                        <c:forEach var="c" items="${categories}">
                            <option value="${c}"
                                    <c:if test="${c == category}">selected</c:if>>
                                ${c}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- PRICE -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">
                        Price <span class="text-danger">*</span>
                    </label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fa-solid fa-dollar-sign text-muted"></i>
                        </span>
                        <input type="text"
                               name="price"
                               value="${price}"
                               class="form-control"
                               placeholder="Enter service price..." />
                    </div>
                </div>

            </div>

            <!-- ACTION BUTTONS -->
            <div class="d-flex justify-content-end gap-2 mt-4">
                <a href="${pageContext.request.contextPath}/admin/service/list"
                   class="btn btn-outline-secondary">
                    <i class="fa-solid fa-arrow-left me-1"></i>
                    Cancel
                </a>

                <button type="submit"
                        class="btn btn-primary px-4">
                    <i class="fa-solid fa-save me-1"></i>
                    Save
                </button>
            </div>

        </form>

    </div>
</div>


<style>
    .form-control,
    .form-select {
        border-radius: 8px;
    }

    .form-control:focus,
    .form-select:focus {
        box-shadow: 0 0 0 0.15rem rgba(13, 110, 253, 0.25);
    }

    .alert {
        border-radius: 10px;
        border-left: 4px solid #dc3545;
    }

</style>