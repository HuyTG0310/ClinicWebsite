
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="mb-4">
    <h2 class="mb-1">
        <i class="fa-solid fa-plus-circle text-primary me-2"></i>
        Add New Service
    </h2>
    <p class="text-muted mb-0">
        Enter service information. If it is a Lab Test, the system will expand parameter configuration.
    </p>
</div>

<c:if test="${not empty sessionScope.error}">
    <div class="alert alert-danger shadow-sm">
        <i class="fa-solid fa-exclamation-triangle me-2"></i>${sessionScope.error}
        <c:remove var="error" scope="session"/>
    </div>
</c:if>

<form method="post" action="${basePath}/service/add" id="frmServiceAdd" onsubmit="return validateLabTestParams(event)">

    <div class="row g-4">

        <!-- ================= BASIC INFO ================= -->

        <div class="col-lg-12" id="basicInfoCol">

            <div class="card shadow-sm border-0">

                <div class="card-header bg-white py-3">
                    <h5 class="mb-0">
                        <i class="fa-solid fa-info-circle text-primary me-2"></i>
                        Basic Information
                    </h5>
                </div>

                <div class="card-body">

                    <div class="mb-3">

                        <label class="form-label fw-semibold">
                            Service / Test Name <span class="text-danger">*</span>
                        </label>

                        <input type="text"
                               name="serviceName"
                               class="form-control"
                               required
                               placeholder="Example: Internal Medicine, Ultrasound..."
                               pattern=".*\S+.*"
                               title="Name cannot be empty">

                    </div>



                    <div class="mb-3">

                        <label class="form-label fw-semibold">
                            Service Category <span class="text-danger">*</span>
                        </label>

                        <select name="category"
                                id="cboCategory"
                                class="form-select"
                                onchange="toggleLabTestSection()"
                                required>

                            <option value="">-- Select category --</option>

                            <c:forEach var="c" items="${categories}">
                                <option value="${c}">${c}</option>
                            </c:forEach>

                        </select>

                    </div>



                    <div class="mb-3">

                        <label class="form-label fw-semibold">
                            Price (VND) <span class="text-danger">*</span>
                        </label>

                        <input type="number"
                               name="price"
                               class="form-control"
                               required
                               min="1"
                               placeholder="0">

                    </div>

                </div>
            </div>
        </div>



        <!-- ================= LAB TEST CONFIG ================= -->

        <div class="col-lg-8 mb-4 d-none transition-fade" id="labTestSection">

            <div class="card shadow-sm border-0">

                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">

                    <h5 class="mb-0">
                        <i class="fa-solid fa-microscope text-primary me-2"></i>
                        Lab Test Parameters
                    </h5>

                    <button type="button"
                            class="btn btn-outline-primary btn-sm"
                            onclick="addParameterRow()">

                        <i class="fa-solid fa-plus me-1"></i>
                        Add Parameter

                    </button>

                </div>



                <div class="card-body">

                    <div class="row mb-4 border-bottom pb-3">

                        <div class="col-md-4">

                            <label class="form-label fw-semibold small">
                                Test Code
                            </label>

                            <input type="text"
                                   class="form-control text-uppercase"
                                   name="testCode"
                                   id="txtTestCode"
                                   placeholder="Example: GLU, CBC..."
                                   required
                                   pattern=".*\S+.*">

                        </div>



                        <div class="col-md-5">

                            <label class="form-label fw-semibold small">
                                Lab Category
                            </label>

                            <select class="form-select"
                                    name="labCategoryId"
                                    id="cboLabCategory">

                                <c:forEach items="${labCategories}" var="cat">
                                    <option value="${cat.categoryId}">
                                        ${cat.categoryName}
                                    </option>
                                </c:forEach>

                            </select>

                        </div>
                    </div>


                    <div class="table-responsive bg-white border rounded-3">
                        <table class="table table-hover align-middle border-0 mb-0" id="paramTable">
                            <thead class="table-light">
                                <tr>
                                    <th width="25%" class="py-2 text-muted small">MÃ CHỈ SỐ</th>
                                    <th width="35%" class="py-2 text-muted small">TÊN CHỈ SỐ</th>
                                    <th width="15%" class="py-2 text-muted small">ĐƠN VỊ</th>
                                    <th width="15%" class="text-center py-2 text-muted small">THAM CHIẾU</th>
                                    <th width="10%" class="text-center py-2 text-muted"><i class="fa-solid fa-trash"></i></th>
                                </tr>
                            </thead>
                            <tbody id="parameterTableBody">
                                <tr>
                                    <td><input type="text" class="form-control form-control-sm text-uppercase param-req" name="paramCode[]" required pattern=".*\S+.*" title="Vui lòng nhập mã chỉ số hợp lệ"></td>
                                    <td><input type="text" class="form-control form-control-sm param-req" name="paramName[]" required pattern=".*\S+.*" title="Vui lòng nhập tên chỉ số hợp lệ"></td>
                                    <td><input type="text" class="form-control form-control-sm" name="paramUnit[]"></td>
                                    <td class="text-center">
                                        <input type="hidden" name="paramRanges[]" class="range-hidden-val" value="ALL|0|36500||">
                                        <button type="button" class="btn btn-sm btn-outline-primary fw-bold param-config-btn" onclick="openRangeModal(this)">
                                            <i class="fa-solid fa-gears"></i> Configure
                                        </button>
                                    </td>
                                    <td class="text-center">
                                        <button type="button" class="btn btn-outline-danger btn-sm" onclick="removeRow(this)"><i class="fa-solid fa-xmark"></i></button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                </div>

            </div>

        </div>

    </div>



    <!-- ================= ACTION BUTTON ================= -->

    <div class="d-flex justify-content-end gap-2 mt-4 border-top pt-4">

        <a href="${basePath}/service/list"
           class="btn btn-outline-secondary">

            <i class="fa-solid fa-arrow-left me-1"></i>
            Cancel

        </a>

        <button type="submit"
                class="btn btn-primary">

            <i class="fa-solid fa-save me-1"></i>
            Save Service

        </button>

    </div>

</form>


<div class="modal fade" id="rangeModal" tabindex="-1" data-bs-backdrop="static">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">
                    <i class="fa-solid fa-sliders me-2"></i>
                    Reference Range Configuration
                </h5>
                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"></button>
            </div>

            <div class="modal-body">
                <div class="alert alert-info small">
                    <i class="fa-solid fa-circle-info me-1"></i>
                    <b>Note:</b> Age is calculated by <b>Day</b>
                    (Newborns: 0-30 days, Adult: > 6570 days.
                    Default 36500 = 100 year olds).
                </div>

                <div class="table-responsive border rounded">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light text-center">
                            <tr>
                                <th width="15%">SEX</th>
                                <th width="20%">MIN AGE (Day)</th>
                                <th width="20%">MAX AGE (Day)</th>
                                <th width="15%">MIN VALUE</th>
                                <th width="15%">MAX VALUE</th>
                                <th width="15%">
                                    <button type="button"
                                            class="btn btn-sm btn-outline-primary w-100"
                                            onclick="addModalRangeRow()">

                                        <i class="fa-solid fa-plus"></i>
                                        Add
                                    </button>
                                </th>
                            </tr>
                        </thead>
                        <tbody id="modalRangeBody" class="text-center">
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button"
                        class="btn btn-outline-secondary"
                        data-bs-dismiss="modal">
                    Cancel
                </button>

                <button type="button"
                        class="btn btn-primary px-4"
                        onclick="saveRangeData()">

                    <i class="fa-solid fa-check me-1"></i>
                    Save
                </button>
            </div>
        </div>
    </div>
</div>


<style>
    .transition-fade{
        transition:all .3s ease;
    }
</style>

<script>
    // 🔥 MA THUẬT ẨN/HIỆN FORM
    function toggleLabTestSection() {
        const cbo = document.getElementById("cboCategory");
        const labSection = document.getElementById("labTestSection");

        // NEW: lấy cột bên trái
        const basicCol = document.getElementById("basicInfoCol");

        // Các ô bắt buộc nhập nếu mở Form Xét Nghiệm
        const txtTestCode = document.getElementById("txtTestCode");
        const reqInputs = document.querySelectorAll(".param-req");

        if (cbo.value === "Xét nghiệm") {

            // Mở form
            labSection.classList.remove("d-none");

            // NEW: chuyển layout 4 + 8
            basicCol.classList.remove("col-lg-12");
            basicCol.classList.add("col-lg-4");

            // Kích hoạt thuộc tính required để bắt lỗi Validate
            txtTestCode.setAttribute("required", "true");
            reqInputs.forEach(input => input.setAttribute("required", "true"));

        } else {

            // Đóng form
            labSection.classList.add("d-none");

            // NEW: full width lại
            basicCol.classList.remove("col-lg-4");
            basicCol.classList.add("col-lg-12");

            // Gỡ bỏ required để Submit được form Khám bệnh bình thường
            txtTestCode.removeAttribute("required");
            reqInputs.forEach(input => input.removeAttribute("required"));
        }
    }


    let currentActiveRow = null;

    function addParameterRow() {
        const tbody = document.getElementById('parameterTableBody');
        const row = document.createElement('tr');
        // 🔥 Value mặc định giờ là 1 chuỗi JSON hợp lệ
        row.innerHTML = `
            <td><input type="text" class="form-control form-control-sm text-uppercase param-req" name="paramCode[]" required  pattern=".*\\S+.*" title="Please enter a valid parameter code"></td>
            <td><input type="text" class="form-control form-control-sm param-req" name="paramName[]" required pattern=".*\\S+.*" title="Please enter a valid parameter name"></td>
            <td><input type="text" class="form-control form-control-sm" name="paramUnit[]"></td>
            <td class="text-center">
                <input type="hidden" name="paramRanges[]" class="range-hidden-val" value='[{"gender":"ALL","ageMinDays":0,"ageMaxDays":36500}]'>
                <button type="button" class="btn btn-sm btn-outline-primary fw-bold param-config-btn" onclick="openRangeModal(this)">
                    <i class="fa-solid fa-gears"></i> Configure
                </button>
            </td>
            <td class="text-center">
                <button type="button" class="btn btn-outline-danger btn-sm" onclick="removeRow(this)"><i class="fa-solid fa-xmark"></i></button>
            </td>
        `;
        tbody.appendChild(row);
    }


// ĐỌC JSON TỪ THẺ HIDDEN ĐỂ MỞ MODAL
    function openRangeModal(btn) {
        currentActiveRow = btn.closest('tr');
        let hiddenInput = currentActiveRow.querySelector('.range-hidden-val');
        let dataStr = hiddenInput.value;

        let tbody = document.getElementById('modalRangeBody');
        tbody.innerHTML = '';

        if (dataStr && dataStr.trim() !== '') {
            try {
                let ranges = JSON.parse(dataStr); // Parse JSON an toàn
                ranges.forEach(r => {
                    addModalRangeRow(r.gender, r.ageMinDays, r.ageMaxDays, r.refMin == null ? '' : r.refMin, r.refMax == null ? '' : r.refMax);
                });
            } catch (e) {
                console.error("Lỗi đọc JSON:", e);
                addModalRangeRow('ALL', '0', '36500', '', '');
            }
        } else {
            addModalRangeRow('ALL', '0', '36500', '', '');
        }

        new bootstrap.Modal(document.getElementById('rangeModal')).show();
    }


    // 3. Thêm dòng Range con vào Modal (Dùng cộng chuỗi '+' truyền thống)
    function addModalRangeRow(gender = 'ALL', ageMin = '0', ageMax = '36500', refMin = '', refMax = '') {
        let tr = document.createElement('tr');

        // Nối chuỗi HTML bằng dấu '+' siêu an toàn, không dùng tới ký tự $
        let html = '<td>' +
                '<select class="form-select form-select-sm modal-gender fw-bold">' +
                '<option value="ALL" ' + (gender === 'ALL' ? 'selected' : '') + '>All</option>' +
                '<option value="M" ' + (gender === 'M' ? 'selected' : '') + '>Male</option>' +
                '<option value="F" ' + (gender === 'F' ? 'selected' : '') + '>Female</option>' +
                '</select>' +
                '</td>' +
                '<td><input type="number" class="form-control form-control-sm text-center modal-agemin" value="' + ageMin + '"></td>' +
                '<td><input type="number" class="form-control form-control-sm text-center modal-agemax" value="' + ageMax + '"></td>' +
                '<td><input type="number" step="any" class="form-control form-control-sm text-center modal-refmin border-primary" value="' + refMin + '" placeholder="Empty"></td>' +
                '<td><input type="number" step="any" class="form-control form-control-sm text-center modal-refmax border-danger" value="' + refMax + '" placeholder="Empty"></td>' +
                '<td><button type="button" class="btn btn-sm btn-outline-danger w-100" onclick="this.closest(\'tr\').remove()"><i class="fa-solid fa-trash"></i></button></td>';

        tr.innerHTML = html;
        document.getElementById('modalRangeBody').appendChild(tr);
    }

    // 4. LƯU TỪ MODAL XUỐNG THẺ HIDDEN
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
                alert("❌ Error in row " + (index + 1) + ": MAX value cannot be a negative!");
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


    function removeRow(button) {
        const row = button.closest('tr');
        if (document.querySelectorAll('#parameterTableBody tr').length > 1) {
            row.remove();
        } else {
            alert("Phải có ít nhất 1 chỉ số xét nghiệm!");
        }
    }


    // Hàm kiểm tra Min/Max trước khi Submit
    function validateLabTestParams(event) {
        // Lấy tất cả các ô Min và Max hiện có trên form
        const mins = document.getElementsByName("paramMin[]");
        const maxs = document.getElementsByName("paramMax[]");

        let isValid = true;

        for (let i = 0; i < mins.length; i++) {
            const minStr = mins[i].value.trim();
            const maxStr = maxs[i].value.trim();

            // Xóa cảnh báo đỏ (nếu có) từ lần kiểm tra trước
            mins[i].classList.remove("is-invalid");
            maxs[i].classList.remove("is-invalid");

            // CHỈ kiểm tra khi cả 2 ô đều có dữ liệu
            if (minStr !== "" && maxStr !== "") {
                const minVal = parseFloat(minStr);
                const maxVal = parseFloat(maxStr);

                // Nếu MIN >= MAX -> Báo lỗi!
                if (minVal >= maxVal) {
                    alert(`❌ Lỗi: Ngưỡng MAX (${maxVal}) phải lớn hơn ngưỡng MIN (${minVal})!`);

                    // Bôi đỏ 2 ô bị lỗi và focus con trỏ chuột vào đó
                    mins[i].classList.add("is-invalid");
                    maxs[i].classList.add("is-invalid");
                    maxs[i].focus();

                    isValid = false;
                    break; // Dừng vòng lặp, bắt sửa từng lỗi một
                }
            }
        }

        // Nếu có lỗi, chặn không cho form gửi về Server
        if (!isValid && event) {
            event.preventDefault();
        }

        return isValid;
    }

    // Tiện ích nhỏ: Khi người dùng bắt đầu gõ sửa lỗi, tự động xóa viền đỏ
    document.addEventListener('input', function (e) {
        if (e.target.name === 'paramMin[]' || e.target.name === 'paramMax[]') {
            e.target.classList.remove('is-invalid');
        }
    });
</script>