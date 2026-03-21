<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<div class="container-fluid mb-5">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-certificate text-primary me-2"></i>
                My Certifications
            </h2>
            <p class="text-muted mb-0">
                Manage your certifications (Lab Staff)
            </p>
        </div>

        <div>
            <a href="${basePath}/certification/add"
               class="btn btn-primary">
                <i class="fa-solid fa-plus me-2"></i>Add Certification
            </a>
        </div>

    </div>

    <!-- TABLE -->
    <div class="card shadow-sm">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-list-check text-primary me-2"></i>
                Certification List
            </h5>
        </div>

        <div class="card-body p-0">

            <div class="table-responsive">

                <table class="table table-hover align-middle mb-0">

                    <thead class="table-light">
                        <tr>
                            <th>Certificate</th>
                            <th>Number</th>
                            <th>Issue Date</th>
                            <th>Expiry Date</th>
                            <th>Status</th>
                            <th>File</th>
                            <th class="text-center">Actions</th>
                        </tr>
                    </thead>

                    <tbody>

                        <c:forEach items="${list}" var="c">

                            <tr>

                                <td class="fw-semibold">
                                    ${c.certificateName}
                                </td>

                                <td>
                                    ${c.certificateNumber}
                                </td>

                                <td>
                                    ${c.issueDate}
                                </td>

                                <td>
                                    ${c.expiryDate}
                                </td>

                                <td>
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

                                <td>
                                    <c:if test="${not empty c.filePath}">
                                        <c:set var="fileUrl" value="${pageContext.request.contextPath}/certification/file?name=${c.filePath}" />

                                        <c:choose>

                                            <c:when test="${fn:endsWith(c.filePath,'.jpg') or fn:endsWith(c.filePath,'.jpeg') or fn:endsWith(c.filePath,'.png')}">
                                                <a href="${fileUrl}" target="_blank">
                                                    <img src="${fileUrl}"
                                                         style="width:50px;height:50px;object-fit:cover;border-radius:6px;border:1px solid #dee2e6;">
                                                </a>
                                            </c:when>

                                            <c:otherwise>
                                                <a href="${fileUrl}" target="_blank"
                                                   class="btn btn-sm btn-outline-primary">
                                                    <i class="fa-solid fa-eye me-1"></i>View
                                                </a>
                                            </c:otherwise>

                                        </c:choose>
                                    </c:if>
                                </td>

                                <td class="text-center">

                                    <c:choose>

                                        <c:when test="${c.status == 'PENDING'}">

                                            <button class="btn btn-warning btn-sm me-1"
                                                    onclick="openEditModal('${c.certificationId}', '${c.certificateName}', '${c.certificateNumber}', '${fn:substring(c.issueDate,0,10)}', '${fn:substring(c.expiryDate,0,10)}')">
                                                <i class="fa-solid fa-pen"></i>
                                            </button>

                                            <button class="btn btn-danger btn-sm"
                                                    onclick="openDeleteModal('${c.certificationId}')">
                                                <i class="fa-solid fa-trash"></i>
                                            </button>

                                        </c:when>

                                        <c:otherwise>
                                            <span class="text-muted">
                                                <i class="fa-solid fa-lock me-1"></i>Locked
                                            </span>
                                        </c:otherwise>

                                    </c:choose>

                                </td>

                            </tr>

                        </c:forEach>

                        <c:if test="${empty list}">
                            <tr>
                                <td colspan="7" class="text-center py-5 text-muted">
                                    <i class="fa-solid fa-box-open fs-1 mb-2 d-block"></i>
                                    No certifications uploaded yet.
                                </td>
                            </tr>
                        </c:if>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>


<div class="modal fade" id="editModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">

            <form id="editCertForm" method="post" enctype="multipart/form-data"
                  action="${basePath}/certification/edit">

                <div class="modal-header">
                    <h5 class="modal-title">Edit Certification</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">

                    <div id="ajaxError" class="alert alert-danger d-none"></div>

                    <input type="hidden" name="id" id="editId">

                    <div class="mb-3">
                        <label>Name</label>
                        <input type="text" id="editName" name="certificateName" class="form-control">
                    </div>

                    <div class="mb-3">
                        <label>Number</label>
                        <input type="text" id="editNumber" name="certificateNumber" class="form-control">
                    </div>

                    <div class="mb-3">
                        <label>Issue Date</label>
                        <input type="date" id="editIssue" name="issueDate" class="form-control">
                    </div>

                    <div class="mb-3">
                        <label>Expiry Date</label>
                        <input type="date" id="editExpiry" name="expiryDate" class="form-control">
                    </div>

                    <div class="mb-3">
                        <label>File</label>
                        <input type="file" name="certificateFile" class="form-control">
                    </div>

                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" id="saveBtn" class="btn btn-primary" onclick="submitEditForm()">Save</button>
                </div>

            </form>

        </div>
    </div>
</div>         



<div class="modal fade" id="deleteModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">

            <form method="post" action="${basePath}/certification/delete">

                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">Confirm Delete</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body text-center">
                    <input type="hidden" name="id" id="deleteId">
                    <p>Are you sure you want to delete?</p>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button class="btn btn-danger">Delete</button>
                </div>

            </form>

        </div>
    </div>
</div>       

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    });
</script>

<script>
    function openEditModal(id, name, number, issue, expiry) {
        document.getElementById("editId").value = id;
        document.getElementById("editName").value = name;
        document.getElementById("editNumber").value = number;
        document.getElementById("editIssue").value = issue;
        document.getElementById("editExpiry").value = expiry;

        // Reset vùng thông báo lỗi
        const errorDiv = document.getElementById("ajaxError");
        errorDiv.classList.add("d-none");
        errorDiv.innerText = "";

        var modal = new bootstrap.Modal(document.getElementById('editModal'));
        modal.show();
    }

    function openDeleteModal(id) {
        document.getElementById("deleteId").value = id;
        var modal = new bootstrap.Modal(document.getElementById('deleteModal'));
        modal.show();
    }

    function submitEditForm() {
        const form = document.getElementById('editCertForm');
        const formData = new FormData(form);
        const saveBtn = document.getElementById('saveBtn');
        const errorDiv = document.getElementById('ajaxError');

        // Hiệu ứng chờ
        saveBtn.disabled = true;
        saveBtn.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Saving...';
        errorDiv.classList.add("d-none");

        fetch(form.action, {
            method: 'POST',
            body: formData
        })
                .then(async response => {
                    const message = await response.text();

                    if (response.ok && message === "SUCCESS") {
                        // Thành công: Tải lại trang
                        window.location.reload();
                    } else {
                        // Thất bại: Hiện thông báo lỗi từ Servlet (Status 400)
                        errorDiv.innerText = message || "An error occurred. Please try again.";
                        errorDiv.classList.remove("d-none");
                        saveBtn.disabled = false;
                        saveBtn.innerHTML = 'Save Changes';
                    }
                })
                .catch(err => {
                    console.error(err);
                    alert("System connection error!");
                    saveBtn.disabled = false;
                    saveBtn.innerHTML = 'Save Changes';
                });
    }
</script>
