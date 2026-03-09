<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="d-flex justify-content-between align-items-center mb-4">

    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-users-medical text-primary me-2"></i>
            Manage Queue
        </h2>
        <p class="text-muted mb-0">Monitor and receive patients at your clinic</p>
    </div>

    <div class="bg-white px-4 py-2 rounded-3 shadow-sm border border-primary border-opacity-25">
        <i class="fa-regular fa-calendar-check text-primary me-2"></i>
        <strong class="text-dark">
            <fmt:formatDate value="${now}" pattern="EEEE, dd/MM/yyyy"/>
        </strong>
    </div>

</div>



<c:if test="${not empty sessionScope.success}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm">
        <i class="fa-solid fa-circle-check me-2"></i>${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% session.removeAttribute("success"); %>
</c:if>

<c:if test="${not empty sessionScope.error}">
    <div class="alert alert-danger alert-dismissible fade show shadow-sm">
        <i class="fa-solid fa-circle-exclamation me-2"></i>${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% session.removeAttribute("error");%>
</c:if>



<!-- FILTER -->
<div class="card shadow-sm mb-4">
    <div class="card-body">

        <form action="${basePath}/queue/list" method="get">

            <div class="row g-3">
                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Patient Name</label>
                    <input type="text"
                           class="form-control"
                           name="patientName"
                           value="${param.patientName}"
                           placeholder="Search by name...">
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Examination date</label>
                    <input type="date"
                           class="form-control"
                           name="date"
                           value="${param.date != null ? param.date : paramDate}">
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Status</label>
                    <select class="form-select" name="status">
                        <option value="">-- All status --</option>
                        <option value="WAITING" <c:if test="${param.status == 'WAITING'}">selected</c:if>>WAITING</option>
                        <option value="IN_PROGRESS" <c:if test="${param.status == 'IN_PROGRESS'}">selected</c:if>>IN PROGRESS</option>
                        <option value="COMPLETED" <c:if test="${param.status == 'COMPLETED'}">selected</c:if>>COMPLETED</option>
                    </select>
                </div>

                <div class="col-md-3 d-flex align-items-end gap-2">
                    <button class="btn btn-primary flex-grow-1" type="submit">
                        <i class="fas fa-search me-2"></i>Search
                    </button>

                    <c:if test="${not empty param.status or not empty param.date or not empty param.patientName}">
                        <a href="${basePath}/queue/list"
                           class="btn btn-outline-secondary px-3"
                           title="Clear Filters">
                            <i class="fas fa-redo-alt"></i>
                        </a>
                    </c:if>
                </div>

            </div>

        </form>

    </div>
</div>



<!-- QUEUE TABLE -->
<div class="card shadow-sm">

    <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">

        <h5 class="mb-0">
            <i class="fas fa-list-ol me-2 text-primary"></i>Patient Queue
        </h5>

        <span class="badge bg-light text-dark border">
            Total: ${queueList.size()} records
        </span>

    </div>


    <div class="card-body p-0">

        <div class="table-responsive">

            <table class="table table-hover align-middle mb-0">

                <thead class="table-light">

                    <tr>

                        <th class="px-4 text-center">#</th>

                        <th>
                            <i class="fas fa-clock me-2 text-primary"></i>Time
                        </th>

                        <th>
                            <i class="fas fa-user me-2 text-primary"></i>Patient
                        </th>

                        <th class="text-center">
                            <i class="fas fa-calendar me-2 text-primary"></i>Birth Year
                        </th>

                        <th class="text-center">
                            <i class="fas fa-toggle-on me-2 text-primary"></i>Status
                        </th>

                        <th class="text-center">
                            <i class="fas fa-cog me-2 text-primary"></i>Actions
                        </th>

                    </tr>

                </thead>


                <tbody>

                <c:forEach items="${queueList}" var="q" varStatus="loop">

                    <tr class="${q.status == 'IN_PROGRESS' ? 'table-primary bg-opacity-10' : ''}">

                        <td class="px-4 text-center fw-bold text-secondary">
                            ${loop.index + 1}
                        </td>


                        <td>
                            <div class="fw-bold text-dark">
                                <fmt:formatDate value="${q.appointmentTime}" pattern="HH:mm"/>
                            </div>
                        </td>


                        <td>

                            <div class="fw-bold text-primary">
                                ${q.patientName}
                            </div>

                            <div class="small text-muted">
                                <i class="fa-solid fa-venus-mars me-1"></i>${q.patientGender}
                                <i class="fa-solid fa-phone ms-2 me-1"></i>${q.patientPhone}
                            </div>

                        </td>


                        <td class="text-center fw-bold text-secondary">
                    <fmt:formatDate value="${q.patientDob}" pattern="yyyy"/>
                    </td>


                    <td class="text-center">

                    <c:choose>

                        <c:when test="${q.status == 'IN_PROGRESS'}">
                            <span class="badge bg-primary heartbeat-animation">
                                <i class="fa-solid fa-stethoscope me-1"></i>IN PROGRESS
                            </span>
                        </c:when>

                        <c:when test="${q.status == 'WAITING'}">
                            <span class="badge bg-warning text-dark">
                                <i class="fa-solid fa-hourglass-half me-1"></i>WAITING
                            </span>
                        </c:when>

                        <c:when test="${q.status == 'COMPLETED'}">
                            <span class="badge bg-success">
                                <i class="fa-solid fa-check-double me-1"></i>COMPLETED
                            </span>
                        </c:when>

                    </c:choose>

                    </td>


                    <td class="text-center">

                    <c:choose>

                        <c:when test="${q.status == 'WAITING'}">

                            <button onclick="callPatient(${q.appointmentId}, '${q.patientName}')"
                                    class="btn btn-sm btn-outline-success">

                                <i class="fa-solid fa-bullhorn me-2"></i>Start

                            </button>

                        </c:when>


                        <c:when test="${q.status == 'IN_PROGRESS'}">

                            <a href="${basePath}/medical-record/edit?appointmentId=${q.appointmentId}"
                               class="btn btn-sm btn-outline-primary">

                                <i class="fa-solid fa-file-medical me-2"></i>Continue

                            </a>

                        </c:when>


                        <c:when test="${q.status == 'COMPLETED'}">

                            <a href="${basePath}/medical-record/detail?appointmentId=${q.appointmentId}"
                               class="btn btn-sm btn-outline-primary">

                                <i class="fa-solid fa-eye me-2"></i>View

                            </a>

                        </c:when>

                    </c:choose>

                    </td>

                    </tr>

                </c:forEach>


                <c:if test="${empty queueList}">

                    <tr>
                        <td colspan="6" class="text-center py-5">

                            <i class="fa-solid fa-mug-hot fa-3x text-muted mb-3 d-block"></i>

                            <p class="text-muted mb-0">
                                No patients waiting at the moment.
                            </p>

                        </td>
                    </tr>

                </c:if>

                </tbody>

            </table>

        </div>

    </div>

</div>



<form id="callPatientForm" action="${basePath}/queue/call" method="POST" style="display: none;">
    <input type="hidden" name="appointmentId" id="callAppId">
</form>



<style>
    @keyframes heartbeat {
        0% {
            transform: scale(1);
        }
        50% {
            transform: scale(1.05);
        }
        100% {
            transform: scale(1);
        }
    }

    .heartbeat-animation {
        animation: heartbeat 2s infinite;
    }
</style>



<script>

    function callPatient(appointmentId, patientName) {
        if (confirm('Bạn muốn gọi bệnh nhân [' + patientName.toUpperCase() + '] vào khám?')) {
            document.getElementById('callAppId').value = appointmentId;
            document.getElementById('callPatientForm').submit();
        }
    }

    window.addEventListener('DOMContentLoaded', (event) => {

        const urlParams = new URLSearchParams(window.location.search);
        const printMrId = urlParams.get('printMrId');

        if (printMrId) {

            const printUrl = '${basePath}/prescription/print?medicalRecordId=' + printMrId;

            let printWindow = window.open(printUrl, '_blank');

            if (!printWindow || printWindow.closed || typeof printWindow.closed == 'undefined') {

                alert('⚠️ Trình duyệt đang CHẶN cửa sổ In Đơn Thuốc!\n\nVui lòng nhìn lên góc phải thanh địa chỉ (có biểu tượng dấu X đỏ), bấm vào đó và chọn "Luôn cho phép cửa sổ bật lên từ trang web này".');

            }

            window.history.replaceState({}, document.title, window.location.pathname);

        }

    });

</script>