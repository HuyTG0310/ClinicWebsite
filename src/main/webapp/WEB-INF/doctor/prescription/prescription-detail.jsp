<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 

<div class="container-fluid mb-5">
    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fas fa-file-medical text-primary me-2"></i>
                Prescription Detail
            </h2>
            <p class="text-muted mb-0">
                Medical Record #${record.medicalRecordId}
            </p>
        </div>

        <div class="d-flex gap-2">
            <a href="${basePath}/prescription/list"
               class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-2"></i>Back to list
            </a>

            <a href="${basePath}/prescription/print?medicalRecordId=${record.medicalRecordId}"
               target="_blank"
               class="btn btn-primary">
                <i class="fas fa-print me-2"></i>Print
            </a>

            <c:if test="${canEdit}">
                <button type="button"
                        class="btn btn-warning"
                        data-bs-toggle="modal"
                        data-bs-target="#editPrescriptionModal">
                    <i class="fas fa-edit me-2"></i>Edit
                </button>
            </c:if>

        </div>

    </div>

    <c:if test="${sessionScope.success != null}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm">
            <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% session.removeAttribute("success"); %>
    </c:if>

    <c:if test="${sessionScope.error != null}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm">
            <i class="fas fa-exclamation-triangle me-2"></i>${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% session.removeAttribute("error");%>
    </c:if>


    <!-- PATIENT INFORMATION -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fas fa-user-injured text-primary me-2"></i>
                Patient Information
            </h5>
        </div>

        <div class="card-body">

            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">Full Name</label>
                    <div class="fw-semibold text-uppercase text-primary">
                        ${record.patientName}
                    </div>
                </div>

                <div class="col-md-3">
                    <label class="text-muted small">Age</label>
                    <div class="fw-semibold">
                        ${record.age} years
                    </div>
                </div>

                <div class="col-md-3">
                    <label class="text-muted small">Gender</label>
                    <div class="fw-semibold">
                        ${record.gender}
                    </div>
                </div>

            </div>


            <div class="row">

                <div class="col-md-6">
                    <label class="text-muted small">Weight</label>
                    <div class="fw-semibold">
                        ${not empty record.weight ? record.weight : '--'} kg
                    </div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Doctor</label>
                    <div class="fw-semibold">
                        Dr. ${record.doctorName}
                    </div>
                </div>

            </div>

        </div>

    </div>



    <!-- DIAGNOSIS -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fas fa-stethoscope text-primary me-2"></i>
                Medical Diagnosis
            </h5>
        </div>

        <div class="card-body">

            <div class="mb-3">
                <label class="text-muted small">Diagnosis</label>
                <div class="fw-bold text-danger">
                    ${not empty record.diagnosis ? record.diagnosis : 'No diagnosis yet'}
                </div>
            </div>

            <div>
                <label class="text-muted small">Symptoms</label>
                <div class="fst-italic">
                    ${not empty record.symptom ? record.symptom : '--'}
                </div>
            </div>

        </div>

    </div>



    <!-- PRESCRIBED MEDICINES -->
    <div class="card shadow-sm">

        <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">

            <h5 class="mb-0">
                <i class="fas fa-pills text-primary me-2"></i>
                Prescribed Medicines
            </h5>

            <span class="badge bg-light text-dark border">
                Total: ${prescribedList.size()} items
            </span>

        </div>

        <div class="card-body p-0">

            <div class="table-responsive">

                <table class="table table-hover align-middle mb-0">

                    <thead class="table-light">

                        <tr>
                            <th width="5%" class="text-center">#</th>
                            <th width="25%">Medicine Name</th>
                            <th width="10%" class="text-center">Qty</th>
                            <th width="10%" class="text-center">Unit</th>
                            <th width="30%">Dosage</th>
                            <th width="20%">Note</th>
                        </tr>

                    </thead>

                    <tbody>

                        <c:forEach items="${prescribedList}" var="p" varStatus="st">

                            <tr>

                                <td class="text-center fw-bold text-muted">
                                    ${st.index + 1}
                                </td>

                                <%-- <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> --%>

                                <td class="fw-semibold d-flex justify-content-between align-items-center">
                                    <span>${p.medicineName}</span>

                                    <button type="button" 
                                            class="btn btn-sm btn-link text-info p-0 ms-2"
                                            data-name="${fn:escapeXml(p.medicineName)}"
                                            data-ingredients="${fn:escapeXml(p.ingredients)}"
                                            data-usage="${fn:escapeXml(p.usage)}"
                                            data-contra="${fn:escapeXml(p.contraindication)}"
                                            onclick="showMedInfo(this)" 
                                            title="View Medicine Info">
                                        <i class="fa-solid fa-circle-info fs-6"></i>
                                    </button>
                                </td>

                                <td class="text-center fw-bold">
                                    ${p.quantity}
                                </td>

                                <td class="text-center">
                                    ${p.unit}
                                </td>

                                <td>
                                    ${p.dosage}
                                </td>

                                <td class="text-muted small">
                                    ${p.note}
                                </td>

                            </tr>

                        </c:forEach>


                        <c:if test="${empty prescribedList}">

                            <tr>

                                <td colspan="6" class="text-center text-muted py-5">

                                    <i class="fas fa-prescription-bottle fs-1 mb-2"></i>

                                    <div>No medicines prescribed yet.</div>

                                    <button type="button"
                                            class="btn btn-sm btn-primary mt-3"
                                            data-bs-toggle="modal"
                                            data-bs-target="#editPrescriptionModal">

                                        <i class="fas fa-plus me-1"></i>
                                        Add Medicines

                                    </button>

                                </td>

                            </tr>

                        </c:if>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

    <div class="modal fade" id="editPrescriptionModal" tabindex="-1" aria-hidden="true" data-bs-backdrop="static">
        <div class="modal-dialog modal-xl modal-dialog-centered">
            <div class="modal-content border-0 shadow">

                <form action="${basePath}/prescription/detail" method="post" id="rxForm">

                    <div class="modal-header">
                        <h5 class="modal-title">
                            <i class="fas fa-edit me-2"></i>
                            Edit Prescription - Record #${record.medicalRecordId}
                        </h5>
                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close"></button>
                    </div>


                    <div class="modal-body">

                        <input type="hidden" name="medicalRecordId" value="${record.medicalRecordId}">

                        <div class="d-flex justify-content-between align-items-end mb-3">

                            <h6 class="text-primary fw-bold mb-0">
                                <i class="fas fa-list-ul me-2"></i>
                                Medicine List
                            </h6>

                            <button type="button"
                                    class="btn btn-sm btn-success fw-bold px-3 shadow-sm"
                                    id="btnAddRow">

                                <i class="fas fa-plus-circle me-1"></i>
                                Add Another Medicine

                            </button>

                        </div>


                        <div class="table-responsive shadow-sm rounded">

                            <table class="table table-bordered bg-white mb-0" id="rxTable">

                                <thead class="table-light text-dark text-center align-middle border-bottom border-top">

                                    <tr>

                                        <th width="30%">
                                            Medicine <span class="text-danger">*</span>
                                        </th>

                                        <th width="10%">
                                            Quantity <span class="text-danger">*</span>
                                        </th>

                                        <th width="10%">Unit</th>

                                        <th width="25%">Dosage / Usage</th>

                                        <th width="20%">Note</th>

                                        <th width="5%">
                                            <i class="fas fa-trash text-danger"></i>
                                        </th>

                                    </tr>

                                </thead>


                                <tbody id="rxBody">

                                    <c:forEach items="${prescribedList}" var="p">

                                        <tr class="rx-row align-middle">

                                            <td class="px-2 py-2">

                                                <select class="form-select medicine-select"
                                                        name="medicineIds"
                                                        required>

                                                    <option value="">-- Search medicine --</option>

                                                    <c:forEach items="${allMedicines}" var="m">

                                                        <option value="${m.medicineId}"
                                                                data-unit="${m.unit}"
                                                                data-usage="${m.usage}"
                                                                ${m.medicineId == p.medicineId ? 'selected' : ''}>

                                                            ${m.medicineName} (${m.unit})

                                                        </option>

                                                    </c:forEach>

                                                </select>

                                            </td>


                                            <td class="py-2">

                                                <input type="number"
                                                       class="form-control text-center"
                                                       name="quantities"
                                                       value="${p.quantity}"
                                                       min="1"
                                                       required>

                                            </td>


                                            <td class="unit-display text-center fw-bold text-muted py-2">
                                                ${p.unit}
                                            </td>


                                            <td class="py-2">

                                                <input type="text"
                                                       class="form-control usage-input"
                                                       name="dosages"
                                                       value="${p.dosage}"
                                                       required>

                                            </td>


                                            <td class="py-2">

                                                <input type="text"
                                                       class="form-control"
                                                       name="notes"
                                                       value="${p.note}">

                                            </td>


                                            <td class="text-center py-2">

                                                <button type="button"
                                                        class="btn btn-sm btn-outline-danger border-0 btn-remove">

                                                    <i class="fas fa-times fs-5"></i>

                                                </button>

                                            </td>

                                        </tr>

                                    </c:forEach>

                                </tbody>

                            </table>

                        </div>

                    </div>


                    <div class="modal-footer">

                        <button type="button"
                                class="btn btn-outline-secondary px-4"
                                data-bs-dismiss="modal">

                            Cancel

                        </button>

                        <button type="submit"
                                class="btn btn-primary fw-bold px-4">

                            <i class="fas fa-save me-2"></i>
                            Save Prescription

                        </button>

                    </div>

                </form>

            </div>
        </div>
    </div>

    <div id="medicineSource" style="display: none;">
        <option value="">-- Search medicine --</option>
        <c:forEach items="${allMedicines}" var="m">
            <option value="${m.medicineId}" data-unit="${m.unit}" data-usage="${m.usage}">
                ${m.medicineName} (${m.unit})
            </option>
        </c:forEach>
    </div>



    <div class="modal fade" id="medicineInfoModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow">
                <div class="modal-header bg-light">
                    <h5 class="modal-title">
                        <i class="fa-solid fa-pills text-dark me-2"></i>
                        <span id="infoMedName" class="fw-bold text-dark">Medicine</span>
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body p-4">
                    <div class="mb-3">
                        <h6 class="fw-bold text-dark border-bottom pb-1">
                            <i class="fa-solid fa-flask me-2 text-dark"></i>Ingredients
                        </h6>
                        <p id="infoMedIngredients" class="text-muted small mb-0">No data</p>
                    </div>

                    <div class="mb-3">
                        <h6 class="fw-bold text-dark border-bottom pb-1">
                            <i class="fa-solid fa-book-medical me-2 text-dark"></i>Usage
                        </h6>
                        <p id="infoMedUsage" class="text-muted small mb-0">No data</p>
                    </div>

                    <div class="mb-0">
                        <h6 class="fw-bold text-dark border-bottom pb-1">
                            <i class="fa-solid fa-triangle-exclamation me-2 text-dark"></i>Contraindication
                        </h6>
                        <p id="infoMedContra" class="text-muted small mb-0">No data</p>
                    </div>
                </div>

                <div class="modal-footer border-top-0">
                    <button type="button" class="btn btn-outline-secondary px-4" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>


    <style>
        .card {
            border-radius: 12px;
            border: none;
        }
        .table-bordered th {
            background-color: #f8f9fa;
        }
        .form-select:focus, .form-control:focus {
            border-color: #86b7fe;
            box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
        }
        .btn-remove {
            transition: all 0.2s;
        }
        .btn-remove:hover {
            transform: scale(1.1);
        }
    </style>
    <script>
        // Hàm khởi tạo Select2 (Có thuộc tính dropdownParent cực kỳ quan trọng cho Modal)
        function initSelect2() {
            $('.medicine-select').select2({
                dropdownParent: $('#editPrescriptionModal'),
                width: '100%',
                placeholder: "-- Search medicine --"
            });
        }

        // Hàm thêm dòng mới
        function addMedicineRow() {
            const tbody = document.getElementById('rxBody');
            const tr = document.createElement('tr');
            tr.className = 'rx-row align-middle border-bottom';
            const optionsHtml = document.getElementById('medicineSource').innerHTML;

            tr.innerHTML = `
                <td class="px-2 py-2">
                    <select class="form-select medicine-select" name="medicineIds" required>
                        ` + optionsHtml + `
                    </select>
                </td>
                <td class="py-2">
                    <input type="number" class="form-control text-center" name="quantities" value="1" min="1" required>
                </td>
                <td class="unit-display text-center fw-bold text-muted py-2">--</td>
                <td class="py-2">
                    <input type="text" class="form-control usage-input" name="dosages" required placeholder="Ex: Sáng 1, Tối 1..." required pattern=".*\\S+.*" title="Please enter a valid dosage">
                </td>
                <td class="py-2">
                    <input type="text" class="form-control" name="notes" placeholder="Note..." required pattern=".*\\S+.*" title="Please enter a valid note">
                </td>
                <td class="text-center py-2">
                    <button type="button" class="btn btn-sm btn-outline-danger border-0 btn-remove"><i class="fas fa-times fs-5"></i></button>
                </td>
            `;
            tbody.appendChild(tr);

            // Gọi lại Select2 cho dòng vừa được tạo
            initSelect2();
        }

        $(document).ready(function () {
            const rxBody = document.getElementById("rxBody");

            // Khi mở modal lên, khởi tạo Select2 cho các dòng có sẵn
            $('#editPrescriptionModal').on('shown.bs.modal', function () {
                initSelect2();
                // Nếu toa trống, tự động thêm 1 dòng trắng
                if (rxBody.children.length === 0) {
                    addMedicineRow();
                }
            });

            // Nút Add
            $('#btnAddRow').on('click', function () {
                addMedicineRow();
            });

            // Bắt sự kiện Xóa dòng
            $(document).on('click', '.btn-remove', function () {
                $(this).closest('tr').remove();
                if (rxBody.children.length === 0) {
                    addMedicineRow();
                }
            });

            // Bắt sự kiện Đổi Thuốc (Dùng jQuery on change cho tương thích Select2)
            $(document).on('change', '.medicine-select', function () {
                const selectEl = $(this);
                const row = selectEl.closest('tr');
                const selectedOption = selectEl.find('option:selected');

                const unit = selectedOption.attr('data-unit') || '--';
                const usage = selectedOption.attr('data-usage') || '';

                // Điền Đơn vị hiển thị
                row.find('.unit-display').text(unit);

                // Tự động điền Cách dùng nếu ô đang trống
                const usageInput = row.find('.usage-input');
                if (usageInput.val().trim() === '') {
                    usageInput.val(usage);
                }
            });
        });
        
        function showMedInfo(btnElement) {
        // 1. Lấy dữ liệu từ các thuộc tính data-* của nút bấm
        let name = btnElement.getAttribute('data-name') || 'Không xác định';
        let ingredients = btnElement.getAttribute('data-ingredients') || 'Đang cập nhật...';
        let usage = btnElement.getAttribute('data-usage') || 'Đang cập nhật...';
        let contra = btnElement.getAttribute('data-contra') || 'Đang cập nhật...';

        // 2. Đổ dữ liệu vào Modal
        document.getElementById('infoMedName').textContent = name;
        document.getElementById('infoMedIngredients').textContent = ingredients;
        document.getElementById('infoMedUsage').textContent = usage;
        document.getElementById('infoMedContra').textContent = contra;

        // 3. Hiển thị Modal lên
        let infoModal = new bootstrap.Modal(document.getElementById('medicineInfoModal'));
        infoModal.show();
    }
        
    </script>