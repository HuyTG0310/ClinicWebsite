<%-- 
    Document   : addCertification
    Created on : Mar 13, 2026, 3:48:40 PM
    Author     : Tai Loi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>

        <meta charset="UTF-8">
        <title>Add Certification</title>

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">

        <!-- Google Font -->
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

        <style>

            body{
                font-family:'Poppins',sans-serif;
                background:linear-gradient(135deg,#0d6efd,#0dcaf0);
                height:100vh;
                display:flex;
                align-items:center;
                justify-content:center;
            }

            .cert-card{

                background:#ffffff;
                border-radius:20px;
                padding:45px;

                width:100%;
                max-width:520px;

                border:1px solid rgba(13,110,253,0.15);

                box-shadow:0 20px 40px rgba(13,110,253,0.25);

            }

            .form-control{

                border-radius:10px;
                padding:12px 15px;

            }

            .form-control:focus{

                border-color:#0d6efd;
                box-shadow:0 0 6px rgba(13,110,253,0.35);

            }

            .input-group-text{

                background:#0d6efd;
                color:white;

                border-radius:10px 0 0 10px;

            }

            .btn-primary{

                border-radius:10px;
                padding:12px;

                font-weight:500;

            }

            .btn-primary:hover{

                transform:translateY(-2px);

                box-shadow:0 10px 20px rgba(13,110,253,0.35);

            }

            .back-link{

                font-size:14px;
                text-decoration:none;

            }

            .back-link:hover{

                text-decoration:underline;

            }

        </style>

    </head>

    <body>

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


            <form action="${pageContext.request.contextPath}/certification/add"

                  method="post"

                  enctype="multipart/form-data">


                <!-- Certificate Name -->

                <div class="mb-3">

                    <label class="form-label">Certificate Name</label>

                    <div class="input-group">

                        <span class="input-group-text">

                            <i class="fa-solid fa-file"></i>

                        </span>

                        <input type="text"

                               name="certificateName"

                               class="form-control"

                               placeholder="Example: ACLS Certification"

                               required>

                    </div>

                </div>


                <!-- Certificate Number -->

                <div class="mb-3">

                    <label class="form-label">Certificate Number</label>

                    <div class="input-group">

                        <span class="input-group-text">

                            <i class="fa-solid fa-hashtag"></i>

                        </span>

                        <input type="text"

                               name="certificateNumber"

                               class="form-control"

                               placeholder="Enter certificate number"

                               required>

                    </div>

                </div>


                <!-- Issue Date -->

                <div class="mb-3">

                    <label class="form-label">Issue Date</label>

                    <div class="input-group">

                        <span class="input-group-text">

                            <i class="fa-solid fa-calendar"></i>

                        </span>

                        <input type="date"

                               name="issueDate"

                               class="form-control"

                               required>

                    </div>

                </div>


                <!-- Expiry Date -->

                <div class="mb-3">

                    <label class="form-label">Expiry Date</label>

                    <div class="input-group">

                        <span class="input-group-text">

                            <i class="fa-solid fa-hourglass-end"></i>

                        </span>

                        <input type="date"

                               name="expiryDate"

                               class="form-control"

                               required>

                    </div>

                </div>


                <!-- Upload File -->

                <div class="mb-4">

                    <label class="form-label">Upload Certificate (PDF/JPG/PNG)</label>

                    <div class="input-group">

                        <span class="input-group-text">

                            <i class="fa-solid fa-upload"></i>

                        </span>

                        <input type="file"

                               name="certificateFile"

                               class="form-control"

                               accept=".pdf,.jpg,.jpeg,.png"

                               required>

                    </div>

                </div>


                <button type="submit"

                        class="btn btn-primary w-100">

                    <i class="fa-solid fa-paper-plane me-2"></i>

                    Submit Certification

                </button>

            </form>


            <div class="text-center mt-4">

                <a href="${pageContext.request.contextPath}/doctor/dashboard"

                   class="back-link text-primary">

                    <i class="fa-solid fa-arrow-left me-1"></i>

                    Back to Dashboard

                </a>

            </div>

        </div>

    </body>

</html>