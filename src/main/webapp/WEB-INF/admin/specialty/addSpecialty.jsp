<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container-fluid">

    <!-- ================= HEADER ================= -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-plus-circle text-primary me-2"></i>
                Add Specialty
            </h2>

            <p class="text-muted mb-0">
                Create new specialty
            </p>
        </div>

        <a href="${pageContext.request.contextPath}/admin/specialty/list"
           class="btn btn-outline-secondary">

            <i class="fa-solid fa-arrow-left me-2"></i>
            Back to list

        </a>

    </div>



    <!-- ================= CARD ================= -->

    <div class="card shadow-sm">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">

                <i class="fa-solid fa-stethoscope text-primary me-2"></i>
                Specialty Information

            </h5>

        </div>



        <div class="card-body">

            <!-- ERROR -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger shadow-sm">

                    <i class="fa-solid fa-triangle-exclamation me-2"></i>
                    ${error}

                </div>
            </c:if>



            <form method="post"
                  action="${pageContext.request.contextPath}/admin/specialty/add">

                <div class="row g-4">


                    <!-- NAME -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Name <span class="text-danger">*</span>
                        </label>

                        <input type="text"
                               class="form-control"
                               name="name"
                               placeholder="Enter specialty name"
                               required>

                    </div>



                    <!-- DESCRIPTION -->
                    <div class="col-md-12">

                        <label class="form-label fw-semibold">
                            Description <span class="text-danger">*</span>
                        </label>

                        <textarea class="form-control"
                                  name="description"
                                  rows="3"
                                  placeholder="Enter description"></textarea>

                    </div>


                </div>



                <!-- ================= ACTION BUTTONS ================= -->

                <div class="d-flex justify-content-end gap-2 mt-4">

                    <a href="${pageContext.request.contextPath}/admin/specialty/list"
                       class="btn btn-outline-secondary">

                        Cancel

                    </a>

                    <button type="submit"
                            class="btn btn-primary">

                        <i class="fa-solid fa-plus me-1"></i>
                        Add Specialty

                    </button>

                </div>


            </form>

        </div>

    </div>

</div>