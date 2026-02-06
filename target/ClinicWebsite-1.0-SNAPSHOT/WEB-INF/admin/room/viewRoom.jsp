<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<div class="container-fluid">

    <!-- HEADER -->
    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="mb-1">
                <i class="fas fa-door-open text-primary me-2"></i>
                Room Detail
            </h2>
            <p class="text-muted mb-0">View detailed information of examination room</p>
        </div>

        <a href="RoomList" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left me-2"></i>Back to List
        </a>
    </div>


    <c:choose>
        <c:when test="${not empty room}">

            <!-- DETAIL CARD -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white py-3">
                    <h5 class="mb-0">
                        <i class="fas fa-info-circle text-primary me-2"></i>
                        Room Information
                    </h5>
                </div>

                <div class="card-body">

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="text-muted small">Room ID</label>
                            <div class="fw-semibold">#${room.roomId}</div>
                        </div>

                        <div class="col-md-6">
                            <label class="text-muted small">Status</label>
                            <div>
                                <c:choose>
                                    <c:when test="${room.isActive}">
                                        <span class="badge bg-success">
                                            <i class="fas fa-check-circle me-1"></i>Active
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">
                                            <i class="fas fa-times-circle me-1"></i>Inactive
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="text-muted small">Room Name</label>
                        <div class="fs-5 fw-medium">${room.roomName}</div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="text-muted small">Specialty ID</label>
                            <div>
                                <span class="badge bg-light text-dark border">
                                    <i class="fas fa-stethoscope me-1"></i>
                                    ${room.specialtyId}
                                </span>
                            </div>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label class="text-muted small">Current Doctor</label>
                            <div>
                                <c:choose>
                                    <c:when test="${room.currentDoctorId != null}">
                                        <span class="badge bg-info">
                                            <i class="fas fa-user-md me-1"></i>
                                            ${room.currentDoctorId}
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">
                                            <i class="fas fa-user-slash me-1"></i>N/A
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

            <!-- ACTION BUTTON -->
            <div class="d-flex gap-2">
                <a href="RoomList" class="btn btn-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Back
                </a>

                <button class="btn btn-warning"
                        onclick="openEditModal(
                                        '${room.roomId}',
                                        '${room.roomName}',
                                        '${room.specialtyId}',
                                        '${room.currentDoctorId}',
                                        '${room.isActive}'
                                        )">
                    <i class="fas fa-edit me-2"></i>Edit Room
                </button>

                <button class="btn btn-danger"
                        onclick="openDeleteModal(${room.roomId}, '${room.roomName}')">
                    <i class="fas fa-trash me-2"></i>Delete Room
                </button>

            </div>

        </c:when>

        <c:otherwise>
            <div class="alert alert-danger shadow-sm">
                <i class="fas fa-exclamation-triangle me-2"></i>
                Room not found
            </div>
            <a href="RoomList" class="btn btn-secondary">
                <i class="fas fa-arrow-left me-2"></i>Back to List
            </a>
        </c:otherwise>
    </c:choose>

    <!-- ===== EDIT MODAL ===== -->
    <div class="modal fade" id="editModal" tabindex="-1">
        <div class="modal-dialog">
            <form class="modal-content" method="post" action="${pageContext.request.contextPath}/EditRoom">

                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-edit text-primary me-2"></i>
                        Edit Room
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">

                    <input type="hidden" name="roomId">

                    <div class="mb-3">
                        <label class="form-label">Room Name</label>
                        <input type="text" class="form-control" name="roomName" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Specialty ID</label>
                        <input type="number" class="form-control" name="specialtyId">
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Current Doctor ID</label>
                        <input type="number" class="form-control" name="currentDoctorId">
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Status</label>
                        <select name="isActive" class="form-select">
                            <option value="true">🟢 Active</option>
                            <option value="false">🔴 Inactive</option>
                        </select>
                    </div>

                </div>

                <div class="modal-footer">
                    <button class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Cancel
                    </button>
                    <button class="btn btn-primary">
                        <i class="fas fa-save me-2"></i>Save
                    </button>
                </div>

            </form>
        </div>
    </div>

    <!-- ===== DELETE MODAL ===== -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-exclamation-triangle me-2"></i>
                        Confirm Delete
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete this room?</p>
                    <p><strong>Room Name:</strong> <span id="deleteRoomName"></span></p>
                    <p><strong>Room ID:</strong> #<span id="deleteRoomIdDisplay"></span></p>
                    <p class="text-danger"><strong>Note:</strong> This action cannot be undone!</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Cancel
                    </button>
                    <form method="post" action="DeleteRoom" style="display: inline;">
                        <input type="hidden" name="id" id="deleteRoomId">
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash me-2"></i>Delete Room
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function openEditModal(id, name, specialtyId, doctorId, active) {

        document.querySelector('input[name="roomId"]').value = id;
        document.querySelector('input[name="roomName"]').value = name;
        document.querySelector('input[name="specialtyId"]').value = specialtyId;
        document.querySelector('input[name="currentDoctorId"]').value = doctorId ?? '';
        document.querySelector('select[name="isActive"]').value = active;

        new bootstrap.Modal(
                document.getElementById("editModal")
                ).show();
    }

    function openDeleteModal(roomId, roomName) {
        document.getElementById('deleteRoomId').value = roomId;
        document.getElementById('deleteRoomIdDisplay').textContent = roomId;
        document.getElementById('deleteRoomName').textContent = roomName;
        new bootstrap.Modal(document.getElementById("deleteModal")).show();
    }
</script>

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


