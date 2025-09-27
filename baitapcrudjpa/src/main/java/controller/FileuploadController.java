package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/image")
public class FileuploadController extends HttpServlet {

    // folder chứa file upload ngoài Tomcat
    private final String UPLOAD_DIR = "D:/Code/web/ProjectWeb/uploads";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        if (name == null || name.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "File name is missing");
            return;
        }

        File file = new File(UPLOAD_DIR + File.separator + name);
        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
            return;
        }

        // thiết lập content type đúng loại file
        String mime = getServletContext().getMimeType(name);
        if (mime == null) mime = "application/octet-stream";
        resp.setContentType(mime);

        // gửi file ra output
        Files.copy(file.toPath(), resp.getOutputStream());
    }
}
