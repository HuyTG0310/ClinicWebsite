<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- HEADER -->
<div class="container-fluid mb-5">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-clipboard-user text-primary me-2"></i>
                Laboratory Sample Check
            </h2>
            <p class="text-muted mb-0">
                Medical Record ID:
                <strong>#${mrId}</strong>
            </p>
        </div>

        <a href="${basePath}/lab-queue/list"
           class="btn btn-outline-secondary">
            <i class="fa-solid fa-arrow-left me-2"></i>
            Back to list
        </a>

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



    <!-- LAB TEST LIST -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-1">
                <i class="fa-solid fa-vial text-primary me-2"></i>
                Laboratory Tests Awaiting Sample Check-in
            </h5>

            <small class="text-muted">
                Please verify specimen condition before accepting.
            </small>
        </div>



        <div class="card-body p-0">

            <form id="acceptAllForm"
                  action="${basePath}/lab-test/checkin"
                  method="post">

                <input type="hidden" name="mrId" value="${mrId}">
                <input type="hidden" name="action" value="ACCEPT_ALL">

                <div class="table-responsive">

                    <table class="table table-hover align-middle mb-0">

                        <thead class="table-light text-center">

                            <tr>
                                <th width="15%">Test Code</th>
                                <th width="35%" class="text-start ps-4">Test / Service Name</th>
                                <th width="15%">Status</th>
                                <th width="20%">Reject Note</th>
                                <th width="15%">Action</th>
                            </tr>

                        </thead>


                        <tbody>

                            <c:set var="currentCategory" value="" />

                            <c:forEach items="${orderedServices}" var="s">

                                <c:if test="${s.categoryName != currentCategory}">
                                    <tr class="table-light">
                                        <td colspan="5"
                                            class="fw-bold text-uppercase px-4 py-2">

                                            <i class="fa-solid fa-layer-group me-2 text-primary"></i>
                                            ${s.categoryName}

                                        </td>
                                    </tr>

                                    <c:set var="currentCategory"
                                           value="${s.categoryName}" />
                                </c:if>



                                <tr class="${s.status == 'REJECTED' ? 'table-danger text-muted' :
                                             (s.status == 'ACCEPTED' || s.status == 'COMPLETED' ? 'table-success text-muted' : '')}">

                                    <td class="text-center fw-bold">
                                        ${s.testCode}
                                    </td>


                                    <td class="text-start fw-semibold ps-4">

                                        <i class="fa-solid fa-vial-virus me-2
                                           ${s.status == 'ORDERED' ? 'text-primary' : 'text-secondary'}"></i>

                                        <span class="${s.status == 'REJECTED' ? 'text-decoration-line-through' : ''}">
                                            ${s.testName}
                                        </span>

                                        <c:if test="${s.status == 'ORDERED'}">
                                            <input type="hidden"
                                                   name="labOrderTestIds"
                                                   value="${s.labOrderTestId}">
                                        </c:if>

                                    </td>



                                    <td class="text-center">

                                        <c:choose>

                                            <c:when test="${s.status == 'ORDERED'}">
                                                <span class="badge bg-warning text-dark">
                                                    Waiting
                                                </span>
                                            </c:when>

                                            <c:when test="${s.status == 'ACCEPTED' || s.status == 'COMPLETED'}">
                                                <span class="badge bg-success">
                                                    Accepted
                                                </span>
                                            </c:when>

                                            <c:when test="${s.status == 'REJECTED'}">
                                                <span class="badge bg-danger">
                                                    Rejected
                                                </span>
                                            </c:when>

                                        </c:choose>

                                    </td>



                                    <td class="text-center small text-danger fw-semibold">
                                        ${not empty s.rejectReason ? s.rejectReason : '---'}
                                    </td>



                                    <td class="text-center">

                                        <c:if test="${s.status == 'ORDERED'}">

                                            <button type="button"
                                                    class="btn btn-sm btn-outline-danger"
                                                    onclick="openRejectModal(${s.labOrderTestId}, '${s.testName}')">

                                                <i class="fa-solid fa-xmark me-1"></i>
                                                Reject

                                            </button>

                                        </c:if>

                                    </td>

                                </tr>

                            </c:forEach>

                        </tbody>

                    </table>

                </div>



                <div class="card-footer bg-white text-end py-4">

                    <button type="button"
                            class="btn btn-primary fw-bold px-5"
                            onclick="confirmAcceptAll()">

                        <i class="fa-solid fa-check-double me-2"></i>
                        Accept All

                    </button>

                </div>

            </form>

        </div>

    </div>

</div>

<!-- REJECT MODAL -->
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

<style>

    .card{
        border:none;
        border-radius:12px;
    }

    .card-header{
        border-bottom:1px solid rgba(0,0,0,0.08);
    }

    .table-hover tbody tr:hover{
        background-color: rgba(13,110,253,0.05);
    }

    .info-row{
        display:flex;
        margin-bottom:8px;
    }

    .info-row .label{
        width:150px;
        color:#6c757d;
        font-weight:500;
    }

    .info-row .value{
        flex:1;
    }

</style>



<script>

    function openRejectModal(testId, testName) {
        document.getElementById('rejectTestId').value = testId;
        document.getElementById('rejectTestName').innerText = testName;
        document.getElementById('quickRejectReason').value = "";
        document.getElementById('rejectReasonText').value = "";
        var myModal = new bootstrap.Modal(document.getElementById('rejectModal'));
        myModal.show();
    }

    function updateRejectReason() {
        var quickReason = document.getElementById('quickRejectReason').value;

        if (quickReason !== "") {
            document.getElementById('rejectReasonText').value = quickReason;
        }
    }

    function confirmAcceptAll() {
        var orderedTests = document.querySelectorAll('input[name="labOrderTestIds"]');

        if (orderedTests.length === 0) {
            alert("All tests have already been processed.");
            return;
        }

        if (confirm("Confirm accepting " + orderedTests.length + " remaining valid tests?")) {
            document.getElementById('acceptAllForm').submit();
        }
    }

</script>