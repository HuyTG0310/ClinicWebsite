<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- HEADER -->
<div class="d-flex justify-content-between align-items-center mb-4">

    <div>
        <h2 class="mb-1">
            <i class="fas fa-stethoscope text-primary me-2"></i>
            Specialty Management
        </h2>
        <p class="text-muted mb-0">Manage and monitor all specialties</p>
    </div>

    <a href="${pageContext.request.contextPath}/admin/specialty/add"
       class="btn btn-primary">

        <i class="fas fa-plus-circle me-2"></i>
        Add New Specialty
    </a>

</div>


<!-- SEARCH -->
<div class="card shadow-sm mb-4">

    <div class="card-body">

        <form method="get"
              action="${pageContext.request.contextPath}/admin/specialty/list">

            <div class="row g-3">

                <div class="col-md-10">

                    <div class="input-group">

                        <span class="input-group-text bg-white">
                            <i class="fas fa-search text-muted"></i>
                        </span>

                        <input type="text"
                               name="keyword"
                               class="form-control"
                               placeholder="Search by specialty name">

                    </div>

                </div>

                <div class="col-md-2">

                    <button class="btn btn-primary w-100">

                        <i class="fas fa-search me-2"></i>
                        Search

                    </button>

                </div>

            </div>

        </form>

    </div>

</div>



<!-- TABLE -->
<div class="card shadow-sm">

    <div class="card-header bg-white py-3">

        <h5 class="mb-0">
            <i class="fas fa-list me-2 text-primary"></i>
            Specialty List
        </h5>

    </div>

    <div class="card-body p-0">

        <div class="table-responsive">

            <table class="table table-hover align-middle mb-0">

                <thead class="table-light">

                    <tr>

                        <th>ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Status</th>
                        <th class="text-center">Action</th>

                    </tr>

                </thead>

                <tbody>

                    <c:forEach items="${list}" var="s">

                        <tr>

                            <td>
                                <span class="badge bg-light text-dark border">
                                    #${s.specialtyId}
                                </span>
                            </td>

                            <td class="fw-semibold">
                                ${s.name}
                            </td>

                            <td class="text-muted">
                                ${s.description}
                            </td>

                            <td>

                                <c:choose>

                                    <c:when test="${s.isActive}">
                                        <span class="badge bg-success">
                                            <i class="fas fa-check-circle me-1"></i>
                                            Active
                                        </span>
                                    </c:when>

                                    <c:otherwise>
                                        <span class="badge bg-danger">
                                            <i class="fas fa-times-circle me-1"></i>
                                            Inactive
                                        </span>
                                    </c:otherwise>

                                </c:choose>

                            </td>

                            <td class="text-center">

                                <a class="btn btn-sm btn-outline-primary"
                                   href="${pageContext.request.contextPath}/admin/specialty/detail?id=${s.specialtyId}">

                                    <i class="fas fa-eye"></i>

                                </a>

                            </td>

                        </tr>

                    </c:forEach>

                    <c:if test="${empty list}">
                        <tr>
                            <td colspan="5" class="text-center py-5">
                                <i class="fas fa-inbox fa-3x text-muted mb-3 d-block"></i>
                                <p class="text-muted mb-0">No specialties found</p>
                            </td>
                        </tr>
                    </c:if>

                </tbody>

            </table>

        </div>

    </div>

</div>



<style>

/* hover giống room list */

.table-hover tbody tr:hover {
    background-color: rgba(13,110,253,0.05);
    transition: background-color 0.2s ease;
}

.card {
    border:none;
    border-radius:12px;
}

.card-header {
    border-bottom:1px solid rgba(0,0,0,0.08);
    border-radius:12px 12px 0 0 !important;
}

.badge {
    padding:0.4em 0.8em;
    font-weight:500;
    border-radius:6px;
}

.btn {
    border-radius:8px;
    padding:0.5rem 1.2rem;
    font-weight:500;
    transition:all 0.25s ease;
}

.btn:hover {
    transform:translateY(-2px);
    box-shadow:0 4px 12px rgba(0,0,0,0.15);
}

</style>
