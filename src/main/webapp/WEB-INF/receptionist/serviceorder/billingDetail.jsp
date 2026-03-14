<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="container-fluid mb-5">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">

        <div>
            <h2 class="mb-1">
                <i class="fa-solid fa-file-invoice text-primary me-2"></i>
                Service Order Detail
            </h2>
            <p class="text-muted mb-0">Check service details before making payment.</p>
        </div>

        <div class="d-flex gap-2">
            <a href="${basePath}/service-order/list"
               class="btn btn-outline-secondary">
                <i class="fa-solid fa-arrow-left me-2"></i>Back to list
            </a>
               
            <c:if test="${currentStatus == 'PAID'}">
                <a href="${basePath}/receipt/print?mrId=${mrId}&soId=${soId}&patientId=${patientId}&time=${param.time}"
                   target="_blank"
                   class="btn btn-primary">
                    <i class="fa-solid fa-print me-2"></i>Print
                </a>
            </c:if>
        </div>

    </div>



    <!-- PATIENT INFORMATION -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-user text-primary me-2"></i>
                Patient Information
            </h5>
        </div>

        <div class="card-body">

            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">Full Name</label>
                    <div class="fw-semibold text-uppercase text-primary">
                        ${patientName}
                    </div>
                </div>

                <div class="col-md-3">
                    <label class="text-muted small">Gender</label>
                    <div class="fw-semibold">
                        ${patientGender}
                    </div>
                </div>

                <div class="col-md-3">
                    <label class="text-muted small">DOB</label>
                    <div class="fw-semibold">
                        <fmt:formatDate value="${patientDob}" pattern="yyyy"/>
                    </div>
                </div>

            </div>


            <div class="row mb-3">

                <div class="col-md-6">
                    <label class="text-muted small">Phone</label>
                    <div class="fw-semibold">
                        ${patientPhone}
                    </div>
                </div>

                <div class="col-md-6">
                    <label class="text-muted small">Medical Record ID</label>
                    <div class="fw-semibold">
                        <c:choose>
                            <c:when test="${mrId > 0}">
                                #BA${mrId}
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary bg-opacity-25 text-secondary border">
                                    None
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

            </div>

        </div>
    </div>



    <!-- TRANSACTION INFORMATION -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-receipt text-primary me-2"></i>
                Transaction Information
            </h5>
        </div>

        <div class="card-body">

            <div class="mb-3">
                <label class="text-muted small">Receipt Code</label>
                <div class="fw-semibold fs-5">
                    #
                    <c:choose>
                        <c:when test="${mrId > 0}">BA${mrId}</c:when>
                        <c:otherwise>PK${soId}</c:otherwise>
                    </c:choose>
                </div>
            </div>


            <div class="mb-3">
                <label class="text-muted small">Status</label>
                <div>

                    <c:choose>

                        <c:when test="${currentStatus == 'PAID'}">
                            <span class="badge bg-success">
                                <i class="fa-solid fa-check-circle me-1"></i>PAID
                            </span>
                        </c:when>

                        <c:when test="${currentStatus == 'UNPAID'}">
                            <span class="badge bg-warning text-dark">
                                <i class="fa-solid fa-clock me-1"></i>UNPAID
                            </span>
                        </c:when>

                        <c:when test="${currentStatus == 'REFUNDED'}">
                            <span class="badge bg-secondary">
                                <i class="fa-solid fa-ban me-1"></i>REFUNDED
                            </span>
                        </c:when>

                        <c:otherwise>
                            <span class="badge bg-secondary">
                                <i class="fa-solid fa-ban me-1"></i>CANCELLED
                            </span>
                        </c:otherwise>

                    </c:choose>

                </div>
            </div>


            <c:if test="${currentStatus == 'PAID'}">

                <div class="mb-3">
                    <label class="text-muted small">Collection Time</label>
                    <div class="fw-semibold">
                        <fmt:formatDate value="${paidAt}" pattern="dd/MM/yyyy HH:mm"/>
                    </div>
                </div>

                <div>
                    <label class="text-muted small">Cashier</label>
                    <div class="fw-semibold">
                        <i class="fa-regular fa-user me-1"></i>${cashierName}
                    </div>
                </div>

            </c:if>

        </div>
    </div>



    <!-- SERVICE LIST -->
    <div id="invoice-print-area" class="card shadow-sm mb-4">

        <div class="card-header bg-white py-3">
            <h5 class="mb-0">
                <i class="fa-solid fa-list-check text-primary me-2"></i>
                Order List
            </h5>
        </div>

        <div class="card-body p-0">

            <div class="table-responsive">

                <table class="table table-hover align-middle mb-0">

                    <thead class="table-light">
                        <tr>
                            <th width="10%" class="text-center">No</th>
                            <th width="60%">Service Name</th>
                            <th width="30%" class="text-end pe-5">Amount</th>
                        </tr>
                    </thead>

                    <tbody>

                        <c:choose>

                            <c:when test="${not empty details}">

                                <c:forEach items="${details}" var="item" varStatus="loop">

                                    <tr>

                                        <td class="text-center fw-bold">
                                            ${loop.index + 1}
                                        </td>

                                        <td class="fw-semibold">
                                            ${item.serviceName}
                                        </td>

                                        <td class="text-end pe-5 fw-bold font-monospace">
                                            <fmt:formatNumber value="${item.price}" pattern="#,###"/> đ
                                        </td>

                                    </tr>

                                </c:forEach>

                            </c:when>


                            <c:otherwise>

                                <tr>

                                    <td colspan="3" class="text-center text-muted py-5">

                                        <i class="fa-solid fa-box-open fs-1 mb-2"></i>
                                        <div>No services found.</div>

                                    </td>

                                </tr>

                            </c:otherwise>

                        </c:choose>

                    </tbody>


                    <tfoot class="bg-light border-top">

                        <tr>

                            <td colspan="2" class="text-end fw-bold fs-5">
                                Total Amount
                            </td>

                            <td class="text-end pe-5 fw-bold text-danger fs-4 font-monospace">
                                <fmt:formatNumber value="${totalAmount}" pattern="#,###"/> đ
                            </td>

                        </tr>

                    </tfoot>

                </table>

            </div>

        </div>




    </div>

    <c:if test="${currentStatus == 'UNPAID'}">

        <div class="card shadow-sm mb-4 no-print">

            <div class="card-header bg-white py-3">
                <h5 class="mb-0">
                    <i class="fa-solid fa-cash-register text-primary me-2"></i>
                    Payment
                </h5>
            </div>

            <div class="card-body">

                <!-- CHOOSE PAYMENT METHOD -->

                <div class="mb-4 text-center">

                    <label class="form-label fw-bold text-muted text-uppercase mb-3">
                        Choose payment method
                    </label>

                    <div class="d-flex justify-content-center gap-4">

                        <div class="form-check form-check-inline custom-radio">

                            <input class="form-check-input"
                                   type="radio"
                                   name="payMethod"
                                   id="payCash"
                                   value="CASH"
                                   checked
                                   onchange="togglePaymentMethod()">

                            <label class="form-check-label fw-bold text-success fs-5 ms-2" for="payCash">
                                <i class="fa-solid fa-money-bill-wave me-2"></i>Cash
                            </label>

                        </div>

                        <div class="form-check form-check-inline custom-radio">

                            <input class="form-check-input"
                                   type="radio"
                                   name="payMethod"
                                   id="payBank"
                                   value="BANKING"
                                   onchange="togglePaymentMethod()">

                            <label class="form-check-label fw-bold text-primary fs-5 ms-2" for="payBank">
                                <i class="fa-solid fa-qrcode me-2"></i>Banking
                            </label>

                        </div>

                    </div>

                </div>

                <!-- CASH PAYMENT -->

                <div id="cashSection" class="bg-light p-4 rounded-3 border">

                    <div class="row align-items-center text-center">

                        <div class="col-md-4">

                            <div class="text-muted small mb-2">Need</div>

                            <h3 class="text-danger fw-bold" id="lblTotalAmount" data-total="${totalAmount}">
                                <fmt:formatNumber value="${totalAmount}" pattern="#,###"/> đ
                            </h3>

                        </div>

                        <div class="col-md-4">

                            <div class="text-muted small mb-2">Receive</div>

                            <input type="number"
                                   class="form-control text-center fw-bold"
                                   id="inputCashGiven"
                                   placeholder="0"
                                   oninput="calculateChange()">

                        </div>

                        <div class="col-md-4">

                            <div class="text-muted small mb-2">Return change</div>

                            <h3 class="text-primary fw-bold" id="lblChangeAmount">
                                0 đ
                            </h3>

                        </div>

                    </div>

                </div>

                <!-- BANK PAYMENT -->

                <div id="bankSection"
                     class="bg-light p-4 rounded-3 border text-center d-none">

                    <h5 class="text-primary fw-bold mb-4">
                        <i class="fa-solid fa-mobile-screen-button me-2"></i>
                        Scan QR to pay
                    </h5>

                    <div class="bg-white p-3 rounded shadow-sm d-inline-block mb-3">

                        <img id="qrCodeImage"
                             src=""
                             style="max-width:250px; display:none;">

                        <div id="qrLoading"
                             class="spinner-border text-primary"
                             role="status">
                        </div>

                    </div>

                    <div class="alert alert-primary">

                        Content:
                        <strong id="qrTransferContent" class="text-danger"></strong>

                        <br>

                        Amount:
                        <strong class="text-danger">
                            <fmt:formatNumber value="${totalAmount}" pattern="#,###"/> đ
                        </strong>

                    </div>
                    <button type="button"
                            class="btn btn-warning btn-lg text-dark fw-bold px-5 shadow-sm rounded-pill"
                            id="btnCheckPayment"
                            disabled>
                        <i class="fa-solid fa-spinner fa-spin me-2"></i>The system is awaiting payment.
                    </button>

                </div>

                <!-- CONFIRM PAYMENT -->

                <form action="${basePath}/service-order/checkout"
                      method="POST"
                      class="mt-4 d-flex justify-content-end"
                      onsubmit="return validatePayment();">

                    <input type="hidden" name="serviceOrderId" value="${soId}">
                    <input type="hidden" name="medicalRecordId" value="${mrId}">
                    <input type="hidden" name="patientId" value="${empty patientId ? 0 : patientId}">
                    <input type="hidden" name="paymentMethod" id="hiddenPaymentMethod" value="CASH">

                    <button type="submit"
                            class="btn btn-success btn-lg fw-bold"
                            id="btnConfirmPay"
                            disabled>

                        <i class="fa-solid fa-check-double me-2"></i>
                        Confirm Payment

                    </button>

                </form>

            </div>

        </div>

    </c:if>


    <div id="paymentSuccessOverlay" class="payment-success-overlay d-none no-print">

        <div class="payment-success-box">

            <div class="success-animation">

                <div class="loader-circle"></div>

                <div class="checkmark-circle d-none">
                    <i class="fa-solid fa-check"></i>
                </div>

            </div>

            <h4 class="mt-4 fw-bold text-success">
                Payment successful!
            </h4>

            <p class="text-muted">
                The system is updating the invoice...
            </p>

            <audio id="tingSound"
                   src="${pageContext.request.contextPath}/assets/pay-success.mp3">
            </audio>

        </div>

    </div>




    <style>
        .card {
            border-radius: 12px;
            border: none;
        }

        .card-header {
            border-bottom: 1px solid rgba(0,0,0,0.08);
            border-radius: 12px 12px 0 0 !important;
        }

        .badge {
            padding: 0.45em 0.9em;
            font-weight: 500;
            border-radius: 6px;
        }

        .btn {
            border-radius: 8px;
            font-weight: 500;
            transition: all 0.25s ease;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }

        .payment-success-overlay{
            position:fixed;
            inset:0;
            background:rgba(0,0,0,0.45);
            backdrop-filter:blur(4px);
            display:flex;
            justify-content:center;
            align-items:center;
            z-index:9999;
        }

        .payment-success-box{
            background:white;
            padding:40px 60px;
            border-radius:20px;
            text-align:center;
            box-shadow:0 20px 60px rgba(0,0,0,0.2);
        }

        .loader-circle{
            width:90px;
            height:90px;
            border:6px solid #d1e7dd;
            border-top:6px solid #198754;
            border-radius:50%;
            animation:spin 0.8s linear infinite;
            margin:auto;
        }

        @keyframes spin{
            to{
                transform:rotate(360deg);
            }
        }

        .checkmark-circle{
            width:90px;
            height:90px;
            background:#198754;
            border-radius:50%;
            display:flex;
            justify-content:center;
            align-items:center;
            margin:auto;
        }

        .checkmark-circle i{
            color:white;
            font-size:40px;
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