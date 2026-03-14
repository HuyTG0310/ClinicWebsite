<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Header Section -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-flask-vial text-primary me-2"></i>
            Manage Test Result
        </h2>
        <p class="text-muted mb-0">List of patients who have paid and are waiting for sample collection and result entry </p>
    </div>
</div>


<!-- Messages -->
<c:if test="${sessionScope.success != null}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
        <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% session.removeAttribute("success"); %>
</c:if>

<c:if test="${sessionScope.error != null}">
    <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
        <i class="fas fa-exclamation-triangle me-2"></i>${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% session.removeAttribute("error");%>
</c:if>


<!-- Search Form -->
<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form action="${basePath}/lab-queue/list" method="get">
            <div class="row g-3">

                <div class="col-md-5">
                    <label class="form-label small text-muted fw-bold">Patient Name / Medical Record Code</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fa-solid fa-magnifying-glass text-muted"></i>
                        </span>
                        <input type="text" class="form-control" name="search"
                               value="${param.search}" placeholder="Enter keyword...">
                    </div>
                </div>

                <div class="col-md-4">
                    <label class="form-label small text-muted fw-bold">Test status</label>
                    <select class="form-select" name="status">
                        <option value="ALL" ${param.status == 'ALL' ? 'selected' : ''}>
                            All status
                        </option>

                        <option value="PENDING" ${param.status == 'PENDING' || empty param.status ? 'selected' : ''}>
                            Waiting
                        </option>

                        <option value="COMPLETED" ${param.status == 'COMPLETED' ? 'selected' : ''}>
                            Completed
                        </option>
                    </select>
                </div>

                <div class="col-md-3 d-flex align-items-end gap-2">
                    <button class="btn btn-primary flex-grow-1" type="submit">
                        <i class="fas fa-search me-2"></i>Search
                    </button>

                    <c:if test="${not empty param.search or param.status == 'COMPLETED' or param.status == 'ALL'}">
                        <a href="${basePath}/lab-queue/list"
                           class="btn btn-outline-secondary" title="Reset filter">
                            <i class="fas fa-times"></i>
                        </a>
                    </c:if>
                </div>

            </div>
        </form>
    </div>
</div>


<!-- Queue Table -->
<div class="card shadow-sm">
    <div class="card-header bg-white py-3">
        <h5 class="mb-0">
            <i class="fa-solid fa-list-check me-2 text-primary"></i>
            Test list
        </h5>
    </div>

    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">

                <thead class="table-light">
                    <tr>
                        <th class="px-4 text-center" width="10%">Mr Id</th>
                        <th width="25%">Patient</th>
                        <th width="15%" class="text-center">Gender / Age</th>
                        <th width="20%">Doctor's prescription</th>
                        <th width="15%" class="text-center">Progress</th>
                        <th width="15%" class="text-center">Action</th>
                    </tr>
                </thead>

                <tbody>

                    <c:forEach items="${queue}" var="row">

                        <tr class="${row.isFullyCompleted ? 'bg-light' : ''}">

                            <td class="px-4 text-center">
                                <strong>#${row.medicalRecordId}</strong>
                            </td>

                            <td>
                                <strong class="text-primary">${row.patientName}</strong>
                                <div class="small text-muted">
                                    Patient code: ${row.patientId}
                                </div>
                            </td>

                            <td class="text-center">

                                <span class="badge ${row.gender == 'Male' ? 'bg-primary' : 'bg-danger'}">
                                    ${row.gender}
                                </span>

                                <span class="ms-1 fw-bold">${row.age}T</span>

                            </td>

                            <td class="small fw-bold text-muted">
                                <i class="fa-solid fa-user-doctor me-1"></i>
                                ${row.doctorName}
                            </td>

                            <td class="text-center">

                                <c:choose>

                                    <c:when test="${row.isFullyCompleted}">
                                        <span class="badge bg-success">
                                            <i class="fa-solid fa-check me-1"></i>
                                            Completed (${row.progress})
                                        </span>
                                    </c:when>

                                    <c:when test="${row.completedTests == 0}">
                                        <span class="badge bg-warning text-dark">
                                            Waiting (${row.progress})
                                        </span>
                                    </c:when>

                                    <c:otherwise>
                                        <span class="badge bg-primary">
                                            Processing (${row.progress})
                                        </span>
                                    </c:otherwise>

                                </c:choose>

                            </td>

                            <td class="text-center">

                                <c:choose>

                                    <c:when test="${row.isFullyCompleted}">

                                        <a href="${basePath}/lab-test/detail?mrId=${row.medicalRecordId}" 
                                           class="btn btn-sm btn-outline-primary"
                                           title="View Details">
                                            <i class="fas fa-eye"></i> View
                                        </a>

                                    </c:when>

                                    <c:otherwise>

                                        <a href="${basePath}/lab-test/edit?mrId=${row.medicalRecordId}"
                                           class="btn btn-sm btn-success">
                                            <i class="fa-solid fa-microscope me-1"></i>
                                            Process
                                        </a>

                                    </c:otherwise>

                                </c:choose>

                            </td>

                        </tr>

                    </c:forEach>

                </tbody>

            </table>
        </div>
    </div>
</div>
