package controller;

import java.io.IOException;
import entity.User;
import service.UserService;
import service.Impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns="/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            redirectByRole(resp, (User) session.getAttribute("account"), req.getContextPath());
            return;
        }

        // Check cookie
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    session = req.getSession(true);
                    User user = userService.findByUsername(cookie.getValue());
                    if (user != null) {
                        session.setAttribute("account", user);
                        redirectByRole(resp, user, req.getContextPath());
                        return;
                    }
                }
            }
        }

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean remember = "on".equals(req.getParameter("remember"));

        if (username.isEmpty() || password.isEmpty()) {
            req.setAttribute("alert", "Tài khoản hoặc mật khẩu không được rỗng");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("account", user);
            if (remember) saveRememberMe(resp, user.getUsername());
            redirectByRole(resp, user, req.getContextPath());
        } else {
            req.setAttribute("alert", "Tài khoản hoặc mật khẩu không đúng");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void redirectByRole(HttpServletResponse resp, User user, String contextPath) throws IOException {
        switch (user.getRole()) {
            case 1: resp.sendRedirect(contextPath + "/user/home"); break;
            case 2: resp.sendRedirect(contextPath + "/manager/home"); break;
            case 3: resp.sendRedirect(contextPath + "/admin/home"); break;
            default: resp.sendRedirect(contextPath + "/login");
        }
    }

    private void saveRememberMe(HttpServletResponse resp, String username) {
        Cookie cookie = new Cookie("username", username);
        cookie.setMaxAge(30 * 60); // 30 phút
        resp.addCookie(cookie);
    }
}
