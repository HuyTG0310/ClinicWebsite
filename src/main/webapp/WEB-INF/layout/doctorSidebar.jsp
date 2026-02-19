<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="col-md-3 col-lg-2 sidebar min-vh-100 p-3">

    <h5 class="text-center mb-4 sidebar-title">
        <i class="fa-solid fa-vials me-1"></i>
        Doctor
    </h5>

    <ul class="nav nav-pills flex-column gap-2 sidebar-menu">

        <!-- Dashboard -->
        <li class="nav-item">
            <a class="nav-link ${activePage == 'doctorDashboard' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/doctor/dashboard">
                <i class="fa-solid fa-gauge me-2"></i>
                Dashboard
            </a>
        </li>


        <li class="nav-item">
            <a class="nav-link ${activePage == 'labHistory' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/history">
                <i class="fa-solid fa-calendar-check me-2"></i>
                My appointment
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link ${activePage == 'labDashboard' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/dashboard">
                <i class="fa-solid fa-file-medical me-2"></i>
                Manage medical record
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link ${activePage == 'labDashboard' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/dashboard">
                <i class="fa-solid fa-prescription me-2"></i>
                Manage prescription
            </a>
        </li>


        <!-- Test Requests -->
        <c:if test="${hasMedicineView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'manageMedicine' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/doctor/medicine/list">
                    <i class="fa-solid fa-pills me-2"></i>
                    Manage medicine
                </a>
            </li>
        </c:if>


        <c:if test="${hasRoomView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'manageRoom' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/doctor/room/list">
                    <i class="fa-solid fa-door-open me-2"></i>
                    Manage room
                </a>
            </li>
        </c:if>


        <c:if test="${hasPatientView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'managePatient' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/doctor/patient/list">
                    <i class="fa-solid fa-user-injured me-2"></i>
                    Manage patient
                </a>
            </li>
        </c:if>


        <li class="nav-item">
            <a class="nav-link ${activePage == 'profile' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/profile/view">
                <i class="fa-solid fa-id-card me-2"></i>
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
