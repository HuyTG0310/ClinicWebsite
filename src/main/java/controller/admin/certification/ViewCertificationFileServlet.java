/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin.certification;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;

/**
 *
 * @author Tai Loi
 */
@WebServlet("/certification/file")
public class ViewCertificationFileServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "D:/ClinicData/uploads/certifications";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fileName = request.getParameter("name");

        if (fileName == null || fileName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        File file = new File(UPLOAD_DIR, fileName);

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // FIX MIME TYPE NULL
        String mime = getServletContext().getMimeType(file.getName());
        if (mime == null) {
            mime = "application/octet-stream";
        }

        response.setContentType(mime);
        response.setContentLengthLong(file.length());

        Files.copy(file.toPath(), response.getOutputStream());
    }
}
