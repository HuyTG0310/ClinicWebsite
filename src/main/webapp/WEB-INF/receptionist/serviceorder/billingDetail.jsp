<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container-fluid mt-4 mb-5">
    <div class="d-flex justify-content-between align-items-center mb-4 no-print">
        <div>
            <h2 class="text-primary fw-bold mb-0"><i class="fa-solid fa-file-invoice me-2"></i>Service Order Detail</h2>
            <p class="text-muted mb-0">Check service details before making payment.</p>
        </div>

        <div class="d-flex gap-2 align-items-center">

            <c:if test="${currentStatus == 'PAID'}">
                <a href="${basePath}/receipt/print?mrId=${mrId}&soId=${soId}&patientId=${patientId}&time=${param.time}" 
                   target="_blank" 
                   class="btn btn-primary fw-bold shadow-sm px-4">
                    <i class="fa-solid fa-print me-2"></i>Print receipt
                </a>
            </c:if>

            <a href="${basePath}/service-order/list" class="btn btn-outline-secondary fw-bold shadow-sm">
                <i class="fa-solid fa-arrow-left me-2"></i>Back to list
            </a>
        </div>

    </div>

    <div class="row justify-content-center">

        <div class="col-lg-10"> 
            <div class="row mb-4 no-print">
                <div class="col-md-6">
                    <div class="card shadow-sm border-0 h-100 rounded-4">
                        <div class="card-header bg-info text-white fw-bold py-3 rounded-top-4">
                            <i class="fa-solid fa-hospital-user me-2"></i>Patient Information
                        </div>
                        <div class="card-body p-4" style="line-height: 1.8;">
                            <div class="d-flex justify-content-between border-bottom pb-2 mb-2">
                                <span class="text-muted">Fullname:</span>
                                <strong class="text-uppercase text-primary fs-5">${patientName}</strong>
                            </div>
                            <div class="row border-bottom pb-2 mb-2">
                                <div class="col-6 text-muted">DOB: <strong class="text-dark ms-1"><fmt:formatDate value="${patientDob}" pattern="yyyy"/></strong></div>
                                <div class="col-6 text-muted text-end">Sex: <strong class="text-dark ms-1">${patientGender}</strong></div>
                            </div>
                            <div class="d-flex justify-content-between border-bottom pb-2 mb-2">
                                <span class="text-muted">Phone:</span>
                                <strong class="text-dark">${patientPhone}</strong>
                            </div>
                            <div class="d-flex justify-content-between">
                                <span class="text-muted">Medical Record Id:</span>
                                <strong class="text-dark">
                                    <c:choose>
                                        <c:when test="${mrId > 0}">
                                            #BA${mrId}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary bg-opacity-25 text-secondary border">Chưa có</span>
                                        </c:otherwise>
                                    </c:choose>
                                </strong>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 mb-3 mb-md-0">
                    <div class="card shadow-sm border-0 h-100 rounded-4">
                        <div class="card-header bg-primary text-white fw-bold py-3 rounded-top-4">
                            <i class="fa-solid fa-receipt me-2"></i>Transaction Information
                        </div>
                        <div class="card-body p-4" style="line-height: 1.8;">
                            <div class="d-flex justify-content-between border-bottom pb-2 mb-2">
                                <span class="text-muted">Receipt code:</span>
                                <strong class="text-dark fs-5">
                                    #<c:choose>
                                        <c:when test="${mrId > 0}">BA${mrId}</c:when>
                                        <c:otherwise>PK${soId}</c:otherwise>
                                    </c:choose>
                                </strong>
                            </div>
                            <div class="d-flex justify-content-between border-bottom pb-2 mb-2">
                                <span class="text-muted">Status:</span>
                                <span>
                                    <c:choose>
                                        <c:when test="${currentStatus == 'PAID'}">
                                            <span class="badge bg-success px-3 py-2"><i class="fa-solid fa-check-circle me-1"></i>PAID</span>
                                        </c:when>
                                        <c:when test="${currentStatus == 'UNPAID'}">
                                            <span class="badge bg-warning text-dark px-3 py-2"><i class="fa-solid fa-clock me-1"></i>UNPAID</span>
                                        </c:when>
                                        <c:when test="${currentStatus == 'REFUNDED'}">
                                            <span class="badge bg-secondary text-dark px-3 py-2"><i class="fa-solid fa-ban me-1"></i>REFUNDED</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary px-3 py-2"><i class="fa-solid fa-ban me-1"></i>CANCELLED</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                            <c:if test="${currentStatus == 'PAID'}">
                                <div class="d-flex justify-content-between border-bottom pb-2 mb-2">
                                    <span class="text-muted">Collection time:</span>
                                    <strong class="text-dark">
                                        <fmt:formatDate value="${paidAt}" pattern="dd/MM/yyyy HH:mm"/>
                                    </strong>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <span class="text-muted">Cashier:</span>
                                    <strong class="text-dark"><i class="fa-regular fa-user me-1"></i>${cashierName}</strong>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <div id="invoice-print-area" class="card border-0 shadow-sm rounded-4 mb-4">
                <div class="card-header bg-white py-3 border-bottom">
                    <h5 class="fw-bold text-dark mb-0"><i class="fa-solid fa-list-check text-primary me-2"></i>Order list</h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle border-0 mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th width="10%" class="text-center text-muted border-0 py-3">No</th>
                                    <th width="60%" class="text-start ps-4 text-muted border-0 py-3">SERVICE NAME</th>
                                    <th width="30%" class="text-end pe-5 text-muted border-0 py-3">AMOUNT</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty details}">
                                        <c:forEach items="${details}" var="item" varStatus="loop">
                                            <tr>
                                                <td class="text-center text-muted fw-bold border-bottom-0 py-3">${loop.index + 1}</td>
                                                <td class="ps-4 fw-bold text-dark fs-6 border-bottom-0 py-3">${item.serviceName}</td>
                                                <td class="text-end pe-5 font-monospace text-dark fw-bold border-bottom-0 py-3">
                                                    <fmt:formatNumber value="${item.price}" pattern="#,###"/> đ
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="3" class="text-center text-muted py-5 border-bottom-0">
                                                <div class="text-muted"><i class="fa-solid fa-box-open fs-1 mb-2"></i></div>
                                                <i>Không có dịch vụ nào cần thanh toán.</i>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                            <tfoot class="bg-primary bg-opacity-10 border-top border-primary border-2">
                                <tr>
                                    <td colspan="2" class="text-end fw-bold py-4 fs-5 text-dark text-uppercase border-0">Total amount:</td>
                                    <td class="text-end pe-5 fw-bold text-danger fs-4 font-monospace border-0">
                                        <fmt:formatNumber value="${totalAmount}" pattern="#,###"/> đ
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>



            <c:if test="${currentStatus == 'UNPAID'}">
                <div class="card border-0 shadow-sm rounded-4 no-print overflow-hidden">
                    <div class="card-header bg-success text-white fw-bold py-3 fs-5">
                        <i class="fa-solid fa-cash-register me-2"></i>Collecting payment.
                    </div>
                    <div class="card-body p-4 p-md-5">

                        <div class="mb-4 text-center">
                            <label class="form-label fw-bold text-muted text-uppercase letter-spacing-1 mb-3">Choose payment method</label>
                            <div class="d-flex justify-content-center gap-4">
                                <div class="form-check form-check-inline custom-radio">
                                    <input class="form-check-input" type="radio" name="payMethod" id="payCash" value="CASH" checked onchange="togglePaymentMethod()">
                                    <label class="form-check-label fw-bold text-success fs-5 ms-2" for="payCash">
                                        <i class="fa-solid fa-money-bill-wave me-2"></i>Cash
                                    </label>
                                </div>
                                <div class="form-check form-check-inline custom-radio">
                                    <input class="form-check-input" type="radio" name="payMethod" id="payBank" value="BANKING" onchange="togglePaymentMethod()">
                                    <label class="form-check-label fw-bold text-primary fs-5 ms-2" for="payBank">
                                        <i class="fa-solid fa-qrcode me-2"></i>Banking
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div id="cashSection" class="bg-success bg-opacity-10 p-4 rounded-4 border border-success border-opacity-25 transition-fade">
                            <div class="row align-items-center text-center">
                                <div class="col-md-4 border-end border-success border-opacity-25 mb-3 mb-md-0">
                                    <div class="text-muted small fw-bold text-uppercase mb-2">Need</div>
                                    <h2 class="text-danger fw-bold mb-0" id="lblTotalAmount" data-total="${totalAmount}">
                                        <fmt:formatNumber value="${totalAmount}" pattern="#,###"/> đ
                                    </h2>
                                </div>
                                <div class="col-md-4 border-end border-success border-opacity-25 px-4 mb-3 mb-md-0">
                                    <div class="text-muted small fw-bold text-uppercase mb-2">Receive</div>
                                    <div class="input-group input-group-lg shadow-sm">
                                        <input type="number" class="form-control fw-bold text-success text-center" id="inputCashGiven" placeholder="0" oninput="calculateChange()">
                                        <span class="input-group-text bg-white text-success fw-bold">đ</span>
                                    </div>
                                </div>
                                <div class="col-md-4 px-4">
                                    <div class="text-muted small fw-bold text-uppercase mb-2">Return change</div>
                                    <h2 class="text-primary fw-bold mb-0" id="lblChangeAmount">0 đ</h2>
                                </div>
                            </div>
                        </div>

                        <div id="bankSection" class="bg-primary bg-opacity-10 p-4 p-md-5 rounded-4 border border-primary border-opacity-25 text-center d-none transition-fade">
                            <h4 class="text-primary fw-bold mb-4"><i class="fa-solid fa-mobile-screen-button me-2"></i>Scan the QR code using your banking app</h4>

                            <div class="bg-white p-3 rounded-4 shadow-sm d-inline-flex mb-4 border align-items-center justify-content-center" style="width: 400px; height: 400px;">
                                <img id="qrCodeImage" src="" alt="Mã QR Thanh Toán" style="max-width: 100%; max-height: 100%; display: none; border-radius: 8px;">
                                <div id="qrLoading" class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
                                    <span class="visually-hidden">Loading...</span>
                                </div>
                            </div>

                            <div class="alert alert-primary py-3 mx-auto shadow-sm border-0 rounded-3 mb-4" style="max-width: 400px;">
                                <div class="mb-2">Content: <strong id="qrTransferContent" class="text-danger fs-5 ms-2"></strong></div>
                                <div>Amount: <strong class="text-danger fs-4 ms-2"><fmt:formatNumber value="${totalAmount}" pattern="#,###"/> đ</strong></div>
                            </div>

                            <button type="button" class="btn btn-warning btn-lg text-dark fw-bold px-5 shadow-sm rounded-pill" id="btnCheckPayment" disabled>
                                <i class="fa-solid fa-spinner fa-spin me-2"></i>The system is awaiting payment.
                            </button>
                        </div>

                        <form action="${basePath}/service-order/checkout" method="POST" class="mt-5 pt-4 border-top d-flex justify-content-between align-items-center" onsubmit="return validatePayment();">
                            <input type="hidden" name="serviceOrderId" value="${soId}">
                            <input type="hidden" name="medicalRecordId" value="${mrId}">
                            <input type="hidden" name="patientId" value="${empty patientId ? 0 : patientId}"> 
                            <input type="hidden" name="paymentMethod" id="hiddenPaymentMethod" value="CASH">

                            <button type="submit" class="btn btn-success btn-lg fw-bold shadow-lg px-5 rounded-pill" id="btnConfirmPay" disabled>
                                <i class="fa-solid fa-check-double me-2"></i>Confirm Payment
                            </button>
                        </form>

                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<div id="paymentSuccessOverlay" class="payment-success-overlay d-none no-print">
    <div class="payment-success-box">
        <div class="success-animation">
            <div class="loader-circle"></div>
            <div class="checkmark-circle d-none">
                <i class="fa-solid fa-check"></i>
            </div>
        </div>
        <h4 class="mt-4 fw-bold text-success">Payment successful!</h4>
        <p class="text-muted">The system is updating the invoice...</p>
        <audio id="tingSound" src="${pageContext.request.contextPath}/assets/pay-success.mp3"></audio> 
    </div>
</div>

<style>
    /* ===== NÂNG CẤP GIAO DIỆN ===== */
    .letter-spacing-1 {
        letter-spacing: 1px;
    }
    .transition-fade {
        transition: all 0.3s ease-in-out;
    }

    /* Làm to radio button cho dễ bấm */
    .custom-radio .form-check-input {
        width: 1.5em;
        height: 1.5em;
        cursor: pointer;
    }
    .custom-radio .form-check-label {
        cursor: pointer;
    }

    /* ===== SUCCESS OVERLAY ===== */
    .payment-success-overlay {
        position: fixed;
        inset: 0;
        background: rgba(0,0,0,0.45);
        backdrop-filter: blur(4px);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 9999;
    }
    .payment-success-box {
        background: white;
        padding: 40px 60px;
        border-radius: 20px;
        text-align: center;
        box-shadow: 0 20px 60px rgba(0,0,0,0.2);
        animation: popIn 0.4s ease;
    }
    @keyframes popIn {
        from {
            transform: scale(0.7);
            opacity: 0;
        }
        to {
            transform: scale(1);
            opacity: 1;
        }
    }
    .loader-circle {
        width: 90px;
        height: 90px;
        border: 6px solid #d1e7dd;
        border-top: 6px solid #198754;
        border-radius: 50%;
        animation: spin 0.8s linear infinite;
        margin: auto;
    }
    @keyframes spin {
        to {
            transform: rotate(360deg);
        }
    }
    .checkmark-circle {
        width: 90px;
        height: 90px;
        background: #198754;
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
        margin: auto;
        animation: scaleCheck 0.4s ease;
    }
    .checkmark-circle i {
        color: white;
        font-size: 40px;
    }
    @keyframes scaleCheck {
        from {
            transform: scale(0.3);
            opacity: 0;
        }
        to {
            transform: scale(1);
            opacity: 1;
        }
    }

    /* =======================================================
       ===== PHÉP MÀU CHO MÁY IN (@media print) =====
       ======================================================= */
    @media print {
        /* 1. Ẩn TOÀN BỘ trang web gốc (menu, nút bấm, sidebar...) */
        body * {
            visibility: hidden !important;
        }

        /* 2. Chỉ hiện thị duy nhất khu vực biên lai */
        #invoice-print-area, #invoice-print-area * {
            visibility: visible !important;
        }

        /* 3. Kéo tờ biên lai lên góc trên cùng bên trái của tờ giấy A4 */
        #invoice-print-area {
            position: absolute !important;
            left: 0 !important;
            top: 0 !important;
            width: 100% !important;
            border: none !important;
            box-shadow: none !important;
            margin: 0 !important;
            padding: 0 !important;
        }

        /* 4. Ẩn những thứ không cần thiết trong lúc in */
        .no-print {
            display: none !important;
        }

        /* 5. Hiện header, chữ ký (chỉ dành riêng cho lúc in) */
        .print-only {
            display: block !important;
        }
        .print-only.row {
            display: flex !important; /* Fix lỗi lưới row khi in */
        }

        /* 6. Đảm bảo màu in ra đen trắng sắc nét */
        .text-primary, .text-danger, .text-success, .bg-light {
            color: #000 !important;
            background-color: transparent !important;
        }
        .badge {
            border: 1px solid #000;
            color: #000 !important;
        }
    }

</style>

<script>
    // Gắn ngày giờ hiện tại cho Biên lai lúc In
    document.addEventListener("DOMContentLoaded", function () {
        const today = new Date();
        const dateStr = today.toLocaleDateString('vi-VN') + ' - ' + today.toLocaleTimeString('vi-VN');
        const printDateEl = document.getElementById('printDate');
        if (printDateEl)
            printDateEl.innerText = dateStr;
    });

    const BANK_ID = "MB";
    const ACCOUNT_NO = "0373347253";
    const ACCOUNT_NAME = "TRAN GIA HUY";

    let paymentCheckInterval = null;

    function togglePaymentMethod() {
        const isCash = document.getElementById('payCash').checked;
        const cashSec = document.getElementById('cashSection');
        const bankSec = document.getElementById('bankSection');

        if (isCash) {
            bankSec.classList.add('d-none');
            cashSec.classList.remove('d-none');
        } else {
            cashSec.classList.add('d-none');
            bankSec.classList.remove('d-none');
        }

        document.getElementById('hiddenPaymentMethod').value = isCash ? 'CASH' : 'BANKING';
        const btnConfirm = document.getElementById('btnConfirmPay');

        if (isCash) {
            calculateChange();
            btnConfirm.style.display = 'block';
            if (paymentCheckInterval)
                clearInterval(paymentCheckInterval);
        } else {
            generateQRCode();
            btnConfirm.style.display = 'none';
            startAutoCheckPayment();
        }
    }

    function calculateChange() {
        const totalAmount = parseFloat(document.getElementById('lblTotalAmount').getAttribute('data-total')) || 0;
        const cashGiven = parseFloat(document.getElementById('inputCashGiven').value) || 0;
        const changeAmount = cashGiven - totalAmount;
        const lblChange = document.getElementById('lblChangeAmount');
        const btnConfirm = document.getElementById('btnConfirmPay');

        if (cashGiven === 0) {
            lblChange.innerText = "0 đ";
            lblChange.className = "text-muted fw-bold mb-0";
            btnConfirm.disabled = true;
        } else if (changeAmount < 0) {
            lblChange.innerText = "Not enough money!";
            lblChange.className = "text-danger fw-bold mb-0";
            btnConfirm.disabled = true;
        } else {
            lblChange.innerText = changeAmount.toLocaleString('vi-VN') + " đ";
            lblChange.className = "text-primary fw-bold mb-0 fs-3";
            btnConfirm.disabled = false;
        }
    }

    function validatePayment() {
        const isCash = document.getElementById('payCash').checked;
        if (isCash) {
            const totalAmount = parseFloat(document.getElementById('lblTotalAmount').getAttribute('data-total')) || 0;
            const cashGiven = parseFloat(document.getElementById('inputCashGiven').value) || 0;
            if (cashGiven < totalAmount) {
                alert("The money the customer gave wasn't enough to pay this bill!");
                return false;
            }
        }
        return confirm('Confirm that payment has been received enough and begin the system update?');
    }

    function generateQRCode() {
        const totalAmount = parseFloat(document.getElementById('lblTotalAmount').getAttribute('data-total')) || 0;

        const mrIdInput = document.querySelector('input[name="medicalRecordId"]');
        const soIdInput = document.querySelector('input[name="serviceOrderId"]');

        const mrId = mrIdInput ? (parseInt(mrIdInput.value) || 0) : 0;
        const soId = soIdInput ? (parseInt(soIdInput.value) || 0) : 0;


        let addInfo = "";
        if (mrId > 0) {
            addInfo = "THANHTOAN BA" + mrId; //xét nghiệm
        } else if (soId > 0) {
            addInfo = "THANHTOAN PK" + soId; //phiếu khám
        } else {
            addInfo = "LOI_MA_DON";     //fallbaclk nếu lỗi
        }


        document.getElementById('qrTransferContent').innerText = addInfo;
        const qrUrl = "https://img.vietqr.io/image/" + BANK_ID + "-" + ACCOUNT_NO + "-compact2.png?amount=" + totalAmount + "&addInfo=" + encodeURIComponent(addInfo) + "&accountName=" + encodeURIComponent(ACCOUNT_NAME);

        const qrImage = document.getElementById('qrCodeImage');
        const qrLoading = document.getElementById('qrLoading');

        qrImage.style.display = 'none';
        qrLoading.style.display = 'inline-block';

        qrImage.onload = function () {
            qrLoading.style.display = 'none';
            qrImage.style.display = 'inline-block';
        };
        qrImage.src = qrUrl;
    }

    function startAutoCheckPayment() {
        const mrId = document.querySelector('input[name="medicalRecordId"]').value;
        const soId = document.querySelector('input[name="serviceOrderId"]').value || 0;

        const btnCheck = document.getElementById('btnCheckPayment');

        btnCheck.innerHTML = '<i class="fa-solid fa-spinner fa-spin me-2"></i>The system is awaiting payment';
        btnCheck.disabled = true;

        paymentCheckInterval = setInterval(() => {
            fetch('${pageContext.request.contextPath}/api/check-payment?mrId=' + mrId + '&soId=' + soId)
                    .then(response => response.json())
                    .then(data => {
                        if (data.status === 'OVERPAID') {
                            clearInterval(paymentCheckInterval);
                            btnCheck.classList.replace('btn-warning', 'btn-success');
                            btnCheck.innerHTML = '<i class="fa-solid fa-check-circle me-2"></i>Payment successful!';
                            alert("⚠️ NOTE: The customer has transferred an EXCESS amount " + data.excess.toLocaleString('vi-VN') + " đ.\nThe system has confirmed the order. Please return the change to the customer!");
                            window.location.href = '${basePath}/service-order/list';
                        } else if (data.status === 'UNDERPAID') {
                            clearInterval(paymentCheckInterval);
                            btnCheck.classList.replace('btn-warning', 'btn-danger');
                            btnCheck.innerHTML = '<i class="fa-solid fa-triangle-exclamation me-2"></i>The money was insufficient!';
                            alert("❌ ❌ WARNING: A customer has just transferred " + data.received.toLocaleString('vi-VN') + " đ.\nUnpaid bill.");

                            setTimeout(() => {
                                btnCheck.innerHTML = '<i class="fa-solid fa-rotate-right me-2"></i>Continue waiting for payment...';
                                btnCheck.disabled = false;
                                btnCheck.classList.replace('btn-danger', 'btn-warning');
                                btnCheck.onclick = startAutoCheckPayment;
                            }, 5000);
                        } else if (data.status === 'PAID') {
                            clearInterval(paymentCheckInterval);
                            btnCheck.classList.replace('btn-warning', 'btn-success');
                            btnCheck.innerHTML = '<i class="fa-solid fa-check-circle me-2"></i>Payment successful!';
                            showPaymentSuccess();
                        }
                    })
                    .catch(err => console.error("Awaiting payment..."));
        }, 3000);
    }

    function showPaymentSuccess() {
        const overlay = document.getElementById("paymentSuccessOverlay");
        const loader = overlay.querySelector(".loader-circle");
        const checkmark = overlay.querySelector(".checkmark-circle");
        const ting = document.getElementById("tingSound");

        overlay.classList.remove("d-none");

        setTimeout(() => {
            loader.classList.add("d-none");
            checkmark.classList.remove("d-none");
            if (ting) {
                ting.currentTime = 0;
                ting.play().catch(() => {
                });
            }
        }, 1200);

        setTimeout(() => {
            window.location.href = '${basePath}/service-order/list';
        }, 2700);
    }
</script>