<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Thêm thư viện functions để xử lý chuỗi --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Add Certification</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

        <style>
            body {
                font-family: 'Poppins', sans-serif;
                background: linear-gradient(135deg, #0d6efd, #0dcaf0);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 20px;
            }
            .cert-card {
                background: #ffffff;
                border-radius: 20px;
                padding: 45px;
                width: 100%;
                max-width: 520px;
                border: 1px solid rgba(13, 110, 253, 0.15);
                box-shadow: 0 20px 40px rgba(13, 110, 253, 0.25);
            }
            .form-control {
                border-radius: 10px;
                padding: 12px 15px;
            }
            .input-group-text {
                background: #0d6efd;
                color: white;
                border-radius: 10px 0 0 10px;
                width: 45px;
                justify-content: center;
            }
            .btn-primary {
                border-radius: 10px;
                padding: 12px;
                font-weight: 500;
            }
        </style>
    </head>

    <body>
        <%-- Bước 1: Xác định Prefix dựa trên Role của User --%>
        <c:set var="userRole" value="${fn:toLowerCase(sessionScope.user.roleName)}" />
        <c:set var="rolePrefix" value="${fn:contains(userRole, 'lab') ? 'lab' : (fn:contains(userRole, 'receptionist') ? 'receptionist' : 'doctor')}" />

        <div class="cert-card">
            <h3 class="text-center fw-bold mb-3">
                <i class="fa-solid fa-certificate text-primary me-2"></i>
                Add Certification
            </h3>

            <p class="text-center text-muted mb-4">
                Upload your professional certificate for verification.
            </p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger text-center">
                    <i class="fa-solid fa-circle-exclamation me-2"></i>
                    ${error}
                </div>
            </c:if>

            <%-- Bước 2: Sửa action thành rỗng để nó tự POST về URL hiện tại (VD: /lab/certification/add) --%>
            <form action="" method="POST" enctype="multipart/form-data">
                <div class="mb-3">
                    <label class="form-label">Certificate Name</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa-solid fa-file"></i></span>
                        <input type="text" name="certificateName" class="form-control"
                               value="${certificateName}" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label">Certificate Number</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa-solid fa-hashtag"></i></span>
                        <input type="text" name="certificateNumber" class="form-control"
                               value="${certificateNumber}" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label">Issue Date</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa-solid fa-calendar"></i></span>
                        <input type="date" name="issueDate" class="form-control"
                               value="${issueDate}" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label">Expiry Date</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa-solid fa-hourglass-end"></i></span>
                        <input type="date" name="expiryDate" class="form-control"
                               value="${expiryDate}" required>
                    </div>
                </div>

                <div class="mb-4">
                    <label class="form-label">Upload Certificate (Image/PDF)</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa-solid fa-upload"></i></span>
                        <input type="file" name="certificateFile" class="form-control" accept=".pdf,.jpg,.jpeg,.png" required>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-100">
                    <i class="fa-solid fa-paper-plane me-2"></i>
                    Submit Certification
                </button>
            </form>

            <%-- Bước 3: Sửa link quay về Dashboard theo Role --%>
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/${rolePrefix}/dashboard" class="text-primary text-decoration-none">
                    <i class="fa-solid fa-arrow-left"></i>
                    Back to Dashboard
                </a>
            </div>
        </div>
    </body>