package controller.admin.specialty;

import dao.RoomDAO;
import dao.SpecialtyDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Specialty;
import java.io.IOException;

@WebServlet("/admin/specialty/detail")
public class SpecialtyDetailServlet extends HttpServlet {

    // ================= VIEW DETAIL =================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        if (idRaw == null) {
            response.sendRedirect(request.getContextPath() + "/admin/specialty/list");
            return;
        }

        int id = Integer.parseInt(idRaw);

        SpecialtyDAO dao = new SpecialtyDAO();
        Specialty specialty = dao.getById(id);

        if (specialty == null) {
            response.sendRedirect(request.getContextPath() + "/admin/specialty/list");
            return;
        }

        request.setAttribute("specialty", specialty);
        request.setAttribute("pageTitle", "Manage speiclaty");
        request.setAttribute("activePage", "manageSpecialty");
        request.setAttribute("contentPage", "/WEB-INF/admin/specialty/detailSpecialty.jsp");

        request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

    }

    // ================= EDIT (POPUP - AJAX) =================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String activeParam = request.getParameter("isActive");
        boolean isActive = Boolean.parseBoolean(activeParam);
        SpecialtyDAO dao = new SpecialtyDAO();

        // ===== VALIDATE EMPTY / SPACE =====
        if (name == null || name.trim().isEmpty()
                || description == null || description.trim().isEmpty()) {

            request.setAttribute("error", "Name và Description không được chỉ chứa khoảng trắng.");
            request.setAttribute("openModal", true); // ⚠️ QUAN TRỌNG
            request.setAttribute("specialty", dao.getById(id));
            request.setAttribute("pageTitle", "Manage speiclaty");
            request.setAttribute("activePage", "manageSpecialty");
            request.setAttribute("contentPage", "/WEB-INF/admin/specialty/detailSpecialty.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

            return;
        }

        // ===== DUPLICATE CHECK (TRỪ CHÍNH NÓ) =====
        if (dao.existsByNameExceptId(name.trim(), id)) {
            request.setAttribute("error", "Specialty name đã tồn tại.");
            request.setAttribute("openModal", true);
            request.setAttribute("specialty", dao.getById(id));
            request.setAttribute("pageTitle", "Manage speiclaty");
            request.setAttribute("activePage", "manageSpecialty");
            request.setAttribute("contentPage", "/WEB-INF/admin/specialty/detailSpecialty.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

            return;
        }

        // ===== UPDATE =====
        Specialty s = new Specialty();
        s.setSpecialtyId(id);
        s.setName(name.trim());
        s.setDescription(description.trim());
        s.setIsActive(isActive);

        dao.update(s);
        //nếu specialty bị inactive -> inactive room
        if (!isActive) {
            RoomDAO roomDAO = new RoomDAO();
            roomDAO.inactiveRoomsBySpecialty(id);
        }

        // ✅ THÀNH CÔNG → redirect → popup tự đóng
        response.sendRedirect(request.getContextPath() + "/admin/specialty/detail?id=" + id);
    }

}
