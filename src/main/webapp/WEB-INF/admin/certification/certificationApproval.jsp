<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Certification Approval</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">

        <style>
            body{
                font-family:'Poppins',sans-serif;
                background:linear-gradient(135deg,#0d6efd,#0dcaf0);
                min-height:100vh;
                padding:40px;
            }
            .table-card{
                background:white;
                border-radius:20px;
                padding:35px;
                box-shadow:0 20px 40px rgba(0,0,0,0.15);
            }
            .preview-img{
                width:60px;
                height:60px;
                object-fit:cover;
                border-radius:6px;
                border:1px solid #ddd;
            }
            .tooltip-inner {
                max-width: 200px;
                padding: 8px 12px;
                background-color: #333;
                border-radius: 8px;
            }
            .role-badge {
                font-size: 0.75rem;
                text-transform: uppercase;
                letter-spacing: 1px;
            }
        </style>
    </head>

    <body>

        <div class="container">
            <div class="table-card">

                <!-- HEADER -->
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h3>
                        <i class="fa-solid fa-certificate text-primary me-2"></i>
                        Certification Approval
                    </h3>

                    <a href="${pageContext.request.contextPath}/admin/dashboard" 
                       class="btn btn-outline-primary">
                        <i class="fa-solid fa-arrow-left me-1"></i> Back Dashboard
                    </a>
                </div>

                <!-- 🔥 SEARCH BAR (GIỐNG ROLE UI) -->
                <div class="card shadow-sm mb-4">
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/admin/certification/search" method="GET">

                            <div class="row g-3">

                                <!-- NAME -->
                                <div class="col-md-6">
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
                                               placeholder="Nhập tên hoặc chứng chỉ..." required>
                                    </div>
                                </div>

                                <!-- PHONE -->
                                <div class="col-md-4">
                                    <label class="form-label fw-bold small text-muted">
                                        Phone Number
                                    </label>
                                    <input type="text" id="searchPhone" name="searchPhone" 
                                           class="form-control" 
                                           value="${param.searchPhone}"
                                           placeholder="Enter phone number">

                                    <!-- 🔥 ERROR realtime -->
                                    <small id="phoneError" class="text-danger d-none">
                                        Số điện thoại chỉ được chứa chữ số!
                                    </small>

                                    <!-- 🔥 ERROR từ server -->
                                    <c:if test="${not empty error}">
                                        <div class="text-danger mt-1">
                                            ${error}
                                        </div>
                                    </c:if>
                                </div>

                                <!-- BUTTON -->
                                <div class="col-md-2 d-flex align-items-end gap-2">
                                    <button class="btn btn-primary flex-grow-1">
                                        <i class="fas fa-filter me-2"></i>Search
                                    </button>

                                    <c:if test="${not empty param.searchName}">
                                        <a href="${pageContext.request.contextPath}/admin/certification/list"
                                           class="btn btn-outline-secondary px-3"
                                           title="Clear Filters">
                                            <i class="fas fa-redo-alt"></i>
                                        </a>
                                    </c:if>
                                </div>

                            </div>
                        </form>
                    </div>
                </div>

                <!-- TABLE -->
                <table class="table table-striped align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>Owner</th>
                            <th>Role</th>
                            <th>Certificate</th>
                            <th>Number</th>
                            <th>Issue Date</th>
                            <th>Expiry Date</th>
                            <th>Status</th>
                            <th>File</th>
                            <th>Actions</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach items="${list}" var="c">
                            <tr>
                                <td>
                                    <div class="fw-bold">
                                        ${not empty c.fullName ? c.fullName : 'User ID: '.concat(c.userId)}
                                    </div>
                                    <div class="text-muted small">${c.phoneNumber}</div>
                                </td>

                                <td>
                                    <span class="badge bg-info text-dark role-badge">
                                        ${not empty c.roleName ? c.roleName : 'N/A'}
                                    </span>
                                </td>

                                <td>${c.certificateName}</td>
                                <td>${c.certificateNumber}</td>
                                <td>${c.issueDate}</td>
                                <td>${c.expiryDate}</td>

                                <td>
                                    <c:choose>
                                        <c:when test="${c.status == 'VERIFIED'}">
                                            <span class="badge bg-success">Verified</span>
                                        </c:when>
                                        <c:when test="${c.status == 'PENDING'}">
                                            <span class="badge bg-warning text-dark">Pending</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger"
                                                  data-bs-toggle="tooltip"
                                                  title="Lý do: ${not empty c.rejectionNote ? c.rejectionNote : 'N/A'}">
                                                Rejected <i class="fa-solid fa-circle-info ms-1"></i>
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <c:if test="${not empty c.filePath}">
                                        <a href="${pageContext.request.contextPath}/certification/file?name=${c.filePath}" 
                                           target="_blank"
                                           class="btn btn-sm btn-outline-primary">
                                            <i class="fa-solid fa-eye"></i>
                                        </a>
                                    </c:if>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${c.status == 'PENDING'}">
                                            <a href="${pageContext.request.contextPath}/admin/certification/approve?id=${c.certificationId}"
                                               class="btn btn-success btn-sm">
                                                <i class="fa-solid fa-check"></i>
                                            </a>

                                            <button class="btn btn-danger btn-sm"
                                                    onclick="openRejectModal('${c.certificationId}')">
                                                <i class="fa-solid fa-xmark"></i>
                                            </button>
                                        </c:when>

                                        <c:otherwise>
                                            <button class="btn btn-outline-danger btn-sm"
                                                    onclick="openDeleteModal('${c.certificationId}')">
                                                <i class="fa-solid fa-trash"></i> Delete
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty list}">
                            <tr>
                                <td colspan="9" class="text-center text-muted py-4">
                                    Không tìm thấy dữ liệu nào.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>

            </div>
        </div>

        <!-- REJECT MODAL -->
        <div class="modal fade" id="rejectModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form id="rejectForm">
                        <div class="modal-header bg-danger text-white">
                            <h5 class="modal-title">Reject Certification</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">
                            <input type="hidden" name="id" id="rejectId">
                            <textarea name="rejectionNote" id="rejectionNote" 
                                      class="form-control" rows="4" required></textarea>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="button" class="btn btn-danger" onclick="submitReject()">Xác nhận</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- DELETE MODAL -->
        <div class="modal fade" id="deleteModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <form method="post" action="${pageContext.request.contextPath}/admin/certification/delete">
                        <input type="hidden" name="id" id="deleteId">

                        <div class="modal-header bg-dark text-white">
                            <h5>Xác nhận xóa</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body text-center">
                            <h5 class="text-danger">Bạn có chắc chắn?</h5>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button class="btn btn-danger">Xóa</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

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
    </body>
</html>