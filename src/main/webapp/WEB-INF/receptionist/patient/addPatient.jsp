<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    body {
        background-color: #f5f7fa;
        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
    }

    .container {
        max-width: 1200px;
        padding: 2rem 1rem;
    }

    /* Header */
    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 2rem;
    }

    .page-header h2 {
        font-size: 1.75rem;
        font-weight: 600;
        color: #1a1a1a;
        margin: 0;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .page-header h2 i {
        color: #0d6efd;
        font-size: 1.5rem;
    }

    .page-subtitle {
        color: #6c757d;
        font-size: 0.95rem;
        margin-top: 0.25rem;
    }

    /* Card */
    .card {
        border: none;
        border-radius: 12px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.08);
        background: white;
        overflow: hidden;
    }

    .card-header {
        background: white;
        border-bottom: 1px solid #e9ecef;
        padding: 1.25rem 1.5rem;
    }

    .card-header h5 {
        margin: 0;
        font-size: 1.1rem;
        font-weight: 600;
        color: #1a1a1a;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .card-header h5 i {
        color: #0d6efd;
    }

    .card-body {
        padding: 2rem 1.5rem;
    }

    /* Form */
    .form-label {
        font-weight: 600;
        color: #1a1a1a;
        font-size: 0.95rem;
        margin-bottom: 0.5rem;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .form-label i {
        color: #0d6efd;
        font-size: 0.9rem;
    }

    .required::after {
        content: " *";
        color: #dc3545;
        margin-left: 0.25rem;
    }

    .input-group {
        box-shadow: 0 1px 2px rgba(0,0,0,0.05);
        border-radius: 8px;
        overflow: hidden;
    }

    .input-group-text {
        background: white;
        border: 1px solid #dee2e6;
        border-right: none;
        padding: 0.625rem 1rem;
    }

    .input-group-text i {
        color: #6c757d;
    }

    .form-control, .form-select {
        border: 1px solid #dee2e6;
        padding: 0.625rem 1rem;
        font-size: 0.95rem;
        transition: all 0.2s;
    }

    .input-group .form-control {
        border-left: none;
    }

    .form-control:focus, .form-select:focus {
        border-color: #86b7fe;
        box-shadow: none;
    }

    .input-group:focus-within .input-group-text {
        border-color: #86b7fe;
    }

    .form-text {
        color: #6c757d;
        font-size: 0.875rem;
        margin-top: 0.5rem;
    }

    .invalid-feedback {
        font-size: 0.875rem;
        color: #dc3545;
        margin-top: 0.5rem;
    }

    /* Buttons */
    .btn {
        padding: 0.625rem 1.5rem;
        font-weight: 500;
        border-radius: 8px;
        font-size: 0.95rem;
        transition: all 0.2s;
        border: none;
        display: inline-flex;
        align-items: center;
        gap: 0.5rem;
    }

    .btn-primary {
        background: #0d6efd;
        color: white;
    }

    .btn-primary:hover:not(:disabled) {
        background: #0b5ed7;
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(13,110,253,0.3);
    }

    .btn-primary:disabled {
        background: #6c757d;
        opacity: 0.6;
        cursor: not-allowed;
    }

    .btn-secondary {
        background: #6c757d;
        color: white;
    }

    .btn-secondary:hover {
        background: #5c636a;
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(108,117,125,0.3);
    }

    .btn-outline-secondary {
        border: 1px solid #dee2e6;
        color: #6c757d;
        background: white;
    }

    .btn-outline-secondary:hover {
        background: #f8f9fa;
        color: #495057;
        border-color: #dee2e6;
        transform: translateY(-1px);
    }

    /* Alert */
    .alert {
        border: none;
        border-radius: 10px;
        border-left: 4px solid;
        padding: 1rem 1.25rem;
        margin-bottom: 1.5rem;
        display: flex;
        align-items: center;
        gap: 0.75rem;
    }

    .alert-danger {
        background: #f8d7da;
        color: #842029;
        border-left-color: #dc3545;
    }

    .alert-success {
        background: #d1e7dd;
        color: #0f5132;
        border-left-color: #28a745;
    }

    .alert i {
        font-size: 1.1rem;
    }

    .btn-close {
        margin-left: auto;
    }

    /* Form spacing */
    .mb-4 {
        margin-bottom: 1.5rem !important;
    }

    .action-buttons {
        display: flex;
        gap: 1rem;
        justify-content: flex-start;
        margin-top: 2rem;
        padding-top: 1.5rem;
        border-top: 1px solid #e9ecef;
    }

    /* Grid layout for form fields */
    .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 1.5rem;
    }

    @media (max-width: 768px) {
        .form-row {
            grid-template-columns: 1fr;
        }
    }
</style>
<div class="container">
    <!-- Header Section -->
    <div class="page-header">
        <div>
            <h2>
                <i class="fas fa-user-plus"></i>
                Add New Patient
            </h2>
            <p class="page-subtitle">Create a new patient record in the system</p>
        </div>
        <a href="${basePath}/patient/list" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left"></i>
            Back to List
        </a>
    </div>

    <!-- Error Message -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-triangle"></i>
            <span><strong>Error!</strong> ${error}</span>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Success Message -->
    <c:if test="${not empty success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i>
            <span><strong>Success!</strong> ${success}</span>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Form Card -->
    <div class="card">
        <div class="card-header">
            <h5>
                <i class="fas fa-edit"></i>
                Patient Information
            </h5>
        </div>

        <div class="card-body">
            <form method="post" action="${basePath}/patient/create" class="needs-validation" novalidate>

                <!-- Full Name -->
                <div class="mb-4">
                    <label class="form-label required">
                        <i class="fas fa-user"></i>
                        Full Name
                    </label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="fas fa-id-card"></i>
                        </span>
                        <input type="text" class="form-control" id="fullName" name="fullName"
                               placeholder="Enter full name (letters and spaces only)" required
                               oninput="validateFullName(this)" value="${patient.fullName}">
                    </div>
                    <div class="invalid-feedback" id="fullNameError">
                        Full name can only contain Vietnamese/English letters and spaces.
                    </div>
                </div>

                <!-- Phone & Date of Birth Row -->
                <div class="form-row mb-4">
                    <!-- Phone -->
                    <div>
                        <label class="form-label required">
                            <i class="fas fa-phone"></i>
                            Phone
                        </label>
                        <div class="input-group">
                            <span class="input-group-text">
                                <i class="fas fa-mobile-alt"></i>
                            </span>
                            <input type="tel" class="form-control" id="phone" name="phone"
                                   placeholder="10-digit phone number" pattern="[0-9\-\+\s]+" required
                                   oninput="validatePhone(this)" onblur="checkPhoneExists(this)" value="${patient.phone}">
                        </div>
                        <div class="invalid-feedback" id="phoneError">
                            Please enter exactly 10 digits for Vietnamese phone number.
                        </div>
                    </div>

                    <!-- Date of Birth -->
                    <div>
                        <label class="form-label required">
                            <i class="fas fa-calendar"></i>
                            Date of Birth
                        </label>
                        <div class="input-group">
                            <span class="input-group-text">
                                <i class="fas fa-birthday-cake"></i>
                            </span>
                            <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth" required
                                   oninput="validateDateOfBirth(this)" value="${patient.dateOfBirth}">
                        </div>
                        <div class="invalid-feedback" id="dobError">
                            Date of birth cannot be in the future.
                        </div>
                    </div>
                </div>

                <!-- Gender -->
                <div class="mb-4">
                    <label class="form-label required">
                        <i class="fas fa-venus-mars"></i>
                        Gender
                    </label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="fas fa-genderless"></i>
                        </span>
                        <select class="form-select" id="gender" name="gender" required>
                            <option value="">-- Select Gender --</option>
                            <option value="Male" ${patient.gender == 'Male' ? 'selected' : ''}>Male</option>
                            <option value="Female" ${patient.gender == 'Female' ? 'selected' : ''}>Female</option>
                            <option value="Other" ${patient.gender == 'Other' ? 'selected' : ''}>Other</option>
                        </select>
                    </div>
                    <div class="invalid-feedback">
                        Please select a gender.
                    </div>
                </div>

                <!-- Address -->
                <div class="mb-4">
                    <label class="form-label required">
                        <i class="fas fa-map-marker-alt"></i>
                        Address
                    </label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="fas fa-home"></i>
                        </span>
                        <textarea class="form-control" id="address" name="address" 
                                  placeholder="Enter full address" rows="2" required>${patient.address}</textarea>
                    </div>
                    <div class="invalid-feedback">
                        Address is required.
                    </div>
                </div>

                <!-- Medical History -->
                <div class="mb-4">
                    <label class="form-label">
                        <i class="fas fa-notes-medical"></i>
                        Medical History
                    </label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="fas fa-file-medical"></i>
                        </span>
                        <textarea class="form-control" id="medicalHistory" name="medicalHistory"
                                  placeholder="Enter medical history (optional)" rows="2">${patient.medicalHistory}</textarea>
                    </div>
                </div>

                <!-- Allergy -->
                <div class="mb-4">
                    <label class="form-label">
                        <i class="fas fa-exclamation-circle"></i>
                        Allergy
                    </label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="fas fa-allergies"></i>
                        </span>
                        <input type="text" class="form-control" id="allergy" name="allergy"
                               placeholder="Enter allergy information (optional)" value="${patient.allergy}">
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="action-buttons">
                    <button type="submit" class="btn btn-primary" id="addPatientBtn" disabled>
                        <i class="fas fa-save"></i>
                        Add Patient
                    </button>
                    <a href="${basePath}/patient/list" class="btn btn-secondary">
                        <i class="fas fa-times"></i>
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // Validate full name - only Vietnamese/English letters and spaces
    function validateFullName(input) {
        const validNamePattern = /^[a-zA-ZÀ-ỿ\s]*$/;
        const fullNameError = document.getElementById('fullNameError');

        if (input.value.length > 0 && !validNamePattern.test(input.value)) {
            input.classList.add('is-invalid');
            fullNameError.textContent = 'Full name can only contain Vietnamese/English letters and spaces (no numbers or special characters).';
        } else {
            input.classList.remove('is-invalid');
            fullNameError.textContent = 'Full name can only contain Vietnamese/English letters and spaces.';
        }
        checkFormValidity();
    }

    // Validate phone number - must be exactly 10 digits starting with 0
    function validatePhone(input) {
        const phoneDigitsOnly = input.value.replace(/[^0-9]/g, '');
        const phoneError = document.getElementById('phoneError');

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
        checkFormValidity();
    }

    // Check if phone number already exists
    let phoneExistsCheckTimer;
    function checkPhoneExists(input) {
        const phoneDigitsOnly = input.value.replace(/[^0-9]/g, '');
        const phoneError = document.getElementById('phoneError');

        clearTimeout(phoneExistsCheckTimer);

        if (phoneDigitsOnly.length !== 10 || !phoneDigitsOnly.startsWith('0')) {
            return;
        }

        phoneExistsCheckTimer = setTimeout(() => {
            const phone = input.value.trim();

            fetch('${basePath}/api/checkPhoneExists?phone=' + encodeURIComponent(phone))
                    .then(response => response.json())
                    .then(data => {
                        if (data.exists) {
                            input.classList.add('is-invalid');
                            phoneError.textContent = 'This phone number is already registered!';
                        } else {
                            if (!input.classList.contains('is-invalid')) {
                                input.classList.remove('is-invalid');
                                phoneError.textContent = 'Please enter exactly 10 digits for Vietnamese phone number.';
                            }
                        }
                        checkFormValidity();
                    })
                    .catch(error => {
                        console.error('Error checking phone:', error);
                    });
        }, 500);
    }

    // Validate date of birth - cannot be in the future
    function validateDateOfBirth(input) {
        if (!input.value)
            return;

        const selectedDate = new Date(input.value);
        const today = new Date();
        const dobError = document.getElementById('dobError');

        if (selectedDate > today) {
            input.classList.add('is-invalid');
            dobError.textContent = 'Date of birth cannot be in the future!';
        } else {
            input.classList.remove('is-invalid');
            dobError.textContent = 'Date of birth cannot be in the future.';
        }
        checkFormValidity();
    }

    // Check if form is valid and enable/disable submit button
    function checkFormValidity() {
        const fullName = document.getElementById('fullName').value.trim();
        const phone = document.getElementById('phone').value.trim();
        const dateOfBirth = document.getElementById('dateOfBirth').value;
        const gender = document.getElementById('gender').value;
        const address = document.getElementById('address').value.trim();
        const phoneInput = document.getElementById('phone');
        const addBtn = document.getElementById('addPatientBtn');

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
            addBtn.disabled = false;
        } else {
            addBtn.disabled = true;
        }
    }

    // Add event listeners for real-time validation
    document.getElementById('fullName').addEventListener('input', checkFormValidity);
    document.getElementById('phone').addEventListener('input', checkFormValidity);
    document.getElementById('dateOfBirth').addEventListener('input', checkFormValidity);
    document.getElementById('gender').addEventListener('change', checkFormValidity);
    document.getElementById('address').addEventListener('input', checkFormValidity);

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
