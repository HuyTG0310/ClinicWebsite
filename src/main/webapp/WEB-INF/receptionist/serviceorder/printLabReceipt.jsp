<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Biên Lai Thu Tiền - BA${mrId}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            /* CSS CHUẨN A5 - ĐÃ XÓA dashed-line */
            body {
                font-family: "Times New Roman", Times, serif;
                font-size: 11pt;
                background-color: #ccc;
                color: #000;
            }
            .a5-landscape {
                width: 210mm;
                min-height: 148mm;
                padding: 8mm 15mm;
                margin: 10mm auto;
                background: white;
                box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
                position: relative;
            }

            /* CSS Bảng Biểu chuẩn Y tế */
            .table-receipt {
                width: 100%;
                border-collapse: collapse;
                margin-top: 15px;
                margin-bottom: 15px;
            }
            .table-receipt th, .table-receipt td {
                border: 1px solid #000;
                padding: 6px;
                text-align: center;
                vertical-align: middle;
            }
            .table-receipt th {
                font-weight: bold;
                background-color: #f8f9fa !important;
            }
            .table-receipt .text-start {
                text-align: left !important;
            }
            .table-receipt .text-end {
                text-align: right !important;
            }

            @media print {
                body {
                    background-color: #fff;
                    margin: 0;
                    padding: 0;
                }
                .a5-landscape {
                    margin: 0;
                    padding: 10mm;
                    box-shadow: none;
                    width: 100%;
                    min-height: 100%;
                }
                .no-print {
                    display: none !important;
                }
                @page {
                    size: A5 landscape;
                    margin: 5mm;
                }
                /* Đảm bảo border bảng in ra đen đậm không bị mờ */
                .table-receipt th, .table-receipt td {
                    border: 1px solid #000 !important;
                }
            }
        </style>
    </head>
    <body>
        <div class="text-center mt-3 mb-3 no-print">
            <button class="btn btn-primary btn-lg px-5 shadow rounded-pill" onclick="window.print()">
                <i class="fa-solid fa-print me-2"></i> IN BIÊN LAI (A5)
            </button>
            
        </div>

        <div class="a5-landscape">

            <div class="row align-items-start mb-2">
                <div class="col-8">
                    <h6 class="mb-0 fw-bold">PHÒNG KHÁM ĐA KHOA DEV-CARE</h6>
                    <div style="font-size: 10pt; line-height: 1.3;">
                        Lô 20 Võ Nguyên Giáp, P. Hưng Phú, TP Cần Thơ<br>
                        ĐT: 0292.3.917.901 - MST: 1800553823
                    </div>
                </div>
                <div class="col-4 text-end" style="font-size: 10pt;">
                    Số BA: <strong class="fs-6">00${mrId}</strong><br>
                    Ngày in: <fmt:formatDate value="<%= new java.util.Date()%>" pattern="dd/MM/yyyy HH:mm"/>
                </div>
            </div>

            <div class="text-center mb-3 mt-3">
                <h4 class="fw-bold mb-0">BIÊN LAI THU TIỀN</h4>
                <div style="font-size: 10pt; font-style: italic;">Liên 2: Giao người bệnh</div>
            </div>

            <div style="line-height: 1.6; font-size: 11pt;">
                <div class="row">
                    <div class="col-8">Họ tên người bệnh: <strong class="text-uppercase ms-2">${patient.fullName}</strong></div>
                    <div class="col-4 text-end">Ngày sinh: <strong class="ms-1"><fmt:formatDate value="${patient.dateOfBirth}" pattern="dd/MM/yyyy"/></strong></div>
                </div>
                <div class="row mt-1">
                    <div class="col-8">Địa chỉ: <span class="ms-2">${patient.address}</span></div>
                    <div class="col-4 text-end">Giới tính: <strong class="ms-2">${patient.gender}</strong></div>
                </div>
            </div>

            <table class="table-receipt">
                <thead>
                    <tr>
                        <th width="5%">STT</th>
                        <th width="35%">Tên Dịch Vụ / Xét Nghiệm</th>
                        <th width="10%">SL</th>
                        <th width="15%">Đơn giá</th>
                        <th width="15%">Thành tiền</th>
                        <th width="20%">Nơi thực hiện</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${details}" varStatus="loop">
                        <tr>
                            <td>${loop.index + 1}</td>
                            <td class="text-start">${item.serviceName}</td>
                            <td>1.00</td>
                            <td class="text-end"><fmt:formatNumber value="${item.price}" pattern="#,###"/></td>
                            <td class="text-end"><fmt:formatNumber value="${item.price}" pattern="#,###"/></td>
                            <td>${item.departmentName != null ? item.departmentName : 'Khu Xét Nghiệm'}</td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <th colspan="4" class="text-end fs-6">Tổng cộng:</th>
                        <th class="text-end fw-bold fs-6"><fmt:formatNumber value="${totalAmount}" pattern="#,###"/></th>
                        <th></th>
                    </tr>
                </tfoot>
            </table>

            <div style="line-height: 1.6;">
                <div>
                    <span style="font-style: italic;">Bằng chữ:</span> 
                    <strong class="ms-2">${amountInWords}</strong>
                </div>
                <div class="mt-1">
                    Hình thức thanh toán: 
                    <strong class="ms-2">${paymentMethod == 'CASH' ? 'Tiền mặt' : 'Chuyển khoản'}</strong>
                </div>
            </div>

            <div class="row mt-4 pt-2 text-center">
                <div class="col-6">
                    <div class="fw-bold">Người nộp tiền</div>
                    <div style="font-size: 10pt; font-style: italic;">(Ký, ghi rõ họ tên)</div>
                </div>
                <div class="col-6">
                    <div style="font-size: 10pt; font-style: italic;">
                        Ngày <fmt:formatDate value="${paidAt}" pattern="dd"/> 
                        tháng <fmt:formatDate value="${paidAt}" pattern="MM"/> 
                        năm <fmt:formatDate value="${paidAt}" pattern="yyyy"/>
                    </div>
                    <div class="fw-bold mt-1">Thu ngân</div>
                    <div style="font-size: 10pt; font-style: italic;">(Ký, ghi rõ họ tên)</div>
                    <div style="height: 60px;"></div>
                    <div class="fw-bold text-uppercase">${cashierName}</div>
                </div>
            </div>
        </div>
    </body>
</html>