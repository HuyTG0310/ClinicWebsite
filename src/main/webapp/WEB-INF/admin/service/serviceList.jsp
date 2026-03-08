
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-stethoscope text-primary me-2"></i>
            Manage Service
        </h2>
        <p class="text-muted mb-0">Manage and monitor all medical services</p>
    </div>

    <a href="${pageContext.request.contextPath}/admin/service/add" class="btn btn-primary">
        <i class="fa-solid fa-plus-circle me-2"></i>
        Add New Service
    </a>
</div>

<c:if test="${not empty sessionScope.success}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
        <i class="fa-solid fa-check-circle me-2"></i>${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="success" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.error}">
    <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
        <i class="fa-solid fa-triangle-exclamation me-2"></i>${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="error" scope="session"/>
</c:if>

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form method="get" action="${pageContext.request.contextPath}/admin/service/list">
            <div class="row g-3">
                <div class="col-md-10">
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fa-solid fa-magnifying-glass text-muted"></i>
                        </span>
                        <input type="text" name="keyword" value="${keyword}" 
                               class="form-control" placeholder="Search by service name...">
                    </div>
                </div>
                <div class="col-md-2">
                    <button class="btn btn-primary w-100" type="submit">
                        <i class="fa-solid fa-magnifying-glass me-2"></i>
                        Search
                    </button>
                </div>

                <c:if test="${not empty keyword}">
                    <div class="col-12 mt-2">
                        <a href="${pageContext.request.contextPath}/admin/service/list" class="btn btn-outline-secondary btn-sm">
                            <i class="fas fa-times me-2"></i>Clear Search
                        </a>
                    </div>
                </c:if>
            </div>
        </form>
    </div>
</div>

<div class="card shadow-sm">
    <div class="card-header bg-white py-3">
        <h5 class="mb-0">
            <i class="fa-solid fa-list me-2 text-primary"></i>Service List
        </h5>
    </div>

    <div class="card-body p-0">
        <c:choose>
            <c:when test="${services == null || services.size() == 0}">
                <div class="text-center py-5">
                    <i class="fa-solid fa-inbox fa-3x text-muted mb-3 d-block"></i>
                    <p class="text-muted mb-0">No services found</p>
                </div>
            </c:when>

            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="px-4">
                                    <i class="fa-solid fa-hashtag me-2 text-primary"></i>ID
                                </th>
                                <th>
                                    <i class="fa-solid fa-notes-medical me-2 text-primary"></i>Service Name
                                </th>
                                <th>
                                    <i class="fa-solid fa-layer-group me-2 text-primary"></i>Category
                                </th>
                                <th>
                                    <i class="fa-solid fa-dollar-sign me-2 text-primary"></i>Price
                                </th>
                                <th>
                                    <i class="fa-solid fa-toggle-on me-2 text-primary"></i>Status
                                </th>
                                <th class="text-center">
                                    <i class="fa-solid fa-gear me-2 text-primary"></i>Actions
                                </th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="s" items="${services}">
                                <tr>
                                    <td class="px-4">${s.serviceId}</td>
                                    <td class="fw-semibold">${s.serviceName}</td>
                                    <td>
                                        <span class="badge bg-light text-dark border">
                                            ${s.category}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="fw-bold text-danger"><fmt:formatNumber value="${s.currentPrice}" pattern="#,###"/> đ</div>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${s.isActive}">
                                                <span class="badge bg-success">
                                                    <i class="fa-solid fa-check-circle me-1"></i>Active
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger">
                                                    <i class="fa-solid fa-times-circle me-1"></i>Inactive
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <div class="btn-group" role="group">
                                            <a href="${pageContext.request.contextPath}/admin/service/detail?id=${s.serviceId}"
                                               class="btn btn-sm btn-outline-primary" title="View Details">
                                                <i class="fa-solid fa-eye me-1"></i> View
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<style>
    .table-hover tbody tr:hover {
        background-color: rgba(13, 110, 253, 0.05);
        transition: background-color 0.2s ease;
    }

    .card {
        border: none;
        border-radius: 12px;
    }

    .card-header {
        border-bottom: 1px solid rgba(0,0,0,0.08);
        border-radius: 12px 12px 0 0 !important;
    }

    .badge {
        padding: 0.4em 0.8em;
        font-weight: 500;
        border-radius: 6px;
    }

    .btn {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        border-radius: 8px;
        padding: 0.5rem 1.2rem;
        font-weight: 500;
        transition: all 0.25s ease;
    }

    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }

    /* Style cho input group đồng bộ */
    .input-group-text {
        border-right: none;
        border-radius: 8px 0 0 8px;
    }

    .input-group .form-control {
        border-left: none;
        border-radius: 0 8px 8px 0;
    }
</style>