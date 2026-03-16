<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Kết Quả Xét Nghiệm - ${mr.patientName}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <style>
            /* CSS CHUẨN IN A4 DỌC */
            body {
                font-family: "Times New Roman", Times, serif;
                font-size: 11pt;
                background-color: #525659; /* Màu nền lúc xem trên web giống PDF viewer */
            }
            .a4-page {
                width: 210mm;
                min-height: 297mm;
                padding: 15mm 15mm;
                margin: 10mm auto;
                background: white;
                box-shadow: 0 0 10px rgba(0,0,0,0.5);
            }

            /* Bảng Xét Nghiệm */
            .lab-table th, .lab-table td {
                border: 1px solid #000 !important;
                padding: 4px 8px;
                vertical-align: middle;
            }
            .lab-table th {
                text-align: center;
                font-weight: bold;
                background-color: #f0f0f0 !important;
                -webkit-print-color-adjust: exact;
            }
            .category-row {
                background-color: #e9ecef !important;
                font-weight: bold;
                text-transform: uppercase;
                -webkit-print-color-adjust: exact;
            }
            .test-name-row {
                font-weight: bold;
                font-style: italic;
            }

            /* Ẩn nút bấm khi in */
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
                    height: 100%;
                }
                .no-print {
                    display: none !important;
                }
                @page {
                    size: A4 portrait;
                    margin: 5mm;
                }
            }
        </style>
    </head>
    <body>

        <div class="text-center py-3 bg-dark sticky-top no-print">
            <button class="btn btn-primary fw-bold px-4 me-2" onclick="window.print()">
                <i class="fa-solid fa-print me-2"></i> IN KẾT QUẢ (A4)
            </button>
            <button class="btn btn-secondary px-4" onclick="window.close()">Đóng</button>
        </div>

        <div class="a4-page">
            <div class="row border-bottom border-2 border-dark pb-2 mb-3">
                <div class="col-8">
                    <h5 class="fw-bold mb-1 text-uppercase">PHÒNG KHÁM ĐA KHOA DEV-CARE</h5>
                    <div style="font-size: 10pt;">
                        <strong>KHOA XÉT NGHIỆM - Laboratory Department</strong><br>
                        Địa chỉ: Lô 20 Võ Nguyên Giáp, Phú Thứ, Cái Răng, Cần Thơ<br>
                        Tel: 0292.3.917.901
                    </div>
                </div>
                <div class="col-4 text-end" style="font-size: 10pt;">
                    Số hồ sơ (PID): <strong>${mr.patientId}</strong><br>
                    Số bệnh phẩm (SID): <strong>111125-${mr.medicalRecordId}</strong><br>
                    Khoa - Dept: <strong>Phòng Khám Ngoại Trú</strong>
                </div>
            </div>

            <div class="text-center mb-4">
                <h4 class="fw-bold mb-0">BÁO CÁO KẾT QUẢ XÉT NGHIỆM</h4>
                <div class="fst-italic">Laboratory Report</div>
            </div>

            <div class="row mb-3" style="font-size: 11pt; line-height: 1.6;">
                <div class="col-7">
                    <div class="d-flex"><div style="width: 100px;">Họ tên:</div> <strong class="text-uppercase">${mr.patientName}</strong></div>
                    <div class="d-flex"><div style="width: 100px;">Địa chỉ:</div> <div>${mr.patientAddress}</div></div>
                    <div class="d-flex"><div style="width: 100px;">Chẩn đoán:</div> <div>${mr.diagnosis}</div></div>
                    <div class="d-flex"><div style="width: 100px;">BS chỉ định:</div> <strong>BS. ${mr.doctorName}</strong></div>
                </div>
                <div class="col-5">
                    <div class="row">
                        <div class="col-6">Ngày sinh: <fmt:formatDate value="${mr.patientDob}" pattern="dd/MM/yyyy"/></div>
                        <div class="col-6">Giới tính: ${mr.patientGender}</div>
                    </div>
                    <div>Đối tượng: Thu phí</div>
                    <div>Thời gian in KQ: <fmt:formatDate value="<%= new java.util.Date()%>" pattern="dd/MM/yyyy HH:mm"/></div>
                    <div>Chất lượng mẫu: <strong class="text-success">Đạt</strong></div>
                </div>
            </div>

            <table class="table lab-table w-100 mb-4" style="font-size: 10.5pt;">
                <thead>
                    <tr>
                        <th width="35%">Yêu cầu xét nghiệm<br><span class="fst-italic fw-normal" style="font-size: 9pt;">Requested Test</span></th>
                        <th width="15%">Kết quả<br><span class="fst-italic fw-normal" style="font-size: 9pt;">Result</span></th>
                        <th width="20%">Khoảng tham chiếu sinh học/NL<br><span class="fst-italic fw-normal" style="font-size: 9pt;">Biological Reference Interval</span></th>
                        <th width="10%">Đơn vị<br><span class="fst-italic fw-normal" style="font-size: 9pt;">Unit</span></th>
                        <th width="10%">QTXN<br><span class="fst-italic fw-normal" style="font-size: 9pt;">Procedure</span></th>
                        <th width="10%">Ghi chú<br><span class="fst-italic fw-normal" style="font-size: 9pt;">Note</span></th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="currCat" value="" />
                    <c:set var="currTest" value="" />

                    <c:forEach items="${results}" var="r">
                        <c:if test="${r.categoryName != currCat}">
                            <tr class="category-row">
                                <td colspan="6">${r.categoryName}</td>
                            </tr>
                            <c:set var="currCat" value="${r.categoryName}" />
                        </c:if>

                        <c:if test="${r.testName != currTest}">
                            <c:if test="${r.isPanel}">
                                <tr class="test-name-row">
                                    <td colspan="6">${r.testName}</td>
                                </tr>
                                <c:set var="currTest" value="${r.testName}" />
                            </c:if>
                        </c:if>

                        <tr>
                            <td class="ps-4">${r.parameterName}</td>
                            <td class="text-center fw-bold ${r.isAbnormal ? 'text-danger' : ''}">
                                ${not empty r.resultValue ? r.resultValue : 'Hủy'}

                                <c:if test="${r.isAbnormal}">
                                    <span style="font-size: 14pt; margin-left: 2px;">
                                        <c:choose>
                                            <c:when test="${r.flag == 'H'}">&uparrow;</c:when> <c:when test="${r.flag == 'L'}">&downarrow;</c:when> <c:otherwise>*</c:otherwise> </c:choose>
                                            </span>
                                </c:if>
                            </td>
                            <td class="text-center">${r.normalRange}</td>
                            <td class="text-center">${r.unit}</td>
                            <td class="text-center" style="font-size: 8pt; color: #555;">AUTO</td>
                            <td></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="row mt-5" style="page-break-inside: avoid;">
                <div class="col-6 text-center">
                    <div class="fw-bold">Xem xét kết quả:</div>
                    <div class="fst-italic" style="font-size: 9pt;">Reviewed Result</div>
                    <div style="height: 80px;"></div>
                    <div class="fw-bold">BS. ${mr.doctorName}</div>
                </div>
                <div class="col-6 text-center">
                    <div class="fw-bold">Khoa Xét Nghiệm</div>
                    <div class="fst-italic" style="font-size: 9pt;">Laboratory Department</div>
                    <div style="height: 80px;">
                    </div>
                    <div class="fw-bold">KTV. ${inChargeLab}</div>
                </div>
            </div>


        </div>
    </body>
</html>