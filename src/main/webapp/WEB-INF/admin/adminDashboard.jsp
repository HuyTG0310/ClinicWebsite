<%-- 
    Document   : adminDashboard
    Created on : Feb 3, 2026, 9:08:21 PM
    Author     : huytr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- ================= HEADER ================= -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-gauge text-primary me-2"></i>
            Admin Dashboard
        </h2>
        <p class="text-muted mb-0">
            Tổng quan và truy cập nhanh các chức năng quản trị
        </p>
    </div>

    <span class="text-muted">
        Xin chào, <b>Admin</b>
    </span>
</div>

<!-- ================= QUICK ACCESS ================= -->
<div class="row g-4">

    <!-- STAFF -->
    <div class="col-xl-3 col-md-4 col-sm-6">
        <a href="${pageContext.request.contextPath}/lab/history"
           class="dashboard-card text-decoration-none">
            <div class="card shadow-sm h-100">
                <div class="card-body text-center">
                    <div class="dashboard-icon bg-primary">
                        <i class="fa-solid fa-users"></i>
                    </div>
                    <h6 class="mt-3 mb-1">Manage Staff</h6>
                    <p class="text-muted small mb-0">
                        Quản lý nhân viên
                    </p>
                </div>
            </div>
        </a>
    </div>

    <!-- ROLE -->
    <div class="col-xl-3 col-md-4 col-sm-6">
        <a href="${pageContext.request.contextPath}/lab/dashboard"
           class="dashboard-card text-decoration-none">
            <div class="card shadow-sm h-100">
                <div class="card-body text-center">
                    <div class="dashboard-icon bg-success">
                        <i class="fa-solid fa-user-shield"></i>
                    </div>
                    <h6 class="mt-3 mb-1">Manage Role</h6>
                    <p class="text-muted small mb-0">
                        Phân quyền hệ thống
                    </p>
                </div>
            </div>
        </a>
    </div>

    <!-- SPECIALTY -->
    <div class="col-xl-3 col-md-4 col-sm-6">
        <a href="${pageContext.request.contextPath}/lab/tests"
           class="dashboard-card text-decoration-none">
            <div class="card shadow-sm h-100">
                <div class="card-body text-center">
                    <div class="dashboard-icon bg-info">
                        <i class="fa-solid fa-stethoscope"></i>
                    </div>
                    <h6 class="mt-3 mb-1">Manage Specialty</h6>
                    <p class="text-muted small mb-0">
                        Chuyên khoa
                    </p>
                </div>
            </div>
        </a>
    </div>

    <!-- SERVICE -->
    <div class="col-xl-3 col-md-4 col-sm-6">
        <a href="${pageContext.request.contextPath}/admin/service/list"
           class="dashboard-card text-decoration-none">
            <div class="card shadow-sm h-100">
                <div class="card-body text-center">
                    <div class="dashboard-icon bg-warning text-dark">
                        <i class="fa-solid fa-notes-medical"></i>
                    </div>
                    <h6 class="mt-3 mb-1">Manage Service</h6>
                    <p class="text-muted small mb-0">
                        Dịch vụ khám bệnh
                    </p>
                </div>
            </div>
        </a>
    </div>

    <!-- ROOM -->
    <div class="col-xl-3 col-md-4 col-sm-6">
        <a href="${pageContext.request.contextPath}/lab/history"
           class="dashboard-card text-decoration-none">
            <div class="card shadow-sm h-100">
                <div class="card-body text-center">
                    <div class="dashboard-icon bg-secondary">
                        <i class="fa-solid fa-door-open"></i>
                    </div>
                    <h6 class="mt-3 mb-1">Manage Room</h6>
                    <p class="text-muted small mb-0">
                        Phòng khám
                    </p>
                </div>
            </div>
        </a>
    </div>

    <!-- MEDICINE -->
    <div class="col-xl-3 col-md-4 col-sm-6">
        <a href="${pageContext.request.contextPath}/lab/history"
           class="dashboard-card text-decoration-none">
            <div class="card shadow-sm h-100">
                <div class="card-body text-center">
                    <div class="dashboard-icon bg-danger">
                        <i class="fa-solid fa-pills"></i>
                    </div>
                    <h6 class="mt-3 mb-1">Manage Medicine</h6>
                    <p class="text-muted small mb-0">
                        Thuốc & vật tư
                    </p>
                </div>
            </div>
        </a>
    </div>

</div>

<style>
    /* ===== DASHBOARD ===== */
    .dashboard-card .card {
        border-radius: 14px;
        transition: all 0.25s ease;
    }

    .dashboard-card .card:hover {
        transform: translateY(-6px);
        box-shadow: 0 12px 24px rgba(0,0,0,0.12);
    }

    .dashboard-icon {
        width: 56px;
        height: 56px;
        border-radius: 50%;
        color: #fff;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.4rem;
        margin: 0 auto;
    }

</style>