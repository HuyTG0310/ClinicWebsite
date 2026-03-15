<%-- 
    Document   : certificationApproval
    Created on : Mar 13, 2026, 3:57:42 PM
    Author     : Tai Loi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>

        <meta charset="UTF-8">
        <title>Certification Approval</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">

        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

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

            .badge{
                font-size:14px;
                padding:6px 10px;
            }

        </style>

    </head>

    <body>

        <div class="container">

            <div class="table-card">

                <div class="d-flex justify-content-between mb-4">

                    <h3>

                        <i class="fa-solid fa-certificate text-primary me-2"></i>

                        Certification Approval

                    </h3>

                    <a href="${pageContext.request.contextPath}/admin/dashboard"
                       class="btn btn-outline-primary">

                        <i class="fa-solid fa-arrow-left me-1"></i>
                        Back Dashboard

                    </a>

                </div>

                <table class="table table-striped align-middle">

                    <thead class="table-light">

                        <tr>

                            <th>User ID</th>

                            <th>Certificate</th>

                            <th>Number</th>

                            <th>Issue Date</th>

                            <th>Expiry Date</th>

                            <th>Status</th>

                            <th>File</th>

                            <th>Action</th>

                        </tr>

                    </thead>

                    <tbody>

                    <c:forEach items="${list}" var="c">

                        <tr>

                            <td>${c.userId}</td>

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

                            <a href="${pageContext.request.contextPath}/${c.filePath}"
                               target="_blank"
                               class="btn btn-sm btn-outline-primary">

                                <i class="fa-solid fa-eye"></i>
                                View

                            </a>

                        </td>

                        <td>

                        <c:if test="${c.status == 'PENDING'}">

                            <a href="${pageContext.request.contextPath}/admin/certification/approve?id=${c.certificationId}"
                               class="btn btn-success btn-sm">

                                <i class="fa-solid fa-check"></i>
                                Approve

                            </a>

                            <a href="${pageContext.request.contextPath}/admin/certification/reject?id=${c.certificationId}"
                               class="btn btn-danger btn-sm">

                                <i class="fa-solid fa-xmark"></i>
                                Reject

                            </a>

                        </c:if>

                        </td>

                        </tr>

                    </c:forEach>

                    </tbody>

                </table>

            </div>

        </div>

    </body>
</html>
