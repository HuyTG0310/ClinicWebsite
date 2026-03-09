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
        <h4 class="fw-bold mb-1">
            <i class="fa-solid fa-user-doctor text-primary me-2"></i>
            Doctor Dashboard
        </h4>
        <small class="text-muted">Quản lý bệnh nhân và lịch khám hôm nay</small>
    </div>

    <div class="text-end">
        <span class="text-muted">Xin chào,</span>
        <span class="fw-semibold text-primary">Doctor</span>
    </div>
</div>


<!-- ================= QUICK STATS ================= -->
<div class="row mb-4 g-3">

    <div class="col-md-4">
        <div class="card border-0 shadow-sm stat-card">
            <div class="card-body d-flex align-items-center">
                <div class="icon-box bg-primary text-white me-3">
                    <i class="fa-solid fa-users"></i>
                </div>
                <div>
                    <h5 class="mb-0 fw-bold">12</h5>
                    <small class="text-muted">Patients Today</small>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card border-0 shadow-sm stat-card">
            <div class="card-body d-flex align-items-center">
                <div class="icon-box bg-warning text-white me-3">
                    <i class="fa-solid fa-hourglass-half"></i>
                </div>
                <div>
                    <h5 class="mb-0 fw-bold">5</h5>
                    <small class="text-muted">Waiting</small>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card border-0 shadow-sm stat-card">
            <div class="card-body d-flex align-items-center">
                <div class="icon-box bg-success text-white me-3">
                    <i class="fa-solid fa-check"></i>
                </div>
                <div>
                    <h5 class="mb-0 fw-bold">7</h5>
                    <small class="text-muted">Completed</small>
                </div>
            </div>
        </div>
    </div>

</div>


<!-- ================= WAITING PATIENTS ================= -->
<div class="card shadow-sm border-0">

    <div class="card-header bg-white border-0 d-flex justify-content-between align-items-center">
        <h6 class="mb-0 fw-semibold">
            <i class="fa-solid fa-calendar-check text-primary me-2"></i>
            Danh sách bệnh nhân chờ khám
        </h6>
        <span class="badge bg-primary">5 người đang chờ</span>
    </div>

    <div class="card-body p-0">
        <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
                <tr>
                    <th class="text-center" style="width:70px;">#</th>
                    <th>Bệnh nhân</th>
                    <th>Số điện thoại</th>
                    <th>Giờ check-in</th>
                    <th>Trạng thái</th>
                    <th class="text-center" style="width:180px;">Hành động</th>
                </tr>
            </thead>
            <tbody>

                <!-- Demo Row -->
                <tr>
                    <td class="text-center fw-semibold">1</td>
                    <td>
                        <div class="fw-semibold">Nguyễn Văn A</div>
                        <small class="text-muted">Nam • 25 tuổi</small>
                    </td>
                    <td>0987 654 321</td>
                    <td>08:15 AM</td>
                    <td>
                        <span class="badge bg-warning text-dark">Đang chờ</span>
                    </td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-primary me-2">
                            <i class="fa-solid fa-stethoscope me-1"></i> Khám
                        </button>
                        <button class="btn btn-sm btn-outline-secondary">
                            <i class="fa-solid fa-eye me-1"></i> Xem
                        </button>
                    </td>
                </tr>

            </tbody>
        </table>
    </div>
</div>


