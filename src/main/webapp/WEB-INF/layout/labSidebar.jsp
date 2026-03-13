<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="col-md-3 col-lg-2 sidebar min-vh-100 p-3">

    <h5 class="text-center mb-4 sidebar-title">
        <i class="fa-solid fa-vials me-1"></i>
        Lab Technician
    </h5>

    <ul class="nav nav-pills flex-column gap-2 sidebar-menu">

        <!-- Dashboard -->
        <li class="nav-item">
            <a class="nav-link ${activePage == 'labDashboard' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/dashboard">
                <i class="fa-solid fa-gauge me-2"></i>
                Dashboard
            </a>
        </li>


        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageTest' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/queue/list">
                <i class="fa-solid fa-vials me-2"></i>
                Manage test
            </a>
        </li>


        <!-- Test History -->
        <li class="nav-item">
            <a class="nav-link ${activePage == 'labHistory' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/history">
                <i class="fa-solid fa-clock-rotate-left me-2"></i>
                Profile
            </a>
        </li>


        <!-- Logout -->
        <li class="nav-item mt-3">
            <a class="nav-link logout"
               href="${pageContext.request.contextPath}/logout">
                <i class="fa-solid fa-right-from-bracket me-2"></i>
                Logout
            </a>
        </li>

    </ul>
</div>
