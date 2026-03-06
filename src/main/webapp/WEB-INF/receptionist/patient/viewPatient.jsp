<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<div class="container-fluid">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fas fa-user text-primary me-2"></i>
                Patient Detail
            </h2>
            <p class="text-muted mb-0">View complete patient information</p>
        </div>

        <div class="d-flex gap-2">

            <c:if test="${hasPatientDelete}">
                <button class="btn btn-danger"
                        onclick="confirmDelete(${patient.patientId}, '${patient.fullName}')">
                    <i class="fas fa-trash me-2"></i>Delete
                </button>
            </c:if>

            <c:if test="${hasPatientEdit}">
                <button class="btn btn-warning"
                        onclick="openEditModal(
                                        '${patient.patientId}',
                                        '${patient.fullName}',
                                        '${patient.phone}',
                                        '<fmt:formatDate value="${patient.dateOfBirth}" pattern="yyyy-MM-dd"/>',
                                        '${patient.gender}',
                                        '${patient.address}',
                                        '${patient.medicalHistory}',
                                        '${patient.allergy}'
                                        )">
                    <i class="fas fa-edit me-2"></i>Edit
                </button>
            </c:if>

            <a href="${basePath}/patient/list" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-2"></i>Back to list
            </a>

        </div>

    </div>

    <c:choose>
        <c:when test="${not empty patient}">

            <!-- PERSONAL INFORMATION CARD -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white py-3">
                    <h5 class="mb-0">
                        <i class="fas fa-user-circle text-primary me-2"></i>
                        Personal Information
                    </h5>
                </div>

                <div class="card-body">

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="text-muted small">Patient ID</label>
                            <div class="fw-semibold">#${patient.patientId}</div>
                        </div>

                        <div class="col-md-6">
                            <label class="text-muted small">Gender</label>
                            <div>
                                <c:if test="${patient.gender == 'Male'}">
                                    <span class="badge bg-primary">
                                        <i class="fas fa-mars me-1"></i>Male
                                    </span>
                                </c:if>
                                <c:if test="${patient.gender == 'Female'}">
                                    <span class="badge bg-danger">
                                        <i class="fas fa-venus me-1"></i>Female
                                    </span>
                                </c:if>
                                <c:if test="${patient.gender != 'Male' && patient.gender != 'Female'}">
                                    <span class="badge bg-secondary">
                                        <i class="fas fa-genderless me-1"></i>${patient.gender}
                                    </span>
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="text-muted small">Full Name</label>
                        <div class="fs-5 fw-medium">${patient.fullName}</div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="text-muted small">Phone</label>
                            <div>
                                <span class="badge bg-light text-dark border">
                                    <i class="fas fa-phone me-1"></i>
                                    ${patient.phone}
                                </span>
                            </div>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label class="text-muted small">Date of Birth</label>
                            <div>
                                <span class="badge bg-light text-dark border">
                                    <i class="fas fa-calendar me-1"></i>
                                    <fmt:formatDate value="${patient.dateOfBirth}" pattern="dd/MM/yyyy" />
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="text-muted small">Address</label>
                        <div>${patient.address}</div>
                    </div>

                </div>
            </div>

            <!-- MEDICAL INFORMATION CARD -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white py-3">
                    <h5 class="mb-0">
                        <i class="fas fa-notes-medical text-primary me-2"></i>
                        Medical Information
                    </h5>
                </div>

                <div class="card-body">

                    <div class="mb-3">
                        <label class="text-muted small">Medical History</label>
                        <div>
                            <c:choose>
                                <c:when test="${not empty patient.medicalHistory}">
                                    ${patient.medicalHistory}
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">
                                        <i class="fas fa-info-circle me-1"></i>No medical history recorded
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="text-muted small">Allergy</label>
                        <div>
                            <c:choose>
                                <c:when test="${not empty patient.allergy}">
                                    <span class="badge bg-warning text-dark">
                                        <i class="fas fa-exclamation-triangle me-1"></i>${patient.allergy}
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">
                                        <i class="fas fa-info-circle me-1"></i>No allergies recorded
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                </div>
            </div>

            <!-- SYSTEM INFORMATION CARD -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white py-3">
                    <h5 class="mb-0">
                        <i class="fas fa-clock text-primary me-2"></i>
                        System Information
                    </h5>
                </div>

                <div class="card-body">

                    <div class="mb-3">
                        <label class="text-muted small">Created At</label>
                        <div>
                            <span class="badge bg-light text-dark border">
                                <i class="fas fa-calendar-plus me-1"></i>
                                <fmt:formatDate value="${patient.createdAt}" pattern="dd/MM/yyyy HH:mm:ss" />
                            </span>
                        </div>
                    </div>

                </div>
            </div>
        </c:when>

        <c:otherwise>
            <div class="alert alert-danger shadow-sm">
                <i class="fas fa-exclamation-triangle me-2"></i>
                Patient not found
            </div>
            <a href="${basePath}/patient/list" class="btn btn-secondary">
                <i class="fas fa-arrow-left me-2"></i>Back to List
            </a>
        </c:otherwise>
    </c:choose>

    <!-- ===== EDIT MODAL ===== -->
    <div class="modal fade" id="editModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <form class="modal-content" method="post" action="${basePath}/patient/edit" class="needs-validation" novalidate>

                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-edit text-primary me-2"></i>
                        Edit Patient
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">

                    <input type="hidden" name="patientId">

                    <div class="mb-3">
                        <label class="form-label">Full Name <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="editFullName" name="fullName" required oninput="validateEditFullName(this)">
                        <div class="invalid-feedback" id="editFullNameError">
                            Full name can only contain Vietnamese/English letters and spaces.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Phone <span class="text-danger">*</span></label>
                        <input type="tel" class="form-control" id="editPhone" name="phone" pattern="[0-9\-\+\s]+" required oninput="validateEditPhone(this)" onblur="checkEditPhoneExists(this)">
                        <div class="invalid-feedback" id="editPhoneError">
                            Please enter exactly 10 digits.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Date of Birth <span class="text-danger">*</span></label>
                        <input type="date" class="form-control" id="editDateOfBirth" name="dateOfBirth" required oninput="validateEditDateOfBirth(this)">
                        <div class="invalid-feedback" id="editDobError">
                            Date of birth cannot be in the future.
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Gender <span class="text-danger">*</span></label>
                        <select class="form-select" id="editGender" name="gender" required>
                            <option value="">-- Select Gender --</option>
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            <option value="Other">Other</option>
                        </select>
                        <div class="invalid-feedback">Please select a gender.</div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Address <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="editAddress" name="address" rows="2" required></textarea>
                        <div class="invalid-feedback">Address is required.</div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Medical History</label>
                        <textarea class="form-control" id="editMedicalHistory" name="medicalHistory" rows="2"></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Allergy</label>
                        <input type="text" class="form-control" id="editAllergy" name="allergy">
                    </div>

                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Cancel
                    </button>
                    <button type="submit" class="btn btn-primary" id="editPatientBtn" disabled>
                        <i class="fas fa-save me-2"></i>Save Changes
                    </button>
                </div>

            </form>
        </div>
    </div>

    <!-- ===== DELETE MODAL ===== -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-exclamation-triangle me-2"></i>
                        Confirm Delete
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete this patient?</p>
                    <p><strong>Patient Name:</strong> <span id="deletePatientName"></span></p>
                    <p class="text-danger"><strong>Note:</strong> This action cannot be undone!</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <form method="post" action="${basePath}/patient/delete" style="display: inline;">
                        <input type="hidden" name="patientId" id="deletePatientId">
                        <input type="hidden" name="confirmDelete" value="yes">
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash me-2"></i>Delete Patient
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function openEditModal(id, name, phone, dob, gender, address, medicalHistory, allergy) {
        document.querySelector('input[name="patientId"]').value = id;
        document.getElementById('editFullName').value = name;
        document.getElementById('editPhone').value = phone;
        document.getElementById('editDateOfBirth').value = dob;
        document.getElementById('editGender').value = gender;
        document.getElementById('editAddress').value = address;
        document.getElementById('editMedicalHistory').value = medicalHistory || '';
        document.getElementById('editAllergy').value = allergy || '';

        new bootstrap.Modal(document.getElementById("editModal")).show();

        // Initialize validation after modal is shown
        setTimeout(() => checkEditFormValidity(), 100);
    }

    function confirmDelete(patientId, patientName) {
        document.getElementById('deletePatientId').value = patientId;
        document.getElementById('deletePatientName').textContent = patientName;
        new bootstrap.Modal(document.getElementById("deleteModal")).show();
    }

    // Validate edit full name - only Vietnamese/English letters and spaces
    function validateEditFullName(input) {
        const validNamePattern = /^[a-zA-Z\u00c0-\u1eff\s]*$/;
        const fullNameError = document.getElementById('editFullNameError');

        if (input.value.length > 0 && !validNamePattern.test(input.value)) {
            input.classList.add('is-invalid');
            fullNameError.textContent = 'Full name can only contain Vietnamese/English letters and spaces (no numbers or special characters).';
        } else {
            input.classList.remove('is-invalid');
            fullNameError.textContent = 'Full name can only contain Vietnamese/English letters and spaces.';
        }
        checkEditFormValidity();
    }

    // Validate edit phone - must be exactly 10 digits
    function validateEditPhone(input) {
        const phoneDigitsOnly = input.value.replace(/[^0-9]/g, '');
        const phoneError = document.getElementById('editPhoneError');

        if (input.value.length > 0) {
            if (phoneDigitsOnly.length !== 10) {
                input.classList.add('is-invalid');
                phoneError.textContent = `Phone must be exactly 10 digits (current: ${phoneDigitsOnly.length} digits)`;
            } else if (!phoneDigitsOnly.startsWith('0')) {
                input.classList.add('is-invalid');
                phoneError.textContent = 'Phone number must start with 0 (Vietnamese standard)';
            } else {
                input.classList.remove('is-invalid');
                phoneError.textContent = 'Please enter exactly 10 digits starting with 0.';
            }
        } else {
            input.classList.remove('is-invalid');
            phoneError.textContent = 'Please enter exactly 10 digits starting with 0.';
        }
        checkEditFormValidity();
    }

    // Check if edit phone number already exists (excluding current patient)
    let editPhoneExistsCheckTimer;
    function checkEditPhoneExists(input) {
        const phoneDigitsOnly = input.value.replace(/[^0-9]/g, '');
        const phoneError = document.getElementById('editPhoneError');
        const patientId = document.querySelector('input[name="patientId"]').value;

        clearTimeout(editPhoneExistsCheckTimer);

        if (phoneDigitsOnly.length !== 10 || !phoneDigitsOnly.startsWith('0')) {
            return;
        }

        editPhoneExistsCheckTimer = setTimeout(() => {
            const phone = input.value.trim();

            fetch('${pageContext.request.contextPath}/api/checkPhoneExists?phone=' + encodeURIComponent(phone) + '&excludePatientId=' + patientId)
                    .then(response => response.json())
                    .then(data => {
                        if (data.exists) {
                            input.classList.add('is-invalid');
                            phoneError.textContent = 'This phone number is already registered!';
                        } else {
                            if (!input.classList.contains('is-invalid')) {
                                input.classList.remove('is-invalid');
                                phoneError.textContent = 'Please enter exactly 10 digits.';
                            }
                        }
                        checkEditFormValidity();
                    })
                    .catch(error => {
                        console.error('Error checking phone:', error);
                    });
        }, 500);
    }

    // Validate edit date of birth - cannot be in the future
    function validateEditDateOfBirth(input) {
        if (!input.value)
            return;

        const selectedDate = new Date(input.value);
        const today = new Date();
        const dobError = document.getElementById('editDobError');

        if (selectedDate > today) {
            input.classList.add('is-invalid');
            dobError.textContent = 'Date of birth cannot be in the future!';
        } else {
            input.classList.remove('is-invalid');
            dobError.textContent = 'Date of birth cannot be in the future.';
        }
        checkEditFormValidity();
    }

    // Check if edit form is valid and enable/disable submit button
    function checkEditFormValidity() {
        const fullName = document.getElementById('editFullName').value.trim();
        const phone = document.getElementById('editPhone').value.trim();
        const dateOfBirth = document.getElementById('editDateOfBirth').value;
        const gender = document.getElementById('editGender').value;
        const address = document.getElementById('editAddress').value.trim();
        const phoneInput = document.getElementById('editPhone');
        const editBtn = document.getElementById('editPatientBtn');

        const hasAllRequired = fullName && phone && dateOfBirth && gender && address;

        const validNamePattern = /^[a-zA-Z\u00c0-\u1eff\s]*$/;
        const fullNameIsValid = validNamePattern.test(fullName) || fullName === '';

        const phoneDigitsOnly = phone.replace(/[^0-9]/g, '');
        const phoneIsValid = phoneDigitsOnly.length === 10 && phoneDigitsOnly.startsWith('0') && !phoneInput.classList.contains('is-invalid');

        let dobIsValid = true;
        if (dateOfBirth) {
            const selectedDate = new Date(dateOfBirth);
            const today = new Date();
            dobIsValid = selectedDate <= today;
        }

        if (hasAllRequired && fullNameIsValid && phoneIsValid && dobIsValid) {
            editBtn.disabled = false;
        } else {
            editBtn.disabled = true;
        }
    }

    // Add event listeners for real-time validation
    document.getElementById('editFullName')?.addEventListener('input', checkEditFormValidity);
    document.getElementById('editPhone')?.addEventListener('input', checkEditFormValidity);
    document.getElementById('editDateOfBirth')?.addEventListener('input', checkEditFormValidity);
    document.getElementById('editGender')?.addEventListener('change', checkEditFormValidity);
    document.getElementById('editAddress')?.addEventListener('input', checkEditFormValidity);

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

<style>
    .card {
        border-radius: 12px;
        border: none;
    }

    .card-header {
        border-bottom: 1px solid rgba(0,0,0,0.08);
        border-radius: 12px 12px 0 0 !important;
    }

    .badge {
        padding: 0.45em 0.9em;
        font-weight: 500;
        border-radius: 6px;
    }

    .btn {
        border-radius: 8px;
        font-weight: 500;
        transition: all 0.25s ease;
    }

    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
</style>