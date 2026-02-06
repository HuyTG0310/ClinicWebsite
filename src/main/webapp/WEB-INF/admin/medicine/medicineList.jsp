<%-- 
    Document   : medicineList
    Created on : Feb 4, 2026, 2:51:32 PM
    Author     : TRUONGTHINHNGUYEN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<h4 class="mb-4 fw-semibold text-primary">
    <i class="fa-solid fa-pills me-2"></i> Medicine Management
</h4>

<div class="card shadow-sm border-0">
    <div class="card-body">

        <!-- SEARCH + ADD -->
        <div class="d-flex align-items-end gap-3 mb-3">

    <!-- SEARCH FORM -->
    <div class="flex-grow-1">
        <div class="card-body p-0">
            <form method="get"
                  action="${pageContext.request.contextPath}/admin/medicine">

                <div class="row g-3 align-items-end">

                    <!-- KEYWORD -->
                    <div class="col-lg-6 col-md-12">
                        <label class="form-label fw-semibold">Medicine Name</label>
                        <div class="input-group">
                            <span class="input-group-text bg-white">
                                <i class="fa-solid fa-magnifying-glass text-muted"></i>
                            </span>
                            <input type="text"
                                   name="keyword"
                                   value="${keyword}"
                                   class="form-control"
                                   placeholder="Search medicine..." />
                        </div>
                    </div>

                    <!-- STATUS -->
                    <div class="col-lg-3 col-md-6">
                        <label class="form-label fw-semibold">Status</label>
                        <select name="status" class="form-select">
                            <option value="all"
                                    ${status == 'all' || empty status ? 'selected' : ''}>
                                All
                            </option>
                            <option value="active"
                                    ${status == 'active' ? 'selected' : ''}>
                                Active
                            </option>
                            <option value="inactive"
                                    ${status == 'inactive' ? 'selected' : ''}>
                                Inactive
                            </option>
                        </select>
                    </div>

                    <!-- SEARCH BUTTON -->
                    <div class="col-lg-2 col-md-6">
                        <button type="submit"
                                class="btn btn-primary w-100">
                            <i class="fa-solid fa-filter me-2"></i>
                            Search
                        </button>
                    </div>

                </div>
            </form>
        </div>
    </div>

    <!-- ADD MEDICINE -->
    <div>
        <a href="${pageContext.request.contextPath}/admin/medicine/create"
           class="btn btn-success">
            <i class="fa-solid fa-plus me-1"></i> Add Medicine
        </a>
    </div>

</div>


        <!-- TABLE -->
        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle mb-0">
                <thead class="table-primary text-center">
                    <tr>
                        <th style="width: 80px">ID</th>
                        <th>Medicine Name</th>
                        <th style="width: 150px">Unit</th>
                        <th style="width: 150px">Status</th>
                        <th style="width: 120px">Action</th>
                    </tr>
                </thead>

                <tbody>
                    <c:choose>
                        <c:when test="${empty list}">
                            <tr>
                                <td colspan="4" class="text-center text-muted py-4">
                                    <i class="fa-regular fa-folder-open fa-2x mb-2"></i><br>
                                    No medicine found
                                </td>
                            </tr>
                        </c:when>

                        <c:otherwise>
                            <c:forEach items="${list}" var="m">
                                <tr>
                                    <td class="text-center">${m.medicineId}</td>
                                    <td class="fw-medium">${m.medicineName}</td>
                                    <td class="text-center">${m.unit}</td>
                                    <!--<td><span style="color:${m.isActive ? 'green' : 'red'}">${m.isActive ? "Active" : "Inactive"}</td>-->
                                    <td>
                                        <c:choose>
                                            <c:when test="${m.isActive}">
                                                <span class="badge bg-success">
                                                    <i class="fa-solid fa-check-circle me-1"></i>
                                                    Active
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger">
                                                    <i class="fa-solid fa-times-circle me-1"></i>
                                                    Inactive
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>



                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/admin/medicine/detail?id=${m.medicineId}"
                                           class="btn btn-sm btn-outline-primary">
                                            <i class="fa-solid fa-eye"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

    </div>
</div>
