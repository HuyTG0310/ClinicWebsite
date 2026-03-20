package controller.certification;

import dao.CertificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import java.io.IOException;

@WebServlet({
    "/doctor/certification/my",
    "/receptionist/certification/my",
    "/lab/certification/my"
})
public class MyCertificationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

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
            request.getRequestDispatcher(jspPath).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách chứng chỉ");

            // Xử lý quay về dashboard tương ứng khi lỗi
            String role = user.getRoleName().trim().toLowerCase();
            String prefix = role.contains("lab") ? "lab" : role.replace(" ", "");
            response.sendRedirect(request.getContextPath() + "/" + prefix + "/dashboard");
        }
    }
}
