package controller.admin.room;

import dao.*;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "EditRoomServlet", urlPatterns = {"/admin/room/edit", "/receptionist/room/edit", "/doctor/room/edit", "/lab/room/edit"})
public class EditRoomServlet extends HttpServlet {

    /**
     * doGet - Hiển thị trang Edit riêng (giống Patient)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * doPost - Xử lý submit từ CẢ 2 NƠI: 1. Popup Edit trong ViewRoom 2. Trang
     * Edit riêng
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        String basePath;
        if (uri.startsWith(ctx + "/admin")) {
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            basePath = ctx + "/doctor";
        } else if (uri.startsWith(ctx + "/receptionist")) {
            basePath = ctx + "/receptionist";
        } else if (uri.startsWith(ctx + "/lab")) {
            basePath = ctx + "/lab";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);

        //NEW
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        RoomDAO roomDAO = new RoomDAO();
        UserDAO userDAO = new UserDAO();

        int roomId;
        try {
            roomId = Integer.parseInt(request.getParameter("roomId"));
        } catch (Exception e) {
            response.sendRedirect(basePath + "/room/list");
            return;
        }

        String roomName = request.getParameter("roomName");
        String specialtyIdStr = request.getParameter("specialtyId");
        String doctorIdStr = request.getParameter("currentDoctorId");
        boolean isActive = "true".equals(request.getParameter("isActive"));

        if (roomName == null || roomName.isBlank()) {
            response.sendRedirect(basePath + "/room/detail?id=" + roomId + "&editError=Room name is required");
            return;
        }

        if (specialtyIdStr == null || specialtyIdStr.isBlank()) {
            response.sendRedirect(basePath + "/room/detail?id=" + roomId + "&editError=Specialty is required");
            return;
        }

        int specialtyId = Integer.parseInt(specialtyIdStr);

        Integer doctorId = null;
        if (doctorIdStr != null && !doctorIdStr.isBlank()) {
            doctorId = Integer.parseInt(doctorIdStr);

            if (!userDAO.isDoctorInSpecialty(doctorId, specialtyId)) {
                response.sendRedirect(basePath + "/room/detail?id=" + roomId + "&editError=Doctor not in specialty");
                return;
            }
        }

        if (roomDAO.isRoomNameExists(roomName.trim(), roomId)) {
            response.sendRedirect(basePath + "/room/detail?id=" + roomId + "&editError=Room name already exists");
            return;
        }

        Room room = new Room();
        room.setRoomId(roomId);
        room.setRoomName(roomName.trim());
        room.setSpecialtyId(specialtyId);
        room.setCurrentDoctorId(doctorId);
        room.setIsActive(isActive);

        roomDAO.updateRoom(room);

        response.sendRedirect(basePath + "/room/detail?id=" + roomId);
    }

    @Override
    public String getServletInfo() {
        return "Edit Room Servlet";
    }
}
