<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/css/select2.min.css" rel="stylesheet" />

<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/js/select2.min.js"></script>

<style>
    .select2-container .select2-selection--single{
        height:38px !important;
        border:1px solid #ced4da;
        border-radius:0.375rem;
        display:flex;
        align-items:center;
    }
    .select2-container--default .select2-selection--single .select2-selection__arrow{
        height:36px !important;
    }
</style>


<div class="container-fluid">

    <!-- ================= HEADER ================= -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-notes-medical text-primary me-2"></i>
                Create Appointment
            </h2>

            <p class="text-muted mb-0">
                Register a new patient appointment
            </p>
        </div>

        <a href="list"
           class="btn btn-outline-secondary">

            <i class="fa-solid fa-arrow-left me-2"></i>
            Back to list

        </a>

    </div>



    <!-- ================= CARD ================= -->

    <div class="card shadow-sm">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">

                <i class="fa-solid fa-calendar-plus text-primary me-2"></i>
                Appointment Information

            </h5>

        </div>


        <div class="card-body">

            <form action="create" method="POST">


                <div class="row g-4">


                    <!-- PATIENT -->
                    <div class="col-md-12">

                        <label class="form-label fw-semibold">
                            Select patient <span class="text-danger">*</span>
                        </label>

                        <div class="d-flex gap-2">

                            <select class="form-select"
                                    name="patientId"
                                    id="patientSelect"
                                    required
                                    style="width:100%;">

                                <option value="">-- Search by name or phone --</option>

                                <c:forEach var="p" items="${patients}">
                                    <option value="${p.patientId}">
                                        ${p.fullName} - ${p.phone} (${p.gender})
                                    </option>
                                </c:forEach>

                            </select>

                            <a href="${basePath}/patient/create"
                               class="btn btn-outline-primary text-nowrap">

                                <i class="fa-solid fa-plus"></i>
                                Add

                            </a>

                        </div>

                        <div class="form-text">
                            Type patient name or phone number to search quickly
                        </div>

                    </div>



                    <!-- SPECIALTY -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Specialty
                        </label>

                        <select class="form-select"
                                id="specialtySelect"
                                onchange="filterRooms()">

                            <option value="all">-- All specialties --</option>

                            <c:forEach var="s" items="${specialties}">
                                <option value="${s.specialtyId}">
                                    ${s.name}
                                </option>
                            </c:forEach>

                        </select>

                    </div>



                    <!-- ROOM -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Select room <span class="text-danger">*</span>
                        </label>

                        <select class="form-select"
                                name="roomId"
                                id="roomSelect"
                                required>

                            <option value="">-- Please select room --</option>

                            <c:forEach var="r" items="${rooms}">
                                <option value="${r.roomId}" data-specialty="${r.specialtyId}">
                                    ${r.roomName} - BS. ${r.doctorName}
                                </option>
                            </c:forEach>

                        </select>

                    </div>

                </div>



                <!-- ================= PAYMENT ================= -->

                <div class="card bg-light border-0 mt-4">

                    <div class="card-body">

                        <div class="row align-items-center">

                            <div class="col-md-6 border-end">

                                <div class="d-flex align-items-center">

                                    <div class="bg-white p-3 rounded-circle shadow-sm text-primary me-3">
                                        <i class="fa-solid fa-money-bill-wave fs-4"></i>
                                    </div>

                                    <div>

                                        <div class="text-muted small text-uppercase fw-bold">
                                            Total amount
                                        </div>

                                        <div class="fs-4 fw-bold text-danger">
                                            200.000 VNĐ
                                        </div>

                                        <div class="small text-muted">
                                            <i class="fa-solid fa-circle-info me-1"></i>
                                            Fixed clinical examination fee
                                        </div>

                                    </div>

                                </div>

                            </div>



                            <div class="col-md-6 ps-md-4 mt-3 mt-md-0">

                                <label class="form-label fw-semibold">
                                    Payment method <span class="text-danger">*</span>
                                </label>

                                <div class="input-group">

                                    <span class="input-group-text bg-white">
                                        <i class="fa-solid fa-cash-register"></i>
                                    </span>

                                    <select class="form-select"
                                            name="paymentMethod"
                                            required>

                                        <option value="CASH" selected>
                                            Cash
                                        </option>

                                        <option value="BANKING">
                                            Banking
                                        </option>

                                    </select>

                                </div>

                                <div class="form-text fst-italic mt-1">
                                    System will create invoice with status "PAID"
                                </div>

                            </div>

                        </div>

                    </div>

                </div>



                <!-- ================= ACTION BUTTONS ================= -->

                <div class="d-flex justify-content-end gap-2 mt-4">

                    <a href="list"
                       class="btn btn-outline-secondary">

                        Cancel

                    </a>

                    <button type="submit"
                            class="btn btn-primary">

                        <i class="fa-solid fa-check me-1"></i>
                        Confirm

                    </button>

                </div>


            </form>

        </div>

    </div>

</div>


<script>
    $(document).ready(function () {
        $('#patientSelect').select2({
            placeholder: "-- Tìm kiếm theo Tên hoặc SĐT --",
            allowClear: true,
            language: {
                noResults: function () {
                    return "Không tìm thấy bệnh nhân nào";
                }
            }
        });
    });

    function filterRooms() {
        var selectedSpecialty = document.getElementById('specialtySelect').value;
        var roomSelect = document.getElementById('roomSelect');
        var options = roomSelect.options;

        roomSelect.value = "";

        for (var i = 1; i < options.length; i++) {
            var option = options[i];
            var roomSpecialty = option.getAttribute('data-specialty');

            if (selectedSpecialty === 'all' || roomSpecialty === selectedSpecialty) {
                option.hidden = false;
                option.disabled = false;
            } else {
                option.hidden = true;
                option.disabled = true;
            }
        }
    }
</script>