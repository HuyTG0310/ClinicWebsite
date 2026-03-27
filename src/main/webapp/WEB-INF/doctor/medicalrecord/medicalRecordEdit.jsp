<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="d-flex justify-content-between align-items-center mb-4">

    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-file-medical text-primary me-2"></i>
            ${empty app ? 'Medical Record Update' : 'Medical Record Detail'}
        </h2>
        <p class="text-muted mb-0">Healthcare Management Information System (HIS)</p>
    </div>

    <div class="d-flex gap-2">

        <c:choose>

            <c:when test="${empty app}">
                <a href="${basePath}/medical-record/detail?id=${mr.medicalRecordId}"
                   class="btn btn-outline-secondary">
                    <i class="fa-solid fa-arrow-left me-1"></i>Back
                </a>
            </c:when>

            <c:otherwise>
                <a href="${basePath}/queue/list"
                   class="btn btn-outline-secondary">
                    <i class="fa-solid fa-arrow-left me-1"></i>Back to list
                </a>
            </c:otherwise>

        </c:choose>

    </div>

</div>


<c:if test="${sessionScope.success != null}">
    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
        <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% session.removeAttribute("success"); %>
</c:if>

<c:if test="${sessionScope.error != null}">
    <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
        <i class="fas fa-exclamation-triangle me-2"></i>${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% session.removeAttribute("error");%>
</c:if>

<form action="${basePath}/medical-record/edit" method="POST" id="mainRecordForm">
    <input type="hidden" name="appointmentId" value="${app.appointmentId}">
    <input type="hidden" name="patientId" value="${app.patientId}">
    <input type="hidden" name="medicalRecordId" id="mainMedicalRecordId" value="${mr != null ? mr.medicalRecordId : ''}">

    <div class="row g-3">
        <div class="col-xl-3 col-lg-4">
            <div class="card shadow-sm border-0 mb-3 border-4">
                <div class="card-header bg-white py-2">
                    <h5 class="text-primary fw-bold mb-1">${empty app ? mr.patientName : app.patientName}</h5>
                </div>
                <div class="card-body p-3">

                    <div class="text-muted mb-3 small">
                        <i class="fa-solid fa-venus-mars me-1"></i>${empty app ? mr.patientGender : app.patientGender} | 
                        <i class="fa-solid fa-cake-candles me-1 ms-1"></i><fmt:formatDate value="${empty app ? mr.patientDob : app.patientDob}" pattern="dd/MM/yyyy"/>
                    </div>
                    <div class="alert alert-danger p-2 mb-3 small border-danger border-opacity-50">
                        <i class="fa-solid fa-triangle-exclamation me-1"></i><strong>Medical history / Allergies:</strong><br>
                        <em>${allergies}</em> 
                    </div>
                    <div class="small text-muted">
                        <div class="mb-1"><i class="fa-solid fa-phone me-2"></i>${empty app ? mr.patientPhone : app.patientPhone}</div>
                        <div class="mb-1"><i class="fa-solid fa-clock me-2"></i><fmt:formatDate value="${app.appointmentTime}" pattern="HH:mm dd/MM/yyyy"/></div>
                    </div>
                </div>
            </div>

            <div class="card shadow-sm border-0 sticky-top" style="top: 15px; z-index: 10;">
                <div class="card-header bg-white py-2">
                    <h6 class="mb-0 fw-bold text-danger"><i class="fa-solid fa-heart-pulse me-2"></i>Vital singals</h6>
                </div>
                <div class="card-body bg-light p-3">
                    <div class="row g-2 small">
                        <div class="col-6">
                            <label class="form-label fw-bold text-muted mb-1">Blood pressure</label>
                            <input type="text" 
                                   class="form-control form-control-sm" 
                                   name="bloodPressure" 
                                   value="${mr.bloodPressure}" 
                                   placeholder="120/80"
                                   pattern="\d{2,3}/\d{2,3}"
                                   title="Vui lòng nhập đúng định dạng Huyết áp (Ví dụ: 120/80)"
                                   maxlength="7" required>
                        </div>
                        <div class="col-6">
                            <label class="form-label fw-bold text-muted mb-1">Heart beat</label>
                            <div class="input-group input-group-sm">
                                <input type="number" class="form-control" name="heartRate" value="${mr.heartRate}" placeholder="80" min="0" required>
                                <span class="input-group-text">bpm</span>
                            </div>
                        </div>
                        <div class="col-6">
                            <label class="form-label fw-bold text-muted mb-1">Temp</label>
                            <div class="input-group input-group-sm">
                                <input type="number" step="0.1" class="form-control" name="temperature" value="${mr.temperature}" placeholder="37.0" min="0" required>
                                <span class="input-group-text">°C</span>
                            </div>
                        </div>
                        <div class="col-6">
                            <label class="form-label fw-bold text-muted mb-1">Weight</label>
                            <div class="input-group input-group-sm">
                                <input type="number" step="0.1" class="form-control" name="weight" value="${mr.weight}" min="0" required>
                                <span class="input-group-text">kg</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xl-9 col-lg-8">
            <div class="card shadow-sm border-0 h-100 d-flex flex-column">

                <div class="card-header bg-white p-0 border-bottom-0">
                    <ul class="nav nav-tabs nav-justified" id="medicalTabs" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link active fw-bold py-3" id="clinical-tab" data-bs-toggle="tab" data-bs-target="#clinical" type="button" role="tab">
                                <i class="fa-solid fa-stethoscope text-primary me-2"></i>1. Clinical Examination
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link fw-bold py-3" id="lab-tab" data-bs-toggle="tab" data-bs-target="#lab" type="button" role="tab">
                                <i class="fa-solid fa-microscope text-warning me-2"></i>2. Clinical Laboratory
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link fw-bold py-3" id="prescription-tab" data-bs-toggle="tab" data-bs-target="#prescription" type="button" role="tab">
                                <i class="fa-solid fa-pills text-success me-2"></i>3. Treatment & Prescription
                            </button>
                        </li>
                    </ul>
                </div>

                <div class="card-body p-4 bg-light flex-grow-1">
                    <div class="tab-content" id="medicalTabsContent">

                        <div class="tab-pane fade show active" id="clinical" role="tabpanel">
                            <div class="mb-3">
                                <label class="form-label fw-bold">Symptoms</label>
                                <textarea class="form-control" name="symptom" rows="3" placeholder="Nhập lời kể của bệnh nhân...">${mr.symptom}</textarea>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">General examination & body parts</label>
                                <textarea class="form-control" name="physicalExam" rows="4" placeholder="Ghi nhận khám thực thể...">${mr.physicalExam}</textarea>
                            </div>
                            <div class="mb-0">
                                <label class="form-label fw-bold text-muted">Internal notes</label>
                                <textarea class="form-control" name="doctorNotes" rows="2" placeholder="Ghi chú dành riêng cho bác sĩ...">${mr.doctorNotes}</textarea>
                            </div>
                        </div>

                        <div class="tab-pane fade" id="lab" role="tabpanel">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <!--<h5 class="fw-bold text-dark mb-0"><i class="fa-solid fa-list-check me-2"></i>Designation List</h5>-->
                                <div class="fw-bold text-dark mb-0"><i class="fa-solid fa-list-check me-2"></i>Designation List</div>
                                <c:if test="${not empty app}">
                                    <button type="button" class="btn btn-warning fw-bold shadow-sm" onclick="openLabModal()">
                                        <i class="fa-solid fa-plus me-2"></i>Assign new service
                                    </button>
                                </c:if>
                            </div>

                            <div class="table-responsive">
                                <table class="table table-hover table-bordered align-middle bg-white shadow-sm">
                                    <thead class="table-light">
                                        <tr>
                                            <th width="15%" class="text-center">Batch ID</th>
                                            <th width="20%">Created time</th>
                                            <th width="35%">Service list</th>
                                            <th width="15%" class="text-center">Status</th>
                                            <th width="15%" class="text-center">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${not empty batches}">
                                                <c:forEach items="${batches}" var="b">
                                                    <tr>
                                                        <td class="text-center fw-bold text-primary">#BATCH-${b.batchId}</td>
                                                        <td>
                                                            <div class="fw-bold text-dark">
                                                                <fmt:formatDate value="${b.orderTime}" pattern="HH:mm dd/MM/yyyy"/>
                                                            </div>
                                                            <small class="text-muted">BS. ${b.doctorName}</small>
                                                        </td>
                                                        <td>
                                                            <ul class="mb-0 ps-3 small">
                                                                <c:forEach items="${b.testNames}" var="testName">
                                                                    <li>${testName}</li>
                                                                    </c:forEach>
                                                            </ul>
                                                        </td>
                                                        <td class="text-center">
                                                            <c:if test="${b.status == 'CREATED'}"><span class="badge bg-secondary">Created</span></c:if>
                                                            <c:if test="${b.status == 'IN_PROGRESS'}"><span class="badge bg-info text-dark">In progress</span></c:if>
                                                            <c:if test="${b.status == 'COMPLETED'}"><span class="badge bg-success">Completed</span></c:if>
                                                            </td>
                                                            <td class="text-center">
                                                                <a href="${basePath}/lab-order/print?batchId=${b.batchId}" 
                                                               target="_blank" 
                                                               class="btn btn-sm btn-outline-primary mb-1 w-100">
                                                                <i class="fa-solid fa-print me-1"></i> Print
                                                            </a>
                                                            <button type="button" class="btn btn-sm btn-outline-danger w-100" 
                                                                    ${b.status == 'COMPLETED' ? '' : 'disabled'} 
                                                                    onclick="filterResults(${b.batchId})">
                                                                <i class="fa-solid fa-filter me-1"></i> View
                                                            </button>
                                                            <c:if test="${not empty app && b.status == 'CREATED'}">
                                                                <button type="button" class="btn btn-sm btn-outline-warning w-100 mt-1" 
                                                                        onclick="openEditLabModal(${b.batchId}, '${b.testIds}')">
                                                                    <i class="fa-solid fa-pen-to-square me-1"></i> Edit
                                                                </button>
                                                                <button type="button" class="btn btn-sm btn-outline-danger w-100 mt-1" 
                                                                        onclick="submitCancelBatch(${b.batchId}, ${app.appointmentId})">
                                                                    <i class="fa-solid fa-trash-can me-1"></i> Cancel
                                                                </button>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td colspan="5" class="text-center py-4 text-muted">
                                                        <i class="fa-solid fa-flask fs-2 mb-2 opacity-50"></i>
                                                        <p class="mb-0">The patient has not yet been ordered any further diagnostic tests.</p>
                                                    </td>
                                                </tr>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                            <c:if test="${not empty consolidatedResults}">
                                <hr class="my-4 border-secondary opacity-25">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <!--                                    <h5 class="fw-bold text-dark mb-0">
                                                                            <i class="fa-solid fa-chart-line me-2"></i>Summary of Results
                                                                        </h5>-->
                                    <div class="fw-bold text-dark mb-0">
                                        <i class="fa-solid fa-chart-line me-2"></i>Summary of Results
                                    </div>
                                    <button type="button" class="btn btn-sm btn-outline-secondary fw-bold" onclick="filterResults('all')">
                                        <i class="fa-solid fa-rotate me-1"></i>View all
                                    </button>
                                </div>

                                <div class="table-responsive border rounded shadow-sm">
                                    <table class="table table-sm table-hover align-middle mb-0 bg-white" id="consolidatedTable">
                                        <thead class="table-success text-center border-bottom border-success">
                                            <tr>
                                                <th width="30%" class="text-start ps-3">Lab test</th>
                                                <th width="15%">Result</th>
                                                <th width="20%">Reference range</th>
                                                <th width="10%">Unit</th>
                                                <th width="15%">Time</th>
                                                <th width="10%">Batch ID</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="currCat" value="" />
                                            <c:forEach items="${consolidatedResults}" var="r">

                                                <c:if test="${r.categoryName != currCat}">
                                                    <tr class="table-light category-header" data-cat="${r.categoryName}">
                                                        <td colspan="6" class="fw-bolder text-uppercase text-primary ps-3 py-2 border-bottom">
                                                            <i class="fa-solid fa-layer-group me-2"></i>${r.categoryName}
                                                        </td>
                                                    </tr>
                                                    <c:set var="currCat" value="${r.categoryName}" />
                                                </c:if>

                                                <tr class="result-row batch-${r.batchId} cat-${r.categoryName} ${r.isAbnormal ? 'table-danger table-opacity-10' : ''}">
                                                    <td class="ps-4">
                                                        <div class="fw-bold text-dark">${r.parameterName}</div>
                                                        <div class="small text-muted fst-italic">${r.testName}</div>
                                                    </td>

                                                    <td class="text-center fw-bold fs-6">
                                                        <c:choose>

                                                            <c:when test="${r.status == 'REJECTED'}">
                                                                <span class="badge bg-danger text-white text-wrap lh-base" style="max-width: 150px; font-size: 0.75rem;" title="${r.rejectReason}">
                                                                    <i class="fa-solid fa-ban me-1"></i>Cancelled: ${r.rejectReason}
                                                                </span>
                                                            </c:when>

                                                            <c:when test="${not empty r.resultValue}">
                                                                <span class="${r.isAbnormal ? 'text-danger' : 'text-success'}">
                                                                    ${r.resultValue}

                                                                    <c:if test="${r.isAbnormal}">
                                                                        <c:choose>
                                                                            <c:when test="${r.flag == 'H'}">
                                                                                <i class="fa-solid fa-arrow-up ms-1" title="Cao"></i>
                                                                            </c:when>
                                                                            <c:when test="${r.flag == 'L'}">
                                                                                <i class="fa-solid fa-arrow-down ms-1" title="Thấp"></i>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <i class="fa-solid fa-asterisk ms-1" style="font-size: 0.75rem;" title="Bất thường"></i>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:if>

                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-warning text-dark"><i class="fa-solid fa-spinner fa-spin me-1"></i>Processing</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>

                                                    <td class="text-center text-muted small">${r.normalRange}</td>
                                                    <td class="text-center text-muted small">${r.unit}</td>

                                                    <td class="text-center small fw-bold text-secondary">
                                                        <c:if test="${not empty r.resultTime}">
                                                            <fmt:formatDate value="${r.resultTime}" pattern="HH:mm"/> <br>
                                                            <span class="fw-normal text-muted" style="font-size: 0.75rem;"><fmt:formatDate value="${r.resultTime}" pattern="dd/MM/yyyy"/></span>
                                                        </c:if>
                                                        <c:if test="${empty r.resultTime}">___</c:if>
                                                        </td>

                                                        <td class="text-center">
                                                            <span class="badge bg-secondary">#${r.batchId}</span>
                                                    </td>
                                                </tr>

                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:if>
                        </div>

                        <div class="tab-pane fade" id="prescription" role="tabpanel">
                            <div class="mb-4">
                                <label class="form-label fw-bold text-dark">Diagnosis (ICD-10) <span class="text-danger">*</span></label>
                                <textarea class="form-control border-primary" name="diagnosis" rows="2" placeholder="Kết luận bệnh lý...">${mr.diagnosis}</textarea>
                            </div>
                            <div class="mb-4">
                                <label class="form-label fw-bold text-dark">Treatment plan</label>
                                <textarea class="form-control" name="treatmentPlan" rows="2" placeholder="Tư vấn dinh dưỡng, sinh hoạt...">${mr.treatmentPlan}</textarea>
                            </div>

                            <div class="card border-success border-opacity-25 shadow-sm">
                                <div class="card-header bg-white py-2 d-flex justify-content-between align-items-center border-bottom-0">
                                    <h6 class="mb-0 fw-bold text-dark"><i class="fa-solid fa-pills me-2"></i>Prescription</h6>
                                    <div>
                                        <c:if test="${mr != null && mr.medicalRecordId > 0}">
                                            <a href="${basePath}/prescription/print?medicalRecordId=${mr.medicalRecordId}" 
                                               target="_blank" class="btn btn-sm btn-outline-success fw-bold me-2">
                                                <i class="fa-solid fa-print me-1"></i>Print
                                            </a>
                                        </c:if>
                                        <button type="button" class="btn btn-sm btn-success fw-bold" onclick="addMedicineRow()">
                                            <i class="fa-solid fa-plus me-1"></i>Add
                                        </button>
                                    </div>
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-borderless align-middle mb-0" id="prescriptionTable">
                                        <thead class="table-light border-top border-bottom">
                                            <tr>
                                                <th width="35%">Medicine</th>
                                                <th width="15%">Qty</th>
                                                <th width="25%">Dosage</th>
                                                <th width="20%">Note</th>
                                                <th width="5%" class="text-center">Delete</th>
                                            </tr>
                                        </thead>
                                        <tbody id="prescriptionBody">
                                            <c:if test="${not empty savedPrescriptions}">
                                                <c:forEach items="${savedPrescriptions}" var="pre">
                                                    <tr class="border-bottom">
                                                        <td class="px-2 py-2">
                                                            <div class="d-flex align-items-center gap-2">
                                                                <select class="form-select med-select flex-grow-1" name="medicineId" required onchange="updateMedInfoBtn(this)">
                                                                    <option value="">-- Choose medicine --</option>
                                                                    <c:forEach var="m" items="${medicines}">
                                                                        <option value="${m.medicineId}" 
                                                                                ${m.medicineId == pre.medicineId ? 'selected' : ''}
                                                                                data-ingredients="${fn:escapeXml(m.ingredients)}" 
                                                                                data-usage="${fn:escapeXml(m.usage)}" 
                                                                                data-contra="${fn:escapeXml(m.contraindication)}"
                                                                                data-name="${fn:escapeXml(m.medicineName)}">
                                                                            ${m.medicineName} (${m.unit})
                                                                        </option>
                                                                    </c:forEach>
                                                                </select>
                                                                <button type="button" class="btn btn-sm btn-link text-info ms-1 btn-med-info d-none" onclick="showMedInfo(this)" title="Xem thông tin thuốc">
                                                                    <i class="fa-solid fa-circle-info"></i>
                                                                </button>
                                                            </div>
                                                        </td>
                                                        <td class="py-2"><input type="number" class="form-control text-center" name="quantity" min="1" value="${pre.quantity}" required></td>
                                                        <td class="py-2"><input type="text" class="form-control" name="dosage" value="${pre.dosage}" required pattern=".*\S+.*" title="Please enter valid dosage"></td>
                                                        <td class="py-2"><input type="text" class="form-control" name="note" value="${pre.note}" required pattern=".*\S+.*" title="Please enter valid note"></td>
                                                        <td class="text-center py-2"><button type="button" class="btn btn-sm btn-outline-danger border-0 btn-remove-row"><i class="fa-solid fa-trash-can"></i></button></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

                <div class="card-footer bg-white border-top p-3 d-flex justify-content-between align-items-center">
                    <div class="d-flex align-items-center">

                    </div>
                    <div>
                        <c:choose>
                            <c:when test="${empty app}">
                                <button type="submit" name="action" value="update_only" class="btn btn-primary px-5 fw-bold shadow-sm">
                                    <i class="fa-solid fa-save me-2"></i>Save
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button type="submit" name="action" value="save_draft" class="btn btn-warning fw-bold shadow-sm me-2">
                                    <i class="fa-solid fa-floppy-disk me-2"></i>Save draft
                                </button>

                                <button type="submit" name="action" value="complete_print" class="btn btn-success px-4 fw-bold shadow-sm me-2">
                                    <i class="fa-solid fa-print me-2"></i>Finish (print)
                                </button>

                                <button type="submit" name="action" value="complete" class="btn btn-primary px-4 fw-bold shadow-sm">
                                    <i class="fa-solid fa-check-double me-2"></i>Finish (no print)
                                </button>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>

            </div>
        </div>
    </div>
</form>
<div class="modal fade" id="addLabOrderModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header bg-white text-dark">
                <h5 class="modal-title fw-bold"><i class="fa-solid fa-microscope me-2"></i>Create a new form</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form id="labOrderForm" action="${basePath}/lab-order/create" method="POST">
                <input type="hidden" name="medicalRecordId" id="modalMedicalRecordId" value="${mr != null ? mr.medicalRecordId : ''}">
                <input type="hidden" name="appointmentId" value="${app.appointmentId}">
                <input type="hidden" name="patientId" value="${app.patientId}">
                <input type="hidden" name="batchId" id="modalBatchId" value="">

                <div class="modal-body bg-light">
                    <div id="missingRecordWarning" class="alert alert-danger d-none">
                        <i class="fa-solid fa-triangle-exclamation me-2"></i>Please clicks <strong>"Save draft"</strong> before creating a new form!
                    </div>

                    <div class="row g-4" id="labCheckboxesArea">
                        <c:set var="currentCategory" value="" />
                        <c:forEach items="${labTests}" var="t">
                            <c:if test="${t.categoryName != currentCategory}">
                                <c:if test="${not empty currentCategory}">
                                </div>
                            </div>
                        </c:if>
                        <div class="col-md-4">
                            <div class="card border-0 shadow-sm p-3 h-100">
                                <h6 class="text-primary fw-bold border-bottom pb-2 mb-3">${t.categoryName}</h6>
                                <c:set var="currentCategory" value="${t.categoryName}" />
                            </c:if>

                            <div class="form-check mb-2">
                                <c:set var="isOrdered" value="${orderedTestIds != null && orderedTestIds.contains(t.labTestId)}" />

                                <input class="form-check-input lab-test-checkbox ${isOrdered ? 'bg-secondary border-secondary' : 'border-secondary'}" 
                                       type="checkbox" name="labTestIds" value="${t.labTestId}" id="new_test_${t.labTestId}"
                                       data-testid="${t.labTestId}"
                                       data-ordered="${isOrdered}" ${isOrdered ? 'checked disabled' : ''}>

                                <label class="form-check-label d-flex justify-content-between w-100 ${isOrdered ? 'text-muted text-decoration-line-through' : ''}" 
                                       for="new_test_${t.labTestId}">
                                    <span>${t.testName} <c:if test="${t.isPanel}"><span class="badge bg-info text-dark ms-1" style="font-size:0.6rem;">GÓI</span></c:if></span>

                                        <span class="${isOrdered ? 'text-muted' : 'text-danger fw-bold'} ms-2">
                                        <fmt:formatNumber value="${t.currentPrice}" pattern="#,###"/> đ
                                    </span>
                                </label>
                            </div>
                        </c:forEach>
                        <c:if test="${not empty currentCategory}">
                        </div>
                    </div>
                </c:if>
        </div>
    </div>

    <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button type="submit" class="btn btn-primary fw-bold" id="btnSubmitLabOrder">
            <i class="fa-solid fa-file-signature me-2"></i>Create
        </button>
    </div>
</form>
</div>
</div>
</div>



<div class="modal fade" id="medicineInfoModal" tabindex="-1" aria-hidden="true">

    <div class="modal-dialog modal-dialog-centered">

        <div class="modal-content border-0 shadow">

            <div class="modal-header">

                <h5 class="modal-title">
                    <i class="fa-solid fa-pills text-dark me-2"></i>
                    <span id="infoMedName">Medicine</span>
                </h5>

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"></button>

            </div>


            <div class="modal-body">

                <div class="mb-3">

                    <h6 class="fw-bold text-dark border-bottom pb-1">
                        <i class="fa-solid fa-flask me-2"></i>
                        Ingredients
                    </h6>

                    <p id="infoMedIngredients"
                       class="text-muted small mb-0">
                        No data
                    </p>

                </div>


                <div class="mb-3">

                    <h6 class="fw-bold text-dark border-bottom pb-1">
                        <i class="fa-solid fa-book-medical me-2"></i>
                        Usage
                    </h6>

                    <p id="infoMedUsage"
                       class="text-muted small mb-0">
                        No data
                    </p>

                </div>


                <div class="mb-0">

                    <h6 class="fw-bold text-dark border-bottom pb-1">
                        <i class="fa-solid fa-triangle-exclamation me-2"></i>
                        Contraindication
                    </h6>

                    <p id="infoMedContra"
                       class="text-muted small mb-0">
                        No data
                    </p>

                </div>

            </div>


            <div class="modal-footer">

                <button type="button"
                        class="btn btn-outline-secondary px-4"
                        data-bs-dismiss="modal">
                    Cancel
                </button>

            </div>

        </div>

    </div>

</div>




<style>
    .nav-tabs .nav-link {
        color: #6c757d;
        border: none;
        border-bottom: 3px solid transparent;
    }
    .nav-tabs .nav-link:hover {
        border-color: transparent;
        background-color: #f8f9fa;
    }
    .nav-tabs .nav-link.active {
        color: #0d6efd;
        border-color: transparent;
        border-bottom: 3px solid #0d6efd;
        background-color: transparent;
    }
    .form-control:focus, .form-check-input:focus {
        border-color: #86b7fe;
        box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.15);
    }
    textarea {
        resize: vertical;
    }

    .d-flex .select2-container{
        flex:1;
    }

    .select2-container .select2-selection--single{
        height:38px;
        display:flex;
        align-items:center;
    }

    .select2-selection__rendered{
        line-height:38px !important;
    }

    .btn-med-info{
        height:38px;
        width:38px;
        display:flex;
        align-items:center;
        justify-content:center;
    }

</style>

<select id="medicineSource" style="display:none;">
    <option value="">-- Choose medicine --</option>
    <c:forEach var="m" items="${medicines}">
        <option value="${m.medicineId}"
                data-ingredients="${fn:escapeXml(m.ingredients)}" 
                data-usage="${fn:escapeXml(m.usage)}" 
                data-contra="${fn:escapeXml(m.contraindication)}"
                data-name="${fn:escapeXml(m.medicineName)}">
            ${m.medicineName} (${m.unit})
        </option>
    </c:forEach>
</select>

<script>
    // --- 1. Khởi tạo Select2 cho Đơn thuốc (Giữ nguyên của bạn) ---
    function initSelect2() {
        $('.med-select').select2({
            placeholder: "-- Tìm thuốc --",
            allowClear: false,
            width: '100%'
        }).on('change', function () {
            // Khi Select2 thay đổi, gọi hàm cập nhật nút Info
            updateMedInfoBtn(this);
        });

        // Khởi tạo trạng thái nút Info cho các dòng đã load sẵn lúc đầu
        $('.med-select').each(function () {
            updateMedInfoBtn(this);
        });
    }

    function updateMedInfoBtn(selectElement) {
        let btn = selectElement.closest('td').querySelector('.btn-med-info');
        if (!btn) {
            btn = selectElement.closest('tr').querySelector('.btn-med-info');
        }
        if (selectElement.value !== "") {
            btn.classList.remove('d-none'); // Hiện nút nếu đã chọn thuốc
        } else {
            btn.classList.add('d-none'); // Ẩn nút nếu chọn rỗng
        }
    }

    function showMedInfo(btnElement) {
        // Dùng jQuery tìm Option đang được chọn
        let $select = $(btnElement).closest('td').find('.med-select');
        let $selectedOption = $select.find(':selected');

        // Lấy data bằng .attr()
        let name = $selectedOption.attr('data-name') || 'Không xác định';
        let ingredients = $selectedOption.attr('data-ingredients') || 'Đang cập nhật...';
        let usage = $selectedOption.attr('data-usage') || 'Đang cập nhật...';
        let contra = $selectedOption.attr('data-contra') || 'Đang cập nhật...';

        // Đổ dữ liệu vào Modal
        document.getElementById('infoMedName').textContent = name;
        document.getElementById('infoMedIngredients').textContent = ingredients;
        document.getElementById('infoMedUsage').textContent = usage;
        document.getElementById('infoMedContra').textContent = contra;

        // Bật Modal
        let infoModal = new bootstrap.Modal(document.getElementById('medicineInfoModal'));
        infoModal.show();
    }


    // Đừng quên gọi initSelect2() khi trang vừa load xong
    document.addEventListener("DOMContentLoaded", function () {
        initSelect2();
    });

    function addMedicineRow() {
        var tbody = document.getElementById('prescriptionBody');
        var tr = document.createElement('tr');
        tr.className = 'border-bottom';

        var optionsHtml = document.getElementById('medicineSource').innerHTML;

        tr.innerHTML =
                '<td class="px-2 py-2">' +
                '   <div class="d-flex align-items-center gap-2">' +
                '       <select class="form-select med-select flex-grow-1" name="medicineId" required onchange="updateMedInfoBtn(this)">' +
                optionsHtml +
                '       </select>' +
                '       <button type="button" class="btn btn-sm btn-link text-info ms-1 btn-med-info d-none" onclick="showMedInfo(this)">' +
                '           <i class="fa-solid fa-circle-info"></i>' +
                '       </button>' +
                '   </div>' +
                '</td>' +
                '<td class="py-2"><input type="number" class="form-control text-center" name="quantity" min="1" value="1" required></td>' +
                '<td class="py-2"><input type="text" class="form-control" name="dosage" placeholder="Sáng 1, Tối 1" required  pattern=".*\\S+.*" title="Please enter a valid dosage"></td>' +
                '<td class="py-2"><input type="text" class="form-control" name="note" placeholder="Uống sau ăn..." required  pattern=".*\\S+.*" title="Please enter a valid note"></td>' +
                '<td class="text-center py-2"><button type="button" class="btn btn-sm btn-outline-danger border-0 btn-remove-row"><i class="fa-solid fa-trash-can"></i></button></td>';

        tbody.appendChild(tr);

        $(tr).find('.med-select').select2({
            placeholder: "-- Tìm thuốc --",
            allowClear: false,
            width: '100%'
        }).on('change', function () {
            updateMedInfoBtn(this);
        });
    }

    $(document).ready(function () {
        // Xóa dòng thuốc
        $('#prescriptionBody').on('click', '.btn-remove-row', function () {
            var $row = $(this).closest('tr');
            try {
                var $select = $row.find('.med-select');
                if ($select.hasClass("select2-hidden-accessible")) {
                    $select.select2('destroy');
                }
            } catch (e) {
            }
            $row.remove();
        });

        // Khởi tạo Select2 lúc load trang
        initSelect2();
    });

    // --- 2. Filter Kết quả Xét nghiệm (Giữ nguyên của bạn) ---
    function filterResults(batchId) {
        if (batchId === 'all') {
            $('.result-row').fadeIn();
            $('.category-header').fadeIn();
        } else {
            $('.result-row').hide();
            $('.batch-' + batchId).fadeIn();
            document.getElementById('consolidatedTable').scrollIntoView({behavior: 'smooth', block: 'start'});
        }
    }

    // --- 3. Hàm xử lý Hủy Lô Xét Nghiệm (Giữ nguyên của bạn) ---
    function submitCancelBatch(batchId, appointmentId) {
        if (confirm('Are you sure you want to cancel all assignments in the Batch? #' + batchId + ' This will automatically delete the corresponding payments.')) {
            let f = document.createElement('form');
            f.action = '${basePath}/lab-order/cancel';
            f.method = 'POST';
            f.innerHTML = '<input type="hidden" name="batchId" value="' + batchId + '">' +
                    '<input type="hidden" name="appointmentId" value="' + appointmentId + '">';
            document.body.appendChild(f);
            f.submit();
        }
    }

    // --- 4. CÁC HÀM MỚI: QUẢN LÝ MODAL CHỈ ĐỊNH XÉT NGHIỆM ---

    // Biến lưu Base URL
    const contextPath = '${basePath}';

    // 4.1. Mở Modal để TẠO MỚI Phiếu (Nâng cấp từ hàm cũ của bạn)
    function openLabModal() {
        var recordId = document.getElementById('modalMedicalRecordId').value;
        var modal = new bootstrap.Modal(document.getElementById('addLabOrderModal'));

        if (!recordId || recordId.trim() === '') {
            document.getElementById('missingRecordWarning').classList.remove('d-none');
            document.getElementById('labCheckboxesArea').style.opacity = '0.5';
            document.getElementById('labCheckboxesArea').style.pointerEvents = 'none';
            document.getElementById('btnSubmitLabOrder').disabled = true;
            modal.show();
            return;
        } else {
            document.getElementById('missingRecordWarning').classList.add('d-none');
            document.getElementById('labCheckboxesArea').style.opacity = '1';
            document.getElementById('labCheckboxesArea').style.pointerEvents = 'auto';
            document.getElementById('btnSubmitLabOrder').disabled = false;
        }

        document.getElementById('labOrderForm').action = contextPath + '/lab-order/create';
        document.getElementById('modalBatchId').value = '';
        document.querySelector('#addLabOrderModal .modal-title').innerHTML = '<i class="fa-solid fa-microscope me-2"></i>Create new form';
        document.getElementById('btnSubmitLabOrder').innerHTML = '<i class="fa-solid fa-file-signature me-2"></i>Create';

        let checkboxes = document.querySelectorAll('.lab-test-checkbox');
        checkboxes.forEach(cb => {
            let isOrderedDb = cb.getAttribute('data-ordered') === 'true';
            let testId = cb.getAttribute('data-testid');
            let label = document.querySelector('label[for="new_test_' + testId + '"]');
            let priceSpan = label ? label.querySelector('span:last-child') : null;

            if (isOrderedDb) {
                // Đã nằm trong DB -> Phủ xám, khóa cứng, gạch ngang
                cb.checked = true;
                cb.disabled = true;
                cb.classList.add('bg-secondary');
                if (label)
                    label.classList.add('text-muted', 'text-decoration-line-through');
                if (priceSpan) {
                    priceSpan.classList.add('text-muted');
                    priceSpan.classList.remove('text-danger', 'fw-bold');
                }
            } else {
                // Chưa chọn -> Trắng trẻo, mở khóa
                cb.checked = false;
                cb.disabled = false;
                cb.classList.remove('bg-secondary');
                if (label)
                    label.classList.remove('text-muted', 'text-decoration-line-through');
                if (priceSpan) {
                    priceSpan.classList.remove('text-muted');
                    priceSpan.classList.add('text-danger', 'fw-bold');
                }
            }
        });

        modal.show();
    }

    // 4.2. Mở Modal để SỬA LÔ (Edit)
    function openEditLabModal(batchId, testIdsStr) {
        let currentTestIds = [];
        if (testIdsStr && testIdsStr.length > 2) {
            currentTestIds = testIdsStr.replace('[', '').replace(']', '').split(',').map(item => parseInt(item.trim()));
        }

        document.getElementById('labOrderForm').action = contextPath + '/lab-order/edit';
        document.getElementById('modalBatchId').value = batchId;
        document.querySelector('#addLabOrderModal .modal-title').innerHTML = '<i class="fa-solid fa-pen-to-square me-2 text-warning"></i>Edit form (Batch #' + batchId + ')';
        document.getElementById('btnSubmitLabOrder').innerHTML = '<i class="fa-solid fa-floppy-disk me-2"></i>Save';

        let checkboxes = document.querySelectorAll('.lab-test-checkbox');
        checkboxes.forEach(cb => {
            let testId = parseInt(cb.getAttribute('data-testid'));
            let isOrderedDb = cb.getAttribute('data-ordered') === 'true';
            let label = document.querySelector('label[for="new_test_' + testId + '"]');
            let priceSpan = label ? label.querySelector('span:last-child') : null;

            if (currentTestIds.includes(testId)) {
                // THUỘC LÔ NÀY -> Bật tick, mở khóa, LỘT SẠCH CSS XÁM GẠCH NGANG!
                cb.checked = true;
                cb.disabled = false;
                cb.classList.remove('bg-secondary');
                if (label)
                    label.classList.remove('text-muted', 'text-decoration-line-through');
                if (priceSpan) {
                    priceSpan.classList.remove('text-muted');
                    priceSpan.classList.add('text-danger', 'fw-bold');
                }
            } else if (isOrderedDb) {
                // THUỘC LÔ KHÁC -> Phủ xám, gạch ngang, khóa cứng
                cb.checked = true;
                cb.disabled = true;
                cb.classList.add('bg-secondary');
                if (label)
                    label.classList.add('text-muted', 'text-decoration-line-through');
                if (priceSpan) {
                    priceSpan.classList.add('text-muted');
                    priceSpan.classList.remove('text-danger', 'fw-bold');
                }
            } else {
                // CHƯA AI CHỌN -> Tắt tick, trắng trẻo, mở khóa
                cb.checked = false;
                cb.disabled = false;
                cb.classList.remove('bg-secondary');
                if (label)
                    label.classList.remove('text-muted', 'text-decoration-line-through');
                if (priceSpan) {
                    priceSpan.classList.remove('text-muted');
                    priceSpan.classList.add('text-danger', 'fw-bold');
                }
            }
        });

        document.getElementById('missingRecordWarning').classList.add('d-none');
        document.getElementById('labCheckboxesArea').style.opacity = '1';
        document.getElementById('labCheckboxesArea').style.pointerEvents = 'auto';
        document.getElementById('btnSubmitLabOrder').disabled = false;

        var modal = new bootstrap.Modal(document.getElementById('addLabOrderModal'));
        modal.show();
    }

    document.addEventListener("DOMContentLoaded", function () {

        // Lấy tab đã lưu
        var activeTab = localStorage.getItem("medical_active_tab");

        if (activeTab) {
            var triggerEl = document.querySelector('[data-bs-target="' + activeTab + '"]');
            if (triggerEl) {
                var tab = new bootstrap.Tab(triggerEl);
                tab.show();
            }
        }

        // Khi click tab -> lưu lại
        var tabButtons = document.querySelectorAll('#medicalTabs button[data-bs-toggle="tab"]');

        tabButtons.forEach(function (btn) {
            btn.addEventListener('shown.bs.tab', function (event) {
                var target = event.target.getAttribute("data-bs-target");
                localStorage.setItem("medical_active_tab", target);
            });
        });

    });
</script>