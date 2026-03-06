<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-1">
            <i class="fa-solid fa-file-invoice-dollar text-primary me-2"></i>
            Manage Service Order
        </h2>
        <p class="text-muted mb-0">Manage daily service orders and revenue</p>
    </div>
</div>

<div class="row mb-4 g-3">
    <div class="col-md-4">
        <div class="card bg-primary text-white shadow border-0 h-100 rounded-4">
            <div class="card-body p-4 d-flex align-items-center">
                <div class="bg-white bg-opacity-25 rounded-circle p-3 me-3">
                    <i class="fa-solid fa-sack-dollar fa-2x"></i>
                </div>
                <div>
                    <h6 class="text-uppercase mb-1 fw-bold opacity-75">Total revenue</h6>
                    <h3 class="mb-0 fw-bolder"><fmt:formatNumber value="${totalRevenue}" pattern="#,###"/> VNĐ</h3>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card bg-success text-white shadow border-0 h-100 rounded-4">
            <div class="card-body p-4 d-flex align-items-center">
                <div class="bg-white bg-opacity-25 rounded-circle p-3 me-3">
                    <i class="fa-solid fa-money-bill-wave fa-2x"></i>
                </div>
                <div>
                    <h6 class="text-uppercase mb-1 fw-bold opacity-75">Cash</h6>
                    <h3 class="mb-0 fw-bolder"><fmt:formatNumber value="${totalCash}" pattern="#,###"/> VNĐ</h3>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card bg-info text-white shadow border-0 h-100 rounded-4">
            <div class="card-body p-4 d-flex align-items-center">
                <div class="bg-white bg-opacity-25 rounded-circle p-3 me-3">
                    <i class="fa-solid fa-building-columns fa-2x"></i>
                </div>
                <div>
                    <h6 class="text-uppercase mb-1 fw-bold opacity-75">Bank</h6>
                    <h3 class="mb-0 fw-bolder"><fmt:formatNumber value="${totalBanking}" pattern="#,###"/> VNĐ</h3>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="card shadow-sm mb-4 border-0 rounded-4">
    <div class="card-body p-4">
        <form action="${basePath}/service-order/list" method="get">
            <div class="row g-3 align-items-end">

                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Transaction date</label>
                    <input type="date" class="form-control" name="date" 
                           value="${param.date != null ? param.date : paramDate}">
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold small text-muted">Status</label>
                    <select class="form-select" name="status">
                        <option value="">-- All status--</option>
                        <option value="UNPAID" <c:if test="${param.status == 'UNPAID'}">selected</c:if>>⏳ UNPAID</option>
                        <option value="PAID" <c:if test="${param.status == 'PAID'}">selected</c:if>>✅ PAID</option>
                        <option value="CANCELLED" <c:if test="${param.status == 'CANCELLED'}">selected</c:if>>↩ CANCELLED</option>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label fw-bold small text-muted">Payment method</label>
                        <select class="form-select" name="paymentMethod">
                            <option value="">-- All methods --</option>
                            <option value="CASH" <c:if test="${param.paymentMethod == 'CASH'}">selected</c:if>>💵 Cash</option>
                        <option value="BANKING" <c:if test="${param.paymentMethod == 'BANKING'}">selected</c:if>>🏦 Banking</option>
                        </select>
                    </div>

                    <div class="col-md-3 d-flex gap-2">
                        <button class="btn btn-primary flex-grow-1" type="submit">
                            <i class="fas fa-filter me-2"></i>Search
                        </button>

                    <c:if test="${not empty param.paymentMethod or not empty param.date or not empty param.status}">
                        <a href="${basePath}/service-order/list" 
                           class="btn btn-outline-secondary px-3" title="Xóa bộ lọc">
                            <i class="fas fa-redo-alt"></i>
                        </a>
                    </c:if>
                </div>

            </div>
        </form>
    </div>
</div>

<div class="card shadow-sm border-0 rounded-4">
    <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center border-bottom-0">
        <h5 class="mb-0 fw-bold text-dark">
            <i class="fas fa-list me-2 text-primary"></i>Order list
        </h5>
        <span class="badge bg-light text-dark border">
            Total ${orderList.size()} orders
        </span>
    </div>

    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="px-4 text-center">Order Id</th>
                        <th>Collection time</th>
                        <th>Patient</th>
                        <th>Service name</th>
                        <th class="text-end">Amount</th>
                        <th class="text-center">Method</th>
                        <th class="text-center">Status</th>
                        <th class="text-center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${orderList}" var="o">
                        <tr>
                            <td class="px-4 text-center fw-bold text-secondary">#${o.serviceOrderId}</td>

                            <td>
                                <div class="fw-bold text-dark">
                                    <fmt:formatDate value="${o.paidAt}" pattern="HH:mm"/>
                                </div>
                                <div class="small text-muted">
                                    <fmt:formatDate value="${o.paidAt}" pattern="dd/MM/yyyy"/>
                                </div>
                            </td>

                            <td class="fw-bold text-primary">${o.patientName}</td>

                            <td>${o.serviceName}</td>

                            <td class="text-end fw-bold text-danger">
                                <fmt:formatNumber value="${o.priceAtTime}" pattern="#,###"/> đ
                            </td>

                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${o.paymentMethod == 'CASH'}">
                                        <span class="badge bg-success bg-opacity-10 text-success border border-success border-opacity-25 px-2 py-1">
                                            <i class="fa-solid fa-money-bill-wave me-1"></i>CASH
                                        </span>
                                    </c:when>
                                    <c:when test="${o.paymentMethod == 'BANKING'}">
                                        <span class="badge bg-info bg-opacity-10 text-info border border-info border-opacity-25 px-2 py-1">
                                            <i class="fa-solid fa-building-columns me-1"></i>BANK
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">N/A</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>


                            <td class="text-center">
                                <c:if test="${o.status == 'PAID'}">
                                    <span class="badge bg-success"><i class="fa-solid fa-check me-1"></i>PAID</span>
                                </c:if>
                                <c:if test="${o.status == 'UNPAID'}">
                                    <span class="badge bg-warning text-dark">UNPAID</span>
                                </c:if>
                                <c:if test="${o.status == 'CANCELLED'}">
                                    <span class="badge bg-secondary">CANCELED</span>
                                </c:if>
                            </td>

                            <td class="text-center">
                                <c:set var="paidTime" value="${o.paidAt != null ? o.paidAt.time : ''}" />

                                <c:choose>
                                    <c:when test="${o.medicalRecordId > 0}">
                                        <a href="${basePath}/service-order/detail?mrId=${o.medicalRecordId}&patientId=${o.patientId}&status=${o.status}<c:if test="${o.status == 'PAID'}">&time=${o.paidAt.time}</c:if>" class="btn btn-sm btn-outline-primary"><i class="fas fa-eye me-1"></i>View</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${basePath}/service-order/detail?soId=${o.serviceOrderId}&patientId=${o.patientId}&status=${o.status}<c:if test="${o.status == 'PAID'}">&time=${o.paidAt.time}</c:if>" class="btn btn-sm btn-outline-primary"><i class="fas fa-eye me-1"></i>View</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                        </tr>
                    </c:forEach>

                    <c:if test="${empty orderList}">
                        <tr>
                            <td colspan="8" class="text-center py-5">
                                <i class="fa-solid fa-box-open fa-3x text-muted mb-3 d-block"></i>
                                <p class="text-muted mb-0">Không có giao dịch nào trong ngày này.</p>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<style>
    /* Bo góc và bóng mờ cho giao diện mượt hơn */
    .rounded-4 {
        border-radius: 1rem !important;
    }
    .table-hover tbody tr:hover {
        background-color: rgba(13, 110, 253, 0.05);
        transition: background-color 0.2s ease;
    }

    .btn {
        border-radius: 8px;
        padding: 0.5rem 1.2rem;
        font-weight: 500;
        transition: all 0.25s ease;
    }
    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
</style>