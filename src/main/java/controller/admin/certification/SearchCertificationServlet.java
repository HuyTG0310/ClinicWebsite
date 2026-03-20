package controller.admin.certification;

import dao.CertificationDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Certification;

@WebServlet("/admin/certification/search")
public class SearchCertificationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("searchName");
        String phone = request.getParameter("searchPhone");

        // 🔥 Trim dữ liệu
        if (name != null) {
            name = name.trim();
        }

        if (phone != null) {
            phone = phone.trim();

            // 🔥 Validate số điện thoại chỉ chứa số
            if (!phone.isEmpty() && !phone.matches("\\d+")) {
                request.setAttribute("error", "Số điện thoại chỉ được chứa chữ số!");
                request.getRequestDispatcher("/WEB-INF/admin/certification/certificationApproval.jsp")
                        .forward(request, response);
                return;
            }
        }

        CertificationDAO dao = new CertificationDAO();
        List<Certification> list;

        if ((name != null && !name.isEmpty())
                || (phone != null && !phone.isEmpty())) {

            list = dao.searchCertifications(name, phone);
            request.setAttribute("isSearch", true);

        } else {
            list = dao.getAll();
        }

        request.setAttribute("list", list);

        request.getRequestDispatcher("/WEB-INF/admin/certification/certificationApproval.jsp")
                .forward(request, response);
    }
}
