package controller.admin.room;

import dao.RoomDAO;
import model.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ViewRoom", urlPatterns = {"/ViewRoom"})
public class ViewRoomServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String roomIdStr = request.getParameter("id");
            
            if (roomIdStr == null || roomIdStr.trim().isEmpty()) {
                response.sendRedirect("RoomList");
                return;
            }

            int roomId = Integer.parseInt(roomIdStr);
            RoomDAO roomDAO = new RoomDAO();
            Room room = roomDAO.getRoomById(roomId);

            if (room == null) {
                request.setAttribute("error", "Room not found!");
                response.sendRedirect("RoomList");
                return;
            }

            request.setAttribute("room", room);
//            request.getRequestDispatcher("WEB-INF/admin/room/editRoom.jsp").forward(request, response);
            
              request.setAttribute("pageTitle", "Manage Room");
            request.setAttribute("activePage", "manageRoom");
            request.setAttribute("contentPage", "/WEB-INF/admin/room/viewRoom.jsp");

            request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid room ID: " + e.getMessage());
            response.sendRedirect("RoomList");
        } catch (Exception e) {
            System.out.println("Error viewing room: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while retrieving room details!");
            response.sendRedirect("RoomList");
        }
    }
    
    @Override
    public String getServletInfo() {
        return "View Room Details Servlet";
    }
}