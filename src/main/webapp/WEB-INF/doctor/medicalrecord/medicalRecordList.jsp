<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="d-flex justify-content-between align-items-center mb-4">

    <div>
        <h2 class="mb-1">
            <i class="fas fa-notes-medical text-primary me-2"></i>
            Manage Medical Records
        </h2>
        <p class="text-muted mb-0">Manage and monitor all patient medical records</p>
    </div>

</div>



<c:if test="${sessionScope.success != null}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm">
        <i class="fa-solid fa-circle-check me-2"></i>${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% session.removeAttribute("success"); %>
</c:if>

<c:if test="${sessionScope.error != null}">
    <div class="alert alert-danger alert-dismissible fade show shadow-sm">
        <i class="fa-solid fa-circle-exclamation me-2"></i>${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% session.removeAttribute("error");%>
</c:if>



<!-- SEARCH -->
<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form action="${basePath}/medical-record/list" method="get">
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
                               value="${param.searchKeyword}" placeholder="Patient name or record ID...">
                    </div>
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Completion Date</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fa-regular fa-calendar text-muted"></i>
                        </span>
                        <input type="date"
                               class="form-control"
                               name="searchDate"
                               value="${param.searchDate}"> </div>
                </div>

                <div class="col-md-3 d-flex align-items-end gap-2">
                    <button class="btn btn-primary flex-grow-1" type="submit">
                        <i class="fas fa-filter me-2"></i>Search
                    </button>

                    <c:if test="${not empty searchKeyword or not empty searchDate}">
                        <a href="${basePath}/medical-record/list"
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



<!-- TABLE -->
<div class="card shadow-sm">

    <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">

        <h5 class="mb-0">
            <i class="fas fa-list me-2 text-primary"></i>Record List
        </h5>

        <span class="badge bg-light text-dark border">
            Total: ${recordList.size()} records
        </span>

    </div>


    <div class="card-body p-0">

        <div class="table-responsive">

            <table class="table table-hover align-middle mb-0">

                <thead class="table-light">

                    <tr>

                        <th class="px-4">#ID</th>

                        <th>
                            <i class="fas fa-user-injured me-2 text-primary"></i>Patient
                        </th>

                        <th>
                            <i class="fas fa-stethoscope me-2 text-primary"></i>Diagnosis
                        </th>

                        <th>
                            <i class="fas fa-user-md me-2 text-primary"></i>Doctor
                        </th>

                        <th>
                            <i class="fas fa-calendar-alt me-2 text-primary"></i>Date
                        </th>

                        <th class="text-center">
                            <i class="fas fa-cog me-2 text-primary"></i>Actions
                        </th>

                    </tr>

                </thead>


                <tbody>

                    <c:forEach items="${recordList}" var="r">

                        <tr>

                            <td class="px-4 fw-bold text-secondary">
                                #${r.medicalRecordId}
                            </td>


                            <td>

                                <div class="fw-bold text-primary">
                                    ${r.patientName}
                                </div>

                                <div class="small text-muted">
                                    ${r.age} yrs | ${r.gender}
                                </div>

                            </td>


                            <td>
                                <c:choose>
                                    <c:when test="${not empty r.diagnosis}">
                                        <div class="fw-bold text-dark text-truncate" style="max-width:250px" title="${r.diagnosis}">
                                            ${r.diagnosis}
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted fst-italic">Pending...</span>
                                    </c:otherwise>

                                </c:choose>
                            </td>


                            <td>

                                <span class="badge bg-info">
                                    <i class="fas fa-user-check me-1"></i>
                                    BS. ${r.doctorName}
                                </span>

                            </td>


                            <td>

                                <c:choose>

                                    <c:when test="${not empty r.completedAt}">

                                        <fmt:formatDate value="${r.completedAt}"
                                                        pattern="dd/MM/yyyy HH:mm"/>

                                    </c:when>

                                    <c:otherwise>

                                        <span class="badge bg-warning text-dark">
                                            In Progress
                                        </span>

                                    </c:otherwise>

                                </c:choose>

                            </td>



                            <td class="text-center">

                                <c:choose>

                                    <c:when test="${r.canView}">

                                        <a href="${basePath}/medical-record/detail?id=${r.medicalRecordId}"
                                           class="btn btn-sm btn-outline-primary"
                                           title="View Details">

                                            <i class="fas fa-eye me-1"></i>View

                                        </a>

                                    </c:when>


                                    <c:otherwise>

                                        <button type="button"
                                                class="btn btn-sm btn-outline-secondary"
                                                onclick="alert('BẢO MẬT: Bạn không có quyền xem bệnh án này do Bệnh nhân thuộc Chuyên khoa khác và bạn chưa từng phụ trách!');"
                                                title="Access Denied">

                                            <i class="fas fa-eye me-1"></i>View

                                        </button>

                                    </c:otherwise>

                                </c:choose>

                            </td>

                        </tr>

                    </c:forEach>


                    <c:if test="${empty recordList}">

                        <tr>

                            <td colspan="6" class="text-center py-5">

                                <i class="fas fa-inbox fa-3x text-muted mb-3 d-block"></i>

                                <p class="text-muted mb-0">
                                    No records found
                                </p>

                            </td>

                        </tr>

                    </c:if>

                </tbody>

            </table>

        </div>

    </div>

</div>