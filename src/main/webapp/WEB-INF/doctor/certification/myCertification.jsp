<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>

        <meta charset="UTF-8">
        <title>My Certifications</title>

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
                padding:30px;
                box-shadow:0 20px 40px rgba(0,0,0,0.15);
            }

            .badge{
                font-size:14px;
                padding:6px 10px;
            }

            .btn-primary{
                border-radius:10px;
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

                    <a href="${pageContext.request.contextPath}/certification/add"
                       class="btn btn-primary">

                        <i class="fa-solid fa-plus me-1"></i>
                        Add Certification

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

                            <a href="${pageContext.request.contextPath}/${c.filePath}"
                               target="_blank"
                               class="btn btn-sm btn-outline-primary">

                                <i class="fa-solid fa-eye"></i>
                                View

                            </a>

                        </c:if>

                        </td>

                        </tr>

                    </c:forEach>

                    <c:if test="${empty list}">

                        <tr>
                            <td colspan="6" class="text-center text-muted">
                                No certifications uploaded yet.
                            </td>
                        </tr>

                    </c:if>

                    </tbody>

                </table>

            </div>

        </div>

    </body>
</html>