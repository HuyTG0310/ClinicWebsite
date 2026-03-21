<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="container-fluid">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-certificate text-primary me-2"></i>
                Add Certification
            </h2>
            <p class="text-muted mb-0">
                Upload your professional certificate for verification
            </p>
        </div>

        <a href="${basePath}/certification/my"
           class="btn btn-outline-secondary">
            <i class="fa-solid fa-arrow-left me-2"></i>Back
        </a>

    </div>

    <!-- ALERT -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger shadow-sm">
            <i class="fa-solid fa-circle-exclamation me-2"></i>
            ${error}
        </div>
    </c:if>

    <!-- FORM -->
    <div class="card shadow-sm">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-file-circle-plus text-primary me-2"></i>
                Certification Information
            </h5>
        </div>

        <div class="card-body">

            <form action="" method="POST" enctype="multipart/form-data">

                <div class="row g-3">

                    <!-- NAME -->
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">
                            Certificate Name <span class="text-danger">*</span>
                        </label>
                        <input type="text"
                               name="certificateName"
                               class="form-control"
                               value="${certificateName}"
                               required>
                    </div>

                    <!-- NUMBER -->
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">
                            Certificate Number <span class="text-danger">*</span>
                        </label>
                        <input type="text"
                               name="certificateNumber"
                               class="form-control"
                               value="${certificateNumber}"
                               required>
                    </div>

                    <!-- ISSUE DATE -->
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">
                            Issue Date <span class="text-danger">*</span>
                        </label>
                        <input type="date"
                               name="issueDate"
                               class="form-control"
                               value="${issueDate}"
                               required>
                    </div>

                    <!-- EXPIRY DATE -->
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">
                            Expiry Date <span class="text-danger">*</span>
                        </label>
                        <input type="date"
                               name="expiryDate"
                               class="form-control"
                               value="${expiryDate}"
                               required>
                    </div>

                    <!-- FILE -->
                    <div class="col-12">
                        <label class="form-label fw-semibold">
                            Upload Certificate (Image / PDF) <span class="text-danger">*</span>
                        </label>
                        <input type="file"
                               name="certificateFile"
                               class="form-control"
                               accept=".pdf,.jpg,.jpeg,.png"
                               required>
                        <div class="form-text">
                            Accepted formats: PDF, JPG, PNG
                        </div>
                    </div>

                </div>

                <!-- ACTION -->
                <div class="d-flex justify-content-end mt-4 gap-2">

                    <a href="${basePath}/certification/my"
                       class="btn btn-outline-secondary">
                        Cancel
                    </a>

                    <button type="submit"
                            class="btn btn-primary px-4">
                        <i class="fa-solid fa-paper-plane me-2"></i>
                        Submit
                    </button>

                </div>

            </form>

        </div>

    </div>

</div>