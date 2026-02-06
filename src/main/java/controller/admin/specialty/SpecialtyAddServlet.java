
import dao.SpecialtyDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Specialty;

@WebServlet("/admin/specialty/add")
public class SpecialtyAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        request.getRequestDispatcher("/WEB-INF/admin/specialty/addSpecialty.jsp")
//                .forward(request, response);
        request.setAttribute("pageTitle", "Manage speiclaty");
        request.setAttribute("activePage", "manageSpecialty");
        request.setAttribute("contentPage", "/WEB-INF/admin/specialty/addSpecialty.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");

    String name = request.getParameter("name");
    String description = request.getParameter("description");
    

    SpecialtyDAO dao = new SpecialtyDAO();

    // ===== VALIDATE EMPTY / ONLY SPACE =====
    if (name == null || name.trim().isEmpty()
            || description == null || description.trim().isEmpty()) {

        request.setAttribute("error",
                "Name và Description không được để trống hoặc chỉ chứa khoảng trắng.");

//        request.getRequestDispatcher("/WEB-INF/admin/specialty/addSpecialty.jsp")
//                .forward(request, response);
        request.setAttribute("pageTitle", "Manage speiclaty");
        request.setAttribute("activePage", "manageSpecialty");
        request.setAttribute("contentPage", "/WEB-INF/admin/specialty/addSpecialty.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

        return;
    }

    // ===== DUPLICATE CHECK =====
    if (dao.existsByName(name.trim())) {
        request.setAttribute("error", "Specialty name đã tồn tại.");

//        request.getRequestDispatcher("/WEB-INF/admin/specialty/addSpecialty.jsp")
//                .forward(request, response);
        request.setAttribute("pageTitle", "Manage speiclaty");
        request.setAttribute("activePage", "manageSpecialty");
        request.setAttribute("contentPage", "/WEB-INF/admin/specialty/addSpecialty.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);


        return;
    }

    // ===== INSERT =====
    Specialty s = new Specialty();
    s.setName(name.trim());
    s.setDescription(description.trim());
    dao.insert(s);

    // ✅ THÀNH CÔNG → redirect
    response.sendRedirect(request.getContextPath() + "/admin/specialty/list");
}

}