package controller.admin;

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
import service.UserService;
import service.Impl.CategoryServiceImpl;
import service.Impl.UserServiceImpl;

@WebServlet("/admin/*")
@MultipartConfig(
    fileSizeThreshold = 1024*1024,
    maxFileSize = 5*1024*1024,
    maxRequestSize = 10*1024*1024
)
public class AdminController extends HttpServlet {

    private CategoryService cateService = new CategoryServiceImpl();
    private UserService userService = new UserServiceImpl();
    private final String UPLOAD_DIR = "D:/Code/web/ProjectWeb/uploads"; // đường dẫn ngoài webapp

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getPathInfo();
        if (action == null || action.equals("/home")) {
            // Lấy danh sách user để hiển thị dropdown
            List<User> listUser = userService.findAll();
            req.setAttribute("listUser", listUser);

            // Lọc category theo userId nếu có
            String userIdStr = req.getParameter("userid");
            List<Category> listcate;
            if (userIdStr != null && !userIdStr.isEmpty()) {
                try {
                    int userId = Integer.parseInt(userIdStr);
                    listcate = cateService.findByUser(userId);
                } catch (NumberFormatException e) {
                    listcate = cateService.findAll(); // fallback
                }
            } else {
                listcate = cateService.findAll();
            }

            req.setAttribute("listcate", listcate);

            req.getRequestDispatcher("/views/admin/home.jsp").forward(req, resp);
        } else if (action.equals("/add")) {
            req.getRequestDispatcher("/views/admin/admin-add.jsp").forward(req, resp);
        } else if (action.equals("/update")) {
            String id = req.getParameter("id");
            Category cate = cateService.findById(Integer.parseInt(id));
            req.setAttribute("category", cate);
            req.getRequestDispatcher("/views/admin/admin-update.jsp").forward(req, resp);
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
                // Chỉ cho phép sửa category do chính họ tạo
                if(cate.getUser().getId() != currentUser.getId()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                cate.setCategoryname(name);
                if(fileName != null) cate.setIcon(fileName);
                cateService.update(cate);
            }

            resp.sendRedirect(req.getContextPath() + "/admin/home");
        } else if(action.equals("/delete")) {
        	int id = Integer.parseInt(req.getParameter("id"));
            Category cate = cateService.findById(id);
            // Chỉ xóa category do chính họ tạo
            if(cate.getUser().getId() != currentUser.getId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            cateService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/home");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
