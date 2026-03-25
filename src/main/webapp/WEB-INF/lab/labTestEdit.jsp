<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!-- HEADER -->
<div class="container-fluid mb-5">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-microscope text-primary me-2"></i>
                Enter Test Result
            </h2>

            <p class="text-muted mb-0">
                Medical Record ID:
                <strong>#${mrId}</strong>
            </p>
        </div>

        <div class="d-flex gap-2">

            <a href="${basePath}/lab-queue/list"
               class="btn btn-outline-secondary">
                <i class="fa-solid fa-arrow-left me-2"></i>
                Back to list
            </a>

            <c:if test="${isViewMode}">
                <a href="${basePath}/lab-test/print?mrId=${mrId}"
                   target="_blank"
                   class="btn btn-primary">
                    <i class="fa-solid fa-print me-2"></i>
                    Print
                </a>
            </c:if>

            <c:if test="${isViewMode}">
                <a href="${basePath}/lab-test/edit?mrId=${mrId}&forceEdit=true"
                   class="btn btn-warning">
                    <i class="fa-solid fa-pen-to-square me-2"></i>
                    Edit
                </a>
            </c:if>

        </div>

    </div>



    <!-- ALERT -->
    <c:if test="${sessionScope.success != null}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm">
            <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% session.removeAttribute("success"); %>
    </c:if>

    <c:if test="${sessionScope.error != null}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm">
            <i class="fas fa-exclamation-triangle me-2"></i>${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% session.removeAttribute("error");%>
    </c:if>



    <!-- PATIENT INFORMATION -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-user text-primary me-2"></i>
                Patient Information
            </h5>
        </div>

        <div class="card-body">

            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">Full Name</label>
                    <div class="fw-semibold text-uppercase text-primary">
                        ${mr.patientName}
                    </div>
                </div>

                <div class="col-md-3">
                    <label class="text-muted small">Gender</label>
                    <div class="fw-semibold">
                        ${mr.patientGender}
                    </div>
                </div>

                <div class="col-md-3">
                    <label class="text-muted small">Date of Birth</label>
                    <div class="fw-semibold">
                        <fmt:formatDate value="${mr.patientDob}" pattern="dd/MM/yyyy"/>
                    </div>
                </div>

            </div>


            <div class="row">

                <div class="col-md-6">
                    <label class="text-muted small">Diagnosis</label>
                    <div class="fw-semibold">
                        ${mr.diagnosis}
                    </div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Ordering Doctor</label>
                    <div class="fw-semibold text-primary">
                        Dr. ${mr.doctorName}
                    </div>
                </div>

            </div>


            <div class="mt-3">
                <label class="text-muted small">Address</label>
                <div>
                    ${mr.patientAddress}
                </div>
            </div>

        </div>

    </div>



    <!-- TEST RESULT TABLE -->
    <div class="card shadow-sm">

        <div class="card-body p-0">

            <form action="${basePath}/lab-test/save" method="post">

                <input type="hidden" name="medicalRecordId" value="${mrId}">


                <table class="table table-hover align-middle mb-0">

                    <thead class="table-light">

                        <tr>

                            <th class="px-4 text-center" width="10%">Batch</th>
                            <th width="30%">Test Name</th>
                            <th width="20%">Result</th>
                            <th width="10%" class="text-center">Abnormal</th>
                            <th width="15%" class="text-center">Reference Range</th>
                            <th width="10%" class="text-center">Unit</th>
                            <th width="10%" class="text-center">Status</th>

                        </tr>

                    </thead>


                    <tbody>

                        <c:set var="currentCategory" value="" />
                        <c:set var="currentTest" value="" />

                        <c:forEach items="${tests}" var="t">


                            <c:if test="${t.categoryName != currentCategory}">

                                <tr class="table-light">

                                    <td colspan="7" class="fw-bold text-uppercase px-4 py-2">

                                        <i class="fa-solid fa-layer-group me-2 text-primary"></i>
                                        ${t.categoryName}

                                    </td>

                                </tr>

                                <c:set var="currentCategory" value="${t.categoryName}" />

                            </c:if>



                            <c:if test="${t.testName != currentTest}">
                                <tr class="bg-light">
                                    <td colspan="7" class="px-4 py-2">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div class="fw-semibold">
                                                <i class="fa-solid fa-vial-virus me-2 text-primary"></i>
                                                ${t.testName}
                                                <span class="badge bg-white text-dark border ms-2 shadow-sm">
                                                    Batch #${t.batchId}
                                                </span>
                                            </div>

                                            <c:if test="${not isViewMode and t.status != 'COMPLETED' and t.status != 'REJECTED'}">
                                                <button type="button" 
                                                        class="btn btn-sm btn-outline-danger"
                                                        onclick="openRejectModal(${t.labOrderTestId}, '${t.testName}')">
                                                    <i class="fa-solid fa-ban"></i>
                                                </button>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                                <c:set var="currentTest" value="${t.testName}" />
                            </c:if>



                            <tr class="${t.status == 'COMPLETED' ? 'text-muted' : ''}">

                                <td class="text-center text-muted small">
                                    <i class="fa-solid fa-caret-right"></i>
                                </td>

                                <td class="fw-semibold ps-4">
                                    ${t.parameterName}
                                </td>



                                <td>

                                    <c:choose>

                                        <c:when test="${t.status == 'REJECTED'}">

                                            <span class="badge bg-danger">
                                                Cancelled: ${t.rejectReason}
                                            </span>

                                        </c:when>



                                        <c:otherwise>

                                            <c:set var="isLocked" value="${isViewMode || (t.status == 'COMPLETED' && not isForceEdit)}" />

                                            <c:if test="${not isLocked}">
                                                <input type="hidden" name="orderTestId" value="${t.labOrderTestId}">
                                                <input type="hidden" name="paramId" value="${t.parameterId}">
                                            </c:if>

                                            <c:choose>

                                                <c:when test="${t.isNumeric}">

                                                    <input type="number" step="any"
                                                           class="form-control form-control-sm border-primary ${t.isAbnormal && isLocked ? 'text-danger fw-bold' : ''}"
                                                           name="result_${t.labOrderTestId}_${t.parameterId}"
                                                           value="${t.resultValue}"
                                                           data-min="${t.refMin}"
                                                           data-max="${t.refMax}"
                                                           data-checkbox="flag_${t.labOrderTestId}_${t.parameterId}"
                                                           placeholder="Enter number..."
                                                           ${isLocked ? 'readonly' : 'required'}
                                                           oninput="checkAutoFlag(this)" min="0">

                                                </c:when>

                                                <c:otherwise>

                                                    <input type="text"
                                                           class="form-control form-control-sm border-primary ${t.isAbnormal && isLocked ? 'text-danger fw-bold' : ''}"
                                                           name="result_${t.labOrderTestId}_${t.parameterId}"
                                                           value="${t.resultValue}"
                                                           placeholder="Enter result..."
                                                           ${isLocked ? 'readonly' : 'required'}>

                                                </c:otherwise>

                                            </c:choose>

                                        </c:otherwise>

                                    </c:choose>

                                </td>



                                <td class="text-center">

                                    <c:choose>

                                        <c:when test="${t.status == 'REJECTED'}">
                                            <i class="fa-solid fa-ban text-muted"></i>
                                        </c:when>

                                        <c:when test="${isViewMode && t.status == 'COMPLETED'}">

                                            <span class="badge ${t.isAbnormal ? 'bg-danger' : 'bg-light text-dark border'}">
                                                ${t.isAbnormal ? 'Abnormal' : 'Normal'}
                                            </span>

                                        </c:when>

                                        <c:otherwise>

                                            <div class="form-check d-flex justify-content-center mb-0">

                                                <input class="form-check-input border-danger"
                                                       type="checkbox"
                                                       id="flag_${t.labOrderTestId}_${t.parameterId}"
                                                       name="flag_${t.labOrderTestId}_${t.parameterId}"
                                                       value="Y"
                                                       ${t.isAbnormal ? 'checked' : ''}
                                                       ${isLocked ? 'disabled' : ''}>

                                            </div>

                                        </c:otherwise>

                                    </c:choose>

                                </td>



                                <td class="text-center text-muted small">
                                    ${t.normalRange}
                                </td>

                                <td class="text-center text-muted small">
                                    ${t.unit}
                                </td>



                                <td class="text-center">

                                    <c:if test="${t.status == 'COMPLETED'}">
                                        <i class="fa-solid fa-circle-check text-success"></i>
                                    </c:if>

                                    <c:if test="${t.status != 'COMPLETED'}">
                                        <i class="fa-regular fa-clock text-warning"></i>
                                    </c:if>

                                </td>

                            </tr>

                        </c:forEach>

                    </tbody>

                </table>



                <div class="card-footer bg-white text-end py-3">

                    <c:if test="${not isViewMode}">

                        <button type="submit"
                                class="btn btn-primary btn-lg fw-bold px-5">

                            <i class="fa-solid fa-floppy-disk me-2"></i>
                            Save

                        </button>

                    </c:if>

                </div>

            </form>

        </div>

    </div>

</div>



<div class="modal fade" id="rejectModal" tabindex="-1" data-bs-backdrop="static">

    <div class="modal-dialog modal-dialog-centered">

        <div class="modal-content border-0 shadow">

            <div class="modal-header">

                <h5 class="modal-title">
                    <i class="fa-solid fa-triangle-exclamation text-dark me-2"></i>
                    Reject Laboratory Test
                </h5>

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"></button>

            </div>



            <form action="${basePath}/lab-test/checkin" method="post">

                <div class="modal-body p-4">

                    <input type="hidden" name="mrId" value="${mrId}">
                    <input type="hidden" name="action" value="REJECT_SINGLE">
                    <input type="hidden" name="rejectTestId" id="rejectTestId">


                    <p class="mb-3 text-muted">
                        You are rejecting the following test:
                    </p>


                    <div class="border rounded p-3 text-center fw-bold text-danger bg-light">
                        <span id="rejectTestName"></span>
                    </div>


                    <div class="mb-3">

                        <label class="form-label fw-semibold">
                            Reject Reason
                        </label>


                        <select class="form-select mb-2"
                                id="quickRejectReason"
                                onchange="updateRejectReason()">

                            <option value="">-- Select common reason --</option>

                            <option value="Blood sample clotted">
                                Blood sample clotted
                            </option>

                            <option value="Hemolyzed sample">
                                Hemolyzed sample
                            </option>

                            <option value="Insufficient specimen volume">
                                Insufficient specimen volume
                            </option>

                            <option value="Contaminated specimen">
                                Contaminated specimen
                            </option>

                            <option value="Analyzer under maintenance">
                                Analyzer under maintenance
                            </option>

                            <option value="Patient refused sample collection">
                                Patient refused sample collection
                            </option>

                        </select>


                        <textarea class="form-control"
                                  name="rejectReason"
                                  id="rejectReasonText"
                                  rows="2"
                                  required
                                  placeholder="Or enter another reason..."></textarea>

                    </div>


                    <p class="text-muted small mb-0">
                        <i class="fa-solid fa-circle-info me-1 text-warning"></i>
                        Rejected services will be sent to cashier for refund.
                    </p>

                </div>



                <div class="modal-footer">

                    <button type="button"
                            class="btn btn-outline-secondary"
                            data-bs-dismiss="modal">
                        Cancel
                    </button>

                    <button type="submit"
                            class="btn btn-primary">
                        Confirm Reject
                    </button>

                </div>

            </form>

        </div>

    </div>

</div>



<script>

    function checkAutoFlag(inputElement) {

        let val = parseFloat(inputElement.value);
        let minStr = inputElement.getAttribute('data-min');
        let maxStr = inputElement.getAttribute('data-max');
        let checkboxId = inputElement.getAttribute('data-checkbox');
        let checkbox = document.getElementById(checkboxId);

        if (!isNaN(val)) {

            let isAbnormal = false;

            if (minStr !== "" && minStr !== null) {
                if (val < parseFloat(minStr))
                    isAbnormal = true;
            }

            if (maxStr !== "" && maxStr !== null) {
                if (val > parseFloat(maxStr))
                    isAbnormal = true;
            }

            if (checkbox) {
                checkbox.checked = isAbnormal;
            }

            if (isAbnormal) {
                inputElement.classList.add('text-danger', 'fw-bold');
            } else {
                inputElement.classList.remove('text-danger', 'fw-bold');
            }

        } else {

            inputElement.classList.remove('text-danger', 'fw-bold');

            if (checkbox)
                checkbox.checked = false;

        }

    }

    function openRejectModal(testId, testName) {
        // Gắn data vào Modal
        document.getElementById('rejectTestId').value = testId;
        document.getElementById('rejectTestName').innerText = testName;

        // Reset dữ liệu cũ
        document.getElementById('quickRejectReason').value = '';
        document.getElementById('rejectReasonText').value = '';

        // Bật Modal
        var rejectModal = new bootstrap.Modal(document.getElementById('rejectModal'));
        rejectModal.show();
    }

    function updateRejectReason() {
        var select = document.getElementById('quickRejectReason');
        var textarea = document.getElementById('rejectReasonText');
        if (select.value !== "") {
            textarea.value = select.value;
        } else {
            textarea.value = '';
        }
    }

</script>