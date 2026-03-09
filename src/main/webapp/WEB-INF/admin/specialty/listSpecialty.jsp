<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="d-flex justify-content-between align-items-center mb-4">

    <div>
        <h2 class="mb-1">
            <i class="fas fa-stethoscope text-primary me-2"></i>
            Manage Specialty
        </h2>
        <p class="text-muted mb-0">Manage and monitor all specialties</p>
    </div>

    <a href="${pageContext.request.contextPath}/admin/specialty/add"
       class="btn btn-primary">

        <i class="fas fa-plus-circle me-2"></i>Add New Specialty
    </a>

</div>


<div class="card shadow-sm mb-4">

    <div class="card-body">

        <form method="get"
              action="${pageContext.request.contextPath}/admin/specialty/list">

            <div class="row g-3">

                <div class="col-md-9">

                    <label class="form-label fw-bold small text-muted">Keyword</label>

                    <div class="input-group">

                        <span class="input-group-text bg-white">
                            <i class="fas fa-search text-muted"></i>
                        </span>

                        <input type="text"
                               name="keyword"
                               class="form-control"
                               placeholder="Specialty name...">

                    </div>

                </div>

                <div class="col-md-3 d-flex align-items-end gap-2">

                    <button class="btn btn-primary flex-grow-1">

                        <i class="fas fa-filter me-2"></i>Search

                    </button>

                    <c:if test="${not empty param.keyword}">
                        <a href="${pageContext.request.contextPath}/admin/specialty/list"
                           class="btn btn-outline-secondary px-3"
                           title="Clear Filters">

                            <i class="fas fa-redo-alt"></i>

                        </a>
                    </c:if>

                </div>

            </div>

        </form>

    </div>

</div>



<div class="card shadow-sm">

    <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">

        <h5 class="mb-0">
            <i class="fas fa-list me-2 text-primary"></i>Specialty List
        </h5>

        <span class="badge bg-light text-dark border">
            Total: ${list.size()} records
        </span>

    </div>

    <div class="card-body p-0">

        <div class="table-responsive">

            <table class="table table-hover align-middle mb-0">

                <thead class="table-light">

                    <tr>

                        <th class="px-4">#ID</th>

                        <th>
                            <i class="fas fa-stethoscope me-2 text-primary"></i>Specialty
                        </th>

                        <th>
                            <i class="fas fa-align-left me-2 text-primary"></i>Description
                        </th>

                        <th>
                            <i class="fas fa-toggle-on me-2 text-primary"></i>Status
                        </th>

                        <th class="text-center">
                            <i class="fas fa-cog me-2 text-primary"></i>Actions
                        </th>

                    </tr>

                </thead>

                <tbody>

                    <c:forEach items="${list}" var="s">

                        <tr>

                            <td class="px-4 fw-bold text-secondary">
                                #${s.specialtyId}
                            </td>

                            <td>
                                <div class="fw-bold text-primary">
                                    ${s.name}
                                </div>
                            </td>

                            <td class="text-muted">
                                ${s.description}
                            </td>

                            <td>

                                <c:choose>

                                    <c:when test="${s.isActive}">
                                        <span class="badge bg-success">
                                            <i class="fas fa-check me-1"></i>ACTIVE
                                        </span>
                                    </c:when>

                                    <c:otherwise>
                                        <span class="badge bg-secondary">
                                            <i class="fas fa-ban me-1"></i>INACTIVE
                                        </span>
                                    </c:otherwise>

                                </c:choose>

                            </td>

                            <td class="text-center">

                                <a class="btn btn-sm btn-outline-primary"
                                   href="${pageContext.request.contextPath}/admin/specialty/detail?id=${s.specialtyId}">

                                    <i class="fas fa-eye me-1"></i>View

                                </a>

                            </td>

                        </tr>

                    </c:forEach>

                    <c:if test="${empty list}">
                        <tr>
                            <td colspan="5" class="text-center py-5">
                                <i class="fas fa-stethoscope fa-3x text-muted mb-3 d-block"></i>
                                <p class="text-muted mb-0">No specialties found</p>
                            </td>
                        </tr>
                    </c:if>

                </tbody>

            </table>

        </div>

    </div>

</div>