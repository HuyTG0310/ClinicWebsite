<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



<div class="d-flex justify-content-between align-items-center mb-4">

    <div>
        <h2 class="mb-1">
            <i class="fas fa-users text-primary me-2"></i>
            Manage Patient
        </h2>
        <p class="text-muted mb-0">Manage and view all patients</p>
    </div>

    <c:if test="${hasPatientCreate}">
        <a href="${basePath}/patient/create" class="btn btn-primary">
            <i class="fas fa-plus-circle me-2"></i>Add New Patient
        </a>
    </c:if>

</div>



<div class="card shadow-sm mb-4">

    <div class="card-body">

        <form method="get" action="${basePath}/patient/list">

            <div class="row g-3">

                <div class="col-md-9">

                    <label class="form-label fw-bold small text-muted">Keyword</label>

                    <div class="input-group">

                        <span class="input-group-text bg-white">
                            <i class="fas fa-search text-muted"></i>
                        </span>

                        <input type="text"
                               class="form-control"
                               name="searchKeyword"
                               value="${searchKeyword}"
                               placeholder="Name, phone or address...">

                    </div>

                </div>

                <div class="col-md-3 d-flex align-items-end gap-2">

                    <button class="btn btn-primary flex-grow-1" type="submit">
                        <i class="fas fa-search me-2"></i>Search
                    </button>

                    <c:if test="${searchKeyword != null}">
                        <a href="${basePath}/patient/list"
                           class="btn btn-outline-secondary px-3"
                           title="Clear Filters">
                            <i class="fas fa-redo-alt"></i>
                        </a>
                    </c:if>

                </div>

            </div>

        </form>

    </div>

</div>



<div class="card shadow-sm">

    <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">

        <h5 class="mb-0">
            <i class="fas fa-list me-2 text-primary"></i>Patient List
        </h5>

        <span class="badge bg-light text-dark border">
            Total: ${patientList.size()} records
        </span>

    </div>

    <div class="card-body p-0">

        <div class="table-responsive">

            <table class="table table-hover align-middle mb-0">

                <thead class="table-light">

                    <tr>

                        <th class="px-4">#ID</th>

                        <th>
                            <i class="fas fa-user me-2 text-primary"></i>Patient
                        </th>

                        <th>
                            <i class="fas fa-phone me-2 text-primary"></i>Phone
                        </th>

                        <th>
                            <i class="fas fa-calendar me-2 text-primary"></i>Date of Birth
                        </th>

                        <th>
                            <i class="fas fa-venus-mars me-2 text-primary"></i>Gender
                        </th>

                        <th class="text-center">
                            <i class="fas fa-cog me-2 text-primary"></i>Actions
                        </th>

                    </tr>

                </thead>

                <tbody>

                    <c:forEach items="${patientList}" var="patient">

                        <tr>

                            <td class="px-4 fw-bold text-secondary">
                                #${patient.patientId}
                            </td>

                            <td>
                                <div class="fw-bold text-primary">
                                    ${patient.fullName}
                                </div>
                            </td>

                            <td>
                                ${patient.phone}
                            </td>

                            <td>
                                <fmt:formatDate value="${patient.dateOfBirth}" pattern="dd/MM/yyyy"/>
                            </td>

                            <td>

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

                            </td>

                            <td class="text-center">

                                <a href="${basePath}/patient/detail?id=${patient.patientId}"
                                   class="btn btn-sm btn-outline-primary"
                                   title="View Detail">

                                    <i class="fas fa-eye me-1"></i>View

                                </a>

                            </td>

                        </tr>

                    </c:forEach>

                    <c:if test="${empty patientList}">
                        <tr>
                            <td colspan="6" class="text-center py-5">
                                <i class="fas fa-users fa-3x text-muted mb-3 d-block"></i>
                                <p class="text-muted mb-0">No patients found</p>
                            </td>
                        </tr>
                    </c:if>

                </tbody>

            </table>

        </div>

    </div>

</div>

<script>
    // Auto-submit form when search keyword is cleared
    const searchKeywordInput = document.getElementById('searchKeyword');
    const searchForm = document.getElementById('searchForm');

    searchKeywordInput?.addEventListener('input', function () {
        // If input is empty, auto-submit the form to reset list
        if (this.value.trim() === '') {
            searchForm.submit();
        }
    });
</script>
