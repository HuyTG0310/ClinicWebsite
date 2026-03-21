<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<div class="col-md-3 col-lg-2 sidebar min-vh-100 p-3">
    <h5 class="text-center mb-4 sidebar-title">
        <i class="fa-solid fa-vials me-1"></i> Lab Technician
    </h5>

    <ul class="nav nav-pills flex-column gap-2 sidebar-menu">

        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageTest' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/lab-queue/list">
                <i class="fa-solid fa-vials me-2"></i>
                Manage test result
            </a>
        </li>

        <c:if test="${hasCertView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'myCertification' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/lab/certification/my">
                    <i class="fa-solid fa-certificate me-2"></i> My Certifications
                </a>
            </li>
        </c:if>

        <c:if test="${hasAppointmentView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'manageAppointment' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/lab/appointment/list">
                    <i class="fa-solid fa-gauge me-2"></i>
                    Manage appointment
                </a>
            </li>
        </c:if>

            
        <li class="nav-item">
            <a class="nav-link ${activePage == 'manageServiceOrder' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/service-order/list">
                <i class="fa-solid fa-vials me-2"></i>
                Manage service order
            </a>
        </li>


        <c:if test="${hasServiceView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'manageService' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/lab/service/list">
                    <i class="fa-solid fa-briefcase-medical me-2"></i>
                    Manage service
                </a>
            </li>
        </c:if>

        <c:if test="${hasPatientView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'managePatient' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/lab/patient/list">
                    <i class="fa-solid fa-user-injured me-2"></i>
                    Manage patient
                </a>
            </li>
        </c:if>

        <c:if test="${hasMedicineView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'manageMedicine' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/lab/medicine/list">
                    <i class="fa-solid fa-pills me-2"></i>
                    Manage medicine
                </a>
            </li>
        </c:if>


        <c:if test="${hasRoomView}">
            <li class="nav-item">
                <a class="nav-link ${activePage == 'manageRoom' ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/lab/room/list">
                    <i class="fa-solid fa-door-open me-2"></i>
                    Manage room
                </a>
            </li>
        </c:if>

        <li class="nav-item">
            <a class="nav-link ${activePage == 'profile' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/lab/profile/view">
                <i class="fa-solid fa-clock-rotate-left me-2"></i>
                Profile
            </a>
        </li>

        <li class="nav-item mt-3">
            <a class="nav-link logout" href="${pageContext.request.contextPath}/logout">
                <i class="fa-solid fa-right-from-bracket me-2"></i> Logout
            </a>
        </li>
    </ul>
</div>