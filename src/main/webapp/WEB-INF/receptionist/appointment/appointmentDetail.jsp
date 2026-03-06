<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container mt-4">

    <div class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
        <div class="d-flex align-items-center">
            <a href="${basePath}/appointment/list" class="btn btn-outline-secondary me-3">
                <i class="fas fa-arrow-left me-2"></i>Quay lại
            </a>
            <h3 class="fw-bold text-primary mb-0">
                <i class="fa-solid fa-file-medical me-2"></i>Chi Tiết Phiếu Khám #${app.appointmentId}
            </h3>
        </div>

        <div>
            <c:if test="${hasAppointmentEdit}">
                <c:if test="${app.status == 'WAITING'}">
                    <button type="button" class="btn btn-danger me-2 shadow-sm fw-bold" onclick="submitCancelForm()">
                        <i class="fa-solid fa-ban me-2"></i>Hủy phiếu
                    </button>
                    <button type="button" class="btn btn-warning me-2 shadow-sm fw-bold" data-bs-toggle="modal" data-bs-target="#editRoomModal">
                        <i class="fa-solid fa-pen-to-square me-2"></i>Đổi phòng
                    </button>
                </c:if>
            </c:if>

            <a href="${basePath}/receipt/print?appId=${app.appointmentId}" target="_blank" class="btn btn-primary shadow-sm fw-bold">
                <i class="fas fa-print me-2"></i>In Biên Lai Khám
            </a>
        </div>
    </div>

    <form id="cancelForm" action="${basePath}/appointment/edit" method="POST" style="display: none;">
        <input type="hidden" name="appointmentId" value="${app.appointmentId}">
        <input type="hidden" name="action" value="cancel">
    </form>

    <div class="row justify-content-center">
        <div class="col-lg-8">

            <div class="card shadow-sm border-0 mb-4 rounded-4">
                <div class="card-header bg-primary text-white fw-bold py-3 rounded-top-4">
                    <i class="fa-solid fa-user me-2"></i>Thông Tin Bệnh Nhân
                </div>
                <div class="card-body p-4" style="line-height: 1.8; font-size: 1.05rem;">
                    <div class="row border-bottom pb-2 mb-2">
                        <div class="col-4 text-muted">Họ và tên:</div>
                        <div class="col-8 fw-bold text-uppercase text-primary">${app.patientName}</div>
                    </div>
                    <div class="row border-bottom pb-2 mb-2">
                        <div class="col-4 text-muted">Ngày sinh:</div>
                        <div class="col-8 fw-bold"><fmt:formatDate value="${app.patientDob}" pattern="dd/MM/yyyy"/></div>
                    </div>
                    <div class="row border-bottom pb-2 mb-2">
                        <div class="col-4 text-muted">Giới tính:</div>
                        <div class="col-8 fw-bold">${app.patientGender}</div>
                    </div>
                    <div class="row border-bottom pb-2 mb-2">
                        <div class="col-4 text-muted">Điện thoại:</div>
                        <div class="col-8 fw-bold">${app.patientPhone}</div>
                    </div>
                    <div class="row">
                        <div class="col-4 text-muted">Địa chỉ:</div>
                        <div class="col-8 fw-bold">${not empty app.patientAddress ? app.patientAddress : 'Chưa cập nhật'}</div>
                    </div>
                </div>
            </div>

            <div class="card shadow-sm border-0 mb-4 rounded-4">
                <div class="card-header bg-success text-white fw-bold py-3 rounded-top-4">
                    <i class="fa-solid fa-stethoscope me-2"></i>Thông Tin Dịch Vụ & Thanh Toán
                </div>
                <div class="card-body p-4" style="line-height: 1.8; font-size: 1.05rem;">
                    <div class="row border-bottom pb-2 mb-2">
                        <div class="col-4 text-muted">Chuyên khoa:</div>
                        <div class="col-8 fw-bold text-uppercase">Khoa ${app.specialtyName}</div>
                    </div>
                    <div class="row border-bottom pb-2 mb-2">
                        <div class="col-4 text-muted">Phòng khám:</div>
                        <div class="col-8 fw-bold">${app.roomName} <span class="text-muted fst-italic">(BS. ${app.doctorName})</span></div>
                    </div>

                    <div class="row border-bottom pb-2 mb-2">
                        <div class="col-4 text-muted">Lễ tân tiếp nhận:</div>
                        <div class="col-8 fw-bold text-dark">${app.receptionistName}</div>
                    </div>

                    <div class="row border-bottom pb-2 mb-2">
                        <div class="col-4 text-muted">Trạng thái phiếu:</div>
                        <div class="col-8">
                            <c:choose>
                                <c:when test="${app.status == 'WAITING'}"><span class="badge bg-warning text-dark px-3 py-2">Chờ khám</span></c:when>
                                <c:when test="${app.status == 'IN_PROGRESS'}"><span class="badge bg-info px-3 py-2">Đang khám</span></c:when>
                                <c:when test="${app.status == 'COMPLETED'}"><span class="badge bg-success px-3 py-2">Hoàn thành</span></c:when>
                                <c:when test="${app.status == 'CANCELLED'}"><span class="badge bg-danger px-3 py-2">Đã hủy</span></c:when>
                            </c:choose>
                        </div>
                    </div>

                    <div class="bg-light p-3 rounded-3 mt-4 border">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="text-muted">Phương thức thu:</span>
                            <span>
                                <c:if test="${app.paymentMethod == 'CASH'}"><span class="badge bg-secondary px-3 py-2"><i class="fa-solid fa-money-bill me-2"></i>Tiền mặt</span></c:if>
                                <c:if test="${app.paymentMethod == 'BANKING'}"><span class="badge bg-primary px-3 py-2"><i class="fa-solid fa-qrcode me-2"></i>Chuyển khoản</span></c:if>
                                </span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="text-muted">Tổng tiền dịch vụ:</span>
                                <span class="text-danger fw-bold fs-4"><fmt:formatNumber value="${app.price}" pattern="#,###"/> đ</span>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="editRoomModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-header bg-warning text-dark">
                <h5 class="modal-title fw-bold">
                    <i class="fa-solid fa-right-left me-2"></i>Chuyển phòng khám (Đổi dịch vụ)
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${basePath}/appointment/edit" method="POST">
                <div class="modal-body p-4">
                    <input type="hidden" name="appointmentId" value="${app.appointmentId}">
                    <input type="hidden" name="action" value="editRoom">
                    <div class="alert alert-secondary mb-4 small">
                        <i class="fa-solid fa-circle-info me-2"></i>Việc thay đổi phòng khám không làm ảnh hưởng đến hóa đơn đã thu.
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Chuyên khoa</label>
                        <select class="form-select" id="specialtySelect" onchange="filterRooms()">
                            <option value="all">-- Chọn để lọc --</option>
                            <c:forEach var="s" items="${specialties}">
                                <option value="${s.specialtyId}">${s.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold text-primary">Phòng khám mới <span class="text-danger">*</span></label>
                        <select class="form-select border-primary" name="roomId" id="roomSelect" required>
                            <option value="">-- Vui lòng chọn phòng --</option>
                            <c:forEach var="r" items="${rooms}">
                                <option value="${r.roomId}" data-specialty="${r.specialtyId}" 
                                        <c:if test="${r.roomId == app.roomId}">selected</c:if>>
                                    ${r.roomName} - BS. ${r.doctorName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="modal-footer bg-light">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-warning fw-bold">
                        <i class="fa-solid fa-save me-2"></i>Lưu thay đổi
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

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