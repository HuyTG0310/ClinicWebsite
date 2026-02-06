
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="col-md-3 col-lg-2 sidebar min-vh-100 p-3">

    <h5 class="text-center mb-4 sidebar-title">
        <i class="fa-solid fa-vials me-1"></i>
        Admin
    </h5>

    <ul class="nav nav-pills flex-column gap-2 sidebar-menu">

        <!-- Dashboard -->
        <li class="nav-item">
            <a class="nav-link ${activePage == 'adminDashboard' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/dashboard">
                <i class="fa-solid fa-gauge me-2"></i>
                Dashboard
            </a>
        </li>


        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageStaff' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/history">
                <i class="fa-solid fa-clock-rotate-left me-2"></i>
                Manage staff
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link ${activePage == 'labDashboard' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/dashboard">
                <i class="fa-solid fa-gauge me-2"></i>
                Manage role
            </a>
        </li>

        <!-- Test Requests -->
        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageSpecialty' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/specialty/list">
                <i class="fa-solid fa-vials me-2"></i>
                Manage specialty
            </a>
        </li>

        <!-- Test History -->
        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageService' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/service/list">
                <i class="fa-solid fa-clock-rotate-left me-2"></i>
                Manage service
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageRoom' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/RoomList">
                <i class="fa-solid fa-clock-rotate-left me-2"></i>
                Manage room
            </a>
        </li>
        
        <li class="nav-item">
            <a class="nav-link ${activePage == 'managePatien' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/PatientList">
                <i class="fa-solid fa-clock-rotate-left me-2"></i>
                Manage Patient
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageMedicine' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/medicine">
                <i class="fa-solid fa-clock-rotate-left me-2"></i>
                Manage medicine
            </a>
        </li>



        <!-- Logout -->
        <li class="nav-item mt-3">
            <a class="nav-link logout"
               href="${pageContext.request.contextPath}/logout">
                <i class="fa-solid fa-right-from-bracket me-2"></i>
                Đăng xuất
            </a>
        </li>

    </ul>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
