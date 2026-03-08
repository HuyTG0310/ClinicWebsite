<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid">

    <!-- ================= HEADER ================= -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fas fa-door-open text-primary me-2"></i>
                Add Room
            </h2>

            <p class="text-muted mb-0">
                Create and configure a new examination room
            </p>
        </div>

        <a href="${basePath}/room/list"
           class="btn btn-outline-secondary">

            <i class="fas fa-arrow-left me-2"></i>
            Back to list

        </a>

    </div>



    <!-- ================= ERROR ================= -->

    <c:if test="${not empty error}">
        <div class="alert alert-danger shadow-sm">

            <i class="fas fa-exclamation-triangle me-2"></i>
            ${error}

        </div>
    </c:if>



    <!-- ================= CARD ================= -->

    <div class="card shadow-sm">

        <div class="card-header bg-white py-3">

            <h5 class="mb-0">

                <i class="fas fa-edit text-primary me-2"></i>
                Room Information

            </h5>

        </div>



        <div class="card-body">

            <form action="${basePath}/room/create" method="post">


                <div class="row g-4">


                    <!-- ROOM NAME -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Room Name <span class="text-danger">*</span>
                        </label>

                        <input type="text"
                               class="form-control"
                               name="roomName"
                               value="${roomName}"
                               placeholder="Enter room name"
                               required>

                        <div class="form-text">
                            Room name must be unique
                        </div>

                    </div>



                    <!-- SPECIALTY -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Specialty <span class="text-danger">*</span>
                        </label>

                        <select id="specialtySelect"
                                class="form-select"
                                name="specialtyId"
                                required>

                            <option value="">-- Select Specialty --</option>

                            <c:forEach var="s" items="${specialties}">
                                <option value="${s.specialtyId}"
                                        <c:if test="${s.specialtyId == specialtyId}">selected</c:if>>

                                        ${s.name}

                                </option>
                            </c:forEach>

                        </select>

                    </div>



                    <!-- DOCTOR -->
                    <div class="col-md-6">

                        <label class="form-label fw-semibold">
                            Current Doctor
                        </label>

                        <select id="doctorSelect"
                                name="currentDoctorId"
                                class="form-select">

                            <option value="">-- No Doctor Assigned --</option>

                            <c:forEach var="d" items="${doctors}">

                                <option value="${d.userId}"
                                        <c:if test="${d.userId == currentDoctorId}">selected</c:if>>

                                        ${d.fullName}

                                </option>

                            </c:forEach>

                        </select>

                        <div class="form-text">
                            Only doctors belonging to the selected specialty are listed
                        </div>

                    </div>



                    <!-- ACTIVE STATUS -->
                    <div class="col-md-6 d-flex align-items-end">

                        <div class="form-check form-switch">

                            <input class="form-check-input"
                                   type="checkbox"
                                   id="isActive"
                                   name="isActive"
                                   value="true"
                                   ${isActive != null && isActive == 'true' ? 'checked' : ''}>

                            <label class="form-check-label fw-semibold"
                                   for="isActive">

                                Active Room

                            </label>

                        </div>

                    </div>


                </div>



                <!-- ================= ACTION BUTTONS ================= -->

                <div class="d-flex justify-content-end gap-2 mt-4">

                    <a href="${basePath}/room/list"
                       class="btn btn-outline-secondary">

                        Cancel

                    </a>

                    <button type="submit"
                            class="btn btn-primary">

                        <i class="fas fa-save me-1"></i>
                        Add Room

                    </button>

                </div>


            </form>

        </div>

    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>


<script>
    document.addEventListener("DOMContentLoaded", function () {

        const specialtySelect = document.getElementById("specialtySelect");
        const doctorSelect = document.getElementById("doctorSelect");

        if (!specialtySelect || !doctorSelect) {
            console.error("Select elements not found");
            return;
        }

        specialtySelect.addEventListener("change", function () {
            const specialtyId = this.value;

            doctorSelect.innerHTML =
                    '<option value="">-- No Doctor Assigned --</option>';

            if (!specialtyId)
                return;

            fetch('${pageContext.request.contextPath}/api/doctors-by-specialty?specialtyId=' + specialtyId)
                    .then(res => res.json())
                    .then(data => {
                        data.forEach(d => {
                            const opt = document.createElement("option");
                            opt.value = d.userId;
                            opt.textContent = d.fullName;
                            doctorSelect.appendChild(opt);
                        });
                    })
                    .catch(err => console.error("Load doctors failed:", err));
        });
    });
</script>
