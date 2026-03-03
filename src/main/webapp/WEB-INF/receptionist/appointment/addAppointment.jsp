<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/css/select2.min.css" rel="stylesheet" />

<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/js/select2.min.js"></script>

<style>
    .select2-container .select2-selection--single {
        height: 38px !important; /* Chiều cao bằng input Bootstrap */
        border: 1px solid #ced4da;
        border-radius: 0.375rem;
        display: flex;
        align-items: center;
    }
    .select2-container--default .select2-selection--single .select2-selection__arrow {
        height: 36px !important;
    }
</style>


<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-primary text-white py-3">
                <h5 class="mb-0">
                    <i class="fa-solid fa-notes-medical me-2"></i>Appointment Create
                </h5>
            </div>
            <div class="card-body p-4">
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
                        <i class="fa-solid fa-circle-check me-2"></i> ${successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
                        <i class="fa-solid fa-circle-exclamation me-2"></i> ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <form action="create" method="POST">
                    <div class="mb-4">
                        <label class="form-label fw-bold">Chọn bệnh nhân <span class="text-danger">*</span></label>
                        <div class="d-flex gap-2">
                            <select class="form-select" name="patientId" id="patientSelect" required style="width: 100%;">
                                <option value="">-- Tìm kiếm theo Tên hoặc SĐT --</option>
                                <c:forEach var="p" items="${patients}">
                                    <option value="${p.patientId}">
                                        ${p.fullName} - ${p.phone} (${p.gender})
                                    </option>
                                </c:forEach>
                            </select>

                            <a href="${basePath}/patient/create" class="btn btn-outline-primary text-nowrap">
                                <i class="fa-solid fa-plus"></i> Thêm mới
                            </a>
                        </div>
                        <div class="form-text">Gõ tên hoặc số điện thoại để tìm kiếm nhanh.</div>
                    </div>


                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold">Chuyên khoa khám</label>
                            <select class="form-select" id="specialtySelect" onchange="filterRooms()">
                                <option value="all">-- Tất cả chuyên khoa --</option>
                                <c:forEach var="s" items="${specialties}">
                                    <option value="${s.specialtyId}">${s.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold">Chọn phòng khám <span class="text-danger">*</span></label>
                            <select class="form-select" name="roomId" id="roomSelect" required>
                                <option value="">-- Vui lòng chọn phòng --</option>
                                <c:forEach var="r" items="${rooms}">
                                    <option value="${r.roomId}" data-specialty="${r.specialtyId}">
                                        ${r.roomName} - BS. ${r.doctorName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="card bg-light border-0 mb-4">
                        <div class="card-body">
                            <div class="row align-items-center">

                                <div class="col-md-6 border-end">
                                    <div class="d-flex align-items-center">
                                        <div class="bg-white p-3 rounded-circle shadow-sm text-primary me-3">
                                            <i class="fa-solid fa-money-bill-wave fs-4"></i>
                                        </div>
                                        <div>
                                            <div class="text-muted small text-uppercase fw-bold">Tổng chi phí (Total Amount)</div>
                                            <div class="fs-4 fw-bold text-danger">200.000 VNĐ</div>
                                            <div class="small text-muted"><i class="fa-solid fa-circle-info me-1"></i>Phí khám lâm sàng cố định</div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6 ps-md-4 mt-3 mt-md-0">
                                    <label class="form-label fw-bold text-dark">
                                        Hình thức thanh toán <span class="text-danger">*</span>
                                    </label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-white text-muted">
                                            <i class="fa-solid fa-cash-register"></i>
                                        </span>
                                        <select class="form-select border-start-0 ps-0 fw-medium" name="paymentMethod" required>
                                            <option value="CASH" selected> Tiền mặt (Cash)</option>
                                            <option value="BANKING"> Chuyển khoản (Banking)</option>
                                        </select>
                                    </div>
                                    <div class="form-text fst-italic mt-1">
                                        * Hệ thống sẽ tạo hóa đơn "Đã thanh toán" (PAID) ngay lập tức.
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary btn-lg py-3 shadow-sm">
                            <i class="fa-solid fa-print me-2"></i> Xác nhận
                        </button>
                        <a href="list" class="btn btn-outline-secondary">Hủy bỏ</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $('#patientSelect').select2({
            placeholder: "-- Tìm kiếm theo Tên hoặc SĐT --",
            allowClear: true,
            language: {
                noResults: function () {
                    return "Không tìm thấy bệnh nhân nào"; 
                }
            }
        });
    });

    function filterRooms() {
        var selectedSpecialty = document.getElementById('specialtySelect').value;
        var roomSelect = document.getElementById('roomSelect');
        var options = roomSelect.options;

        roomSelect.value = "";

        for (var i = 1; i < options.length; i++) {
            var option = options[i];
            var roomSpecialty = option.getAttribute('data-specialty');

            if (selectedSpecialty === 'all' || roomSpecialty === selectedSpecialty) {
                option.hidden = false;
                option.disabled = false;
            } else {
                option.hidden = true;
                option.disabled = true;
            }
        }
    }
</script>