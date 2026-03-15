
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-users text-primary me-2"></i>
            Manage Staff
        </h2>
        <p class="text-muted mb-0">List and manage user permissions in the system</p>
    </div>

    <a href="${pageContext.request.contextPath}/admin/user/add" class="btn btn-primary">
        <i class="fas fa-plus-circle me-2"></i>Add New Staff
    </a>
</div>

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form method="get" action="${pageContext.request.contextPath}/admin/user/list">

            <div class="row g-3">

                <div class="col-md-5">
                    <label class="form-label fw-bold small text-muted">Keyword</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fas fa-search text-muted"></i>
                        </span>
                        <input type="text" name="keyword"
                               value="${param.keyword}"
                               class="form-control"
                               placeholder="Username or fullname...">
                    </div>
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Role</label>
                    <select name="roleId" class="form-select">
                        <option value="">All Roles</option>

                        <c:forEach var="r" items="${roles}">
                            <option value="${r.roleId}"
                                    <c:if test="${param.roleId == r.roleId}">selected</c:if>>
                                ${r.roleName}
                            </option>
                        </c:forEach>

                    </select>
                </div>

                <div class="col-md-2">
                    <label class="form-label fw-bold small text-muted">Status</label>
                    <select name="status" class="form-select">
                        <option value="">All</option>
                        <option value="1" <c:if test="${param.status == '1'}">selected</c:if>>Active</option>
                        <option value="0" <c:if test="${param.status == '0'}">selected</c:if>>Inactive</option>
                        </select>
                    </div>

                    <div class="col-md-2 d-flex align-items-end gap-2">

                        <button type="submit" class="btn btn-primary flex-grow-1">
                            <i class="fas fa-search me-2"></i>Search
                        </button>

                    <c:if test="${not empty param.keyword or not empty param.roleId or not empty param.status}">
                        <a href="${pageContext.request.contextPath}/admin/user/list"
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
            <i class="fa-solid fa-list me-2 text-primary"></i>User List
        </h5>

        <span class="badge bg-light text-dark border">
            Total: ${users.size()} records
        </span>

    </div>

    <div class="card-body p-0">

        <c:choose>

            <c:when test="${empty users}">

                <div class="text-center py-5">
                    <i class="fa-solid fa-user-slash fa-3x text-muted mb-3 d-block"></i>
                    <p class="text-muted mb-0">No users found</p>
                </div>

            </c:when>

            <c:otherwise>

                <div class="table-responsive">

                    <table class="table table-hover align-middle mb-0">

                        <thead class="table-light">

                            <tr>

                                <th class="px-4">#ID</th>

                                <th>
                                    <i class="fa-solid fa-user me-2 text-primary"></i>Username
                                </th>

                                <th>
                                    <i class="fa-solid fa-id-card me-2 text-primary"></i>Fullname
                                </th>

                                <th>
                                    <i class="fa-solid fa-user-tag me-2 text-primary"></i>Role
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

                            <c:forEach var="u" items="${users}">

                                <tr>

                                    <td class="px-4 fw-bold text-secondary">
                                        #${u.userId}
                                    </td>

                                    <td>
                                        <div class="fw-bold text-primary">
                                            ${u.username}
                                        </div>
                                    </td>

                                    <td class="fw-semibold">
                                        ${u.fullName}
                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${u.roleActive}">
                                                <span class="badge bg-info text-dark">
                                                    ${u.roleName}
                                                </span>
                                            </c:when>

                                            <c:otherwise>

                                                <div class="d-flex flex-column" style="width: fit-content;">

                                                    <span class="badge bg-secondary text-decoration-line-through text-white opacity-75">
                                                        ${u.roleName}
                                                    </span>

                                                    <small class="text-danger fw-bold mt-1" style="font-size: 0.7rem;">
                                                        <i class="fa-solid fa-triangle-exclamation me-1"></i>
                                                        Role Disabled
                                                    </small>

                                                </div>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${u.isActive}">
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

                                        <a href="${pageContext.request.contextPath}/admin/user/detail?id=${u.userId}"
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