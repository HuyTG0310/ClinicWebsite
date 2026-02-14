<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-plus-circle text-primary me-2"></i>
            Role Add
        </h2>
        <p class="text-muted mb-0">Create a new system role to manage user permissions</p>
    </div>
    <a href="${pageContext.request.contextPath}/admin/role/list" class="btn btn-outline-secondary">
        <i class="fa-solid fa-arrow-left me-2"></i>Back to List
    </a>
</div>

<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show shadow-sm mb-4" role="alert">
                        <i class="fa-solid fa-circle-exclamation me-2"></i>
                        ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                <form action="${pageContext.request.contextPath}/admin/role/create" method="POST">

                    <div class="mb-4">
                        <label for="roleName" class="form-label fw-bold">Role Name</label>
                        <div class="input-group">
                            <span class="input-group-text bg-light">
                                <i class="fa-solid fa-id-card-clip text-muted"></i>
                            </span>
                            <input type="text" class="form-control" id="roleName" name="roleName" 
                                   placeholder="e.g. Accountant, Nurse..." required>
                        </div>
                        <div class="form-text text-muted small">
                            Role name should be unique and easy to distinguish.
                        </div>
                    </div>

                    <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                        <button type="reset" class="btn btn-light px-4">Reset</button>
                        <button type="submit" class="btn btn-primary px-5">
                            <i class="fa-solid fa-save me-2"></i>Save Role
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<style>
    .card {
        border-radius: 12px;
    }
    .btn {
        border-radius: 8px;
        font-weight: 500;
        transition: all 0.25s ease;
        display: inline-flex;
        align-items: center;
        justify-content: center;
    }
    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
    .form-control, .input-group-text {
        border-radius: 8px;
        padding: 0.6rem 1rem;
    }
    .input-group-text {
        border-right: none;
        border-radius: 8px 0 0 8px;
    }
    .form-control {
        border-left: none;
        border-radius: 0 8px 8px 0;
    }
</style>
