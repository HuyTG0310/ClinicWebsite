<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Biên Lai Khám Bệnh - #${app.appointmentId}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            /* CSS CHUẨN A5 */
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

            .receipt-content {
                line-height: 1.8;
                margin-top: 15px;
            }

            /* Khi bấm in, ẩn background xám, chỉ in nội dung tờ A5 */
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
                    Số BL: <strong class="fs-6"><fmt:formatNumber value="${app.appointmentId}" minIntegerDigits="6" pattern="000000" /></strong><br>
                    Ngày in: <fmt:formatDate value="<%= new java.util.Date()%>" pattern="dd/MM/yyyy HH:mm"/>
                </div>
            </div>

            <div class="text-center mb-3 mt-3">
                <h4 class="fw-bold mb-0">BIÊN LAI THU TIỀN</h4>
                <div style="font-size: 10pt; font-style: italic;">Liên 2: Giao người bệnh</div>
            </div>

            <div class="receipt-content">

                <div class="row">
                    <div class="col-8">Họ tên người bệnh: <strong class="text-uppercase ms-2">${app.patientName}</strong></div>
                    <div class="col-4 text-end">Ngày sinh: <strong class="ms-1"><fmt:formatDate value="${app.patientDob}" pattern="dd/MM/yyyy"/></strong></div>
                </div>

                <div class="row mt-1">
                    <div class="col-8">
                        Địa chỉ: <span class="ms-2">
                            <c:choose>
                                <c:when test="${not empty app.patientAddress}">${app.patientAddress}</c:when>
                                <c:otherwise>...........................................................................................</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <div class="col-4 text-end">Giới tính: <strong class="ms-2">${app.patientGender}</strong></div>
                </div>

                <div class="mt-2">
                    Khoa phòng: <strong class="ms-2 text-uppercase">Khoa ${app.specialtyName} - ${app.roomName}</strong>
                </div>

                <div class="mt-2">
                    Lý do thu: <span class="ms-2">${app.serviceName}</span>
                </div>



                <div class="mt-2">
                    Thành tiền: <strong class="ms-2 fs-6"><fmt:formatNumber value="${app.price}" pattern="#,###"/> VNĐ</strong>
                </div>

                <div class="mt-2">
                    Bằng chữ: 
                    <span class="ms-2 fst-italic fw-bold">${amountInWords}./.</span>
                </div>

                <div class="mt-2">
                    Hình thức thanh toán  
                    <strong class="ms-2">
                        <c:if test="${app.paymentMethod == 'CASH'}">Tiền mặt</c:if>
                        <c:if test="${app.paymentMethod == 'BANKING'}">Chuyển khoản</c:if>
                        </strong>
                    </div>

                </div>

                <div class="row mt-4 pt-2 text-center">
                    <div class="col-6">
                        <div class="fw-bold">Người nộp tiền</div>
                        <div style="font-size: 10pt; font-style: italic;">(Ký, ghi rõ họ tên)</div>
                        <div style="height: 60px;"></div>
                        <div class="fw-bold text-uppercase">${app.patientName}</div>
                </div>
                <div class="col-6">
                    <div style="font-size: 10pt; font-style: italic;">
                        Ngày <fmt:formatDate value="${app.appointmentTime}" pattern="dd"/> 
                        tháng <fmt:formatDate value="${app.appointmentTime}" pattern="MM"/> 
                        năm <fmt:formatDate value="${app.appointmentTime}" pattern="yyyy"/>
                    </div>
                    <div class="fw-bold mt-1">Thu ngân</div>
                    <div style="font-size: 10pt; font-style: italic;">(Ký, ghi rõ họ tên)</div>
                    <div style="height: 60px;"></div>
                    <div class="fw-bold text-uppercase">${app.receptionistName}</div>
                </div>
            </div>

        </div>
    </body>
</html>