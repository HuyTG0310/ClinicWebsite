<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%-- 🔥 Cần thư viện Gson để chuyển List<Range> thành chuỗi JSON nhét vào HTML --%>
<%@ page import="com.google.gson.Gson" %>

<c:set var="editError" value="${param.editError}" />
<% Gson gson = new Gson();%> <div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="mb-1"><i class="fa-solid fa-notes-medical text-primary me-2"></i>Service Detail</h2>
            <p class="text-muted mb-0">View and update service information</p>
        </div>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/admin/service/list" class="btn btn-outline-secondary">
                <i class="fa-solid fa-arrow-left me-1"></i>Back to list
            </a>
            <button class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#editServiceModal">
                <i class="fa-solid fa-pen-to-square me-1"></i>Edit
            </button>
        </div>
    </div>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="fa-solid fa-check-circle me-2"></i>${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
            <i class="fa-solid fa-triangle-exclamation me-2"></i>${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-notes-medical text-primary me-2"></i>
                Service Information
            </h5>
        </div>

        <div class="card-body">

            <div class="row g-4">

                <!-- Service ID -->
                <div class="col-md-6">
                    <label class="text-muted small">Service ID</label>
                    <div class="fw-semibold">
                        #${service.serviceId}
                    </div>
                </div>

                <!-- Category -->
                <div class="col-md-6">
                    <label class="text-muted small">Category</label>
                    <div>
                        <span class="badge bg-light text-dark border">
                            ${service.category}
                        </span>
                    </div>
                </div>

                <!-- Price -->
                <div class="col-md-6">
                    <label class="text-muted small">Price</label>
                    <div class="fw-semibold text-danger">
                        <fmt:formatNumber value="${service.currentPrice}" pattern="#,###"/> ₫
                    </div>
                </div>

                <!-- Status -->
                <div class="col-md-6">
                    <label class="text-muted small">Status</label>

                    <div>
                        <c:choose>

                            <c:when test="${service.isActive}">
                                <span class="badge bg-success">
                                    <i class="fa-solid fa-check-circle me-1"></i>
                                    Active
                                </span>
                            </c:when>

                            <c:otherwise>
                                <span class="badge bg-danger">
                                    <i class="fa-solid fa-ban me-1"></i>
                                    Inactive
                                </span>
                            </c:otherwise>

                        </c:choose>
                    </div>
                </div>

                <!-- Service Name -->
                <div class="col-12">
                    <label class="text-muted small">Service Name</label>
                    <div class="fs-5 fw-medium">
                        ${service.serviceName}
                    </div>
                </div>

            </div>

        </div>

    </div>

    <c:if test="${service.category == 'Xét nghiệm' && labTest != null}">
        <div class="card shadow-sm">
            <div class="card-header bg-white py-3"><h5 class="mb-0"><i class="fa-solid fa-microscope text-primary me-2"></i>Lab Test Parameters</h5></div>
            <div class="card-body">
                <div class="row mb-4">
                    <div class="col-md-4"><label class="text-muted small">Test Code</label><div class="fw-semibold text-uppercase">${labTest.testCode}</div></div>
                    <div class="col-md-4"><label class="text-muted small">Category</label><div class="fw-semibold">${labTest.categoryName}</div></div>
                    <div class="col-md-4"><label class="text-muted small">Type</label>
                        <div>
                            <c:choose>
                                <c:when test="${labTest.isPanel}"><span class="badge bg-primary">Panel</span></c:when>
                                <c:otherwise><span class="badge bg-secondary">Single Test</span></c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th width="5%">#</th>
                                <th width="15%">Code</th>
                                <th width="25%">Parameter</th>
                                <th width="10%">Unit</th>
                                <th width="45%">Reference Ranges (Rules)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${labTest.parameters}" var="p" varStatus="loop">
                                <tr>
                                    <td class="text-muted">${loop.index + 1}</td>
                                    <td><code>${p.parameterCode}</code></td>
                                    <td class="fw-semibold">${p.parameterName}</td>
                                    <td class="text-muted">${empty p.unit ? '-' : p.unit}</td>
                                    <td>

                                        <c:if test="${empty p.referenceRanges}">
                                            <span class="text-muted fst-italic">Not configured</span>
                                        </c:if>

                                        <c:if test="${not empty p.referenceRanges}">

                                            <div class="reference-rules">

                                                <c:forEach items="${p.referenceRanges}" var="r">

                                                    <div class="rule-row">

                                                        <span class="rule-gender badge bg-light text-dark border">
                                                            ${r.gender == 'M' ? 'Male' : (r.gender == 'F' ? 'Female' : 'All')}
                                                        </span>

                                                        <span class="rule-age">
                                                            ${r.ageMinDays} - ${r.ageMaxDays} days
                                                        </span>

                                                        <span class="rule-min">
                                                            ${r.refMin != null ? r.refMin : '-'}
                                                        </span>

                                                        <span class="rule-max">
                                                            ${r.refMax != null ? r.refMax : '-'}
                                                        </span>

                                                    </div>

                                                </c:forEach>

                                            </div>

                                        </c:if>

                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>
</div>


<div class="modal fade" id="editServiceModal" tabindex="-1" data-bs-backdrop="static">
    <div class="modal-dialog modal-dialog-centered ${service.category == 'Xét nghiệm' ? 'modal-xl' : 'modal-lg'}">
        <div class="modal-content border-0 shadow-lg rounded-4">
            <div class="modal-header bg-white border-0 py-3 rounded-top-4">
                <h5 class="modal-title fw-bold text-dark"><i class="fa-solid fa-pen-to-square me-2"></i>Edit Information</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/admin/service/edit" id="frmEditService">
                <div class="modal-body p-4 bg-light">
                    <input type="hidden" name="serviceId" value="${service.serviceId}" />
                    <input type="hidden" name="category" value="${service.category}" /> 
                    <c:if test="${service.category == 'Xét nghiệm'}">
                        <input type="hidden" name="labTestId" value="${labTest.labTestId}">
                    </c:if>

                    <div class="row">
                        <div class="${service.category == 'Xét nghiệm' ? 'col-lg-4 mb-3' : 'col-12'}">
                            <div class="card border-0 shadow-sm h-100">
                                <div class="card-body p-3">
                                    <h6 class="fw-bold text-primary border-bottom pb-2 mb-3">Basic Information</h6>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold small">Service Category</label>
                                        <select class="form-select bg-light fw-bold text-muted" disabled><option>${service.category}</option></select>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold small">Service Name <span class="text-danger">*</span></label>
                                        <input type="text" name="serviceName" value="${service.serviceName}" class="form-control" required pattern=".*\S+.*" title="Name cannot be empty"/>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold small">Price <span class="text-danger">*</span></label>
                                        <input type="number" name="price" value="<fmt:formatNumber value='${service.currentPrice}' pattern='#'/>" class="form-control font-monospace text-danger fw-bold" required min="1" />
                                    </div>
                                    <div class="form-check form-switch mt-4">
                                        <input class="form-check-input" type="checkbox" name="isActive" id="isActiveEdit" value="true" ${service.isActive ? 'checked' : ''}>
                                        <label class="form-check-label fw-bold small text-success" for="isActiveEdit">Is active</label>
                                    </div>

                                    <c:if test="${service.category == 'Xét nghiệm'}">
                                        <h6 class="fw-bold text-info border-bottom pb-2 mb-3 mt-4">Lab Test Configuration</h6>
                                        <div class="mb-3">
                                            <label class="form-label fw-bold small">Code <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control text-uppercase" name="testCode" value="${labTest.testCode}" required pattern=".*\S+.*" title="Code cannot be empty">
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-bold small">Category <span class="text-danger">*</span></label>
                                            <select class="form-select" name="labCategoryId" required>
                                                <c:forEach items="${labCategories}" var="cat">
                                                    <option value="${cat.categoryId}" ${cat.categoryId == labTest.categoryId ? 'selected' : ''}>${cat.categoryName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <c:if test="${service.category == 'Xét nghiệm'}">
                            <div class="col-lg-8 mb-3">
                                <div class="card border-0 shadow-sm h-100 border-info">
                                    <div class="card-header bg-white d-flex justify-content-between align-items-center py-3 border-bottom">
                                        <h6 class="fw-bold text-dark mb-0"><i class="fa-solid fa-list-ol me-2"></i>Parameter Configuration</h6>
                                        <button type="button" class="btn btn-sm btn-outline-primary fw-bold" onclick="addParameterRowEdit()">
                                            <i class="fa-solid fa-plus"></i> Add New Parameter
                                        </button>
                                    </div>
                                    <div class="card-body p-0">
                                        <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                                            <table class="table table-hover align-middle mb-0 border-0">
                                                <thead class="table-light" style="position: sticky; top: 0; z-index: 1;">
                                                    <tr>
                                                        <th width="25%" class="small text-muted py-2">PARAM CODE</th>
                                                        <th width="35%" class="small text-muted py-2">PARAM NAME</th>
                                                        <th width="15%" class="small text-muted py-2">UNIT</th>
                                                        <th width="15%" class="text-center small text-muted py-2">REF</th>
                                                        <th width="10%" class="text-center small text-muted py-2"><i class="fa-solid fa-trash"></i></th>
                                                    </tr>
                                                </thead>
                                                <tbody id="paramTableBodyEdit">
                                                    <c:forEach items="${labTest.parameters}" var="p">
                                                        <c:set var="jsonRanges" value='<%= gson.toJson(((model.LabTestParameter) pageContext.getAttribute("p")).getReferenceRanges())%>' />

                                                        <tr>
                                                    <input type="hidden" name="paramId[]" value="${p.parameterId}">
                                                    <td><input type="text" class="form-control form-control-sm text-uppercase" name="paramCode[]" value="${p.parameterCode}" required pattern=".*\S+.*" title="Vui lòng nhập mã chỉ số hợp lệ"></td>
                                                    <td><input type="text" class="form-control form-control-sm" name="paramName[]" value="${p.parameterName}" required pattern=".*\S+.*" title="Vui lòng nhập tên chỉ số hợp lệ"></td>
                                                    <td><input type="text" class="form-control form-control-sm" name="paramUnit[]" value="${p.unit}"></td>
                                                    <td class="text-center">
                                                        <input type="hidden" name="paramRanges[]" class="range-hidden-val" value='${jsonRanges}'>
                                                        <button type="button" class="btn btn-sm btn-outline-primary fw-bold param-config-btn" onclick="openRangeModal(this)">
                                                            <i class="fa-solid fa-gears"></i> Edit
                                                        </button>
                                                    </td>
                                                    <td class="text-center">
                                                        <button type="button" class="btn btn-outline-danger btn-sm" onclick="removeRowEdit(this)"><i class="fa-solid fa-xmark"></i></button>
                                                    </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="modal-footer bg-white border-top py-3 rounded-bottom-4">
                    <button type="button" class="btn btn-light fw-bold px-4" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary text-white fw-bold shadow-sm px-5"><i class="fa-solid fa-floppy-disk me-2"></i>Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="rangeModal" tabindex="-1" data-bs-backdrop="static" style="z-index: 1060;"> <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content shadow-lg border-0 rounded-4">
            <div class="modal-header bg-dark text-white py-3 rounded-top-4">
                <h5 class="modal-title fw-bold"><i class="fa-solid fa-sliders me-2"></i>Reference Range Configuration</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body bg-light p-4">
                <div class="alert alert-info small">
                    <i class="fa-solid fa-circle-info me-1"></i>
                    <b>Note:</b> Age is calculated by <b>Day</b>
                    (Newborns: 0-30 days, Adult: > 6570 days.
                    Default 36500 = 100 year olds).
                </div>
                <div class="table-responsive rounded-3 border bg-white shadow-sm">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light text-center small">
                            <tr>
                                <th width="15%">SEX</th>
                                <th width="20%">MIN AGE (DAY)</th>
                                <th width="20%">MAX AGE (DAY)</th>
                                <th width="15%">MIN VALUE</th>
                                <th width="15%">MAX VALUE</th>
                                <th width="15%"><button type="button" class="btn btn-sm btn-outline-primary fw-bold w-100" onclick="addModalRangeRow()"><i class="fa-solid fa-plus"></i> Add</button></th>
                            </tr>
                        </thead>
                        <tbody id="modalRangeBody" class="text-center"></tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer bg-white border-top">
                <button type="button" class="btn btn-secondary fw-bold px-4" data-bs-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary fw-bold px-4" onclick="saveRangeData()"><i class="fa-solid fa-check me-2"></i>Save</button>
            </div>
        </div>
    </div>
</div>

<style>
    .reference-rules{
        display:flex;
        flex-direction:column;
        gap:4px;
    }

    .rule-row{
        display:grid;
        grid-template-columns: 90px 120px 70px 70px;
        column-gap: 12px;
        align-items:center;
        font-size:13px;
        padding:3px 0;
        border-bottom:1px solid #f1f1f1;
    }

    .rule-row:last-child{
        border-bottom:none;
    }

    .rule-gender{
        font-size:12px;
    }

    .rule-age{
        color:#6c757d;
    }

    .rule-min{
        font-weight:600;
        color:#0d6efd;
        text-align:right;
    }

    .rule-max{
        font-weight:600;
        color:#dc3545;
        text-align:right;
    }
</style>

<script>
    let currentActiveRow = null;

    // THÊM CHỈ SỐ MỚI HOÀN TOÀN BÊN TRONG MODAL EDIT
    function addParameterRowEdit() {
        const tbody = document.getElementById('paramTableBodyEdit');
        const row = document.createElement('tr');
        let html = '<input type="hidden" name="paramId[]" value="0">' +
                '<td><input type="text" class="form-control form-control-sm text-uppercase" name="paramCode[]" required pattern=".*\\S+.*" title="Vui lòng nhập mã chỉ số hợp lệ"></td>' +
                '<td><input type="text" class="form-control form-control-sm" name="paramName[]" required pattern=".*\\S+.*" title="Vui lòng nhập tên chỉ số hợp lệ"></td>' +
                '<td><input type="text" class="form-control form-control-sm" name="paramUnit[]"></td>' +
                '<td class="text-center">' +
                '<input type="hidden" name="paramRanges[]" class="range-hidden-val" value=\'[{"gender":"ALL","ageMinDays":0,"ageMaxDays":36500}]\'>' +
                '<button type="button" class="btn btn-sm btn-outline-primary fw-bold param-config-btn" onclick="openRangeModal(this)"><i class="fa-solid fa-gears"></i> Cấu hình</button>' +
                '</td>' +
                '<td class="text-center"><button type="button" class="btn btn-outline-danger btn-sm" onclick="removeRowEdit(this)"><i class="fa-solid fa-xmark"></i></button></td>';
        row.innerHTML = html;
        tbody.appendChild(row);
    }

    function removeRowEdit(button) {
        const tbody = document.getElementById('paramTableBodyEdit');
        if (tbody.querySelectorAll('tr').length > 1) {
            button.closest('tr').remove();
        } else {
            alert("Phải giữ lại ít nhất 1 chỉ số xét nghiệm!");
        }
    }

    // ĐỌC JSON TỪ THẺ HIDDEN ĐỂ MỞ MODAL CON
    function openRangeModal(btn) {
        currentActiveRow = btn.closest('tr');
        let hiddenInput = currentActiveRow.querySelector('.range-hidden-val');
        let dataStr = hiddenInput.value;
        let tbody = document.getElementById('modalRangeBody');
        tbody.innerHTML = '';

        if (dataStr && dataStr.trim() !== '' && dataStr !== 'null') {
            try {
                let ranges = JSON.parse(dataStr);
                if (ranges.length === 0) {
                    addModalRangeRow('ALL', '0', '36500', '', '');
                } else {
                    ranges.forEach(r => {
                        addModalRangeRow(r.gender, r.ageMinDays, r.ageMaxDays, r.refMin == null ? '' : r.refMin, r.refMax == null ? '' : r.refMax);
                    });
                }
            } catch (e) {
                console.error("Lỗi đọc JSON:", e);
                addModalRangeRow('ALL', '0', '36500', '', '');
            }
        } else {
            addModalRangeRow('ALL', '0', '36500', '', '');
        }
        new bootstrap.Modal(document.getElementById('rangeModal')).show();
    }

    // THÊM DÒNG TRONG MODAL CON
    function addModalRangeRow(gender = 'ALL', ageMin = '0', ageMax = '36500', refMin = '', refMax = '') {
        let tr = document.createElement('tr');
        let html = '<td><select class="form-select form-select-sm modal-gender fw-bold">' +
                '<option value="ALL" ' + (gender === 'ALL' ? 'selected' : '') + '>All</option>' +
                '<option value="M" ' + (gender === 'M' ? 'selected' : '') + '>Male</option>' +
                '<option value="F" ' + (gender === 'F' ? 'selected' : '') + '>Female</option></select></td>' +
                '<td><input type="number" class="form-control form-control-sm text-center modal-agemin" value="' + ageMin + '"></td>' +
                '<td><input type="number" class="form-control form-control-sm text-center modal-agemax" value="' + ageMax + '"></td>' +
                '<td><input type="number" step="any" class="form-control form-control-sm text-center modal-refmin border-primary" value="' + refMin + '" placeholder="Empty"></td>' +
                '<td><input type="number" step="any" class="form-control form-control-sm text-center modal-refmax border-danger" value="' + refMax + '" placeholder="Empty"></td>' +
                '<td><button type="button" class="btn btn-sm btn-outline-danger w-100" onclick="this.closest(\'tr\').remove()"><i class="fa-solid fa-trash"></i></button></td>';
        tr.innerHTML = html;
        document.getElementById('modalRangeBody').appendChild(tr);
    }

    // LƯU TỪ MODAL CON XUỐNG THẺ HIDDEN CỦA MODAL CHA
    function saveRangeData() {
        let tbody = document.getElementById('modalRangeBody');
        let trs = tbody.querySelectorAll('tr');
        let dataArr = [];
        let isValid = true;

        trs.forEach((tr, index) => {
            // Lấy các thẻ input
            let aminInput = tr.querySelector('.modal-agemin');
            let amaxInput = tr.querySelector('.modal-agemax');
            let rminInput = tr.querySelector('.modal-refmin');
            let rmaxInput = tr.querySelector('.modal-refmax');

            // Lấy giá trị (Nếu để trống tuổi thì mặc định Min=0, Max=36500)
            let amin = aminInput.value.trim() !== "" ? parseInt(aminInput.value.trim()) : 0;
            let amax = amaxInput.value.trim() !== "" ? parseInt(amaxInput.value.trim()) : 36500;
            let rmin = rminInput.value.trim();
            let rmax = rmaxInput.value.trim();

            // Xóa viền đỏ cảnh báo cũ
            aminInput.classList.remove('is-invalid');
            amaxInput.classList.remove('is-invalid');
            rminInput.classList.remove('is-invalid');
            rmaxInput.classList.remove('is-invalid');

            // 1. VALIDATE: Không được nhập tuổi âm
            if (amin < 0 || amax < 0) {
                alert("❌ Error in row " + (index + 1) + ": Age can not be a negative!");
                if (amin < 0)
                    aminInput.classList.add('is-invalid');
                if (amax < 0)
                    amaxInput.classList.add('is-invalid');
                isValid = false;
            }

            // 2. VALIDATE: Tuổi Max phải >= Tuổi Min
            if (amax < amin) {
                alert("❌ Error in row " + (index + 1) + ": MAX age must be greater than or equal to MIN age!");
                aminInput.classList.add('is-invalid');
                amaxInput.classList.add('is-invalid');
                isValid = false;
            }

            // 3. VALIDATE: Không được nhập tham chiếu âm
            if (rmin !== "" && parseFloat(rmin) < 0) {
                alert("❌ Error in row " + (index + 1) + ": MIN value can not be a negative!");
                rminInput.classList.add('is-invalid');
                isValid = false;
            }
            if (rmax !== "" && parseFloat(rmax) < 0) {
                alert("❌ Error in row " + (index + 1) + ": MAX value can not be a negative!");
                rmaxInput.classList.add('is-invalid');
                isValid = false;
            }

            // 4. VALIDATE: Ngưỡng Max > Ngưỡng Min (Nếu nhập cả 2)
            if (rmin !== "" && rmax !== "") {
                if (parseFloat(rmin) >= parseFloat(rmax)) {
                    alert("❌ Error in row " + (index + 1) + ": MAX value must be greater than MIN value!");
                    rminInput.classList.add('is-invalid');
                    rmaxInput.classList.add('is-invalid');
                    isValid = false;
                }
            }

            // Nếu mọi thứ OK, tạo Object chuẩn JSON
            let rangeObj = {
                gender: tr.querySelector('.modal-gender').value,
                ageMinDays: amin,
                ageMaxDays: amax,
                refMin: rmin !== "" ? parseFloat(rmin) : null,
                refMax: rmax !== "" ? parseFloat(rmax) : null
            };
            dataArr.push(rangeObj);
        });

        if (!isValid)
            return; // Dừng lại, không đóng Modal, bắt người dùng sửa

        if (currentActiveRow) {
            // Ép mảng Object thành chuỗi JSON
            currentActiveRow.querySelector('.range-hidden-val').value = JSON.stringify(dataArr);

            let btn = currentActiveRow.querySelector('.param-config-btn');
            btn.classList.remove('btn-outline-primary');
            btn.classList.add('btn-primary');
            btn.innerHTML = '<i class="fa-solid fa-check"></i> Đã cấu hình (' + trs.length + ' Rule)';
        }

        bootstrap.Modal.getInstance(document.getElementById('rangeModal')).hide();
    }

    // Tiện ích nhỏ: Khi người dùng bắt đầu gõ sửa lỗi, tự động xóa viền đỏ ngay lập tức
    document.addEventListener('input', function (e) {
        if (e.target.classList.contains('modal-agemin') ||
                e.target.classList.contains('modal-agemax') ||
                e.target.classList.contains('modal-refmin') ||
                e.target.classList.contains('modal-refmax')) {
            e.target.classList.remove('is-invalid');
        }
    });
</script>