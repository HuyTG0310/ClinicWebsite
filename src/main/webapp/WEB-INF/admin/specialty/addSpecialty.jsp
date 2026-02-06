<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container-fluid">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h4 class="fw-bold text-primary mb-0">
                <i class="fa-solid fa-plus-circle me-2"></i>
                Add Specialty
            </h4>
            <small class="text-muted">Create new specialty</small>
        </div>

        <a href="${pageContext.request.contextPath}/admin/specialty/list"
           class="btn btn-outline-secondary">

            <i class="fa-solid fa-arrow-left me-2"></i>
            Back
        </a>

    </div>


    <!-- FORM CARD -->
    <div class="card border-0 shadow-sm">

        <div class="card-body">

            <!-- ERROR -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fa-solid fa-triangle-exclamation me-2"></i>
                    ${error}
                </div>
            </c:if>

            <form method="post"
                  action="${pageContext.request.contextPath}/admin/specialty/add">

                <div class="mb-3">
                    <label class="form-label fw-medium">Name</label>

                    <input type="text"
                           class="form-control"
                           name="name"
                           placeholder="Enter specialty name">
                </div>

                <div class="mb-3">
                    <label class="form-label fw-medium">Description</label>

                    <textarea class="form-control"
                              name="description"
                              rows="4"
                              placeholder="Enter description"></textarea>
                </div>

                <div class="mt-4">

                    <button class="btn btn-primary shadow-sm">

                        <i class="fa-solid fa-plus me-2"></i>
                        Add Specialty

                    </button>

                </div>

            </form>

        </div>

    </div>

</div>
