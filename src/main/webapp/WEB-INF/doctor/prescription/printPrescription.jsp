<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Đơn Thuốc - #${medicalRecordId}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                font-family: "Times New Roman", Times, serif;
                font-size: 14pt;
                background-color: #ccc;
                color: #000;
            }
            .a4-page {
                width: 210mm;
                min-height: 297mm;
                padding: 20mm;
                margin: 10mm auto;
                background: white;
                box-shadow: 0 0 5px rgba(0,0,0,0.1);
            }
            .med-item {
                border-bottom: 1px dashed #ccc;
                padding-bottom: 10px;
                margin-bottom: 10px;
            }
            .med-item:last-child {
                border-bottom: none;
            }

            @media print {
                body {
                    background-color: #fff;
                    margin: 0;
                    padding: 0;
                }
                .a4-page {
                    margin: 0;
                    padding: 10mm;
                    box-shadow: none;
                    width: 100%;
                }
                .no-print {
                    display: none !important;
                }
                @page {
                    size: A4 portrait;
                    margin: 10mm;
                }
            }
        </style>
    </head>
    <body>
        <div class="text-center mt-3 no-print">
            <button class="btn btn-success btn-lg px-5" onclick="window.print()"><i class="fa-solid fa-print"></i> IN ĐƠN THUỐC</button>
        </div>

        <div class="a4-page">
            <div class="row mb-4 align-items-center">
                <div class="col-8">
                    <h6 class="mb-0 fw-bold text-primary">PHÒNG KHÁM ĐA KHOA DEV-CARE</h6>
                    <div style="font-size: 11pt;">Địa chỉ: Lô 20 Võ Nguyên Giáp, Phú Thứ, Cái Răng, Cần Thơ</div>
                    <div style="font-size: 11pt;">ĐT: 0292.3.917.901 | Giờ làm việc: Sáng 07:00-11:30; Chiều 12:30-16:00</div>
                </div>
                <div class="col-4 text-center">
                    <div style="font-family: 'Libre Barcode 39', cursive; font-size: 40pt; line-height: 0.8;">*P${medicalRecordId}*</div>
                </div>
            </div>

            <div class="text-center mb-4">
                <h3 class="fw-bold mb-0">ĐƠN THUỐC</h3>
            </div>

            <div class="mb-4" style="line-height: 1.8;">
                <div class="row">
                    <div class="col-6">Họ và tên: <strong class="text-uppercase">${info.PatientName}</strong></div>
                    <div class="col-3">Tuổi: ${info.Age} tuổi</div>
                    <div class="col-3">Cân nặng: ${info.Weight} kg | Nam/Nữ: ${info.Gender}</div>
                </div>
                <div>Địa chỉ: ${info.Address}</div>
                <div>Chẩn đoán: <strong>${info.Diagnosis}</strong></div>
            </div>

            <div class="mb-5">
                <c:forEach items="${prescriptionList}" var="med" varStatus="loop">
                    <div class="med-item">
                        <div class="d-flex justify-content-between fw-bold mb-1">
                            <div>${loop.index + 1}. ${med.medicineName}</div>
                            <div>${med.quantity} ${med.unit}</div>
                        </div>
                        <div style="font-size: 13pt; margin-left: 20px;">
                            <em>Cách dùng:</em> ${med.dosage} 
                            <c:if test="${not empty med.note}"> | Ghi chú: ${med.note}</c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="row mt-5">
                <div class="col-7"></div>
                <div class="col-5 text-center">
                    <div style="font-size: 12pt; font-style: italic;">Ngày <fmt:formatDate value="${currentDate}" pattern="dd"/> tháng <fmt:formatDate value="${currentDate}" pattern="MM"/> năm <fmt:formatDate value="${currentDate}" pattern="yyyy"/></div>
                    <div class="fw-bold mt-1">BÁC SĨ</div>
                    <div style="height: 90px;"></div> <div class="fw-bold" style="color: #004d99; font-size: 15pt;">BS. CKI ${info.DoctorName}</div>
                </div>
            </div>
        </div>

        <script>
            window.onload = function () {
                // Tự động bật hộp thoại in
                setTimeout(function () {
                    window.print();
                }, 800);
            };
        </script>
    </body>
</html>