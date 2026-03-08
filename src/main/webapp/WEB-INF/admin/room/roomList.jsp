<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Header Section -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fas fa-door-open text-primary me-2"></i>
            Manage Room
        </h2>
        <p class="text-muted mb-0">Manage and monitor all examination rooms</p>
    </div>
    <c:if test="${hasRoomCreate}">
        <a href="${basePath}/room/create" class="btn btn-primary">
            <i class="fas fa-plus-circle me-2"></i>Add New Room
        </a>
    </c:if>

</div>

<!-- Messages -->
<c:if test="${sessionScope.success != null}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
        <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% session.removeAttribute("success"); %>
</c:if>

<c:if test="${sessionScope.error != null}">
    <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
        <i class="fas fa-exclamation-triangle me-2"></i>${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% session.removeAttribute("error");%>
</c:if>

<!-- Search Form -->
<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form action="${basePath}/room/list" method="get">
            <div class="row g-3">
                <div class="col-md-10">
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fas fa-search text-muted"></i>
                        </span>
                        <input type="text" class="form-control" name="searchKeyword" 
                               value="${searchKeyword}" placeholder="Search by room name...">
                    </div>
                </div>
                <div class="col-md-2">
                    <button class="btn btn-primary w-100" type="submit">
                        <i class="fas fa-search me-2"></i>Search
                    </button>
                </div>
                <c:if test="${searchKeyword != null}">
                    <div class="col-12">
                        <a href="${basePath}/room/list" class="btn btn-outline-secondary btn-sm">
                            <i class="fas fa-times me-2"></i>Clear Search
                        </a>
                    </div>
                </c:if>
            </div>
        </form>
    </div>
</div>

<!-- Room List Table -->
<div class="card shadow-sm">
    <div class="card-header bg-white py-3">
        <h5 class="mb-0">
            <i class="fas fa-list me-2 text-primary"></i>Room List
        </h5>
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="px-4">
                            <i class="fas fa-door-closed me-2 text-primary"></i>Room Name
                        </th>
                        <th>
                            <i class="fas fa-stethoscope me-2 text-primary"></i>Specialty ID
                        </th>
                        <th>
                            <i class="fas fa-user-md me-2 text-primary"></i>Current Doctor
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
                    <c:forEach items="${roomList}" var="room">
                        <tr>
                            <td class="px-4">
                                <strong>${room.roomName}</strong>
                            </td>
                            <td>
                                <span class="badge bg-light text-dark border">
                                    ${room.specialtyName}
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty room.doctorName}">
                                        <span class="badge bg-info">
                                            <i class="fas fa-user-check me-1"></i>
                                            ${room.doctorName}
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">
                                            <i class="fas fa-user-slash me-1"></i>N/A
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${room.isActive}">
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
                                <div class="btn-group" role="group">
                                    <a href="${basePath}/room/detail?id=${room.roomId}" 
                                       class="btn btn-sm btn-outline-primary" 
                                       title="View Details">
                                        <i class="fas fa-eye"></i> View
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty roomList}">
                        <tr>
                            <td colspan="5" class="text-center py-5">
                                <i class="fas fa-inbox fa-3x text-muted mb-3 d-block"></i>
                                <p class="text-muted mb-0">No rooms found</p>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<style>
    /* Hover effect cho table rows */
    .table-hover tbody tr:hover {
        background-color: rgba(13, 110, 253, 0.05);
        transition: background-color 0.2s ease;
    }

    /* Card styling */
    .card {
        border: none;
        border-radius: 12px;
    }

    .card-header {
        border-bottom: 1px solid rgba(0,0,0,0.08);
        border-radius: 12px 12px 0 0 !important;
    }

    /* Badge styling */
    .badge {
        padding: 0.4em 0.8em;
        font-weight: 500;
        border-radius: 6px;
    }

    /* Button group styling */
    .btn-group .btn {
        border-radius: 6px !important;
        margin: 0 2px;
    }

    /* Input group styling */
    .input-group-text {
        border-right: none;
        border-radius: 8px 0 0 8px;
    }

    .input-group .form-control {
        border-left: none;
        border-radius: 0 8px 8px 0;
    }

    .input-group .form-control:focus {
        border-left: none;
        box-shadow: none;
    }

    .input-group:focus-within .input-group-text {
        border-color: #86b7fe;
    }

    /* Button styling */
    .btn {
        border-radius: 8px;
        padding: 0.5rem 1.2rem;
        font-weight: 500;
        transition: all 0.25s ease;
    }

    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }

    /* Alert styling */
    .alert {
        border: none;
        border-radius: 10px;
        border-left: 4px solid;
    }

    .alert-success {
        border-left-color: #28a745;
    }

    .alert-danger {
        border-left-color: #dc3545;
    }
</style>


