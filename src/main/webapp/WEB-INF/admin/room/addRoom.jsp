<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<style>
    body {
        background-color: #f5f7fa;
    }

    .container {
        max-width: 1200px;
        padding: 2rem 1rem;
    }

    /* Header */
    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 2rem;
    }

    .page-header h2 {
        font-size: 1.75rem;
        font-weight: 600;
        color: #1a1a1a;
        margin: 0;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .page-header h2 i {
        color: #0d6efd;
        font-size: 1.5rem;
    }

    .page-subtitle {
        color: #6c757d;
        font-size: 0.95rem;
        margin-top: 0.25rem;
    }

    /* Card */
    .card {
        border: none;
        border-radius: 12px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.08);
        background: white;
        overflow: hidden;
    }

    .card-header {
        background: white;
        border-bottom: 1px solid #e9ecef;
        padding: 1.25rem 1.5rem;
    }

    .card-header h5 {
        margin: 0;
        font-size: 1.1rem;
        font-weight: 600;
        color: #1a1a1a;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .card-header h5 i {
        color: #0d6efd;
    }

    .card-body {
        padding: 2rem 1.5rem;
    }

    /* Form */
    .form-label {
        font-weight: 600;
        color: #1a1a1a;
        font-size: 0.95rem;
        margin-bottom: 0.5rem;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .form-label i {
        color: #0d6efd;
        font-size: 0.9rem;
    }

    .form-label .text-danger {
        color: #dc3545 !important;
        margin-left: 0.25rem;
    }

    .input-group {
        box-shadow: 0 1px 2px rgba(0,0,0,0.05);
        border-radius: 8px;
        overflow: hidden;
    }

    .input-group-text {
        background: white;
        border: 1px solid #dee2e6;
        border-right: none;
        padding: 0.625rem 1rem;
    }

    .input-group-text i {
        color: #6c757d;
    }

    .form-control {
        border: 1px solid #dee2e6;
        border-left: none;
        padding: 0.625rem 1rem;
        font-size: 0.95rem;
        transition: all 0.2s;
    }

    .form-control:focus {
        border-color: #86b7fe;
        box-shadow: none;
    }

    .input-group:focus-within .input-group-text {
        border-color: #86b7fe;
    }

    .form-text {
        color: #6c757d;
        font-size: 0.875rem;
        margin-top: 0.5rem;
    }

    /* Switch */
    .form-switch {
        padding-left: 2.5em;
    }

    .form-check-input {
        width: 3em;
        height: 1.5em;
        cursor: pointer;
    }

    .form-check-label {
        cursor: pointer;
        font-weight: 600;
        color: #1a1a1a;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .form-check-label i {
        color: #28a745;
    }

    /* Buttons */
    .btn {
        padding: 0.625rem 1.5rem;
        font-weight: 500;
        border-radius: 8px;
        font-size: 0.95rem;
        transition: all 0.2s;
        border: none;
        display: inline-flex;
        align-items: center;
        gap: 0.5rem;
    }

    .btn-primary {
        background: #0d6efd;
        color: white;
    }

    .btn-primary:hover {
        background: #0b5ed7;
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(13,110,253,0.3);
    }

    .btn-secondary {
        background: #6c757d;
        color: white;
    }

    .btn-secondary:hover {
        background: #5c636a;
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(108,117,125,0.3);
    }

    .btn-outline-secondary {
        border: 1px solid #dee2e6;
        color: #6c757d;
        background: white;
    }

    .btn-outline-secondary:hover {
        background: #f8f9fa;
        color: #495057;
        border-color: #dee2e6;
        transform: translateY(-1px);
    }

    /* Alert */
    .alert {
        border: none;
        border-radius: 10px;
        border-left: 4px solid;
        padding: 1rem 1.25rem;
        margin-bottom: 1.5rem;
        display: flex;
        align-items: center;
        gap: 0.75rem;
    }

    .alert-danger {
        background: #f8d7da;
        color: #842029;
        border-left-color: #dc3545;
    }

    .alert i {
        font-size: 1.1rem;
    }

    .btn-close {
        margin-left: auto;
    }

    /* Form spacing */
    .mb-4 {
        margin-bottom: 1.5rem !important;
    }

    .action-buttons {
        display: flex;
        gap: 1rem;
        justify-content: flex-start;
        margin-top: 2rem;
        padding-top: 1.5rem;
        border-top: 1px solid #e9ecef;
    }
</style>
</head>
<body>
    <div class="container">
        <!-- Header Section -->
        <div class="page-header">
            <div>
                <h2>
                    <i class="fas fa-door-open"></i>
                    Add New Room
                </h2>
                <p class="page-subtitle">Create and configure a new examination room</p>
            </div>
            <a href="${basePath}/room/list" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left"></i>
                Back to List
            </a>
        </div>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle"></i>
                <span>${error}</span>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Form Card -->
        <div class="card">
            <div class="card-header">
                <h5>
                    <i class="fas fa-edit"></i>
                    Room Information
                </h5>
            </div>

            <div class="card-body">
                <form action="${basePath}/room/create" method="post">

                    <!-- Room Name -->
                    <div class="mb-4">
                        <label class="form-label">
                            <i class="fas fa-door-closed"></i>
                            Room Name
                            <span class="text-danger">*</span>
                        </label>
                        <div class="input-group">
                            <span class="input-group-text">
                                <i class="fas fa-tag"></i>
                            </span>
                            <input type="text" class="form-control" name="roomName"
                                   value="${roomName}" placeholder="Enter room name" required>
                        </div>
                        <div class="form-text">Room name must be unique</div>
                    </div>

                    <!-- Specialty ID -->
                    <div class="mb-4">
                        <label class="form-label">
                            <i class="fas fa-stethoscope"></i>
                            Specialty
                            <span class="text-danger">*</span>
                        </label>
                        <select id="specialtySelect" class="form-select" name="specialtyId" required>
                            <option value="">-- Select Specialty --</option>
                            <c:forEach var="s" items="${specialties}">
                                <option value="${s.specialtyId}"
                                        <c:if test="${s.specialtyId == specialtyId}">selected</c:if>>
                                    ${s.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Current Doctor ID -->
                    <div class="mb-4">
                        <label class="form-label">
                            <i class="fas fa-user-md"></i>
                            Current Doctor
                        </label>
                        <select id="doctorSelect" name="currentDoctorId" class="form-select">
                            <option value="">-- No Doctor Assigned --</option>
                            <c:forEach var="d" items="${doctors}">
                                <option value="${d.userId}"
                                        <c:if test="${d.userId == currentDoctorId}">selected</c:if>>
                                    ${d.fullName}
                                </option>
                            </c:forEach>
                        </select>
                        <div class="form-text">
                            Only doctors belonging to the selected specialty are listed
                        </div>
                    </div>

                    <!-- Active Status -->
                    <div class="mb-4">
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" id="isActive"
                                   name="isActive" value="true"
                                   ${isActive != null && isActive == 'true' ? 'checked' : ''}>
                            <label class="form-check-label" for="isActive">
                                <i class="fas fa-toggle-on"></i>
                                Active Room
                            </label>
                        </div>
                    </div>

                    <!-- Action Buttons -->
                    <div class="action-buttons">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i>
                            Add Room
                        </button>
                        <a href="${basePath}/room/list" class="btn btn-secondary">
                            <i class="fas fa-times"></i>
                            Cancel
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>


    <script>
        document.addEventListener("DOMContentLoaded", function () {

            const specialtySelect = document.getElementById("specialtySelect");
            const doctorSelect = document.getElementById("doctorSelect");

            if (!specialtySelect || !doctorSelect) {
                console.error("Select elements not found");
                return;
            }

            specialtySelect.addEventListener("change", function () {
                const specialtyId = this.value;

                doctorSelect.innerHTML =
                        '<option value="">-- No Doctor Assigned --</option>';

                if (!specialtyId)
                    return;

                fetch('${pageContext.request.contextPath}/api/doctors-by-specialty?specialtyId=' + specialtyId)
                        .then(res => res.json())
                        .then(data => {
                            data.forEach(d => {
                                const opt = document.createElement("option");
                                opt.value = d.userId;
                                opt.textContent = d.fullName;
                                doctorSelect.appendChild(opt);
                            });
                        })
                        .catch(err => console.error("Load doctors failed:", err));
            });
        });
    </script>
