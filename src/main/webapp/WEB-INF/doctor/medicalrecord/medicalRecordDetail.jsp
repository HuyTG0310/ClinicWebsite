<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="container-fluid">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fas fa-file-medical text-primary me-2"></i>
                Medical Record Detail
            </h2>
            <p class="text-muted mb-0">
                Record ID: <strong>#${mr.medicalRecordId}</strong>
            </p>
        </div>

        <div class="d-flex gap-2">

            <a href="${basePath}/medical-record/list" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-2"></i>Back to list
            </a>

            <c:if test="${not empty consolidatedResults}">
                <a href="${basePath}/lab-test/print?mrId=${mr.medicalRecordId}"
                   target="_blank"
                   class="btn btn-info text-white">
                    <i class="fas fa-flask me-2"></i>Print Test Results
                </a>
            </c:if>

            <c:if test="${not empty mr.prescriptions}">
                <a href="${basePath}/prescription/print?medicalRecordId=${mr.medicalRecordId}"
                   target="_blank"
                   class="btn btn-success">
                    <i class="fas fa-file-prescription me-2"></i>Print Prescription
                </a>
            </c:if>

            <a href="${basePath}/medical-record/print?id=${mr.medicalRecordId}"
               target="_blank"
               class="btn btn-primary">
                <i class="fas fa-print me-2"></i>Print Record
            </a>

            <c:if test="${canEdit}">
                <a href="${basePath}/medical-record/edit?id=${mr.medicalRecordId}"
                   class="btn btn-warning">
                    <i class="fas fa-edit me-2"></i>Edit
                </a>
            </c:if>

        </div>
    </div>

    <!-- ALERT -->
    <c:if test="${sessionScope.success != null}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="fas fa-check-circle me-2"></i>
            ${sessionScope.success}

            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("success"); %>
    </c:if>

    <c:if test="${sessionScope.error != null}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
            <i class="fas fa-exclamation-triangle me-2"></i>
            ${sessionScope.error}

            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("error");%>
    </c:if>

    <!-- PATIENT INFORMATION -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fas fa-user-circle text-primary me-2"></i>
                Patient Information
            </h5>
        </div>

        <div class="card-body">

            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">Patient Name</label>
                    <div class="fs-5 fw-medium">${mr.patientName}</div>
                </div>

                <div class="col-md-3">
                    <label class="text-muted small">Gender</label>
                    <div class="fw-semibold">${mr.patientGender}</div>
                </div>

                <div class="col-md-3">
                    <label class="text-muted small">Year of Birth</label>
                    <div class="fw-semibold">
                        <fmt:formatDate value="${mr.patientDob}" pattern="yyyy"/>
                    </div>
                </div>

            </div>

            <div>
                <label class="text-muted small">Phone</label>
                <div>
                    <span class="badge bg-light text-dark border">
                        <i class="fas fa-phone me-1"></i>
                        ${mr.patientPhone}
                    </span>
                </div>
            </div>

        </div>
    </div>


    <!-- VITAL SIGNS -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fas fa-heartbeat text-danger me-2"></i>
                Vital Signs
            </h5>
        </div>

        <div class="card-body">

            <div class="row">

                <div class="col-md-3 mb-3">
                    <label class="text-muted small">Heart Rate</label>
                    <div class="fw-semibold">
                        ${mr.heartRate != null ? mr.heartRate : 'N/A'} bpm
                    </div>
                </div>

                <div class="col-md-3 mb-3">
                    <label class="text-muted small">Blood Pressure</label>
                    <div class="fw-semibold">
                        ${not empty mr.bloodPressure ? mr.bloodPressure : 'N/A'} mmHg
                    </div>
                </div>

                <div class="col-md-3 mb-3">
                    <label class="text-muted small">Temperature</label>
                    <div class="fw-semibold">
                        ${mr.temperature != null ? mr.temperature : 'N/A'} °C
                    </div>
                </div>

                <div class="col-md-3 mb-3">
                    <label class="text-muted small">Weight / Height</label>
                    <div class="fw-semibold">
                        ${mr.weight != null ? mr.weight : 'N/A'} kg /
                        ${mr.height != null ? mr.height : 'N/A'} cm
                    </div>
                </div>

            </div>

        </div>
    </div>


    <!-- CLINICAL EXAMINATION -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fas fa-stethoscope text-primary me-2"></i>
                Clinical Examination
            </h5>
        </div>

        <div class="card-body">

            <div class="mb-3">
                <label class="text-muted small">Symptoms</label>
                <div>${mr.symptom}</div>
            </div>

            <div class="mb-3">
                <label class="text-muted small">Physical Examination</label>
                <div>
                    <c:choose>
                        <c:when test="${not empty mr.physicalExam}">
                            ${mr.physicalExam}
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted">No abnormal findings recorded</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </div>
    </div>


    <!-- LAB RESULTS -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fas fa-microscope text-primary me-2"></i>
                Laboratory Results
            </h5>
        </div>

        <div class="card-body p-0">

            <c:choose>

                <c:when test="${not empty consolidatedResults}">

                    <div class="table-responsive">

                        <table class="table table-hover align-middle mb-0">

                            <thead class="table-light">
                                <tr>
                                    <th>Parameter</th>
                                    <th class="text-center">Result</th>
                                    <th class="text-center">Reference</th>
                                    <th class="text-center">Time</th>
                                </tr>
                            </thead>

                            <tbody>

                                <c:set var="currCat" value="" />
                                <c:set var="currTest" value="" />

                                <c:forEach items="${consolidatedResults}" var="r">

                                    <!-- CATEGORY -->
                                    <c:if test="${r.categoryName != currCat}">
                                        <tr class="table-secondary">
                                            <td colspan="4" class="fw-bold">
                                                <i class="fas fa-layer-group me-2"></i>${r.categoryName}
                                            </td>
                                        </tr>
                                        <c:set var="currCat" value="${r.categoryName}" />
                                        <c:set var="currTest" value="" />
                                    </c:if>

                                    <!-- PANEL TEST -->
                                    <c:if test="${r.testName != currTest}">
                                        <c:if test="${r.isPanel}">
                                            <tr class="bg-light">
                                                <td colspan="4" class="fw-semibold fst-italic">
                                                    <i class="fas fa-vial me-2 text-secondary"></i>
                                                    ${r.testName}
                                                </td>
                                            </tr>
                                        </c:if>
                                        <c:set var="currTest" value="${r.testName}" />
                                    </c:if>

                                    <!-- PARAMETER -->
                                    <tr>

                                        <td class="ps-4">
                                            ${r.parameterName}
                                        </td>

                                        <td class="text-center">

                                            <c:choose>

                                                <c:when test="${r.status == 'REJECTED'}">
                                                    <span class="badge bg-danger text-wrap text-start lh-base"
                                                          style="max-width:150px;"
                                                          title="${r.rejectReason}">
                                                        <i class="fas fa-ban me-1"></i>
                                                        Cancelled: ${r.rejectReason}
                                                    </span>
                                                </c:when>

                                                <c:when test="${not empty r.resultValue}">
                                                    <span class="${r.isAbnormal ? 'text-danger fw-bold' : ''}">
                                                        ${r.resultValue}
                                                    </span>
                                                </c:when>

                                                <c:otherwise>
                                                    <span class="badge bg-warning text-dark">
                                                        Running
                                                    </span>
                                                </c:otherwise>

                                            </c:choose>

                                        </td>

                                        <td class="text-center text-muted">
                                            ${r.normalRange}
                                        </td>

                                        <td class="text-center text-muted small">
                                            <c:if test="${not empty r.resultTime}">
                                                <fmt:formatDate value="${r.resultTime}" pattern="HH:mm dd/MM/yyyy"/>
                                            </c:if>
                                        </td>

                                    </tr>

                                </c:forEach>

                            </tbody>

                        </table>

                    </div>

                </c:when>

                <c:otherwise>

                    <div class="p-5 text-center text-muted">
                        <i class="fas fa-flask fa-2x mb-2"></i>
                        <div>No laboratory tests ordered</div>
                    </div>

                </c:otherwise>

            </c:choose>

        </div>
    </div>



    <!--TREATMENT CONCLUSION-->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fas fa-notes-medical text-danger me-2"></i>
                Treatment Conclusion
            </h5>
        </div>

        <div class="card-body">

            <div class="mb-3">
                <label class="text-muted small">Final Diagnosis</label>
                <div class="fw-semibold text-danger fs-5">
                    ${mr.diagnosis}
                </div>
            </div>

            <div class="mb-3">
                <label class="text-muted small">Treatment Plan</label>
                <div style="white-space: pre-wrap;">
                    ${mr.treatmentPlan}
                </div>
            </div>

        </div>
    </div>

    <!-- PRESCRIPTIONS -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fas fa-pills text-primary me-2"></i>
                Prescriptions
            </h5>
        </div>

        <div class="card-body p-0">

            <c:choose>

                <c:when test="${not empty mr.prescriptions}">

                    <div class="table-responsive">

                        <table class="table table-hover align-middle mb-0">

                            <thead class="table-light">
                                <tr>
                                    <th>#</th>
                                    <th>Medicine</th>
                                    <th class="text-center">Unit</th>
                                    <th class="text-center">Qty</th>
                                    <th>Dosage</th>
                                </tr>
                            </thead>

                            <tbody>

                                <c:forEach items="${mr.prescriptions}" var="p" varStatus="loop">

                                    <tr>

                                        <td class="text-muted">
                                            ${loop.index + 1}
                                        </td>

                                        <td class="fw-semibold">
                                            ${p.medicineName}
                                        </td>

                                        <td class="text-center">
                                            ${p.unit}
                                        </td>

                                        <td class="text-center fw-bold text-primary">
                                            ${p.quantity}
                                        </td>

                                        <td>
                                            ${p.dosage}

                                            <c:if test="${not empty p.note}">
                                                <div class="small text-muted mt-1">
                                                    ${p.note}
                                                </div>
                                            </c:if>

                                        </td>

                                    </tr>

                                </c:forEach>

                            </tbody>

                        </table>

                    </div>

                </c:when>

                <c:otherwise>

                    <div class="p-5 text-center text-muted">
                        <i class="fas fa-file-prescription fa-2x mb-2"></i>
                        <div>No prescriptions recorded</div>
                    </div>

                </c:otherwise>

            </c:choose>

        </div>
    </div>



    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fas fa-clock text-primary me-2"></i>
                Visit Information
            </h5>
        </div>

        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <label class="text-muted small">Follow-up Date</label>

                    <div>
                        <c:choose>
                            <c:when test="${not empty mr.followUpDate}">
                                <fmt:formatDate value="${mr.followUpDate}" pattern="dd/MM/yyyy"/>
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">
                                    No follow-up required
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Attending Doctor</label>

                    <div class="fw-semibold">
                        Dr. ${mr.doctorName}
                    </div>
                </div>
            </div>

            <c:if test="${not empty mr.doctorNotes}">
                <div class="alert alert-warning mt-3 mb-0">
                    <i class="fas fa-lock me-2"></i>
                    <strong>Internal Notes</strong>

                    <div class="mt-2" style="white-space: pre-wrap;">
                        ${mr.doctorNotes}
                    </div>
                </div>
            </c:if>

        </div>
    </div>

</div>