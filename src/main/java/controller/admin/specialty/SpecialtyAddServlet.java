
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
        request.setAttribute("pageTitle", "Specialty Detail");
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
                "Name and Description not null or only contain space.");

        request.setAttribute("pageTitle", "Specialty Detail");
        request.setAttribute("activePage", "manageSpecialty");
        request.setAttribute("contentPage", "/WEB-INF/admin/specialty/addSpecialty.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

        return;
    }

    // ===== DUPLICATE CHECK =====
    if (dao.existsByName(name.trim())) {
        request.setAttribute("error", "Specialty name is existed.");
        request.setAttribute("pageTitle", "Specialty Detail");
        request.setAttribute("activePage", "manageSpecialty");
        request.setAttribute("contentPage", "/WEB-INF/admin/specialty/addSpecialty.jsp");
        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
        return;
    }

    Specialty s = new Specialty();
    s.setName(name.trim());
    s.setDescription(description.trim());
    dao.insert(s);

    response.sendRedirect(request.getContextPath() + "/admin/specialty/list");
}

}