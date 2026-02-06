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

/**
 * RoomListServlet - Controller for Room List Management
 *
 * @author ClinicWebsite
 */
@WebServlet(name = "RoomList", urlPatterns = {"/RoomList"})
public class RoomListServlet extends HttpServlet {

    /**
     * Handles the HTTP GET method - Display room list
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String searchKeyword = request.getParameter("searchKeyword");
            List<Room> roomList;
            RoomDAO roomDAO = new RoomDAO();

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                // Search rooms by name
                roomList = roomDAO.searchRoomByName(searchKeyword.trim());
                request.setAttribute("searchKeyword", searchKeyword.trim());
            } else {
                // Get all rooms
                roomList = roomDAO.getAllRooms();
            }

            request.setAttribute("roomList", roomList);

//            request.getRequestDispatcher("WEB-INF/admin/room/roomList.jsp").forward(request, response);

            request.setAttribute("pageTitle", "Manage Room");
            request.setAttribute("activePage", "manageRoom");
            request.setAttribute("contentPage", "/WEB-INF/admin/room/roomList.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("Error loading room list: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error loading room list: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/admin/room/roomList.jsp").forward(request, response);
        }
    }
}
