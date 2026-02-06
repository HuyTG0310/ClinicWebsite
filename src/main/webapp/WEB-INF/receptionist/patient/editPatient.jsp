
<%--
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Edit Patient - Clinic Management</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap.min.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
                <style>
                    .form-container {
                        max-width: 700px;
                        margin: 30px auto;
                        padding: 30px;
                        background-color: #f8f9fa;
                        border-radius: 8px;
                        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                    }

                    .form-title {
                        text-align: center;
                        margin-bottom: 30px;
                        color: #333;
                    }

                    .form-group label {
                        font-weight: 500;
                        color: #495057;
                        margin-bottom: 8px;
                    }

                    .form-control:focus {
                        border-color: #007bff;
                        box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
                    }

                    .required::after {
                        content: " *";
                        color: #dc3545;
                    }

                    .alert {
                        margin-bottom: 20px;
                    }

                    .button-group {
                        display: flex;
                        gap: 10px;
                        margin-top: 30px;
                    }

                    .button-group button,
                    .button-group a {
                        flex: 1;
                    }
                </style>
            </head>

            <body>
                <div class="container">
                    <div class="form-container">
                        <!-- Header -->
                        <div class="form-title">
                            <h2>Edit Patient Information</h2>
                            <p class="text-muted">Update patient details</p>
                        </div>

                        <!-- Error Message -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <strong>Error!</strong> ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"
                                    aria-label="Close"></button>
                            </div>
                        </c:if>

                        <!-- Edit Patient Form -->
                        <form method="post" action="${pageContext.request.contextPath}/EditPatient"
                            class="needs-validation" novalidate>

                            <!-- Patient ID (Hidden) -->
                            <input type="hidden" name="patientId" value="${patient.patientId}">

                            <!-- Full Name -->
                            <div class="mb-3">
                                <label for="fullName" class="form-label required">Full Name</label>
                                <input type="text" class="form-control" id="fullName" name="fullName"
                                    placeholder="Enter full name" value="${patient.fullName}" required>
                                <div class="invalid-feedback">
                                    Full name is required.
                                </div>
                            </div>

                            <!-- Phone -->
                            <div class="mb-3">
                                <label for="phone" class="form-label required">Phone</label>
                                <input type="tel" class="form-control" id="phone" name="phone"
                                    placeholder="Enter phone number" value="${patient.phone}" pattern="[0-9\-\+\s]+"
                                    required>
                                <div class="invalid-feedback">
                                    Please enter a valid phone number.
                                </div>
                            </div>

                            <!-- Date of Birth -->
                            <div class="mb-3">
                                <label for="dateOfBirth" class="form-label required">Date of Birth</label>
                                <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth"
                                    value="<fmt:formatDate value='${patient.dateOfBirth}' pattern='yyyy-MM-dd'/>"
                                    required>
                                <div class="invalid-feedback">
                                    Date of birth is required.
                                </div>
                            </div>

                            <!-- Gender -->
                            <div class="mb-3">
                                <label for="gender" class="form-label required">Gender</label>
                                <select class="form-select" id="gender" name="gender" required>
                                    <option value="">-- Select Gender --</option>
                                    <option value="Male" <c:if test="${patient.gender == 'Male'}">selected</c:if>>Male
                                    </option>
                                    <option value="Female" <c:if test="${patient.gender == 'Female'}">selected</c:if>
                                        >Female</option>
                                    <option value="Other" <c:if test="${patient.gender == 'Other'}">selected</c:if>
                                        >Other</option>
                                </select>
                                <div class="invalid-feedback">
                                    Please select a gender.
                                </div>
                            </div>

                            <!-- Address -->
                            <div class="mb-3">
                                <label for="address" class="form-label required">Address</label>
                                <textarea class="form-control" id="address" name="address"
                                    placeholder="Enter full address" rows="3" required>${patient.address}</textarea>
                                <div class="invalid-feedback">
                                    Address is required.
                                </div>
                            </div>

                            <!-- Medical History -->
                            <div class="mb-3">
                                <label for="medicalHistory" class="form-label">Medical History</label>
                                <textarea class="form-control" id="medicalHistory" name="medicalHistory"
                                    placeholder="Enter medical history (optional)"
                                    rows="3">${patient.medicalHistory}</textarea>
                            </div>

                            <!-- Allergy -->
                            <div class="mb-3">
                                <label for="allergy" class="form-label">Allergy</label>
                                <input type="text" class="form-control" id="allergy" name="allergy"
                                    placeholder="Enter allergy information (optional)" value="${patient.allergy}">
                            </div>

                            <!-- Buttons -->
                            <div class="button-group">
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-check-lg"></i> Update Patient
                                </button>
                                <a href="${pageContext.request.contextPath}/ViewPatient?id=${patient.patientId}"
                                    class="btn btn-secondary">
                                    <i class="bi bi-x-lg"></i> Cancel
                                </a>
                            </div>
                        </form>
                    </div>
                </div>

                <script src="${pageContext.request.contextPath}/bootstrap.bundle.min.js"></script>
                <script>
                    // Bootstrap form validation
                    (() => {
                        'use strict'
                        const forms = document.querySelectorAll('.needs-validation')
                        Array.from(forms).forEach(form => {
                            form.addEventListener('submit', event => {
                                if (!form.checkValidity()) {
                                    event.preventDefault()
                                    event.stopPropagation()
                                }
                                form.classList.add('was-validated')
                            }, false)
                        })
                    })()
                </script>
            </body>

            </html>
--%>
