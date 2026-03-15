<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fas fa-pills text-primary me-2"></i>
            Manage Prescriptions
        </h2>
        <p class="text-muted mb-0">Manage and monitor all patient prescriptions</p>
    </div>
</div>

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

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form action="${basePath}/prescription/list" method="get">
            <div class="row g-3">

                <div class="col-md-6">
                    <label class="form-label fw-bold small text-muted">Keyword</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fas fa-search text-muted"></i>
                        </span>
                        <input type="text"
                               class="form-control"
                               name="searchKeyword"
                               value="${param.searchKeyword}"
                               placeholder="Patient name or record ID...">
                    </div>
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Prescription Date</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fa-regular fa-calendar text-muted"></i>
                        </span>
                        <input type="date"
                               class="form-control"
                               name="searchDate"
                               value="${param.searchDate}"> 
                    </div>
                </div>

                <div class="col-md-3 d-flex align-items-end gap-2">
                    <button class="btn btn-primary flex-grow-1" type="submit">
                        <i class="fas fa-filter me-2"></i>Search
                    </button>

                    <c:if test="${not empty param.searchKeyword or not empty param.searchDate}">
                        <a href="${basePath}/prescription/list"
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
            <i class="fas fa-list me-2 text-primary"></i>Prescription Queue
        </h5>
        <span class="badge bg-light text-dark border">
            Total: ${prescriptionList.size()} records
        </span>
    </div>

    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">

                <thead class="table-light">
                    <tr>
                        <th class="px-4">#ID</th>
                        <th><i class="fas fa-user-injured me-2 text-primary"></i>Patient</th>
                        <th><i class="fas fa-stethoscope me-2 text-primary"></i>Diagnosis</th>
                        <th><i class="fas fa-user-md me-2 text-primary"></i>Doctor</th>
                        <th><i class="fas fa-calendar-alt me-2 text-primary"></i>Date</th>
                        <th class="text-center"><i class="fas fa-cog me-2 text-primary"></i>Actions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach items="${prescriptionList}" var="p">
                        <tr>
                            <td class="px-4 fw-bold text-secondary">
                                #${p.medicalRecordId}
                            </td>

                            <td>
                                <div class="fw-bold text-primary">
                                    ${p.patientName}
                                </div>
                                <div class="small text-muted">
                                    ${p.age} yrs | ${p.gender}
                                </div>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${not empty p.diagnosis}">
                                        <div class="fw-bold text-dark text-truncate" style="max-width:250px" title="${p.diagnosis}">
                                            ${p.diagnosis}
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted fst-italic">Pending...</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <span class="badge bg-info">
                                    <i class="fas fa-user-check me-1"></i>BS. ${p.doctorName}
                                </span>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${not empty p.completedAt}">
                                        <fmt:formatDate value="${p.completedAt}" pattern="dd/MM/yyyy HH:mm"/>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">N/A</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${p.canView}">
                                        <a href="${basePath}/prescription/detail?id=${p.medicalRecordId}"
                                           class="btn btn-sm btn-outline-primary"
                                           title="View Details">
                                            <i class="fas fa-eye me-1"></i>View
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button"
                                                class="btn btn-sm btn-outline-secondary"
                                                onclick="alert('BẢO MẬT: Bạn không có quyền xem đơn thuốc này do Bệnh nhân thuộc Chuyên khoa khác và bạn chưa từng phụ trách!');"
                                                title="Access Denied">
                                            <i class="fas fa-eye me-1"></i>View
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty prescriptionList}">
                        <tr>
                            <td colspan="6" class="text-center py-5">
                                <i class="fas fa-inbox fa-3x text-muted mb-3 d-block"></i>
                                <p class="text-muted mb-0">No records found</p>
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