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
            <div class="row g-3 align-items-end">
                <div class="col-lg-7 col-md-8">
                    <label class="form-label fw-semibold">Medicine Name</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fa-solid fa-magnifying-glass text-muted"></i>
                        </span>
                        <input type="text" name="keyword" value="${keyword}" 
                               class="form-control" placeholder="Search medicine by name...">
                    </div>
                </div>

                <div class="col-lg-3 col-md-4">
                    <label class="form-label fw-semibold">Status</label>
                    <select name="status" class="form-select">
                        <option value="all" ${status == 'all' || empty status ? 'selected' : ''}>All Status</option>
                        <option value="active" ${status == 'active' ? 'selected' : ''}>Active</option>
                        <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>

                <div class="col-lg-2 col-md-12">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search me-2"></i>Search
                    </button>
                </div>
                
                <c:if test="${not empty keyword}">
                    <div class="col-12 mt-2">
                        <a href="${basePath}/medicine/list" class="btn btn-outline-secondary btn-sm">
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
            <i class="fa-solid fa-list me-2 text-primary"></i>Medicine List
        </h5>
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="px-4" style="width: 100px">
                            <i class="fa-solid fa-hashtag me-2 text-primary"></i>ID
                        </th>
                        <th>
                            <i class="fa-solid fa-capsules me-2 text-primary"></i>Medicine Name
                        </th>
                        <th style="width: 150px">
                            <i class="fa-solid fa-weight-hanging me-2 text-primary"></i>Unit
                        </th>
                        <th style="width: 150px">
                            <i class="fa-solid fa-toggle-on me-2 text-primary"></i>Status
                        </th>
                        <th class="text-center" style="width: 150px">
                            <i class="fa-solid fa-gear me-2 text-primary"></i>Actions
                        </th>
                    </tr>
                </thead>

                <tbody>
                    <c:choose>
                        <c:when test="${empty list}">
                            <tr>
                                <td colspan="5" class="text-center py-5">
                                    <i class="fa-regular fa-folder-open fa-3x text-muted mb-3 d-block"></i>
                                    <p class="text-muted mb-0">No medicine found</p>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${list}" var="m">
                                <tr>
                                    <td class="px-4"><span class="fw-bold text-secondary">#${m.medicineId}</span></td>
                                    <td class="fw-medium">${m.medicineName}</td>
                                    <td>
                                        <span class="badge bg-light text-dark border">
                                            ${m.unit}
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${m.isActive}">
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
                                        <div class="btn-group" role="group">
                                            <a href="${basePath}/medicine/detail?id=${m.medicineId}"
                                               class="btn btn-sm btn-outline-primary" title="View Details">
                                                <i class="fa-solid fa-eye me-1"></i> View
                                            </a>
                                        </div>
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

<style>
    /* Card & General Styles */
    .card {
        border: none;
        border-radius: 12px;
    }
    .card-header {
        border-bottom: 1px solid rgba(0,0,0,0.08);
        border-radius: 12px 12px 0 0 !important;
    }

    /* Table Hover Effect */
    .table-hover tbody tr:hover {
        background-color: rgba(13, 110, 253, 0.05);
        transition: background-color 0.2s ease;
    }

    /* Badge & Button Styling */
    .badge {
        padding: 0.4em 0.8em;
        font-weight: 500;
        border-radius: 6px;
    }

    .btn {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        border-radius: 8px;
        padding: 0.5rem 1.2rem;
        font-weight: 500;
        transition: all 0.25s ease;
    }

    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }


    /* Input Styling */
    .input-group-text {
        border-right: none;
        border-radius: 8px 0 0 8px;
    }
    .input-group .form-control {
        border-left: none;
        border-radius: 0 8px 8px 0;
    }
</style>