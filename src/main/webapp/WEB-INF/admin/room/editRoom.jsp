
<%--

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- HEADER -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fas fa-door-open text-primary me-2"></i>
            Chi ti?t phòng
        </h2>
        <p class="text-muted mb-0">
            Thông tin chi ti?t c?a phòng khám trong h? th?ng
        </p>
    </div>

    <div class="d-flex gap-2">
        <a href="RoomList"
           class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left me-1"></i>
            Quay l?i
        </a>

        <button class="btn btn-primary"
                data-bs-toggle="modal"
                data-bs-target="#editRoomModal">
            <i class="fas fa-pen-to-square me-1"></i>
            Ch?nh s?a
        </button>
    </div>
</div>

<c:choose>
    <c:when test="${not empty room}">

        <!-- DETAIL CARD -->
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <div class="row g-4">

                    <div class="col-md-6">
                        <label class="text-muted">ID</label>
                        <div class="fw-semibold">
                            ${room.roomId}
                        </div>
                    </div>

                    <div class="col-md-6">
                        <label class="text-muted">Tên phòng</label>
                        <div class="fw-semibold">
                            ${room.roomName}
                        </div>
                    </div>

                    <div class="col-md-6">
                        <label class="text-muted">Chuyên khoa</label>
                        <div>
                            <span class="badge bg-light text-dark border">
                                <i class="fas fa-stethoscope me-1"></i>
                                ${room.specialtyId}
                            </span>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <label class="text-muted">Bác s? hi?n t?i</label>
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
                                        <i class="fas fa-user-slash me-1"></i>
                                        Ch?a có
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <label class="text-muted">Tr?ng thái</label>
                        <div>
                            <c:choose>
                                <c:when test="${room.isActive}">
                                    <span class="badge bg-success">
                                        <i class="fas fa-check-circle me-1"></i>
                                        Ho?t ??ng
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger">
                                        <i class="fas fa-times-circle me-1"></i>
                                        Ng?ng ho?t ??ng
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
            Không tìm th?y phòng
        </div>
        <a href="RoomList" class="btn btn-secondary">
            <i class="fas fa-arrow-left me-2"></i>Quay l?i
        </a>
    </c:otherwise>
</c:choose>

<!-- EDIT MODAL -->
<div class="modal fade" id="editRoomModal" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">

            <div class="modal-header">
                <h5 class="modal-title">
                    <i class="fas fa-edit me-2"></i>
                    Ch?nh s?a phòng
                </h5>
                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"></button>
            </div>

            <form method="post" action="UpdateRoom">

                <div class="modal-body">

                    <input type="hidden" name="roomId"
                           value="${room.roomId}" />

                    <div class="row g-3">

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">
                                Tên phòng
                            </label>
                            <input type="text"
                                   name="roomName"
                                   value="${room.roomName}"
                                   class="form-control"
                                   required />
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">
                                Chuyên khoa
                            </label>
                            <input type="number"
                                   name="specialtyId"
                                   value="${room.specialtyId}"
                                   class="form-control" />
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">
                                Bác s? hi?n t?i
                            </label>
                            <input type="number"
                                   name="currentDoctorId"
                                   value="${room.currentDoctorId}"
                                   class="form-control" />
                        </div>

                        <div class="col-md-6 d-flex align-items-center">
                            <div class="form-check form-switch mt-4">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isActive"
                                       <c:if test="${room.isActive}">checked</c:if>>
                                <label class="form-check-label">
                                    Ho?t ??ng
                                </label>
                            </div>
                        </div>

                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button"
                            class="btn btn-outline-secondary"
                            data-bs-dismiss="modal">
                        H?y
                    </button>

                    <button type="submit"
                            class="btn btn-primary px-4">
                        <i class="fas fa-save me-1"></i>
                        L?u
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>

                                
 Comment JSP --%>
