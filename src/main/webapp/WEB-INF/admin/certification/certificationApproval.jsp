<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<div class="container-fluid">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-certificate text-primary me-2"></i>
                Manage Certification
            </h2>
            <p class="text-muted mb-0">
                Review and approve user certifications
            </p>
        </div>

    </div>



    <!-- FILTER -->
    <div class="card shadow-sm mb-4 border-0">
        <div class="card-body">

            <form action="${pageContext.request.contextPath}/admin/certification/search" method="GET">

                <div class="row g-3 align-items-end">

                    <!-- NAME -->
                    <div class="col-md-5">
                        <label class="form-label fw-bold small text-muted">
                            Full Name / Certificate
                        </label>
                        <div class="input-group">
                            <span class="input-group-text bg-white">
                                <i class="fas fa-search text-muted"></i>
                            </span>
                            <input type="text"
                                   name="searchName"
                                   class="form-control"
                                   value="${param.searchName}"
                                   placeholder="Search name or certificate..." required>
                        </div>
                    </div>

                    <!-- PHONE -->
                    <div class="col-md-4">
                        <label class="form-label fw-bold small text-muted">
                            Phone Number
                        </label>
                        <input type="text"
                               id="searchPhone"
                               name="searchPhone"
                               class="form-control"
                               value="${param.searchPhone}"
                               placeholder="Enter phone number">

                        <small id="phoneError" class="text-danger d-none">
                            Số điện thoại chỉ được chứa chữ số!
                        </small>

                        <c:if test="${not empty error}">
                            <div class="text-danger mt-1">${error}</div>
                        </c:if>
                    </div>

                    <!-- BUTTON -->
                    <div class="col-md-3 d-flex gap-2">
                        <button class="btn btn-primary flex-grow-1">
                            <i class="fas fa-search me-2"></i>Search
                        </button>

                        <c:if test="${not empty param.searchName}">
                            <a href="${pageContext.request.contextPath}/admin/certification/list"
                               class="btn btn-outline-secondary"
                               title="Clear">
                                <i class="fas fa-redo-alt"></i>
                            </a>
                        </c:if>
                    </div>

                </div>

            </form>

        </div>
    </div>



    <!-- TABLE -->
    <div class="card shadow-sm border-0">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0 fw-bold">
                <i class="fas fa-list me-2 text-primary"></i>
                Certification List
            </h5>
        </div>

        <div class="card-body p-0">

            <div class="table-responsive">

                <table class="table table-hover align-middle mb-0">

                    <thead class="table-light">
                        <tr>
                            <th>Owner</th>
                            <th>Role</th>
                            <th>Certificate</th>
                            <th>Number</th>
                            <th>Issue Date</th>
                            <th>Expiry Date</th>
                            <th class="text-center">Status</th>
                            <th class="text-center">File</th>
                            <th class="text-center">Actions</th>
                        </tr>
                    </thead>

                    <tbody>

                        <c:forEach items="${list}" var="c">
                            <tr>

                                <!-- OWNER -->
                                <td>
                                    <div class="fw-bold text-primary">
                                        ${not empty c.fullName ? c.fullName : 'User ID: '.concat(c.userId)}
                                    </div>
                                    <div class="small text-muted">${c.phoneNumber}</div>
                                </td>

                                <!-- ROLE -->
                                <td>
                                    <span class="badge bg-info text-dark">
                                        ${not empty c.roleName ? c.roleName : 'N/A'}
                                    </span>
                                </td>

                                <td>${c.certificateName}</td>
                                <td>${c.certificateNumber}</td>
                                <td>${c.issueDate}</td>
                                <td>${c.expiryDate}</td>

                                <!-- STATUS -->
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${c.status == 'VERIFIED'}">
                                            <span class="badge bg-success">
                                                <i class="fa-solid fa-check me-1"></i>Verified
                                            </span>
                                        </c:when>
                                        <c:when test="${c.status == 'PENDING'}">
                                            <span class="badge bg-warning text-dark">
                                                <i class="fa-solid fa-clock me-1"></i>Pending
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger"
                                                  data-bs-toggle="tooltip"
                                                  title="Reason: ${not empty c.rejectionNote ? c.rejectionNote : 'N/A'}">
                                                <i class="fa-solid fa-ban me-1"></i>Rejected
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <!-- FILE -->
                                <td class="text-center">
                                    <c:if test="${not empty c.filePath}">
                                        <a href="${pageContext.request.contextPath}/certification/file?name=${c.filePath}"
                                           target="_blank"
                                           class="btn btn-sm btn-outline-primary">
                                            <i class="fa-solid fa-eye me-2"></i>View
                                        </a>
                                    </c:if>
                                </td>

                                <!-- ACTION -->
                                <td class="text-center">

                                    <c:choose>

                                        <c:when test="${c.status == 'PENDING'}">

                                            <a href="${pageContext.request.contextPath}/admin/certification/approve?id=${c.certificationId}"
                                               class="btn btn-sm btn-success">
                                                <i class="fa-solid fa-check"></i>
                                            </a>

                                            <button class="btn btn-sm btn-danger"
                                                    onclick="openRejectModal('${c.certificationId}')">
                                                <i class="fa-solid fa-xmark"></i>
                                            </button>

                                        </c:when>

                                        <c:otherwise>

                                            <button class="btn btn-sm btn-outline-danger"
                                                    onclick="openDeleteModal('${c.certificationId}')">
                                                <i class="fa-solid fa-trash me-2"></i>Delete
                                            </button>

                                        </c:otherwise>

                                    </c:choose>

                                </td>

                            </tr>
                        </c:forEach>



                        <!-- EMPTY -->
                        <c:if test="${empty list}">
                            <tr>
                                <td colspan="9" class="text-center py-5 text-muted">
                                    <i class="fa-solid fa-inbox fa-3x mb-3 d-block opacity-25"></i>
                                    No certifications found
                                </td>
                            </tr>
                        </c:if>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>

<!-- DELETE MODAL -->
<div class="modal fade" id="deleteModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">

            <form method="post"
                  action="${pageContext.request.contextPath}/admin/certification/delete">

                <input type="hidden"
                       name="id"
                       id="deleteId">

                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">
                        <i class="fa-solid fa-triangle-exclamation me-2"></i>
                        Confirm Delete
                    </h5>
                    <button type="button"
                            class="btn-close btn-close-white"
                            data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body text-center">

                    <p class="mb-2">
                        Are you sure you want to delete this certification?
                    </p>

                    <div class="alert alert-danger mb-0">
                        <i class="fa-solid fa-circle-exclamation me-2"></i>
                        This action cannot be undone.
                    </div>

                </div>

                <div class="modal-footer">

                    <button type="button"
                            class="btn btn-outline-secondary"
                            data-bs-dismiss="modal">
                        Cancel
                    </button>

                    <button type="submit"
                            class="btn btn-danger px-4">
                        <i class="fa-solid fa-trash me-1"></i>
                        Delete
                    </button>

                </div>

            </form>

        </div>
    </div>
</div>
                  
        <div class="modal fade" id="rejectModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered"> <!-- FIX CHỖ NÀY -->
        <div class="modal-content">

            <form id="rejectForm">

                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">
                        <i class="fa-solid fa-xmark me-2"></i>
                        Reject Certification
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">

                    <input type="hidden" name="id" id="rejectId">

                    <div class="mb-2 fw-semibold">
                        Reason for rejection
                    </div>

                    <textarea name="rejectionNote"
                              id="rejectionNote"
                              class="form-control"
                              rows="4"
                              required></textarea>

                </div>

                <div class="modal-footer">
                    <button type="button"
                            class="btn btn-outline-secondary"
                            data-bs-dismiss="modal">
                        Cancel
                    </button>

                    <button type="button"
                            class="btn btn-danger"
                            onclick="submitReject()">
                        Reject
                    </button>
                </div>

            </form>

        </div>
    </div>
</div>


<script>
    document.addEventListener('DOMContentLoaded', () => {
        [...document.querySelectorAll('[data-bs-toggle="tooltip"]')]
                .map(el => new bootstrap.Tooltip(el));
    });

    function openRejectModal(id) {
        document.getElementById("rejectId").value = id;
        new bootstrap.Modal(document.getElementById('rejectModal')).show();
    }

    function openDeleteModal(id) {
        document.getElementById("deleteId").value = id;
        new bootstrap.Modal(document.getElementById('deleteModal')).show();
    }

    function submitReject() {
        const id = document.getElementById("rejectId").value;
        const note = document.getElementById("rejectionNote").value;

        if (!note.trim())
            return alert("Nhập lý do!");

        fetch('${pageContext.request.contextPath}/admin/certification/reject', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: "id=" + id + "&rejectionNote=" + note
        }).then(r => r.text()).then(msg => {
            if (msg === "SUCCESS")
                location.reload();
            else
                alert(msg);
        });
    }
</script>
<script>
    const phoneInput = document.getElementById("searchPhone");
    const phoneError = document.getElementById("phoneError");

    phoneInput.addEventListener("input", function () {
        let value = this.value;

        // ❌ Nếu có ký tự không phải số → hiện lỗi
        if (!/^\d*$/.test(value)) {
            phoneError.classList.remove("d-none");
        } else {
            phoneError.classList.add("d-none");
        }

        // 🔥 Tự động xóa ký tự không phải số
        this.value = value.replace(/\D/g, '');
    });
</script>
