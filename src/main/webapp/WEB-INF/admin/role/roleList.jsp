
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${param.msg eq 'addSuccess'}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
        <i class="fa-solid fa-circle-check me-2"></i> New role added successfully!
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-user-tag text-primary me-2"></i>
            Manage Role
        </h2>
        <p class="text-muted mb-0">Manage and monitor all system roles and permissions</p>
    </div>

    <a href="${pageContext.request.contextPath}/admin/role/create" class="btn btn-primary">
        <i class="fas fa-plus-circle me-2"></i>Add New Role
    </a>
</div>

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/admin/role/list" method="GET">
            <div class="row g-3">

                <div class="col-md-9">
                    <label class="form-label fw-bold small text-muted">Keyword</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fas fa-search text-muted"></i>
                        </span>
                        <input type="text" name="search" class="form-control"
                               placeholder="Role name..." value="${searchValue}">
                    </div>
                </div>

                <div class="col-md-3 d-flex align-items-end gap-2">
                    <button class="btn btn-primary flex-grow-1" type="submit">
                        <i class="fas fa-search me-2"></i>Search
                    </button>

                    <c:if test="${not empty searchValue}">
                        <a href="${pageContext.request.contextPath}/admin/role/list"
                           class="btn btn-outline-secondary px-3" title="Clear Filters">
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
            <i class="fa-solid fa-list me-2 text-primary"></i>Role List
        </h5>

        <span class="badge bg-light text-dark border">
            Total: ${roles.size()} records
        </span>
    </div>

    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">

                <thead class="table-light">
                    <tr>
                        <th class="px-4">#ID</th>
                        <th>
                            <i class="fa-solid fa-id-card-clip me-2 text-primary"></i>Role
                        </th>
                        <th class="text-center">
                            <i class="fa-solid fa-toggle-on me-2 text-primary"></i>Status
                        </th>
                        <th class="text-center">
                            <i class="fa-solid fa-gear me-2 text-primary"></i>Actions
                        </th>
                    </tr>
                </thead>

                <tbody>

                    <c:forEach var="r" items="${roles}">
                        <tr>

                            <td class="px-4 fw-bold text-secondary">
                                #${r.roleId}
                            </td>

                            <td>
                                <div class="fw-bold text-primary">
                                    ${r.roleName}
                                </div>
                            </td>

                            <td class="text-center">

                                <c:choose>

                                    <c:when test="${r.isActive}">
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
                                <a href="${pageContext.request.contextPath}/admin/role/detail?roleId=${r.roleId}"
                                   class="btn btn-sm btn-outline-primary"
                                   title="View Detail">
                                    <i class="fas fa-eye me-1"></i>View
                                </a>
                            </td>

                        </tr>
                    </c:forEach>

                    <c:if test="${empty roles}">
                        <tr>
                            <td colspan="4" class="text-center py-5">
                                <i class="fa-solid fa-user-tag fa-3x text-muted mb-3 d-block"></i>
                                <p class="text-muted mb-0">No roles found.</p>
                            </td>
                        </tr>
                    </c:if>

                </tbody>

            </table>
        </div>
    </div>
</div>