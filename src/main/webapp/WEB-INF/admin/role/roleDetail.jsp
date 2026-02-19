
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="mb-4 d-flex align-items-center">
    <a href="list" class="btn btn-outline-secondary btn-sm me-3">
        <i class="fa-solid fa-arrow-left"></i>
    </a>
    <h4 class="fw-bold text-primary mb-0">Role detail: ${role.roleName}</h4>
</div>

<c:if test="${param.msg eq 'success'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="fa-solid fa-check-circle me-2"></i> Update role successfully!
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<form action="detail" method="post">
    <input type="hidden" name="roleId" value="${role.roleId}">

    <div class="row">
        <div class="col-md-3">
            <div class="card shadow-sm border-0">
                <div class="card-body text-center">
                    <div class="mb-3">
                        <i class="fa-solid fa-user-shield fa-3x text-primary opacity-25"></i>
                    </div>
                    <h5 class="fw-bold">${role.roleName}</h5>
                    <p class="text-muted small">ID: #${role.roleId}</p>


                    <hr class="my-3 opacity-25">

                    <div class="form-check form-switch d-flex justify-content-center align-items-center gap-2 mb-4">
                        <input class="form-check-input" type="checkbox" name="isActive" 
                               id="roleStatus" ${role.isActive ? 'checked' : ''} style="cursor: pointer;">
                        <label class="form-check-label fw-bold ${role.isActive ? 'text-success' : 'text-danger'}" for="roleStatus">
                            ${role.isActive ? 'Active' : 'Inactive'}
                        </label>
                    </div>


                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fa-solid fa-save me-2"></i>Save
                    </button>
                </div>
            </div>
        </div>

        <div class="col-md-9">
            <div class="card shadow-sm border-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="text-center" style="width: 60px;">
                                    <input class="form-check-input" type="checkbox" id="checkAll">
                                </th>
                                <th>Privilege code</th>
                                <th>Privilege description</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${allPrivileges}">
                                <tr>
                                    <td class="text-center">
                                        <input class="form-check-input privilege-checkbox" 
                                               type="checkbox" 
                                               name="privilegeIds" 
                                               value="${p.privilegeId}"
                                               ${currentPrivilegeIds.contains(p.privilegeId) ? 'checked' : ''}>
                                    </td>
                                    <td><code class="fw-bold">${p.privilegeCode}</code></td>
                                    <td><span class="text-muted">${p.description}</span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</form>


<style>
    .btn {
        border-radius: 8px;
        font-weight: 500;
        transition: all 0.25s ease;
    }

    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
</style>

<script>
    // Giữ nguyên script checkAll thông minh của bạn
    document.getElementById('checkAll').addEventListener('change', function () {
        var checkboxes = document.querySelectorAll('.privilege-checkbox');
        for (var checkbox of checkboxes) {
            checkbox.checked = this.checked;
        }
    });
</script>