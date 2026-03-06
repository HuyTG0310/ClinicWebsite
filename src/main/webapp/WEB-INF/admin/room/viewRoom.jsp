<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<div class="container-fluid">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fas fa-door-open text-primary me-2"></i>
                Room Detail
            </h2>
            <p class="text-muted mb-0">
                View detailed information of examination room
            </p>
        </div>

        <div class="d-flex gap-2">

            <c:if test="${hasRoomEdit}">
                <button class="btn btn-warning"
                        onclick="openEditModal(${room.roomId})">
                    <i class="fas fa-edit me-2"></i>Edit
                </button>
            </c:if>

            <a href="${basePath}/room/list"
               class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-2"></i>Back to list
            </a>

        </div>

    </div>


    <c:choose>

        <c:when test="${not empty room}">

            <!-- ROOM INFORMATION -->
            <div class="card shadow-sm mb-4">

                <div class="card-header bg-white py-3">
                    <h5 class="mb-0">
                        <i class="fas fa-door-open text-primary me-2"></i>
                        Room Information
                    </h5>
                </div>

                <div class="card-body">

                    <div class="row mb-3">

                        <div class="col-md-6">
                            <label class="text-muted small">Room ID</label>
                            <div class="fw-semibold">
                                #${room.roomId}
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label class="text-muted small">Status</label>
                            <div>

                                <c:choose>

                                    <c:when test="${room.isActive}">
                                        <span class="badge bg-success">
                                            <i class="fas fa-check-circle me-1"></i>
                                            Active
                                        </span>
                                    </c:when>

                                    <c:otherwise>
                                        <span class="badge bg-danger">
                                            <i class="fas fa-times-circle me-1"></i>
                                            Inactive
                                        </span>
                                    </c:otherwise>

                                </c:choose>

                            </div>
                        </div>

                    </div>


                    <div class="mb-3">

                        <label class="text-muted small">Room Name</label>

                        <div class="fs-5 fw-medium">
                            ${room.roomName}
                        </div>

                    </div>


                    <div class="row">

                        <div class="col-md-6 mb-3">

                            <label class="text-muted small">Specialty</label>

                            <div>
                                <span class="badge bg-light text-dark border">
                                    <i class="fas fa-stethoscope me-1"></i>
                                    ${room.specialtyName}
                                </span>
                            </div>

                        </div>


                        <div class="col-md-6 mb-3">

                            <label class="text-muted small">Current Doctor</label>

                            <div>

                                <c:choose>

                                    <c:when test="${not empty room.doctorName}">

                                        <span class="badge bg-info text-dark">
                                            <i class="fas fa-user-md me-1"></i>
                                            ${room.doctorName}
                                        </span>

                                    </c:when>

                                    <c:otherwise>

                                        <span class="text-muted">
                                            <i class="fas fa-user-slash me-1"></i>
                                            N/A
                                        </span>

                                    </c:otherwise>

                                </c:choose>

                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </c:when>


        <c:otherwise>

            <div class="alert alert-danger shadow-sm">

                <i class="fas fa-exclamation-triangle me-2"></i>
                Room not found

            </div>

            <a href="${basePath}/room/list"
               class="btn btn-secondary">

                <i class="fas fa-arrow-left me-2"></i>
                Back to List

            </a>

        </c:otherwise>

    </c:choose>



    <!-- ===== EDIT MODAL ===== -->

    <div class="modal fade" id="editModal" tabindex="-1">

        <div class="modal-dialog">

            <form class="modal-content"
                  method="post"
                  action="${basePath}/room/edit">

                <div class="modal-header">

                    <h5 class="modal-title">
                        <i class="fas fa-edit text-primary me-2"></i>
                        Edit Room
                    </h5>

                    <button type="button"
                            class="btn-close"
                            data-bs-dismiss="modal"></button>

                </div>


                <div class="modal-body">

                    <input type="hidden"
                           id="editRoomId"
                           name="roomId">


                    <div class="mb-3">

                        <label class="form-label fw-semibold">
                            Room Name
                        </label>

                        <input id="editRoomName"
                               name="roomName"
                               class="form-control"
                               required>

                    </div>


                    <div class="mb-3">

                        <label class="form-label fw-semibold">
                            Specialty
                        </label>

                        <select id="editSpecialty"
                                name="specialtyId"
                                class="form-select"
                                onchange="onEditSpecialtyChange(this)">

                            <c:forEach var="s" items="${specialties}">

                                <option value="${s.specialtyId}">
                                    ${s.name}
                                </option>

                            </c:forEach>

                        </select>

                    </div>


                    <div class="mb-3">

                        <label class="form-label fw-semibold">
                            Doctor
                        </label>

                        <select id="editDoctor"
                                name="currentDoctorId"
                                class="form-select">

                            <option value="">
                                -- Select Doctor --
                            </option>

                        </select>

                    </div>


                    <div class="mb-3">

                        <label class="form-label fw-semibold">
                            Status
                        </label>

                        <select id="editIsActive"
                                name="isActive"
                                class="form-select">

                            <option value="true">
                                🟢 Active
                            </option>

                            <option value="false">
                                🔴 Inactive
                            </option>

                        </select>

                    </div>

                </div>


                <div class="modal-footer">

                    <button class="btn btn-secondary"
                            data-bs-dismiss="modal">

                        <i class="fas fa-times me-2"></i>
                        Cancel

                    </button>

                    <button class="btn btn-primary">

                        <i class="fas fa-save me-2"></i>
                        Save

                    </button>

                </div>

            </form>

        </div>

    </div>


</div>


<c:if test="${not empty param.editError}">
    <script>
        document.addEventListener("DOMContentLoaded", () => {
            openEditModal(${room.roomId});
        });
    </script>
</c:if>


<style>

    .card{
        border-radius:12px;
        border:none;
    }

    .card-header{
        border-bottom:1px solid rgba(0,0,0,0.08);
        border-radius:12px 12px 0 0 !important;
    }

    .badge{
        padding:0.45em 0.9em;
        font-weight:500;
        border-radius:6px;
    }

    .btn{
        border-radius:8px;
        font-weight:500;
        transition:all 0.25s ease;
    }

    .btn:hover{
        transform:translateY(-2px);
        box-shadow:0 4px 12px rgba(0,0,0,0.15);
    }

</style>







<script>
    function openEditModal(roomId) {

        fetch(`${pageContext.request.contextPath}/api/room-detail?id=` + roomId)
                .then(res => res.json())
                .then(room => {

                    // ===== SET BASIC DATA =====
                    document.querySelector('#editRoomId').value = room.roomId;
                    document.querySelector('#editRoomName').value = room.roomName;

                    // ===== SET SPECIALTY =====
                    const specialtySelect = document.querySelector('#editSpecialty');
                    specialtySelect.value = String(room.specialtyId);

                    // ===== SET STATUS (THEO SPECIALTY HIỆN TẠI) =====
                    renderStatusSelect(room.specialtyActive, room.isActive);

                    // ===== LOAD DOCTORS =====
                    loadDoctorsForEdit(room.specialtyId, room.currentDoctorId);

                    new bootstrap.Modal(
                            document.getElementById("editModal")
                            ).show();
                });
    }

    // ==============================
    // LOAD DOCTORS THEO SPECIALTY
    // ==============================
    function loadDoctorsForEdit(specialtyId, selectedDoctorId) {

        fetch(`${pageContext.request.contextPath}/api/doctors-by-specialty?specialtyId=` + specialtyId)
                .then(res => res.json())
                .then(doctors => {

                    const select = document.querySelector('#editDoctor');
                    select.innerHTML = '<option value="">-- Select Doctor --</option>';

                    doctors.forEach(d => {
                        const opt = document.createElement("option");
                        opt.value = d.userId;
                        opt.textContent = d.fullName;

                        if (String(d.userId) === String(selectedDoctorId)) {
                            opt.selected = true;
                        }
                        select.appendChild(opt);
                    });
                });
    }

    // ==============================
    // RENDER STATUS SELECT (CHUẨN)
    // ==============================
    function renderStatusSelect(specialtyActive, roomIsActive) {

        const statusSelect = document.querySelector('#editIsActive');
        statusSelect.innerHTML = '';

        if (!specialtyActive) {
            // ❌ SPECIALTY INACTIVE → ROOM BẮT BUỘC INACTIVE
            const opt = document.createElement('option');
            opt.value = 'false';
            opt.textContent = 'Inactive (Specialty disabled)';
            opt.selected = true;
            statusSelect.appendChild(opt);
        } else {
            // ✅ SPECIALTY ACTIVE → ROOM CÓ 2 OPTION
            const activeOpt = document.createElement('option');
            activeOpt.value = 'true';
            activeOpt.textContent = '🟢 Active';

            const inactiveOpt = document.createElement('option');
            inactiveOpt.value = 'false';
            inactiveOpt.textContent = '🔴 Inactive';

            statusSelect.appendChild(activeOpt);
            statusSelect.appendChild(inactiveOpt);

            statusSelect.value = String(roomIsActive);
        }
    }

    // ==============================
    // KHI ADMIN ĐỔI SPECIALTY
    // ==============================
    function onEditSpecialtyChange(select) {
        console.log('🔥 onEditSpecialtyChange fired');
        console.log('specialtyId =', select.value);
        const specialtyId = select.value;

        // 1️⃣ Load doctors
        loadDoctorsForEdit(specialtyId, null);

        // 2️⃣ Load trạng thái specialty mới
        fetch(`${pageContext.request.contextPath}/api/specialty-status?id=` + specialtyId)
                .then(res => res.json())
                .then(data => {

                    // ⚠️ RESET TRẠNG THÁI ROOM THEO SPECIALTY MỚI
                    if (!data.isActive) {
                        renderStatusSelect(false, false); // chỉ Inactive
                    } else {
                        renderStatusSelect(true, true);   // mặc định Active
                    }
                });
    }
</script>