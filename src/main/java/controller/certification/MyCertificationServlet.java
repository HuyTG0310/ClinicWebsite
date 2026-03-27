package controller.certification;

import dao.CertificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import java.io.IOException;

/**
 *
 * @author Tai Loi
 */
@WebServlet({
    "/doctor/certification/my",
    "/receptionist/certification/my",
    "/lab/certification/my"
})
public class MyCertificationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String layout;
        String basePath;

        if (uri.startsWith(ctx + "/admin")) {
            layout = "/WEB-INF/layout/adminLayout.jsp";
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            layout = "/WEB-INF/layout/doctorLayout.jsp";
            basePath = ctx + "/doctor";
        } else if (uri.startsWith(ctx + "/receptionist")) {
            layout = "/WEB-INF/layout/receptionistLayout.jsp";
            basePath = ctx + "/receptionist";
        } else if (uri.startsWith(ctx + "/lab")) {
            layout = "/WEB-INF/layout/labLayout.jsp";
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath); // Gửi sang JSP để dùng

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // 1. Kiểm tra đăng nhập
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // 2. Lấy dữ liệu chứng chỉ
            CertificationDAO dao = new CertificationDAO();
            request.setAttribute("list", dao.getByUser(user.getUserId()));

            // 3. Xử lý logic đường dẫn JSP động (Fix lỗi folder lab)
            String role = user.getRoleName().trim().toLowerCase();
            String roleFolder = role;

            // Kiểm tra nếu là Lab technician thì dùng folder "lab" như trong ảnh của bạn
            if (role.contains("lab")) {
                roleFolder = "lab";
            } else {
                // Nếu là role khác thì cứ bỏ dấu cách (vd: Receptionist -> receptionist)
                roleFolder = role.replace(" ", "");
            }

            String jspPath = "/WEB-INF/" + roleFolder + "/certification/myCertification.jsp";

            // 4. Forward tới giao diện của Role tương ứng
            request.setAttribute("activePage", "myCertification");

            request.setAttribute("pageTitle", "My Certification");

            request.setAttribute("contentPage", jspPath);

            request.getRequestDispatcher(layout).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Can not load certification list");

            // Xử lý quay về dashboard tương ứng khi lỗi
            String role = user.getRoleName().trim().toLowerCase();
            String prefix = role.contains("lab") ? "lab" : role.replace(" ", "");
            response.sendRedirect(request.getContextPath() + "/" + prefix + "/dashboard");
        }
    }
}
