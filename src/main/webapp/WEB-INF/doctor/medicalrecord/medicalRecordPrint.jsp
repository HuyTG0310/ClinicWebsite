<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>In Bệnh Án - #${mr.medicalRecordId}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>
            /* 1. Thiết lập khổ giấy A4 chuẩn */
            @page {
                size: A4;
                margin: 15mm; /* Lề giấy an toàn */
            }

            /* 2. Style in ấn chung */
            body {
                font-family: "Times New Roman", Times, serif;
                color: #000;
                background-color: #fff;
                line-height: 1.5;
                font-size: 13pt; /* Font to dễ đọc */
            }

            .text-uppercase {
                text-transform: uppercase;
            }
            .fw-bold {
                font-weight: bold;
            }
            .fst-italic {
                font-style: italic;
            }
            .text-center {
                text-align: center;
            }

            /* Chỉnh lại bảng xét nghiệm cho in ấn */
            .table-print {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 1rem;
                font-size: 11pt;
            }
            .table-print th, .table-print td {
                border: 1px solid #000;
                padding: 6px;
            }
            .table-print th {
                background-color: #f0f0f0 !important; /* Ép màu xám nhạt khi in */
                -webkit-print-color-adjust: exact;
            }

            /* Khi mở trang lên là bắt nó In ngay lập tức */
            @media print {
                .btn-print {
                    display: none;
                }
            }
        </style>
    </head>
    <body onload="window.print()">

        <div class="text-center my-3 btn-print">
            <button onclick="window.print()" class="btn btn-primary px-5">Bấm vào đây để In</button>
        </div>

        <div class="container-fluid px-0">

            <div class="row text-center mb-4 pb-2" style="border-bottom: 2px solid #000;">
                <div class="col-4">
                    <div class="fw-bold text-uppercase">Sở Y Tế</div>
                    <div class="fw-bold text-uppercase">Phòng Khám Đa Khoa FPT</div>
                </div>
                <div class="col-5">
                    <div class="fw-bold">CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</div>
                    <div class="fw-bold" style="text-decoration: underline;">Độc lập - Tự do - Hạnh phúc</div>
                </div>
                <div class="col-3 text-start" style="font-size: 11pt;">
                    <div>Mã BA: <fmt:formatNumber value="${mr.medicalRecordId}" minIntegerDigits="6" pattern="000000" /></div>
                    <div>Mã BN: <fmt:formatNumber value="${mr.patientId}" minIntegerDigits="6" pattern="000000" /></div>
                </div>
            </div>

            <div class="text-center mb-4 mt-3">
                <h2 class="fw-bold mb-1 text-uppercase">Phiếu Khám Bệnh & Chẩn Đoán</h2>
            </div>

            <div class="row mb-2">
                <div class="col-7">
                    Họ tên bệnh nhân: <strong class="text-uppercase ms-1 fs-5">${mr.patientName}</strong>
                </div>
                <div class="col-3">
                    Giới tính: <strong class="ms-1">${mr.patientGender}</strong>
                </div>
                <div class="col-2">
                    Tuổi: <strong class="ms-1"><fmt:formatDate value="${mr.patientDob}" pattern="yyyy"/></strong>
                </div>
            </div>
            <div class="mb-4">
                Điện thoại liên lạc: <span class="ms-1">${mr.patientPhone}</span>
                <span class="ms-4">Địa chỉ: ${mr.patientAddress}</span>
            </div>

            <hr style="border-top: 1px dashed #000;">

            <h5 class="fw-bold text-uppercase mt-3 mb-2">I. Chỉ Số Sinh Tồn:</h5>
            <div class="row mb-3">
                <div class="col-4">Mạch: <strong class="ms-1">${mr.heartRate != null ? mr.heartRate : '___'}</strong> l/p</div>
                <div class="col-4">Huyết áp: <strong class="ms-1">${not empty mr.bloodPressure ? mr.bloodPressure : '___/___'}</strong> mmHg</div>
                <div class="col-4">Nhiệt độ: <strong class="ms-1">${mr.temperature != null ? mr.temperature : '___'}</strong> °C</div>
            </div>
            <div class="row mb-4">
                <div class="col-4">Cân nặng: <strong class="ms-1">${mr.weight != null ? mr.weight : '___'}</strong> kg</div>
                <div class="col-4">Chiều cao: <strong class="ms-1">${mr.height != null ? mr.height : '___'}</strong> cm</div>
                <div class="col-4"></div>
            </div>

            <h5 class="fw-bold text-uppercase mt-3 mb-2">II. Khám Lâm Sàng:</h5>
            <div class="mb-2">
                - Lý do khám / Triệu chứng: <strong class="ms-1">${mr.symptom}</strong>
            </div>
            <div class="mb-4">
                - Khám thực thể: <span class="ms-1 fst-italic">${not empty mr.physicalExam ? mr.physicalExam : 'Không ghi nhận bất thường'}</span>
            </div>

            <h5 class="fw-bold text-uppercase mt-3 mb-2">III. Kết Quả Cận Lâm Sàng:</h5>
            <c:choose>
                <c:when test="${not empty consolidatedResults}">
                    <table class="table-print">
                        <thead>
                            <tr>
                                <th width="40%">Chỉ số xét nghiệm</th>
                                <th width="20%">Kết quả</th>
                                <th width="20%">Tham chiếu</th>
                                <th width="20%">Thời gian</th>
                            </tr>
                        </thead>
                        <tbody>

                        <c:set var="currCat" value="" />
                        <c:set var="currTest" value="" />

                        <c:forEach items="${consolidatedResults}" var="r">

                            <!-- CATEGORY -->
                            <c:if test="${r.categoryName != currCat}">
                                <tr>
                                    <td colspan="4"
                                        class="fw-bold text-uppercase"
                                        style="background-color:#f8f9fa !important; -webkit-print-color-adjust: exact;">
                                        ${r.categoryName}
                                    </td>
                                </tr>

                                <c:set var="currCat" value="${r.categoryName}" />
                                <c:set var="currTest" value="" />
                            </c:if>


                            <!-- PANEL TEST -->
                            <c:if test="${r.testName != currTest}">
                                <c:if test="${r.isPanel}">
                                    <tr>
                                        <td colspan="4"
                                            class="fw-bold fst-italic"
                                            style="background-color:#f3f3f3 !important; padding-left:12px; -webkit-print-color-adjust: exact;">
                                            ${r.testName}
                                        </td>
                                    </tr>
                                </c:if>

                                <c:set var="currTest" value="${r.testName}" />
                            </c:if>


                            <!-- PARAMETER -->
                            <tr>

                                <td class="ps-3">
                                    ${r.parameterName}
                                </td>

                                <td class="text-center fw-bold">

                            <c:choose>

                                <c:when test="${r.status == 'REJECTED'}">
                                    Hủy
                                </c:when>

                                <c:when test="${not empty r.resultValue}">

                                    ${r.resultValue}

                                    <c:if test="${r.isAbnormal}">
                                        ${r.flag == 'H' ? '↑' : (r.flag == 'L' ? '↓' : '*')}
                                    </c:if>

                                </c:when>

                                <c:otherwise>
                                    Đang chờ
                                </c:otherwise>

                            </c:choose>

                            </td>

                            <td class="text-center">
                                ${r.normalRange}
                            </td>

                            <td class="text-center">

                            <c:if test="${not empty r.resultTime}">
                                <fmt:formatDate value="${r.resultTime}" pattern="HH:mm dd/MM/yyyy"/>
                            </c:if>

                            </td>

                            </tr>

                        </c:forEach>

                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="mb-4 fst-italic">- Không có chỉ định cận lâm sàng.</div>
                </c:otherwise>
            </c:choose>

            <h5 class="fw-bold text-uppercase mt-4 mb-2">IV. Kết Luận & Điều Trị:</h5>
            <div class="mb-2">
                - Chẩn đoán xác định: <strong class="ms-1 text-uppercase" style="font-size: 14pt;">${mr.diagnosis}</strong>
            </div>
            <div class="mb-4">
                - Lời khuyên / Hướng điều trị:<br>
                <div style="white-space: pre-wrap; padding-left: 15px;">${mr.treatmentPlan}</div>
            </div>



            <h5 class="fw-bold text-uppercase mt-4 mb-2">V. Đơn Thuốc:</h5>

            <c:choose>

                <c:when test="${not empty mr.prescriptions}">

                    <table class="table-print">
                        <thead>
                            <tr>
                                <th width="5%">STT</th>
                                <th width="35%">Tên thuốc</th>
                                <th width="10%">Đơn vị</th>
                                <th width="10%">SL</th>
                                <th width="40%">Cách dùng</th>
                            </tr>
                        </thead>

                        <tbody>

                        <c:forEach items="${mr.prescriptions}" var="p" varStatus="loop">

                            <tr>

                                <td class="text-center">
                                    ${loop.index + 1}
                                </td>

                                <td class="fw-bold text-uppercase">
                                    ${p.medicineName}
                                </td>

                                <td class="text-center">
                                    ${p.unit}
                                </td>

                                <td class="text-center fw-bold">
                                    ${p.quantity}
                                </td>

                                <td>

                                    ${p.dosage}

                            <c:if test="${not empty p.note}">
                                <div class="fst-italic">
                                    - ${p.note}
                                </div>
                            </c:if>

                            </td>

                            </tr>

                        </c:forEach>

                        </tbody>

                    </table>

                </c:when>

                <c:otherwise>

                    <div class="mb-4 fst-italic">
                        - Không kê đơn thuốc.
                    </div>

                </c:otherwise>

            </c:choose>

            <div class="mb-4">
                <span class="fw-bold">Hẹn tái khám:</span> 
                <c:choose>
                    <c:when test="${not empty mr.followUpDate}">
                        Đúng hẹn vào ngày <strong><fmt:formatDate value="${mr.followUpDate}" pattern="dd/MM/yyyy"/></strong>.
                    </c:when>
                    <c:otherwise>
                        Theo dõi thêm tại nhà, tái khám nếu có dấu hiệu bất thường.
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="row text-center mt-5 pt-4">
                <div class="col-6"></div>
                <div class="col-6">
                    <div class="fst-italic mb-1">
                        Ngày <fmt:formatDate value="${mr.completedAt}" pattern="dd"/> 
                        tháng <fmt:formatDate value="${mr.completedAt}" pattern="MM"/> 
                        năm <fmt:formatDate value="${mr.completedAt}" pattern="yyyy"/>
                    </div>
                    <div class="fw-bold">Bác sĩ khám bệnh</div>
                    <div class="fst-italic">(Ký và ghi rõ họ tên)</div>

                    <div style="margin-top: 100px;" class="fw-bold text-uppercase">BS. ${mr.doctorName}</div>
                </div>
            </div>

        </div>
    </body>
</html>