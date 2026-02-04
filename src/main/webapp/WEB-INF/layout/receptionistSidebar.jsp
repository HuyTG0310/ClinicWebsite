<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="col-md-3 col-lg-2 sidebar min-vh-100 p-3">

    <h5 class="text-center mb-4 sidebar-title">
        <i class="fa-solid fa-vials me-1"></i>
        Receptionist
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
            <a class="nav-link ${activePage == 'labHistory' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/history">
                <i class="fa-solid fa-clock-rotate-left me-2"></i>
                Manage patient
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link ${activePage == 'labDashboard' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/dashboard">
                <i class="fa-solid fa-gauge me-2"></i>
                Manage appointment
            </a>
        </li>

        <!-- Test Requests -->
        <li class="nav-item">
            <a class="nav-link ${activePage == 'labTests' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/tests">
                <i class="fa-solid fa-vials me-2"></i>
                Manage invoice
            </a>
        </li>
        
        <li class="nav-item">
            <a class="nav-link ${activePage == 'labTests' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/tests">
                <i class="fa-solid fa-vials me-2"></i>
                Manage follow-up
            </a>
        </li>
        
        
        <li class="nav-item">
            <a class="nav-link ${activePage == 'labTests' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/tests">
                <i class="fa-solid fa-vials me-2"></i>
                Revenue report
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
                Đăng xuất
            </a>
        </li>

    </ul>
</div>
