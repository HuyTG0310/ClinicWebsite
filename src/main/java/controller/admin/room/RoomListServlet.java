package controller.admin.room;

import java.io.IOException;
import java.util.List;

import dao.RoomDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Room;
import model.RoomView;

@WebServlet(name = "RoomListServlet", urlPatterns = {"/admin/room/list", "/receptionist/room/list", "/doctor/room/list"})
public class RoomListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String layout = "";
        try {
            String searchKeyword = request.getParameter("searchKeyword");
            List<RoomView> roomList;
            RoomDAO roomDAO = new RoomDAO();

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                // Search rooms by name
                roomList = roomDAO.searchRoomViewByName(searchKeyword);
                request.setAttribute("searchKeyword", searchKeyword.trim());
            } else {
                // Get all rooms
                roomList = roomDAO.getAllView();
            }

            String uri = request.getRequestURI();
            String ctx = request.getContextPath();

            if (uri.startsWith(ctx + "/admin")) {
                layout = "/WEB-INF/layout/adminLayout.jsp";
            } else if (uri.startsWith(ctx + "/receptionist")) {
                layout = "/WEB-INF/layout/receptionistLayout.jsp";
            } else if (uri.startsWith(ctx + "/doctor")) {
                layout = "/WEB-INF/layout/doctorLayout.jsp";
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            String basePath;
            if (uri.startsWith(ctx + "/admin")) {
                basePath = ctx + "/admin";
            } else if (uri.startsWith(ctx + "/receptionist")) {
                basePath = ctx + "/receptionist";
            } else if (uri.startsWith(ctx + "/doctor")) {
                basePath = ctx + "/doctor";
            } else {
                basePath = ctx;
            }

            request.setAttribute("basePath", basePath);
            request.setAttribute("roomList", roomList);
            request.setAttribute("pageTitle", "Room List");
            request.setAttribute("activePage", "manageRoom");
            request.setAttribute("contentPage", "/WEB-INF/admin/room/roomList.jsp");

            request.getRequestDispatcher(layout).forward(request, response);

        } catch (Exception e) {
            System.out.println("Error loading room list: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error loading room list: " + e.getMessage());
            request.getRequestDispatcher(layout).forward(request, response);
        }
    }
}
