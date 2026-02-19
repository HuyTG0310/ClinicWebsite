<%--  
    Document   : userAdd
    Created on : Feb 7, 2026
    Author     : huytr
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- ================= HEADER ================= -->
<div class="mb-4">
    <h2 class="mb-1">
        <i class="fa-solid fa-user-plus text-primary me-2"></i>
        Thêm người dùng
    </h2>
    <p class="text-muted mb-0">
        Tạo tài khoản người dùng mới trong hệ thống
    </p>
</div>

<!-- ================= ERROR ================= -->
<c:if test="${not empty error}">
    <div class="alert alert-danger shadow-sm">
        <i class="fa-solid fa-exclamation-triangle me-2"></i>
        ${error}
    </div>
</c:if>

<!-- ================= FORM ================= -->
<div class="card shadow-sm">
    <div class="card-body">

        <form method="post">

            <div class="row g-4">

                <!-- USERNAME -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">
                        Username <span class="text-danger">*</span>
                    </label>
                    <input type="text"
                           name="username"
                           value="${username}"
                           class="form-control"
                           placeholder="Nhập username..." />
                </div>

                <!-- FULL NAME -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">
                        Họ tên <span class="text-danger">*</span>
                    </label>
                    <input type="text"
                           name="fullName"
                           value="${fullName}"
                           class="form-control"
                           placeholder="Nhập họ tên..." />
                </div>

                <!-- PHONE -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">
                        Số điện thoại
                    </label>
                    <input type="text"
                           name="phone"
                           value="${phone}"
                           class="form-control"
                           placeholder="Nhập số điện thoại..." />
                </div>

                <!-- EMAIL -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">
                        Email
                    </label>
                    <input type="email"
                           name="email"
                           value="${email}"
                           class="form-control"
                           placeholder="example@email.com" />
                </div>

                <!-- ROLE -->
                <div class="col-md-6">
                    <label class="form-label fw-semibold">
                        Vai trò <span class="text-danger">*</span>
                    </label>
                    <select name="roleId" class="form-select">
                        <c:forEach var="r" items="${roles}">
                            <option value="${r.roleId}"
                                    <c:if test="${roleId == r.roleId}">selected</c:if>>
                                ${r.roleName}
                            </option>
                        </c:forEach>
                    </select>
                </div>

            </div>

            <!-- ================= ACTIONS ================= -->
            <div class="d-flex justify-content-end gap-2 mt-4">
                <a href="${pageContext.request.contextPath}/admin/user/list"
                   class="btn btn-outline-secondary">
                    <i class="fa-solid fa-arrow-left me-1"></i>
                    Hủy
                </a>

                <button type="submit"
                        class="btn btn-primary px-4">
                    <i class="fa-solid fa-save me-1"></i>
                    Lưu
                </button>
            </div>

        </form>

    </div>
</div>
