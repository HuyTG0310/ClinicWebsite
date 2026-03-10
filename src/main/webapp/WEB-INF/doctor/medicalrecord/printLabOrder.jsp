<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>In Phiếu Chỉ Định DVKT - #${batchId}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            /* CSS CHUẨN ĐỂ IN RA GIẤY A4 */
            body {
                font-family: "Times New Roman", Times, serif; /* Form bệnh viện chuẩn phải dùng Times New Roman */
                font-size: 14pt;
                background-color: #ccc; /* Nền xám khi xem trên web */
                color: #000;
            }
            .a4-page {
                width: 210mm;
                min-height: 297mm;
                padding: 20mm;
                margin: 10mm auto;
                background: white;
                box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
            }
            /* Style bảng biểu */
            .table-print {
                width: 100%;
                border-collapse: collapse;
                margin-top: 15px;
            }
            .table-print th, .table-print td {
                border: 1px solid #000;
                padding: 5px;
            }
            .table-print th {
                text-align: center;
                font-weight: bold;
            }
            .text-center {
                text-align: center;
            }
            .text-right {
                text-align: right;
            }

            /* Cấu hình khi người dùng bấm Ctrl+P */
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
                    border: none;
                    width: 100%;
                }
                .no-print {
                    display: none !important;
                } /* Ẩn nút in khi đang in */
                @page {
                    size: A4 portrait;
                    margin: 10mm;
                }
            }
        </style>
    </head>
    <body>

        <div class="text-center mt-3 no-print">
            <button class="btn btn-primary btn-lg px-5" onclick="window.print()">
                <i class="fa-solid fa-print"></i> IN PHIẾU NÀY
            </button>
            <button class="btn btn-secondary btn-lg" onclick="window.close()">Đóng</button>
        </div>

        <div class="a4-page">
            <div class="row mb-3">
                <div class="col-8">
                    <div class="d-flex align-items-center">
                        <img src="https://via.placeholder.com/60" alt="Logo" style="width: 60px; height: 60px; margin-right: 15px;">
                        <div>
                            <h6 class="mb-0 fw-bold">PHÒNG KHÁM ĐA KHOA DEV-CARE</h6>
                            <div style="font-size: 11pt;">
                                Địa chỉ: 123 Đường Code, Phường Bug, TP. Lập Trình<br>
                                Điện thoại: 0292.3.888.999 - Website: www.devcare.com
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-4 text-center">
                    <div style="font-family: 'Libre Barcode 39', cursive; font-size: 40pt; line-height: 0.8;">*${batchId}*</div>
                    <div style="font-size: 11pt;">Mã mẫu: 22499${batchId}</div>
                </div>
            </div>

            <hr style="border-top: 2px solid #000; opacity: 1;">

            <div class="text-center mb-3">
                <h4 class="fw-bold mb-1">PHIẾU CHỈ ĐỊNH DVKT</h4>
                <div style="font-size: 12pt; font-style: italic;">
                    Ngày chỉ định: <fmt:formatDate value="${currentDate}" pattern="HH:mm - dd/MM/yyyy"/><br>
                    PHÒNG KHÁM NỘI TỔNG HỢP
                </div>
            </div>

            <div class="mb-3" style="line-height: 1.6;">
                <div class="row">
                    <div class="col-7">Họ tên: <strong class="text-uppercase">${info.PatientName}</strong></div> 
                    <div class="col-3">Năm sinh: ${info.YOB}</div>
                    <div class="col-2">Giới tính: ${info.Gender}</div>
                </div>
                <div>Địa chỉ: ${info.Address}</div>
                <div>Chẩn đoán: <em>${info.Diagnosis}</em></div> 
            </div>

            <table class="table-print">
                <thead>
                    <tr>
                        <th width="5%">STT</th>
                        <th width="40%">Tên dịch vụ</th>
                        <th width="10%">ĐVT</th>
                        <th width="5%">SL</th>
                        <th width="15%">Đơn giá</th>
                        <th width="25%">Thành tiền</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${printList}" var="item" varStatus="loop">
                    <tr>
                        <td class="text-center">${loop.index + 1}</td>
                        <td>${item.testName} <br><small style="font-size: 10pt;">[Máu]</small></td>
                        <td class="text-center">Lần</td>
                        <td class="text-center">1</td>
                        <td class="text-right"><fmt:formatNumber value="${item.currentPrice}" pattern="#,###"/></td>
                    <td class="text-right"><fmt:formatNumber value="${item.currentPrice}" pattern="#,###"/></td>
                    </tr>
                </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="5" class="text-center fw-bold">Tổng cộng</td>
                        <td class="text-right fw-bold"><fmt:formatNumber value="${totalAmount}" pattern="#,###"/></td>
                </tr>
                </tfoot>
            </table>

            <div class="row mt-4">
                <div class="col-6">
                </div>
                <div class="col-6 text-center">
                    <div style="font-size: 12pt;">
                        <strong>Tổng tiền (tạm tính): <fmt:formatNumber value="${totalAmount}" pattern="#,###"/> đ</strong>
                    </div>
                    <div class="fw-bold mt-3">BÁC SĨ CHỈ ĐỊNH</div>
                    <div style="height: 80px;"></div> 
                    <div><strong>BS. ${info.DoctorName}</strong></div> </div>
            </div>
        </div>

        <script>
            window.onload = function () {
                // setTimeout(function() { window.print(); }, 500); 
                // Bạn có thể mở comment dòng trên để nó tự động nhảy popup in luôn không cần bấm nút
            };
        </script>
    </body>
</html>