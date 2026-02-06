package controller.admin.room;

import dao.RoomDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "DeleteRoom", urlPatterns = {"/DeleteRoom"})
public class DeleteRoomServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            String roomIdStr = request.getParameter("id");
            
            if (roomIdStr == null || roomIdStr.trim().isEmpty()) {
                session.setAttribute("error", "Invalid room ID");
                response.sendRedirect("RoomList");
                return;
            }

            int roomId = Integer.parseInt(roomIdStr);
            RoomDAO roomDAO = new RoomDAO();

            if (roomDAO.deleteRoom(roomId)) {
                session.setAttribute("success", "Room deleted successfully!");
            } else {
                session.setAttribute("error", "Failed to delete room. It may be in use.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid room ID: " + e.getMessage());
            session.setAttribute("error", "Invalid room ID");
        } catch (Exception e) {
            System.out.println("Error in DeleteRoomServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Error deleting room: " + e.getMessage());
        }
        
        response.sendRedirect("RoomList");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}