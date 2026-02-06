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

@WebServlet(name = "AddRoom", urlPatterns = {"/AddRoom"})
public class AddRoomServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        request.getRequestDispatcher("WEB-INF/admin/room/addRoom.jsp").forward(request, response);
        
        request.setAttribute("pageTitle", "Manage Room");
                request.setAttribute("activePage", "manageRoom");
                request.setAttribute("contentPage", "/WEB-INF/admin/room/addRoom.jsp");

                request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        try {
            String roomName = request.getParameter("roomName");
            String specialtyIdStr = request.getParameter("specialtyId");
            String currentDoctorIdStr = request.getParameter("currentDoctorId");
            String isActiveStr = request.getParameter("isActive");

            if (roomName == null || roomName.trim().isEmpty()) {
                request.setAttribute("error", "Room name is required");
//                request.getRequestDispatcher("WEB-INF/admin/room/addRoom.jsp").forward(request, response);
                
                request.setAttribute("pageTitle", "Manage Room");
                request.setAttribute("activePage", "manageRoom");
                request.setAttribute("contentPage", "/WEB-INF/admin/room/addRoom.jsp");

                request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
                
                return;
            }

            RoomDAO roomDAO = new RoomDAO();
            if (roomDAO.isRoomNameExists(roomName.trim(), null)) {
                request.setAttribute("error", "Room name already exists");
                request.setAttribute("roomName", roomName);
                request.setAttribute("specialtyId", specialtyIdStr);
                request.setAttribute("currentDoctorId", currentDoctorIdStr);
                request.setAttribute("isActive", isActiveStr);
//                request.getRequestDispatcher("WEB-INF/admin/room/addRoom.jsp").forward(request, response);
                
                request.setAttribute("pageTitle", "Manage Room");
                request.setAttribute("activePage", "manageRoom");
                request.setAttribute("contentPage", "/WEB-INF/admin/room/addRoom.jsp");

                request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
                
                return;
            }

            Room room = new Room();
            room.setRoomName(roomName.trim());

            if (specialtyIdStr != null && !specialtyIdStr.trim().isEmpty()) {
                try {
                    room.setSpecialtyId(Integer.parseInt(specialtyIdStr.trim()));
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid specialty ID format");
                    request.setAttribute("roomName", roomName);
//                    request.getRequestDispatcher("WEB-INF/admin/room/addRoom.jsp").forward(request, response);
                    
                    request.setAttribute("pageTitle", "Manage Room");
                request.setAttribute("activePage", "manageRoom");
                request.setAttribute("contentPage", "/WEB-INF/admin/room/addRoom.jsp");

                request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
                    
                    return;
                }
            } else {
                room.setSpecialtyId(null);
            }

            if (currentDoctorIdStr != null && !currentDoctorIdStr.trim().isEmpty()) {
                try {
                    room.setCurrentDoctorId(Integer.parseInt(currentDoctorIdStr.trim()));
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid doctor ID format");
                    request.setAttribute("roomName", roomName);
//                    request.getRequestDispatcher("WEB-INF/admin/room/addRoom.jsp").forward(request, response);
                    
                    request.setAttribute("pageTitle", "Manage Room");
                request.setAttribute("activePage", "manageRoom");
                request.setAttribute("contentPage", "/WEB-INF/admin/room/addRoom.jsp");

//                request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
                
                request.setAttribute("pageTitle", "Manage Room");
                request.setAttribute("activePage", "manageRoom");
                request.setAttribute("contentPage", "/WEB-INF/admin/room/addRoom.jsp");

                request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
                
                    return;
                }
            } else {
                room.setCurrentDoctorId(null);
            }

            room.setIsActive((isActiveStr != null && isActiveStr.equals("true")));

            if (roomDAO.addRoom(room)) {
                session.setAttribute("success", "Room added successfully!");
                response.sendRedirect("RoomList");
            } else {
                request.setAttribute("error", "Failed to add room. The Specialty ID or Doctor ID may not exist.");
                request.setAttribute("roomName", roomName);
                request.setAttribute("specialtyId", specialtyIdStr);
                request.setAttribute("currentDoctorId", currentDoctorIdStr);
                request.setAttribute("isActive", isActiveStr);
//                request.getRequestDispatcher("WEB-INF/admin/room/addRoom.jsp").forward(request, response);

                request.setAttribute("pageTitle", "Manage Room");
                request.setAttribute("activePage", "manageRoom");
                request.setAttribute("contentPage", "/WEB-INF/admin/room/addRoom.jsp");

                request.getRequestDispatcher("/WEB-INF/layout/adminLayout.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.out.println("Error in AddRoomServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Error adding room: " + e.getMessage());
            response.sendRedirect("RoomList");
        }
    }
}
