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
import java.util.*;

@WebServlet(name = "AddRoomServlet", urlPatterns = {"/admin/room/create", "/receptionist/room/create", "/doctor/room/create"})
public class AddRoomServlet extends HttpServlet {

    private final SpecialtyDAO specialtyDAO = new SpecialtyDAO();
    private final UserDAO userDAO = new UserDAO();
    private final RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        prepareAddRoomPage(request, null);

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        String layout;

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

        request.getRequestDispatcher(layout).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        String layout;

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

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String roomName = request.getParameter("roomName");
        String specialtyIdStr = request.getParameter("specialtyId");
        String doctorIdStr = request.getParameter("currentDoctorId");
        String isActiveStr = request.getParameter("isActive");

        // ====== giữ lại giá trị form ======
        request.setAttribute("roomName", roomName);
        request.setAttribute("specialtyId", specialtyIdStr);
        request.setAttribute("currentDoctorId", doctorIdStr);
        request.setAttribute("isActive", isActiveStr);

        Integer specialtyId = null;
        Integer doctorId = null;

        try {
            // ====== VALIDATE ======

            if (roomName == null || roomName.trim().isEmpty()) {
                throw new IllegalArgumentException("Room name is required");
            }

            if (roomDAO.isRoomNameExists(roomName.trim(), null)) {
                throw new IllegalArgumentException("Room name already exists");
            }

            if (specialtyIdStr == null || specialtyIdStr.isBlank()) {
                throw new IllegalArgumentException("Specialty is required");
            }

            specialtyId = Integer.parseInt(specialtyIdStr);

            if (doctorIdStr != null && !doctorIdStr.isBlank()) {
                doctorId = Integer.parseInt(doctorIdStr);

                // ✅ CHECK doctor thuộc specialty
                if (!userDAO.isDoctorInSpecialty(doctorId, specialtyId)) {
                    throw new IllegalArgumentException(
                            "Selected doctor does not belong to the chosen specialty"
                    );
                }
            }

            // ====== CREATE ROOM ======
            Room room = new Room();
            room.setRoomName(roomName.trim());
            room.setSpecialtyId(specialtyId);
            room.setCurrentDoctorId(doctorId);
            room.setIsActive("true".equals(isActiveStr));

            if (roomDAO.addRoom(room)) {
                session.setAttribute("success", "Room added successfully!");
                response.sendRedirect(basePath + "/room/list");
                return;
            } else {
                throw new RuntimeException("Failed to add room");
            }

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            prepareAddRoomPage(request, specialtyId);
            request.getRequestDispatcher(layout)
                    .forward(request, response);
        }
    }

    // ====== PREPARE PAGE ======
    private void prepareAddRoomPage(HttpServletRequest request, Integer specialtyId) {
        request.setAttribute("specialties", specialtyDAO.getAllActiveRoom());

        if (specialtyId != null) {
            request.setAttribute(
                    "doctors",
                    userDAO.getDoctorsBySpecialty(specialtyId)
            );
        } else {
            request.setAttribute("doctors", new ArrayList<>());
        }

        request.setAttribute("pageTitle", "Manage Room");
        request.setAttribute("activePage", "manageRoom");
        request.setAttribute("contentPage", "/WEB-INF/admin/room/addRoom.jsp");
    }
}
