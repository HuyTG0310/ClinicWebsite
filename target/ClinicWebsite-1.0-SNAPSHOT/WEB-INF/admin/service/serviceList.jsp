<%-- 
    Document   : serviceList
    Created on : Feb 4, 2026
    Author     : huytr
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- HEADER -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-stethoscope text-primary me-2"></i>
            Service Management
        </h2>
        <p class="text-muted mb-0">
            Manage and monitor all medical services
        </p>
    </div>

    <a href="${pageContext.request.contextPath}/admin/service/add"
       class="btn btn-success">
        <i class="fa-solid fa-plus-circle me-2"></i>
        Add
    </a>
</div>

<!-- SEARCH --><div class="card shadow-sm mb-4">
    <div class="card-body">
        <form method="get"
              action="${pageContext.request.contextPath}/admin/service/list">

            <div class="row g-3 align-items-end">

                <!-- KEYWORD -->
                <div class="col-lg-6 col-md-12">
                    <label class="form-label fw-semibold">Service Name</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fa-solid fa-magnifying-glass text-muted"></i>
                        </span>
                        <input type="text"
                               name="keyword"
                               value="${keyword}"
                               class="form-control"
                               placeholder="Search by service name..." />
                    </div>
                </div>

                <!-- STATUS -->
                <div class="col-lg-3 col-md-6">
                    <label class="form-label fw-semibold">Status</label>
                    <select name="status" class="form-select">
                        <option value="">All</option>
                        <option value="1"
                                <c:if test="${status == '1'}">selected</c:if>>
                                    Active
                                </option>
                                <option value="0"
                                <c:if test="${status == '0'}">selected</c:if>>
                                    Inactive
                                </option>
                        </select>
                    </div>

                    <!-- BUTTON -->
                    <div class="col-lg-2 col-md-6">
                        <button type="submit"
                                class="btn btn-primary w-100">
                            <i class="fa-solid fa-filter me-2"></i>
                            Search
                        </button>
                    </div>

                </div>
            </form>
        </div>
    </div>


    <!-- LIST -->
    <div class="card shadow-sm">
        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-list me-2 text-primary"></i>
                Service List
            </h5>
        </div>

        <div class="card-body p-0">

        <c:choose>
            <c:when test="${services == null || services.size() == 0}">
                <div class="text-center py-5">
                    <i class="fa-solid fa-inbox fa-3x text-muted mb-3 d-block"></i>
                    <p class="text-muted mb-0">
                        No services found
                    </p>
                </div>
            </c:when>

            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>
                                    <i class="fa-solid fa-hashtag me-2 text-primary"></i>ID
                                </th>
                                <th>
                                    <i class="fa-solid fa-notes-medical me-2 text-primary"></i>
                                    Service Name
                                </th>
                                <th>
                                    <i class="fa-solid fa-layer-group me-2 text-primary"></i>
                                    Category
                                </th>
                                <th>
                                    <i class="fa-solid fa-dollar-sign me-2 text-primary"></i>
                                    Price
                                </th>
                                <th>
                                    <i class="fa-solid fa-toggle-on me-2 text-primary"></i>
                                    Status
                                </th>
                                <th class="text-center">
                                    <i class="fa-solid fa-gear me-2 text-primary"></i>
                                    Actions
                                </th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="s" items="${services}">
                                <tr>
                                    <td>
                                        ${s.serviceId}
                                    </td>

                                    <td class="fw-semibold">
                                        ${s.serviceName}
                                    </td>

                                    <td>
                                        <span class="badge bg-light text-dark border">
                                            ${s.category}
                                        </span>
                                    </td>

                                    <td>
                                        ${s.currentPrice}
                                    </td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${s.isActive}">
                                                <span class="badge bg-success">
                                                    <i class="fa-solid fa-check-circle me-1"></i>
                                                    Active
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger">
                                                    <i class="fa-solid fa-times-circle me-1"></i>
                                                    Inactive
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/admin/service/detail?id=${s.serviceId}"
                                           class="btn btn-sm btn-outline-primary"
                                           title="View Details">
                                            <i class="fa-solid fa-eye"></i>
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
    }

    .badge {
        padding: 0.4em 0.8em;
        font-weight: 500;
        border-radius: 6px;
    }

    .btn {
        border-radius: 8px;
        transition: all 0.25s ease;
    }

    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
</style>