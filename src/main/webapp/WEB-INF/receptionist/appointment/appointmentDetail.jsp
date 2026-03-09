<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container-fluid">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-file-medical text-primary me-2"></i>
                Appointment Detail
            </h2>
            <p class="text-muted mb-0">Appointment ID: <strong>#${app.appointmentId}</strong></p>
        </div>

        <div class="d-flex gap-2">

            <c:if test="${hasAppointmentEdit}">
                <c:if test="${app.status == 'WAITING'}">

                    <button type="button"
                            class="btn btn-danger"
                            onclick="submitCancelForm()">
                        <i class="fa-solid fa-ban me-2"></i>Cancel
                    </button>

                    <button type="button"
                            class="btn btn-warning"
                            data-bs-toggle="modal"
                            data-bs-target="#editRoomModal">
                        <i class="fa-solid fa-pen-to-square me-2"></i>Edit
                    </button>

                </c:if>
            </c:if>

            <a href="${basePath}/receipt/print?appId=${app.appointmentId}"
               target="_blank"
               class="btn btn-primary">
                <i class="fas fa-print me-2"></i>Print
            </a>

            <a href="${basePath}/appointment/list"
               class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-2"></i>Back to list
            </a>

        </div>

    </div>


    <!-- ALERT MESSAGE -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm custom-alert-success" role="alert">
            <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% session.removeAttribute("success"); %>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm custom-alert-danger" role="alert">
            <i class="fas fa-exclamation-triangle me-2"></i>${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% session.removeAttribute("error");%>
    </c:if>


    <form id="cancelForm"
          action="${basePath}/appointment/edit"
          method="POST"
          style="display:none;">
        <input type="hidden" name="appointmentId" value="${app.appointmentId}">
        <input type="hidden" name="action" value="cancel">
    </form>




    <!-- PATIENT INFORMATION -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-user text-primary me-2"></i>
                Patient Information
            </h5>
        </div>

        <div class="card-body">

            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">Full Name</label>
                    <div class="fw-semibold text-uppercase text-primary">
                        ${app.patientName}
                    </div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Gender</label>
                    <div class="fw-semibold">
                        ${app.patientGender}
                    </div>
                </div>

            </div>


            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">Date of Birth</label>
                    <div class="fw-semibold">
                        <fmt:formatDate value="${app.patientDob}" pattern="dd/MM/yyyy"/>
                    </div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Phone</label>
                    <div class="fw-semibold">
                        ${app.patientPhone}
                    </div>
                </div>

            </div>


            <div>
                <label class="text-muted small">Address</label>
                <div class="fw-semibold">
                    ${not empty app.patientAddress ? app.patientAddress : 'Not updated'}
                </div>
            </div>

        </div>
    </div>



    <!-- SERVICE INFORMATION -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-stethoscope text-primary me-2"></i>
                Service Information
            </h5>
        </div>

        <div class="card-body">

            <div class="mb-3">
                <label class="text-muted small">Specialty</label>
                <div class="fw-semibold">
                    ${app.specialtyName}
                </div>
            </div>

            <div class="mb-3">
                <label class="text-muted small">Room</label>
                <div class="fw-semibold">
                    ${app.roomName}
                    <span class="text-muted fst-italic">
                        (Dr. ${app.doctorName})
                    </span>
                </div>
            </div>

            <div class="mb-3">
                <label class="text-muted small">Receptionist</label>
                <div class="fw-semibold">
                    ${app.receptionistName}
                </div>
            </div>


            <div class="mb-3">
                <label class="text-muted small">Status</label>
                <div>

                    <c:choose>

                        <c:when test="${app.status == 'WAITING'}">
                            <span class="badge bg-warning text-dark">Waiting</span>
                        </c:when>

                        <c:when test="${app.status == 'IN_PROGRESS'}">
                            <span class="badge bg-info">In Progress</span>
                        </c:when>

                        <c:when test="${app.status == 'COMPLETED'}">
                            <span class="badge bg-success">Completed</span>
                        </c:when>

                        <c:when test="${app.status == 'CANCELLED'}">
                            <span class="badge bg-danger">Cancelled</span>
                        </c:when>

                    </c:choose>

                </div>
            </div>


            <div class="mb-3">
                <label class="text-muted small">Payment Method</label>
                <div>

                    <c:if test="${app.paymentMethod == 'CASH'}">
                        <span class="badge bg-secondary">
                            <i class="fa-solid fa-money-bill me-1"></i>Cash
                        </span>
                    </c:if>

                    <c:if test="${app.paymentMethod == 'BANKING'}">
                        <span class="badge bg-primary">
                            <i class="fa-solid fa-qrcode me-1"></i>Bank
                        </span>
                    </c:if>

                </div>
            </div>


            <div>
                <label class="text-muted small">Total Amount</label>
                <div class="fw-bold text-danger fs-5">
                    <fmt:formatNumber value="${app.price}" pattern="#,###"/> đ
                </div>
            </div>

        </div>

    </div>


</div>




<!-- EDIT ROOM MODAL -->
<div class="modal fade"
     id="editRoomModal"
     tabindex="-1"
     aria-hidden="true">

    <div class="modal-dialog modal-lg modal-dialog-centered">

        <div class="modal-content">

            <div class="modal-header">

                <h5 class="modal-title">
                    <i class="fa-solid fa-right-left me-2"></i>
                    Change Room
                </h5>

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"></button>

            </div>


            <form action="${basePath}/appointment/edit" method="POST">

                <div class="modal-body">

                    <input type="hidden" name="appointmentId" value="${app.appointmentId}">
                    <input type="hidden" name="action" value="editRoom">


                    <div class="alert alert-secondary">
                        <i class="fa-solid fa-circle-info me-2"></i>
                        Changing room does not affect the bills already collected
                    </div>


                    <div class="row g-3">

                        <!-- Specialty filter -->
                        <div class="col-md-6">

                            <label class="form-label fw-semibold">
                                Specialty
                            </label>

                            <select class="form-select"
                                    id="specialtySelect"
                                    onchange="filterRooms()">

                                <option value="all">
                                    -- Choose for filter --
                                </option>

                                <c:forEach var="s" items="${specialties}">
                                    <option value="${s.specialtyId}">
                                        ${s.name}
                                    </option>
                                </c:forEach>

                            </select>

                        </div>


                        <!-- Room -->
                        <div class="col-md-6">

                            <label class="form-label fw-semibold">
                                New Room <span class="text-danger">*</span>
                            </label>

                            <select class="form-select"
                                    name="roomId"
                                    id="roomSelect"
                                    required>

                                <option value="">
                                    -- Please choose a room --
                                </option>

                                <c:forEach var="r" items="${rooms}">

                                    <option value="${r.roomId}"
                                            data-specialty="${r.specialtyId}"

                                            <c:if test="${r.roomId == app.roomId}">
                                                selected
                                            </c:if>>

                                        ${r.roomName} - BS. ${r.doctorName}

                                    </option>

                                </c:forEach>

                            </select>

                        </div>

                    </div>

                </div>


                <div class="modal-footer">

                    <button type="button"
                            class="btn btn-outline-secondary"
                            data-bs-dismiss="modal">
                        Cancel
                    </button>

                    <button type="submit"
                            class="btn btn-primary px-4">
                        <i class="fa-solid fa-save me-1"></i>
                        Save
                    </button>

                </div>

            </form>

        </div>

    </div>

</div>

<style>
    .card {
        border-radius: 12px;
        border: none;
    }

    .card-header {
        border-bottom: 1px solid rgba(0,0,0,0.08);
        border-radius: 12px 12px 0 0 !important;
    }

    .badge {
        padding: 0.45em 0.9em;
        font-weight: 500;
        border-radius: 6px;
    }

    .btn {
        border-radius: 8px;
        font-weight: 500;
        transition: all 0.25s ease;
    }

    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
</style>

<script>
    function filterRooms() {
        var selectedSpecialty = document.getElementById('specialtySelect').value;
        var roomOptions = document.getElementById('roomSelect').options;
        document.getElementById('roomSelect').value = "";
        for (var i = 1; i < roomOptions.length; i++) {
            var option = roomOptions[i];
            var roomSpecialty = option.getAttribute('data-specialty');
            if (selectedSpecialty === 'all' || roomSpecialty === selectedSpecialty) {
                option.style.display = 'block';
            } else {
                option.style.display = 'none';
            }
        }
    }

    function submitCancelForm() {
        if (confirm('Bạn có chắc chắn muốn hủy phiếu khám #${app.appointmentId} không? Hành động này không thể hoàn tác!')) {
            document.getElementById('cancelForm').submit();
        }
    }
</script>