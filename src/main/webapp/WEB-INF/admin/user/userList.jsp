<%-- 
    Document   : userList
    Created on : Feb 7, 2026
    Author     : huytr
--%>

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
        <i class="fa-solid fa-user-plus me-2"></i> Add New Staff
    </a>
</div>

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form method="get" action="${pageContext.request.contextPath}/admin/user/list">
            <div class="row g-3 align-items-end">

                <div class="col-lg-5 col-md-12">
                    <label class="form-label fw-semibold">Search</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fa-solid fa-magnifying-glass text-muted"></i>
                        </span>
                        <input type="text" name="keyword" value="${param.keyword}"
                               class="form-control" placeholder="Enter username or fullname..." />
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <label class="form-label fw-semibold">Role</label>
                    <select name="roleId" class="form-select">
                        <option value="">All Roles</option>
                        <c:forEach var="r" items="${roles}">
                            <option value="${r.roleId}" <c:if test="${param.roleId == r.roleId}">selected</c:if>>
                                ${r.roleName}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="col-lg-2 col-md-6">
                    <label class="form-label fw-semibold">Status</label>
                    <select name="status" class="form-select">
                        <option value="">All</option>
                        <option value="1" <c:if test="${param.status == '1'}">selected</c:if>>Active</option>
                        <option value="0" <c:if test="${param.status == '0'}">selected</c:if>>Inactive</option>
                        </select>
                    </div>

                    <div class="col-lg-2 col-md-12">
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="fas fa-search me-2"></i> Search
                        </button>
                    </div>

                <c:if test="${not empty param.keyword or not empty param.roleId or not empty param.status}">
                    <div class="col-12 mt-2">
                        <a href="${pageContext.request.contextPath}/admin/user/list" class="btn btn-outline-secondary btn-sm">
                            <i class="fas fa-times me-2"></i>Clear Filters
                        </a>
                    </div>
                </c:if>
            </div>
        </form>
    </div>
</div>

<div class="card shadow-sm border-0">
    <div class="card-header bg-white py-3">
        <h5 class="mb-0">
            <i class="fa-solid fa-list me-2 text-primary"></i> User List
        </h5>
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
                                <th class="px-4"><i class="fa-solid fa-user me-2 text-primary"></i>Username</th>
                                <th><i class="fa-solid fa-id-card me-2 text-primary"></i>Fullname</th>
                                <th><i class="fa-solid fa-user-tag me-2 text-primary"></i>Role</th>
                                <th><i class="fa-solid fa-toggle-on me-2 text-primary"></i>Status</th>
                                <th class="text-center"><i class="fa-solid fa-gear me-2 text-primary"></i>Action</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td class="px-4 fw-bold text-secondary">${u.username}</td>
                                    <td class="fw-semibold">${u.fullName}</td>

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
                                                        <i class="fa-solid fa-triangle-exclamation me-1"></i>Role Disabled
                                                    </small>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${u.isActive}">
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
                                        <a href="${pageContext.request.contextPath}/admin/user/detail?id=${u.userId}"
                                           class="btn btn-sm btn-outline-primary" title="View Details">
                                            <i class="fa-solid fa-eye me-1"></i>View
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

<style>
    /* CSS đồng bộ toàn hệ thống */
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

    .bg-success-subtle {
        background-color: #d1e7dd !important;
    }
    .bg-danger-subtle {
        background-color: #f8d7da !important;
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

    .input-group-text {
        border-right: none;
        border-radius: 8px 0 0 8px;
    }
    .input-group .form-control {
        border-left: none;
        border-radius: 0 8px 8px 0;
    }
</style>