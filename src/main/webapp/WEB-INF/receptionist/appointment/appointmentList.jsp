
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-calendar-check text-primary me-2"></i>
            Appointment Manager
        </h2>
        <p class="text-muted mb-0">Track and manage patient queues & schedules</p>
    </div>

    <c:if test="${hasAppointmentCreate}">
        <a href="${basePath}/appointment/create" class="btn btn-primary">
            <i class="fas fa-plus-circle me-2"></i>New Appointment
        </a>
    </c:if>

</div>

<c:if test="${not empty sessionScope.success}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
        <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% session.removeAttribute("success"); %>
</c:if>

<c:if test="${not empty sessionScope.error}">
    <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
        <i class="fas fa-exclamation-triangle me-2"></i>${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% session.removeAttribute("error");%>
</c:if>

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form action="${basePath}/appointment/list" method="get">
            <div class="row g-3">

                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Keyword</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fas fa-search text-muted"></i>
                        </span>
                        <input type="text" class="form-control" name="keyword" 
                               value="${param.keyword}" placeholder="Name or Phone...">
                    </div>
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Date</label>
                    <input type="date" class="form-control" name="date" 
                           value="${param.date != null ? param.date : paramDate}">
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Status</label>
                    <select class="form-select" name="status">
                        <option value="">-- All Status --</option>
                        <option value="WAITING" <c:if test="${param.status == 'WAITING'}">selected</c:if>>WAITING</option>
                        <option value="IN_PROGRESS" <c:if test="${param.status == 'IN_PROGRESS'}">selected</c:if>>IN PROGRESS</option>
                        <option value="COMPLETED" <c:if test="${param.status == 'COMPLETED'}">selected</c:if>>COMPLETED</option>
                        <option value="CANCELLED" <c:if test="${param.status == 'CANCELLED'}">selected</c:if>>CANCELLED</option>
                        </select>
                    </div>

                    <div class="col-md-3 d-flex align-items-end gap-2">
                        <button class="btn btn-primary flex-grow-1" type="submit">
                            <i class="fas fa-filter me-2"></i>Filter
                        </button>

                    <c:if test="${not empty param.keyword or not empty param.status or not empty param.date}">
                        <a href="${basePath}/appointment/list" 
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
            <i class="fas fa-list me-2 text-primary"></i>Patient Queue
        </h5>
        <span class="badge bg-light text-dark border">
            Total: ${appointmentList.size()} records
        </span>
    </div>

    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="px-4">#ID</th>
                        <th><i class="fa-solid fa-hospital-user me-2 text-primary"></i>Patient Info</th>
                        <th><i class="fa-solid fa-door-open me-2 text-primary"></i>Room / Doctor</th>
                        <th><i class="fa-solid fa-clock me-2 text-primary"></i>Time</th>
                        <th><i class="fa-solid fa-receipt me-2 text-primary"></i>Payment</th> 
                        <th><i class="fa-solid fa-toggle-on me-2 text-primary"></i>Status</th>
                        <th class="text-center"><i class="fas fa-cog me-2 text-primary"></i>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${appointmentList}" var="app">
                        <tr>
                            <td class="px-4 fw-bold text-secondary">#${app.appointmentId}</td>

                            <td>
                                <div class="fw-bold text-primary">${app.patientName}</div>
                                <div class="small text-muted">
                                    <i class="fa-solid fa-phone me-1" style="font-size: 0.8em;"></i>${app.patientPhone}
                                </div>
                            </td>

                            <td>
                                <div class="fw-bold">${app.roomName}</div>
                                <div class="small text-muted fst-italic">
                                    <i class="fa-solid fa-user-md me-1"></i>${app.doctorName}
                                </div>
                            </td>

                            <td>
                                <fmt:formatDate value="${app.appointmentTime}" pattern="HH:mm dd/MM/yyyy"/>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${app.paymentStatus == 'PAID'}">
                                        <span class="badge bg-success">
                                            <i class="fa-solid fa-check me-1"></i> PAID
                                        </span>
                                    </c:when>
                                    <c:when test="${app.paymentStatus == 'UNPAID'}">
                                        <span class="badge bg-warning text-dark">
                                            <i class="fa-solid fa-clock me-1"></i> UNPAID
                                        </span>
                                    </c:when>
                                    <c:when test="${app.paymentStatus == 'CANCELLED'}">
                                        <span class="badge bg-secondary">
                                            <i class="fa-solid fa-ban me-1"></i> CANCELLED
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">ERROR</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${app.status == 'WAITING'}">
                                        <span class="badge bg-warning text-dark">
                                            <i class="fa-solid fa-hourglass-half me-1"></i>WAITING
                                        </span>
                                    </c:when>
                                    <c:when test="${app.status == 'IN_PROGRESS'}">
                                        <span class="badge bg-primary">
                                            <i class="fa-solid fa-stethoscope me-1"></i>EXAMINING
                                        </span>
                                    </c:when>
                                    <c:when test="${app.status == 'COMPLETED'}">
                                        <span class="badge bg-success">
                                            <i class="fa-solid fa-check me-1"></i>COMPLETED
                                        </span>
                                    </c:when>
                                    <c:when test="${app.status == 'CANCELLED'}">
                                        <span class="badge bg-secondary">
                                            <i class="fa-solid fa-ban me-1"></i>CANCELLED
                                        </span>
                                    </c:when>
                                </c:choose>
                            </td>

                            <td class="text-center">
                                <a href="${basePath}/appointment/detail?id=${app.appointmentId}" 
                                   class="btn btn-sm btn-outline-primary" title="View Detail">
                                    <i class="fas fa-eye me-1"></i>View
                                </a>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty appointmentList}">
                        <tr>
                            <td colspan="7" class="text-center py-5">
                                <i class="fa-solid fa-calendar-times fa-3x text-muted mb-3 d-block"></i>
                                <p class="text-muted mb-0">No appointments found for this criteria.</p>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    function confirmCancel(id) {
        if (confirm('Are you sure you want to cancel appointment #' + id + '?')) {
            window.location.href = '${basePath}/appointment/cancel?id=' + id;
        }
    }
</script>

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
    .bg-success-subtle {
        background-color: #d1e7dd !important;
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