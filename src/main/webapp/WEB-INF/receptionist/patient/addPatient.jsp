<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container-fluid">

    <!-- ================= HEADER ================= -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fas fa-user-plus text-primary me-2"></i>
                Add Patient
            </h2>

            <p class="text-muted mb-0">
                Create a new patient record in the system
            </p>
        </div>

        <a href="${basePath}/patient/list"
           class="btn btn-outline-secondary">

            <i class="fas fa-arrow-left me-2"></i>
            Back to list

        </a>

    </div>



    <!-- ================= ERROR ================= -->

    <c:if test="${not empty error}">
        <div class="alert alert-danger shadow-sm">

            <i class="fas fa-exclamation-triangle me-2"></i>
            <strong>Error!</strong> ${error}

        </div>
    </c:if>



    <c:if test="${not empty success}">
        <div class="alert alert-success shadow-sm">

            <i class="fas fa-check-circle me-2"></i>
            <strong>Success!</strong> ${success}

        </div>
    </c:if>



    <!-- ================= CARD ================= -->

    <div class="card shadow-sm">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">

                <i class="fas fa-edit text-primary me-2"></i>
                Patient Information

            </h5>

        </div>



        <div class="card-body">

            <form method="post"
                  action="${basePath}/patient/create"
                  class="needs-validation"
                  novalidate>



                <div class="row g-4">


                    <!-- FULL NAME -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Full Name <span class="text-danger">*</span>
                        </label>

                        <input type="text"
                               class="form-control"
                               id="fullName"
                               name="fullName"
                               placeholder="Enter full name"
                               required
                               oninput="validateFullName(this)"
                               value="${patient.fullName}">

                        <div class="invalid-feedback" id="fullNameError">
                            Full name can only contain Vietnamese/English letters and spaces.
                        </div>

                    </div>



                    <!-- PHONE -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Phone <span class="text-danger">*</span>
                        </label>

                        <input type="tel"
                               class="form-control"
                               id="phone"
                               name="phone"
                               placeholder="10-digit phone number"
                               pattern="[0-9\-\+\s]+"
                               required
                               oninput="validatePhone(this)"
                               onblur="checkPhoneExists(this)"
                               value="${patient.phone}">

                        <div class="invalid-feedback" id="phoneError">
                            Please enter exactly 10 digits for Vietnamese phone number.
                        </div>

                    </div>



                    <!-- DATE OF BIRTH -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Date of Birth <span class="text-danger">*</span>
                        </label>

                        <input type="date"
                               class="form-control"
                               id="dateOfBirth"
                               name="dateOfBirth"
                               required
                               oninput="validateDateOfBirth(this)"
                               value="${patient.dateOfBirth}">

                        <div class="invalid-feedback" id="dobError">
                            Date of birth cannot be in the future.
                        </div>

                    </div>



                    <!-- GENDER -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Gender <span class="text-danger">*</span>
                        </label>

                        <select class="form-select"
                                id="gender"
                                name="gender"
                                required>

                            <option value="">-- Select Gender --</option>

                            <option value="Male"
                                    ${patient.gender == 'Male' ? 'selected' : ''}>
                                Male
                            </option>

                            <option value="Female"
                                    ${patient.gender == 'Female' ? 'selected' : ''}>
                                Female
                            </option>

                            <option value="Other"
                                    ${patient.gender == 'Other' ? 'selected' : ''}>
                                Other
                            </option>

                        </select>

                        <div class="invalid-feedback">
                            Please select a gender.
                        </div>

                    </div>



                    <!-- ADDRESS -->
                    <div class="col-md-12">

                        <label class="form-label fw-semibold">
                            Address <span class="text-danger">*</span>
                        </label>

                        <textarea class="form-control"
                                  id="address"
                                  name="address"
                                  rows="2"
                                  required>${patient.address}</textarea>

                        <div class="invalid-feedback">
                            Address is required.
                        </div>

                    </div>



                    <!-- MEDICAL HISTORY -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Medical History
                        </label>

                        <textarea class="form-control"
                                  id="medicalHistory"
                                  name="medicalHistory"
                                  rows="2">${patient.medicalHistory}</textarea>

                    </div>



                    <!-- ALLERGY -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Allergy
                        </label>

                        <input type="text"
                               class="form-control"
                               id="allergy"
                               name="allergy"
                               value="${patient.allergy}">

                    </div>


                </div>



                <!-- ================= ACTION BUTTONS ================= -->

                <div class="d-flex justify-content-end gap-2 mt-4">

                    <a href="${basePath}/patient/list"
                       class="btn btn-outline-secondary">

                        Cancel

                    </a>

                    <button type="submit"
                            class="btn btn-primary"
                            id="addPatientBtn"
                            disabled>

                        <i class="fas fa-save me-1"></i>
                        Add Patient

                    </button>

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
