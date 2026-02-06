<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Header Section -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fas fa-users text-primary me-2"></i>
            Patient Management
        </h2>
        <p class="text-muted mb-0">Manage and view all patients</p>
    </div>
    <a href="${pageContext.request.contextPath}/AddPatient" class="btn btn-primary">
        <i class="fas fa-plus-circle me-2"></i>Add New Patient
    </a>
</div>

<!-- Search Form -->
<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form method="get" action="${pageContext.request.contextPath}/PatientList" id="searchForm">
            <div class="row g-3">
                <div class="col-md-10">
                    <div class="input-group">
                        <span class="input-group-text bg-white">
                            <i class="fas fa-search text-muted"></i>
                        </span>
                        <input type="text" class="form-control" name="searchKeyword" id="searchKeyword"
                               value="${searchKeyword}" placeholder="Search by name, phone, or address...">
                    </div>
                </div>
                <div class="col-md-2">
                    <button class="btn btn-primary w-100" type="submit">
                        <i class="fas fa-search me-2"></i>Search
                    </button>
                </div>
                <c:if test="${searchKeyword != null}">
                    <div class="col-12">
                        <a href="${pageContext.request.contextPath}/PatientList" class="btn btn-outline-secondary btn-sm">
                            <i class="fas fa-times me-2"></i>Clear Search
                        </a>
                    </div>
                </c:if>
            </div>
        </form>
    </div>
</div>

<!-- Patient List Table -->
<div class="card shadow-sm">
    <div class="card-header bg-white py-3">
        <h5 class="mb-0">
            <i class="fas fa-list me-2 text-primary"></i>Patient List
        </h5>
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="px-4">
                            <i class="fas fa-hashtag me-2 text-primary"></i>ID
                        </th>
                        <th>
                            <i class="fas fa-user me-2 text-primary"></i>Full Name
                        </th>
                        <th>
                            <i class="fas fa-phone me-2 text-primary"></i>Phone
                        </th>
                        <th>
                            <i class="fas fa-calendar me-2 text-primary"></i>Date of Birth
                        </th>
                        <th>
                            <i class="fas fa-venus-mars me-2 text-primary"></i>Gender
                        </th>
                        <th class="text-center">
                            <i class="fas fa-cog me-2 text-primary"></i>Actions
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${patientList}" var="patient">
                        <tr>
                            <td class="px-4">
                                <strong>#${patient.patientId}</strong>
                            </td>
                            <td>
                                <strong>${patient.fullName}</strong>
                            </td>
                            <td>
                                ${patient.phone}
                            </td>
                            <td>
                                <fmt:formatDate value="${patient.dateOfBirth}" pattern="dd/MM/yyyy" />
                            </td>
                            <td>
                                <c:if test="${patient.gender == 'Male'}">
                                    <span class="badge bg-primary">
                                        <i class="fas fa-mars me-1"></i>Male
                                    </span>
                                </c:if>
                                <c:if test="${patient.gender == 'Female'}">
                                    <span class="badge bg-danger">
                                        <i class="fas fa-venus me-1"></i>Female
                                    </span>
                                </c:if>
                                <c:if test="${patient.gender != 'Male' && patient.gender != 'Female'}">
                                    <span class="badge bg-secondary">
                                        <i class="fas fa-genderless me-1"></i>${patient.gender}
                                    </span>
                                </c:if>
                            </td>
                            <td class="text-center">
                                <div class="btn-group" role="group">
                                    <a href="${pageContext.request.contextPath}/ViewPatient?id=${patient.patientId}" 
                                       class="btn btn-sm btn-outline-primary" 
                                       title="View Details">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty patientList}">
                        <tr>
                            <td colspan="6" class="text-center py-5">
                                <i class="fas fa-inbox fa-3x text-muted mb-3 d-block"></i>
                                <p class="text-muted mb-0">No patients found</p>
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

<script>
    // Auto-submit form when search keyword is cleared
    const searchKeywordInput = document.getElementById('searchKeyword');
    const searchForm = document.getElementById('searchForm');

    searchKeywordInput?.addEventListener('input', function () {
        // If input is empty, auto-submit the form to reset list
        if (this.value.trim() === '') {
            searchForm.submit();
        }
    });
</script>