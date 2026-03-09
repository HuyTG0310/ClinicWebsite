<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

<c:if test="${sessionScope.success != null}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
        <i class="fa-solid fa-circle-check me-2"></i>${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% session.removeAttribute("success"); %>
</c:if>

<c:if test="${sessionScope.error != null}">
    <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
        <i class="fa-solid fa-circle-exclamation me-2"></i>${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% session.removeAttribute("error");%>
</c:if>

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form action="${basePath}/room/list" method="get">
            <div class="row g-3">

                <div class="col-md-9">
                    <label class="form-label fw-bold small text-muted">Keyword</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fas fa-search text-muted"></i>
                        </span>
                        <input type="text" class="form-control"
                               name="searchKeyword"
                               value="${searchKeyword}"
                               placeholder="Room name...">
                    </div>
                </div>

                <div class="col-md-3 d-flex align-items-end gap-2">

                    <button class="btn btn-primary flex-grow-1" type="submit">
                        <i class="fas fa-filter me-2"></i>Search
                    </button>

                    <c:if test="${searchKeyword != null}">
                        <a href="${basePath}/room/list"
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
            <i class="fas fa-list me-2 text-primary"></i>Room List
        </h5>

        <span class="badge bg-light text-dark border">
            Total: ${roomList.size()} records
        </span>

    </div>

    <div class="card-body p-0">
        <div class="table-responsive">

            <table class="table table-hover align-middle mb-0">

                <thead class="table-light">
                    <tr>

                        <th class="px-4">#ID</th>

                        <th>
                            <i class="fas fa-door-open me-2 text-primary"></i>Room
                        </th>

                        <th>
                            <i class="fas fa-stethoscope me-2 text-primary"></i>Specialty
                        </th>

                        <th>
                            <i class="fas fa-user-md me-2 text-primary"></i>Doctor
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

                            <td class="px-4 fw-bold text-secondary">
                                #${room.roomId}
                            </td>

                            <td>
                                <div class="fw-bold text-primary">
                                    ${room.roomName}
                                </div>
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
                                            <i class="fas fa-user-md me-1"></i>
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

                                <a href="${basePath}/room/detail?id=${room.roomId}"
                                   class="btn btn-sm btn-outline-primary"
                                   title="View Detail">

                                    <i class="fas fa-eye me-1"></i>View

                                </a>

                            </td>

                        </tr>
                    </c:forEach>

                    <c:if test="${empty roomList}">
                        <tr>
                            <td colspan="6" class="text-center py-5">
                                <i class="fas fa-door-closed fa-3x text-muted mb-3 d-block"></i>
                                <p class="text-muted mb-0">No rooms found</p>
                            </td>
                        </tr>
                    </c:if>

                </tbody>

            </table>

        </div>
    </div>
</div>

