package controller.admin.room;

import dao.RoomDAO;
import dao.SpecialtyDAO;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 *
 * @author Chi Duong
 */
@WebServlet(name = "ViewRoomServlet", urlPatterns = {"/admin/room/detail", "/receptionist/room/detail", "/doctor/room/detail", "/lab/room/detail"})
public class ViewRoomServlet extends HttpServlet {

    private SpecialtyDAO specialtyDAO = new SpecialtyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //NEW
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

        request.setAttribute("basePath", basePath);

        //NEW
        try {
            String roomIdStr = request.getParameter("id");

            if (roomIdStr == null || roomIdStr.trim().isEmpty()) {
                response.sendRedirect(basePath + "/room/list");
                return;
            }

            int roomId = Integer.parseInt(roomIdStr);
            RoomDAO roomDAO = new RoomDAO();
            RoomView room = roomDAO.getRoomDetail(roomId);

            if (room == null) {
                request.setAttribute("error", "Room not found!");
                response.sendRedirect(basePath + "/room/list");
                return;
            }

            request.setAttribute("room", room);
            request.setAttribute("specialties", specialtyDAO.getAll());

            request.setAttribute("pageTitle", "Room Detail");
            request.setAttribute("activePage", "manageRoom");
            request.setAttribute("contentPage", "/WEB-INF/admin/room/viewRoom.jsp");

            request.getRequestDispatcher(layout).forward(request, response);

        } catch (NumberFormatException e) {
            System.out.println("Invalid room ID: " + e.getMessage());
            response.sendRedirect(basePath + "/room/list");
        } catch (Exception e) {
            System.out.println("Error viewing room: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while retrieving room details!");
            response.sendRedirect(basePath + "/room/list");
        }
    }

    @Override
    public String getServletInfo() {
        return "View Room Details Servlet";
    }
}
