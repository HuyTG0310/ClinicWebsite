package filter;

import dao.*;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import model.*;

@WebFilter(filterName = "/*", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter {

    private static final boolean debug = true;

    private FilterConfig filterConfig = null;

    public AuthorizationFilter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AuthorizationFilter:DoBeforeProcessing");
        }
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AuthorizationFilter:DoAfterProcessing");
        }
    }

    private PrivilegeDAO privilegeDAO = new PrivilegeDAO();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String ctx = req.getContextPath();
        String uri = req.getRequestURI();
        String path = uri.substring(ctx.length());
        if (!uri.startsWith(ctx)) {
            res.sendError(404);
            return;
        }

        if (uri.contains("/api/sepay-webhook")) {
            chain.doFilter(request, response);
            return; // Cho đi qua luôn và thoát hàm filter
        }

        System.out.println("DEBUG FILTER PATH: " + path);

        // --- 1. Bỏ qua các file tĩnh và trang công khai ---
        if (path.startsWith("/login")
                || path.startsWith("/forget-password")
                || path.startsWith("/send-otp")
                || path.startsWith("/verify-otp")
                || path.startsWith("/reset-password")
                || path.startsWith("/forgotPassword.jsp")
                || path.startsWith("/verifyOTP.jsp")
                || path.startsWith("/resetPassword.jsp")
                || path.startsWith("/assets")
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/uploads")) {

            chain.doFilter(request, response);
            return;
        }

        // --- 2. Kiểm tra Login ---
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        System.out.println("🔍 KIỂM TRA SESSION: ID = " + (session != null ? session.getId() : "NULL") + " | User = " + user);

        if (user == null) {
            res.sendRedirect(ctx + "/login");
            return;
        }
        System.out.println("🚀 FILTER CHECK: Path = " + path);
        System.out.println("👤 User: " + user.getFullName() + " | Role: " + user.getRoleName());

        // --- 3. Tải & Làm sạch danh sách quyền ---
        List<String> rawPrivileges = privilegeDAO.getPrivilegeCodesByUserId(user.getUserId());
        List<String> privileges = new ArrayList<>();
        if (rawPrivileges != null) {
            for (String p : rawPrivileges) {
                if (p != null) {
                    privileges.add(p.trim());
                }
            }
        }
        //KIỂM TRA ROLE CÓ BỊ INACTIVE KHÔNG
        RoleDAO roleDAO = new RoleDAO();
        Role currentRole = roleDAO.getRoleById(user.getRoleId());

        if (currentRole == null || !currentRole.isIsActive()) {
            System.out.println("⛔ CẢNH BÁO: Role '" + user.getRoleName() + "' đã bị vô hiệu hóa!");

            // Hủy session để user phải log out
            session.invalidate();

            // Chuyển hướng về login kèm thông báo lỗi
            res.sendRedirect(ctx + "/login?error=role_inactive");
            return;
        }

        System.out.println("🔑 Quyền đã xử lý (Cleaned): " + privileges);

        // QUYỀN CERTIFICATION DO ADMIN CẤP
        req.setAttribute("hasCertView", privileges.contains("CERTIFICATION_VIEW"));
        req.setAttribute("hasCertAdd", privileges.contains("CERTIFICATION_ADD"));

        req.setAttribute("hasMedicineView", privileges.contains("MEDICINE_VIEW"));
        req.setAttribute("hasMedicineCreate", privileges.contains("MEDICINE_CREATE"));
        req.setAttribute("hasMedicineEdit", privileges.contains("MEDICINE_EDIT"));
        req.setAttribute("hasRoomView", privileges.contains("ROOM_VIEW"));
        req.setAttribute("hasRoomCreate", privileges.contains("ROOM_CREATE"));
        req.setAttribute("hasRoomEdit", privileges.contains("ROOM_EDIT"));
        req.setAttribute("hasPatientView", privileges.contains("PATIENT_VIEW"));
        req.setAttribute("hasPatientCreate", privileges.contains("PATIENT_CREATE"));
        req.setAttribute("hasPatientEdit", privileges.contains("PATIENT_EDIT"));
        req.setAttribute("hasPatientDelete", privileges.contains("PATIENT_DELETE"));

        req.setAttribute("hasAppointmentCreate", privileges.contains("APPOINTMENT_CREATE"));
        req.setAttribute("hasAppointmentEdit", privileges.contains("APPOINTMENT_EDIT"));
        req.setAttribute("hasAppointmentView", privileges.contains("APPOINTMENT_VIEW"));

        if (path.startsWith("/profile")) {
            chain.doFilter(request, response);
            return;
        }

        // --- 5. Kiểm tra Role Prefix (Bảo vệ folder mẹ) ---
        String role = user.getRoleName() != null ? user.getRoleName().trim() : "";

        if (path.startsWith("/admin") && !"ADMIN".equalsIgnoreCase(role)) {
            res.sendError(403);
            return;
        }
        if (path.startsWith("/doctor") && !"DOCTOR".equalsIgnoreCase(role)) {
            res.sendError(403);
            return;
        }
        if (path.startsWith("/receptionist") && !"RECEPTIONIST".equalsIgnoreCase(role)) {
            res.sendError(403);
            return;
        }
        if (path.startsWith("/lab") && !"LAB TECHNICIAN".equalsIgnoreCase(role)) {
            res.sendError(403);
            return;
        }

        if (path.equals("/admin/dashboard") || path.equals("/doctor/dashboard") || path.equals("/receptionist/dashboard") || path.equals("/lab/dashboard")) {
            chain.doFilter(request, response);
            return;
        }

        // --- 6. CHECK PRIVILEGE MAP (Chặn truy cập Module cụ thể) ---
        String requiredPriv = null;

        // Mapping quyền cho Certification
        if (path.contains("/certification/add")) {
            requiredPriv = "CERTIFICATION_ADD";
        } else if (path.contains("/certification/my")) {
            requiredPriv = "CERTIFICATION_VIEW";
        }
        
        if (path.contains("/medicine/create")) {
            requiredPriv = "MEDICINE_CREATE";
        } else if (path.contains("/medicine/edit")) {
            requiredPriv = "MEDICINE_EDIT";
        } else if (path.contains("/medicine/list") || path.contains("/medicine/detail")) { // Bao gồm cả /list và /detail
            requiredPriv = "MEDICINE_VIEW";
        }
        
        if (path.contains("/room/create")) {
            requiredPriv = "ROOM_CREATE";
        } else if (path.contains("/room/edit")) {
            requiredPriv = "ROOM_EDIT";
        } else if (path.contains("/room/list") || path.contains("/room/detail")) {
            requiredPriv = "ROOM_VIEW";
        }

        if (path.contains("/patient/create")) {
            requiredPriv = "PATIENT_CREATE";
        } else if (path.contains("/patient/edit")) {
            requiredPriv = "PATIENT_EDIT";
        } else if (path.contains("/patient/list") || path.contains("/patient/detail")) {
            requiredPriv = "PATIENT_VIEW";
        } else if (path.contains("/patient/delete")) {
            requiredPriv = "PATIENT_DELETE";
        }

        if (path.contains("/appointment/create")) {
            requiredPriv = "APPOINTMENT_CREATE";
        } else if (path.contains("/appointment/edit")) {
            requiredPriv = "APPOINTMENT_EDIT";
        } else if (path.contains("/appointment/list") || path.contains("/appointment/detail")) {
            requiredPriv = "APPOINTMENT_VIEW";
        }

        if (requiredPriv != null) {
            System.out.println("🔍 Hệ thống nhận diện Module cần quyền: " + requiredPriv);

            if (!privileges.contains(requiredPriv)) {
                System.out.println("❌ THIẾU QUYỀN! User " + user.getUsername() + " không có: " + requiredPriv);
                res.sendError(403);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("AuthorizationFilter:Initializing filter");
            }
        }
    }

    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthorizationFilter()");
        }
        StringBuffer sb = new StringBuffer("AuthorizationFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}
