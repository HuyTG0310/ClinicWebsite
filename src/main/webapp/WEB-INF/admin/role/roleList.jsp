
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${param.msg eq 'addSuccess'}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
        <i class="fa-solid fa-check-circle me-2"></i> New role added successfully!
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
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
        <i class="fas fa-plus-circle me-2"></i>
        Add New Role
    </a>
</div>

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/admin/role/list" method="GET">
            <div class="row g-3">
                <div class="col-md-10">
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fa-solid fa-magnifying-glass text-muted"></i>
                        </span>
                        <input type="text" name="search" class="form-control" 
                               placeholder="Search by role name..." value="${searchValue}">
                    </div>
                </div>
                <div class="col-md-2">
                    <button class="btn btn-primary w-100" type="submit">
                        <i class="fa-solid fa-magnifying-glass me-2"></i>
                        Search
                    </button>
                </div>

                <c:if test="${not empty searchValue}">
                    <div class="col-12 mt-2">
                        <a href="${pageContext.request.contextPath}/admin/role/list" class="btn btn-outline-secondary btn-sm">
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
            <i class="fa-solid fa-list me-2 text-primary"></i>
            Role List
        </h5>
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="px-4" style="width: 15%">
                            <i class="fa-solid fa-hashtag me-2 text-primary"></i>ID
                        </th>
                        <th style="width: 40%">
                            <i class="fa-solid fa-id-card-clip me-2 text-primary"></i>Role Name
                        </th>
                        <th style="width: 20%" class="text-center">
                            <i class="fa-solid fa-toggle-on me-2 text-primary"></i>Status
                        </th>
                        <th style="width: 25%" class="text-center">
                            <i class="fa-solid fa-gear me-2 text-primary"></i>Action
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${roles}">
                        <tr>
                            <td class="px-4">
                                <span class="fw-bold text-secondary">#${r.roleId}</span>
                            </td>
                            <td>
                                <span class="fw-bold text-dark">${r.roleName}</span>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${r.isActive}">
                                        <span class="badge bg-success">
                                            <i class="fas fa-check-circle me-1"></i>Active
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">
                                            <i class="fas fa-times-circle me-1"></i>Inactive
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/admin/role/detail?roleId=${r.roleId}" 
                                   class="btn btn-sm btn-outline-primary" title="View Details">
                                    <i class="fa-solid fa-eye me-1"></i>View
                                </a>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty roles}">
                        <tr>
                            <td colspan="4" class="text-center py-5">
                                <i class="fa-solid fa-inbox fa-3x text-muted mb-3 d-block"></i>
                                <p class="text-muted mb-0">No roles found.</p>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<style>
    .table-hover tbody tr:hover {
        background-color: rgba(13,110,253,0.05);
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


    .input-group-text {
        border-right: none;
        border-radius: 8px 0 0 8px;
    }

    .input-group .form-control {
        border-left: none;
        border-radius: 0 8px 8px 0;
    }
</style>