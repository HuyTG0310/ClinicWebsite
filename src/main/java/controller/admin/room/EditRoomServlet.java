package controller.admin.room;

import dao.RoomDAO;
import model.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "EditRoom", urlPatterns = {"/EditRoom"})
public class EditRoomServlet extends HttpServlet {
    
    /**
     * doGet - Hiển thị trang Edit riêng (giống Patient)
     */
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
            request.getRequestDispatcher("WEB-INF/admin/room/editRoom.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid room ID: " + e.getMessage());
            response.sendRedirect("RoomList");
        } catch (Exception e) {
            System.out.println("Error loading edit form: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("RoomList");
        }
    }

    /**
     * doPost - Xử lý submit từ CẢ 2 NƠI:
     * 1. Popup Edit trong ViewRoom
     * 2. Trang Edit riêng
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        
        try {
            String roomIdStr = request.getParameter("roomId");
            String roomName = request.getParameter("roomName");
            String specialtyIdStr = request.getParameter("specialtyId");
            String currentDoctorIdStr = request.getParameter("currentDoctorId");
            String isActiveStr = request.getParameter("isActive");

            // Validation - Room ID
            if (roomIdStr == null || roomIdStr.trim().isEmpty()) {
                session.setAttribute("error", "Invalid room ID");
                response.sendRedirect("RoomList");
                return;
            }

            int roomId = Integer.parseInt(roomIdStr);

            // Validation - Room name
            if (roomName == null || roomName.trim().isEmpty()) {
                request.setAttribute("error", "Please fill in all required fields!");
                request.setAttribute("patient", new RoomDAO().getRoomById(roomId));
                request.getRequestDispatcher("WEB-INF/admin/room/viewRoom.jsp").forward(request, response);
                return;
            }

            // Validation - SpecialtyId (BẮT BUỘC)
            if (specialtyIdStr == null || specialtyIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Specialty ID is required");
                request.setAttribute("patient", new RoomDAO().getRoomById(roomId));
                request.getRequestDispatcher("WEB-INF/admin/room/viewRoom.jsp").forward(request, response);
                return;
            }

            RoomDAO roomDAO = new RoomDAO();
            
            // Get existing room
            Room room = roomDAO.getRoomById(roomId);
            if (room == null) {
                session.setAttribute("error", "No room found!");
                response.sendRedirect("RoomList");
                return;
            }
            
            // Check duplicate room name (exclude current room)
            if (roomDAO.isRoomNameExists(roomName.trim(), roomId)) {
                request.setAttribute("error", "Room name already exists");
                request.setAttribute("room", room);
                request.getRequestDispatcher("WEB-INF/admin/room/viewRoom.jsp").forward(request, response);
                return;
            }

            // Update room fields
            room.setRoomName(roomName.trim());
            
            // Parse SpecialtyId (BẮT BUỘC)
            try {
                Integer specialtyId = Integer.parseInt(specialtyIdStr.trim());
                room.setSpecialtyId(specialtyId);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid specialty ID format");
                request.setAttribute("room", room);
                request.getRequestDispatcher("WEB-INF/admin/room/viewRoom.jsp").forward(request, response);
                return;
            }
            
            // Parse CurrentDoctorId (TÙY CHỌN)
            if (currentDoctorIdStr != null && !currentDoctorIdStr.trim().isEmpty()) {
                try {
                    Integer currentDoctorId = Integer.parseInt(currentDoctorIdStr.trim());
                    room.setCurrentDoctorId(currentDoctorId);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid doctor ID format");
                    request.setAttribute("room", room);
                    request.getRequestDispatcher("WEB-INF/admin/room/viewRoom.jsp").forward(request, response);
                    return;
                }
            } else {
                room.setCurrentDoctorId(null);
            }
            
            // Set active status
            room.setIsActive((isActiveStr != null && isActiveStr.equals("true")));

            // Update database
            boolean isUpdated = roomDAO.updateRoom(room);
            
            if (isUpdated) {
                response.sendRedirect("ViewRoom?id=" + roomId);
            } else {
                request.setAttribute("error", "Room update failed. Please try again!");
                request.setAttribute("room", room);
                request.getRequestDispatcher("WEB-INF/admin/room/viewRoom.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.out.println("Room update error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while updating the room!");
            
            try {
                doGet(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Edit Room Servlet";
    }
}

