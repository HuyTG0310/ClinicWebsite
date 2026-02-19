<%@page contentType="text/html" pageEncoding="UTF-8"%>


<style>
    /* Center content */
    .page-wrapper {
        padding: 40px 0;
    }

    /* =========================
       PROFILE CONTAINER
    ========================= */
    .profile-container {
        width: 900px;
        max-width: 95%;
        background: #ffffff;
        border-radius: 18px;
        padding: 32px;
        box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
        border: 1px solid #e3e8ef;
    }

    /* =========================
       TITLE
    ========================= */
    h2 {
        text-align: center;
        font-size: 26px;
        font-weight: 700;
        color: #0d6efd;
        margin-bottom: 28px;
        letter-spacing: 1px;
        text-transform: uppercase;
    }

    /* =========================
       PROFILE CARD
    ========================= */
    .profile-card {
        background: #f8f9fc;
        border-radius: 14px;
        padding: 22px 26px;
        border: 1px solid #dee2e6;
    }

    .profile-row {
        display: flex;
        justify-content: space-between;
        padding: 12px 0;
        border-bottom: 1px solid #dee2e6;
        font-size: 15px;
    }

    .profile-row:last-child {
        border-bottom: none;
    }

    .profile-row b {
        color: #495057;
        font-weight: 600;
    }

    .profile-value {
        font-weight: 600;
        color: #212529;
    }

    /* =========================
       DIVIDER
    ========================= */
    hr {
        margin: 26px 0;
        border: none;
        height: 1px;
        background: #dee2e6;
    }

    /* =========================
       ACTION BUTTONS
    ========================= */
    .actions {
        display: flex;
        justify-content: center;
        gap: 16px;
        flex-wrap: wrap;
    }

    .profile-btn {
        padding: 12px 20px;
        border-radius: 10px;
        font-size: 14px;
        font-weight: 600;
        transition: all 0.25s ease;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        border: none;
        cursor: pointer;
    }

    /* Primary – Update */
    .profile-btn.btn-update {
        background-color: #0d6efd;
        color: #ffffff;
    }

    .profile-btn.btn-update:hover {
        background-color: #0b5ed7;
        transform: translateY(-2px);
    }

    /* Danger – Change Password */
    .profile-btn.btn-password {
        background-color: #dc3545;
        color: #ffffff;
    }

    .profile-btn.btn-password:hover {
        background-color: #bb2d3b;
        transform: translateY(-2px);
    }

    .icon {
        font-size: 16px;
    }

    /* =========================
       FOOTER NOTE
    ========================= */
    .footer-note {
        text-align: center;
        margin-top: 22px;
        color: #6c757d;
        font-size: 14px;
    }

    /* =========================
       MODAL OVERLAY
    ========================= */
    .modal {
        display: none;
        position: fixed;
        inset: 0;
        background: rgba(0, 0, 0, 0.45);
        z-index: 1000;
        justify-content: center;
        align-items: center;
    }

    /* =========================
       MODAL CONTENT
    ========================= */
    .modal-content {
        background: #ffffff;
        width: 420px;
        max-width: 92%;
        padding: 24px;
        border-radius: 16px;
        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.25);
    }

    .modal-content h3 {
        text-align: center;
        margin-bottom: 18px;
        font-size: 20px;
        font-weight: 700;
        color: #0d6efd;
    }

    /* =========================
       FORM
    ========================= */
    .input-group {
        margin-bottom: 16px;
    }

    .input-group label {
        display: block;
        font-weight: 600;
        margin-bottom: 6px;
        color: #495057;
    }

    .input-group input {
        width: 100%;
        padding: 11px 12px;
        border-radius: 8px;
        border: 1px solid #ced4da;
        font-size: 14px;
    }

    .input-group input:focus {
        outline: none;
        border-color: #0d6efd;
        box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.15);
    }

    .input-group input[readonly] {
        background: #e9ecef;
        cursor: not-allowed;
    }

    /* =========================
       MODAL ACTIONS
    ========================= */
    .modal-actions {
        display: flex;
        gap: 12px;
        margin-top: 18px;
    }

    .modal-actions button {
        flex: 1;
        padding: 11px;
        border-radius: 8px;
        border: none;
        font-weight: 600;
        cursor: pointer;
    }

    .modal-actions button[type="submit"] {
        background-color: #0d6efd;
        color: #ffffff;
    }

    .modal-actions .cancel {
        background-color: #adb5bd;
        color: #212529;
    }

    .modal-actions button:hover {
        opacity: 0.9;
    }

    /* =========================
       RESPONSIVE
    ========================= */
    @media (max-width: 600px) {
        h2 {
            font-size: 22px;
        }

        .profile-row {
            flex-direction: column;
            gap: 4px;
        }
    }

    .profile-container {
        width: 900px;
        max-width: 95%;
        background: #ffffff;
        border-radius: 18px;
        padding: 32px;
        box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
        border: 1px solid #e3e8ef;

        animation: profileFadeUp 0.7s ease-out; /* ⭐ thêm */
    }


    @keyframes profileFadeUp {
        from {
            opacity: 0;
            transform: translateY(24px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }


</style>


<div class="page-wrapper">
    <div class="profile-container">
        <h2>PROFILE DETAIL</h2>

        <div class="profile-card">
            <div class="profile-row">
                <b>User ID:</b>
                <span class="profile-value">${user.userId}</span>
            </div>

            <div class="profile-row">
                <b>Username:</b>
                <span class="profile-value">${user.username}</span>
            </div>

            <div class="profile-row">
                <b>Full Name:</b>
                <span class="profile-value">${user.fullName}</span>
            </div>

            <div class="profile-row">
                <b>Phone:</b>
                <span class="profile-value">${user.phone}</span>
            </div>

            <div class="profile-row">
                <b>Email:</b>
                <span class="profile-value">${user.email}</span>
            </div>

            <div class="profile-row">
                <b>Role:</b>
                <span class="profile-value">${user.roleName}</span>
            </div>
        </div>

        <hr>

        <div class="actions">
            <button class="profile-btn btn-update" onclick="openUpdateModal()">
                <span class="icon">📝</span> Update Profile Detail
            </button>

            <a class="profile-btn btn-password"
               href="${pageContext.request.contextPath}/profile/change-password?userId=${user.userId}">
                <span class="icon">🔒</span> Change Password
            </a>
        </div>

        <div class="footer-note">
            ✨ Your account information is protected & displayed securely.
        </div>
    </div>
</div>
<div id="updateModal" class="modal">
    <div class="modal-content">

        <h3>Update Profile</h3>

        <form action="${pageContext.request.contextPath}/profile/update" method="post">


            <input type="hidden" name="userId" value="${user.userId}" />

            <div class="input-group">
                <label>Username</label>
                <input type="text" value="${user.username}" readonly />
            </div>

            <div class="input-group">
                <label>Full Name</label>
                <input
                    type="text"
                    name="fullName"
                    value="${user.fullName}"
                    required
                    pattern="^([A-ZÀ-Ỹ][a-zà-ỹ]+)(\s[A-ZÀ-Ỹ][a-zà-ỹ]+)*$"
                    title="Each word must start with a capital letter and contain only letters"
                    />
            </div>

            <div class="input-group">
                <label>Phone</label>
                <input
                    type="text"
                    name="phone"
                    value="${user.phone}"
                    required
                    pattern="^0\d{9}$"
                    title="Phone number must contain exactly 10 digits and start with 0"
                    />
            </div>

            <div class="input-group">
                <label>Email</label>
                <input
                    type="email"
                    name="email"
                    value="${user.email}"
                    required
                    pattern="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.com$"
                    title="Email must contain '@' and end with .com"
                    oninput="this.value=this.value.trim()"
                    />
            </div>

            <div class="modal-actions">
                <button type="submit" id="saveBtn">💾 Save</button>
                <button type="button" class="cancel" onclick="closeUpdateModal()">✖ Cancel</button>
            </div>
        </form>
    </div>
</div>      

<script>
    function openUpdateModal() {
        document.getElementById("updateModal").style.display = "flex";
    }

    function closeUpdateModal() {
        document.getElementById("updateModal").style.display = "none";
    }


    window.onclick = function (event) {
        const modal = document.getElementById("updateModal");
    };
</script>    

<script>
    const form = document.querySelector("#updateModal form");
    const saveBtn = document.getElementById("saveBtn");

    saveBtn.addEventListener("click", function (event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            form.reportValidity();
        }

    });
</script>
