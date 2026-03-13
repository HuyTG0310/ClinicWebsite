<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!-- HEADER -->
<div class="mb-4">

    <div class="d-flex justify-content-between align-items-center">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-microscope text-primary me-2"></i>
                Enter Test Result
            </h2>

            <p class="text-muted mb-0">
                Medical Record ID:
                <strong class="text-dark">#${mrId}</strong>
            </p>
        </div>


        <div class="d-flex gap-2">

            <a href="${pageContext.request.contextPath}/lab/queue/list"
               class="btn btn-outline-secondary">
                <i class="fa-solid fa-arrow-left me-1"></i>
                Back to list
            </a>

            <c:if test="${isViewMode}">
                <a href="${pageContext.request.contextPath}/lab/test/print?mrId=${mrId}"
                   target="_blank"
                   class="btn btn-outline-primary">
                    <i class="fa-solid fa-print me-1"></i>
                    Print
                </a>
            </c:if>

            <c:if test="${isViewMode}">
                <a href="${pageContext.request.contextPath}/lab/test/edit?mrId=${mrId}&forceEdit=true"
                   class="btn btn-warning fw-bold px-4">
                    <i class="fa-solid fa-pen-to-square me-1"></i>
                    Edit
                </a>
            </c:if>

        </div>

    </div>


    <!-- ALERT MESSAGE -->
    <c:if test="${sessionScope.success != null}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm mt-3" role="alert">
            <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% session.removeAttribute("success"); %>
    </c:if>

    <c:if test="${sessionScope.error != null}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm mt-3" role="alert">
            <i class="fas fa-exclamation-triangle me-2"></i>${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% session.removeAttribute("error");%>
    </c:if>

</div>



<!-- PATIENT INFORMATION -->
<div class="card shadow-sm mb-4">

    <div class="card-header bg-white py-3">
        <h5 class="mb-0">
            <i class="fa-solid fa-address-card me-2 text-primary"></i>
            Patient Information
        </h5>
    </div>

    <div class="card-body">

        <div class="row g-3">

            <div class="col-md-6">

                <div class="info-row">
                    <span class="label">Full Name</span>
                    <span class="value text-danger text-uppercase fw-bold">
                        ${mr.patientName}
                    </span>
                </div>

                <div class="info-row">
                    <span class="label">Diagnosis</span>
                    <span class="value fw-bold">
                        ${mr.diagnosis}
                    </span>
                </div>

                <div class="info-row">
                    <span class="label">Address</span>
                    <span class="value">
                        ${mr.patientAddress}
                    </span>
                </div>

            </div>



            <div class="col-md-6 border-start">

                <div class="info-row ps-3">
                    <span class="label">Gender</span>
                    <span class="value fw-bold">
                        ${mr.patientGender}
                    </span>
                </div>

                <div class="info-row ps-3">
                    <span class="label">Date of Birth</span>
                    <span class="value">
                        <fmt:formatDate value="${mr.patientDob}" pattern="dd/MM/yyyy"/>
                    </span>
                </div>

                <div class="info-row ps-3">
                    <span class="label">Ordering Doctor</span>
                    <span class="value text-primary fw-bold">
                        Dr. ${mr.doctorName}
                    </span>
                </div>

            </div>

        </div>

    </div>

</div>



<!-- TEST RESULT TABLE -->
<div class="card shadow border-0 rounded-4">

    <div class="card-body p-0">

        <form action="${pageContext.request.contextPath}/lab/test/save" method="post">

            <input type="hidden" name="medicalRecordId" value="${mrId}">


            <table class="table table-hover align-middle mb-0">

                <thead class="table-light">

                    <tr>

                        <th class="px-4 text-center" width="10%">
                            Batch
                        </th>

                        <th width="30%">
                            Test Name
                        </th>

                        <th width="20%">
                            Result
                        </th>

                        <th width="10%" class="text-center">
                            Abnormal
                        </th>

                        <th width="15%" class="text-center">
                            Reference Range
                        </th>

                        <th width="10%" class="text-center">
                            Unit
                        </th>

                        <th width="10%" class="text-center">
                            Status
                        </th>

                    </tr>

                </thead>



                <tbody class="border-dark">

                    <c:set var="currentCategory" value="" />
                    <c:set var="currentTest" value="" />

                    <c:forEach items="${tests}" var="t">


                        <c:if test="${t.categoryName != currentCategory}">

                            <tr style="background-color:#d1e7dd;">

                                <td colspan="7"
                                    class="fw-bolder fs-6 text-uppercase text-success py-2 px-3">

                                    <i class="fa-solid fa-layer-group me-2"></i>
                                    ${t.categoryName}

                                </td>

                            </tr>

                            <c:set var="currentCategory" value="${t.categoryName}" />

                        </c:if>



                        <c:if test="${t.testName != currentTest}">

                            <tr class="bg-light">

                                <td colspan="7"
                                    class="fw-bold text-dark py-2 px-4">

                                    <i class="fa-solid fa-vial-virus me-2 text-primary"></i>

                                    ${t.testName}

                                    <span class="badge bg-secondary ms-2 fw-normal">
                                        Batch #${t.batchId}
                                    </span>

                                </td>

                            </tr>

                            <c:set var="currentTest" value="${t.testName}" />

                        </c:if>



                        <tr class="${t.status == 'COMPLETED' ? 'opacity-75' : ''}">

                            <td class="text-center text-muted small">
                                <i class="fa-solid fa-caret-right"></i>
                            </td>

                            <td class="fw-bold ps-4 text-dark">
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
                                        <%-- 🔥 LOGIC Ổ KHÓA THÔNG MINH --%>
                                        <c:set var="isLocked" value="${isViewMode || (t.status == 'COMPLETED' && not isForceEdit)}" />

                                        <%-- Chỉ những ô được mở khóa mới có hidden input để gửi về Server --%>
                                        <c:if test="${not isLocked}">
                                            <input type="hidden" name="orderTestId" value="${t.labOrderTestId}">
                                            <input type="hidden" name="paramId" value="${t.parameterId}">
                                        </c:if>

                                        <c:choose>
                                            <c:when test="${t.isNumeric}">
                                                <input type="number" step="any" 
                                                       class="form-control form-control-sm border-primary shadow-sm ${t.isAbnormal && isLocked ? 'text-danger fw-bold' : ''}"
                                                       name="result_${t.labOrderTestId}_${t.parameterId}"
                                                       value="${t.resultValue}"
                                                       data-min="${t.refMin}"
                                                       data-max="${t.refMax}"
                                                       data-checkbox="flag_${t.labOrderTestId}_${t.parameterId}"
                                                       placeholder="Enter number..."
                                                       ${isLocked ? 'readonly' : 'required'} 
                                                       oninput="checkAutoFlag(this)">
                                            </c:when>

                                            <c:otherwise>
                                                <input type="text" 
                                                       class="form-control form-control-sm border-primary shadow-sm ${t.isAbnormal && isLocked ? 'text-danger fw-bold' : ''}"
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

                                        <span class="text-muted opacity-50">
                                            <i class="fa-solid fa-ban"></i>
                                        </span>

                                    </c:when>



                                    <c:when test="${isViewMode && t.status == 'COMPLETED'}">

                                        <span class="badge ${t.isAbnormal ? 'bg-danger' : 'bg-light text-muted border'}">
                                            ${t.isAbnormal ? 'Abnormal' : 'Normal'}
                                        </span>

                                    </c:when>



                                    <c:otherwise>

                                        <div class="form-check d-flex justify-content-center mb-0">

                                            <input class="form-check-input border-danger" type="checkbox" 
                                                   id="flag_${t.labOrderTestId}_${t.parameterId}"  
                                                   name="flag_${t.labOrderTestId}_${t.parameterId}" 
                                                   value="Y" title="Đánh dấu bất thường"
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



            <div class="card-footer bg-white text-end py-3 border-top">

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

</script>