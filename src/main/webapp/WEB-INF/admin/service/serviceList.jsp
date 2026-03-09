
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
        <i class="fa-solid fa-plus-circle me-2"></i>Add New Service
    </a>
</div>

<c:if test="${not empty sessionScope.success}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
        <i class="fa-solid fa-circle-check me-2"></i>${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="success" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.error}">
    <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
        <i class="fa-solid fa-circle-exclamation me-2"></i>${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="error" scope="session"/>
</c:if>

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form method="get" action="${pageContext.request.contextPath}/admin/service/list">
            <div class="row g-3">

                <div class="col-md-9">
                    <label class="form-label fw-bold small text-muted">Keyword</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fa-solid fa-magnifying-glass text-muted"></i>
                        </span>
                        <input type="text" name="keyword" value="${keyword}" 
                               class="form-control" placeholder="Service name...">
                    </div>
                </div>

                <div class="col-md-3 d-flex align-items-end gap-2">
                    <button class="btn btn-primary flex-grow-1" type="submit">
                        <i class="fas fa-filter me-2"></i>Search
                    </button>

                    <c:if test="${not empty keyword}">
                        <a href="${pageContext.request.contextPath}/admin/service/list"
                           class="btn btn-outline-secondary px-3" title="Clear Search">
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
            <i class="fa-solid fa-list me-2 text-primary"></i>Service List
        </h5>

        <span class="badge bg-light text-dark border">
            Total: ${services.size()} records
        </span>
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
                                <th class="px-4">#ID</th>

                                <th>
                                    <i class="fa-solid fa-notes-medical me-2 text-primary"></i>
                                    Service
                                </th>

                                <th>
                                    <i class="fa-solid fa-layer-group me-2 text-primary"></i>
                                    Category
                                </th>

                                <th>
                                    <i class="fa-solid fa-dollar-sign me-2 text-primary"></i>
                                    Price
                                </th>

                                <th>
                                    <i class="fa-solid fa-toggle-on me-2 text-primary"></i>
                                    Status
                                </th>

                                <th class="text-center">
                                    <i class="fa-solid fa-gear me-2 text-primary"></i>
                                    Actions
                                </th>
                            </tr>
                        </thead>

                        <tbody>

                            <c:forEach var="s" items="${services}">
                                <tr>

                                    <td class="px-4 fw-bold text-secondary">
                                        #${s.serviceId}
                                    </td>

                                    <td>
                                        <div class="fw-bold text-primary">
                                            ${s.serviceName}
                                        </div>
                                    </td>

                                    <td>
                                        <span class="badge bg-light text-dark border">
                                            ${s.category}
                                        </span>
                                    </td>

                                    <td>
                                        <div class="fw-bold text-danger">
                                            <fmt:formatNumber value="${s.currentPrice}" pattern="#,###"/> đ
                                        </div>
                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${s.isActive}">
                                                <span class="badge bg-success">
                                                    <i class="fa-solid fa-check me-1"></i>ACTIVE
                                                </span>
                                            </c:when>

                                            <c:otherwise>
                                                <span class="badge bg-secondary">
                                                    <i class="fa-solid fa-ban me-1"></i>INACTIVE
                                                </span>
                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/admin/service/detail?id=${s.serviceId}"
                                           class="btn btn-sm btn-outline-primary"
                                           title="View Detail">
                                            <i class="fas fa-eye me-1"></i>View
                                        </a>
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
