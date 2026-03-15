<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-pills text-primary me-2"></i>
            Manage Medicine
        </h2>
        <p class="text-muted mb-0">Manage and monitor pharmacy inventory and drug details</p>
    </div>

    <c:if test="${hasMedicineCreate}">
        <a href="${basePath}/medicine/create" class="btn btn-primary">
            <i class="fas fa-plus-circle me-2"></i>Add New Medicine
        </a>
    </c:if>
</div>

<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form method="get" action="${basePath}/medicine/list">
            <div class="row g-3">

                <div class="col-md-6">
                    <label class="form-label fw-bold small text-muted">Keyword</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fas fa-search text-muted"></i>
                        </span>
                        <input type="text" name="keyword" value="${keyword}" 
                               class="form-control" placeholder="Medicine name...">
                    </div>
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Status</label>
                    <select name="status" class="form-select">
                        <option value="all" ${status == 'all' || empty status ? 'selected' : ''}>All Status</option>
                        <option value="active" ${status == 'active' ? 'selected' : ''}>Active</option>
                        <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>

                <div class="col-md-3 d-flex align-items-end gap-2">
                    <button type="submit" class="btn btn-primary flex-grow-1">
                        <i class="fas fa-search me-2"></i>Search
                    </button>

                    <c:if test="${not empty keyword}">
                        <a href="${basePath}/medicine/list"
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
            <i class="fa-solid fa-list me-2 text-primary"></i>Medicine List
        </h5>

        <span class="badge bg-light text-dark border">
            Total: ${list.size()} records
        </span>
    </div>

    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">

                <thead class="table-light">
                    <tr>
                        <th class="px-4">#ID</th>
                        <th>
                            <i class="fa-solid fa-capsules me-2 text-primary"></i>Medicine
                        </th>
                        <th>
                            <i class="fa-solid fa-weight-hanging me-2 text-primary"></i>Unit
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

                    <c:choose>

                        <c:when test="${empty list}">
                            <tr>
                                <td colspan="5" class="text-center py-5">
                                    <i class="fa-solid fa-pills fa-3x text-muted mb-3 d-block"></i>
                                    <p class="text-muted mb-0">No medicine found</p>
                                </td>
                            </tr>
                        </c:when>

                        <c:otherwise>

                            <c:forEach items="${list}" var="m">
                                <tr>

                                    <td class="px-4 fw-bold text-secondary">
                                        #${m.medicineId}
                                    </td>

                                    <td>
                                        <div class="fw-bold text-primary">
                                            ${m.medicineName}
                                        </div>
                                    </td>

                                    <td>
                                        <span class="badge bg-light text-dark border">
                                            ${m.unit}
                                        </span>
                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${m.isActive}">
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
                                        <a href="${basePath}/medicine/detail?id=${m.medicineId}"
                                           class="btn btn-sm btn-outline-primary"
                                           title="View Detail">
                                            <i class="fas fa-eye me-1"></i>View
                                        </a>
                                    </td>

                                </tr>
                            </c:forEach>

                        </c:otherwise>

                    </c:choose>

                </tbody>

            </table>
        </div>
    </div>
</div>