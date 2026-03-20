<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="UTF-8">
        <title>My Certifications - Lab</title>

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
                padding:30px;
                box-shadow:0 20px 40px rgba(0,0,0,0.15);
            }
            .preview-img{
                width:60px;
                height:60px;
                object-fit:cover;
                border-radius:6px;
                border:1px solid #ddd;
            }
        </style>
    </head>

    <body>

        <div class="container">
            <div class="table-card">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h3>
                        <i class="fa-solid fa-certificate text-primary me-2"></i>
                        My Certifications
                    </h3>

                    <a href="${pageContext.request.contextPath}/lab/certification/add"
                       class="btn btn-primary">
                        <i class="fa-solid fa-plus"></i> Add Certification
                    </a>
                </div>

                <table class="table table-striped align-middle">
                    <thead class="table-light">
                        <tr>
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
                                            <span class="badge bg-danger">Rejected</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <c:if test="${not empty c.filePath}">
                                        <c:set var="fileUrl" value="${pageContext.request.contextPath}/certification/file?name=${c.filePath}" />
                                        <c:choose>
                                            <c:when test="${fn:endsWith(c.filePath,'.jpg') or fn:endsWith(c.filePath,'.jpeg') or fn:endsWith(c.filePath,'.png')}">
                                                <a href="${fileUrl}" target="_blank">
                                                    <img src="${fileUrl}" class="preview-img"/>
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${fileUrl}" target="_blank" class="btn btn-sm btn-outline-primary">
                                                    <i class="fa-solid fa-eye"></i> View
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${c.status == 'PENDING'}">
                                            <button class="btn btn-warning btn-sm me-1"
                                                    onclick="openEditModal('${c.certificationId}', '${c.certificateName}', '${c.certificateNumber}', '${fn:substring(c.issueDate,0,10)}', '${fn:substring(c.expiryDate,0,10)}')">
                                                <i class="fa-solid fa-pen"></i>
                                            </button>
                                            <button class="btn btn-danger btn-sm" onclick="openDeleteModal('${c.certificationId}')">
                                                <i class="fa-solid fa-trash"></i>
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted"><i class="fa-solid fa-lock"></i> Locked</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty list}">
                            <tr><td colspan="7" class="text-center text-muted">No certifications uploaded yet.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="modal fade" id="editModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form id="editCertForm" method="post" enctype="multipart/form-data"
                          action="${pageContext.request.contextPath}/lab/certification/edit">

                        <div class="modal-header">
                            <h5 class="modal-title">Edit Certification</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">
                            <div id="ajaxError" class="alert alert-danger d-none"></div>

                            <input type="hidden" name="id" id="editId">

                            <div class="mb-3">
                                <label class="form-label">Certificate Name</label>
                                <input type="text" name="certificateName" id="editName" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Certificate Number</label>
                                <input type="text" name="certificateNumber" id="editNumber" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Issue Date</label>
                                <input type="date" name="issueDate" id="editIssue" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Expiry Date</label>
                                <input type="date" name="expiryDate" id="editExpiry" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Change File (Optional)</label>
                                <input type="file" name="certificateFile" class="form-control">
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" id="saveBtn" class="btn btn-primary" onclick="submitEditForm()">Save Changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="modal fade" id="deleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form method="post" action="${pageContext.request.contextPath}/lab/certification/delete">
                        <div class="modal-header bg-danger text-white">
                            <h5 class="modal-title">Confirm Delete</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body text-center">
                            <input type="hidden" name="id" id="deleteId">
                            <p>Are you sure you want to delete this certification?</p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button class="btn btn-danger">Delete</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

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
    </body>
</html>