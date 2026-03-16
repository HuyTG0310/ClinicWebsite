
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


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
            <a class="nav-link ${activePage == 'manageRole' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/role/list">
                <i class="fa-solid fa-user-shield me-2"></i>
                Manage role
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link ${activePage == 'certificationApproval' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/certification/list">
                <i class="fa-solid fa-certificate me-2"></i>
                Manage certification
            </a>
        </li>


        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageStaff' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/user/list">
                <i class="fa-solid fa-users me-2"></i>
                Manage staff
            </a>
        </li>



        <!-- Test Requests -->
        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageSpecialty' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/specialty/list">
                <i class="fa-solid fa-stethoscope me-2"></i>
                Manage specialty
            </a>
        </li>

        <!-- Test History -->
        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageService' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/service/list">
                <i class="fa-solid fa-briefcase-medical me-2"></i>
                Manage service
            </a>
        </li>


        <c:if test="${hasRoomView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'manageRoom' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/admin/room/list">
                    <i class="fa-solid fa-door-open me-2"></i>
                    Manage room
                </a>
            </li>
        </c:if>

        <c:if test="${hasPatientView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'managePatient' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/admin/patient/list">
                    <i class="fa-solid fa-user-injured me-2"></i>
                    Manage patient
                </a>
            </li>
        </c:if>



        <c:if test="${hasMedicineView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'manageMedicine' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/admin/medicine/list">
                    <i class="fa-solid fa-pills me-2"></i>
                    Manage medicine
                </a>
            </li>
        </c:if>


        <c:if test="${hasAppointmentView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'manageAppointment' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/admin/appointment/list">
                    <i class="fa-solid fa-gauge me-2"></i>
                    Manage appointment
                </a>
            </li>
        </c:if>

        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageServiceOrder' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/service-order/list">
                <i class="fa-solid fa-vials me-2"></i>
                Manage service order
            </a>
        </li>

        
        <li class="nav-item">
            <a class="nav-link ${activePage == 'myQueue' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/queue/list">
                <i class="fa-solid fa-calendar-check me-2"></i>
                Manage queue
            </a>
        </li>


        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageMedicalRecord' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/medical-record/list">
                <i class="fa-solid fa-file-medical me-2"></i>
                Manage medical record
            </a>
        </li>
        
        <li class="nav-item">
            <a class="nav-link ${activePage == 'managePrescription' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/prescription/list">
                <i class="fa-solid fa-prescription me-2"></i>
                Manage prescription
            </a>
        </li>
        
        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageTest' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/admin/lab-queue/list">
                <i class="fa-solid fa-vials me-2"></i>
                Manage test
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
