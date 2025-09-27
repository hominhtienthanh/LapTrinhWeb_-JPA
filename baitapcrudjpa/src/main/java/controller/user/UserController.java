package controller.user;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import entity.Category;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import service.CategoryService;
import service.Impl.CategoryServiceImpl;

@WebServlet("/user/*")
@MultipartConfig(
    fileSizeThreshold = 1024*1024,
    maxFileSize = 5*1024*1024,
    maxRequestSize = 10*1024*1024
)
public class UserController extends HttpServlet {

    private CategoryService cateService = new CategoryServiceImpl();
    private final String UPLOAD_DIR = "D:/Code/web/ProjectWeb/uploads"; // đường dẫn ngoài webapp

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if(session == null || session.getAttribute("account") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("account");
        String action = req.getPathInfo();

        if (action == null || action.equals("/home")) {
            // User chỉ thấy category của chính họ
            List<Category> list = cateService.findByUser(currentUser.getId());
            req.setAttribute("listcate", list);
            req.getRequestDispatcher("/views/user/home.jsp").forward(req, resp);
        } else if (action.equals("/add")) {
            req.getRequestDispatcher("/views/user/user-add.jsp").forward(req, resp);
        } else if (action.equals("/update")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Category cate = cateService.findById(id);
            // Kiểm tra quyền: user chỉ update category của chính họ
            if(cate.getUser().getId() != currentUser.getId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            req.setAttribute("category", cate);
            req.getRequestDispatcher("/views/user/user-update.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getPathInfo();
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("account");

        if(action.equals("/add") || action.equals("/update")) {
            String name = req.getParameter("categoryname");
            Part filePart = req.getPart("icon");
            String fileName = null;

            if(filePart != null && filePart.getSize() > 0) {
                String ext = Paths.get(filePart.getSubmittedFileName())
                                  .getFileName().toString();
                fileName = UUID.randomUUID() + "_" + ext;
                File uploadDir = new File(UPLOAD_DIR);
                if(!uploadDir.exists()) uploadDir.mkdirs();
                filePart.write(UPLOAD_DIR + File.separator + fileName);
            }

            if(action.equals("/add")) {
                Category cate = new Category();
                cate.setUser(currentUser); // gán user tạo category
                cate.setCategoryname(name);
                if(fileName != null) cate.setIcon(fileName);
                cateService.create(cate);
            } else { // update
                int id = Integer.parseInt(req.getParameter("id"));
                Category cate = cateService.findById(id);
                // Chỉ update category của chính họ
                if(cate.getUser().getId() != currentUser.getId()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                cate.setCategoryname(name);
                if(fileName != null) cate.setIcon(fileName);
                cateService.update(cate);
            }

            resp.sendRedirect(req.getContextPath() + "/user/home");
        } else if(action.equals("/delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Category cate = cateService.findById(id);
            // Chỉ xóa category của chính họ
            if(cate.getUser().getId() != currentUser.getId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            cateService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/user/home");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
