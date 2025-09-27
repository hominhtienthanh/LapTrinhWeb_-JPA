package controller;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import entity.User;

@WebFilter("/*")
public class FilterController implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("account") : null;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        // URLs không cần login
        if (uri.endsWith("/login") || uri.endsWith("/login.jsp") ||
            uri.contains("/css/") || uri.contains("/js/") || uri.contains("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        if (user == null) {
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        int role = user.getRole();

        // admin
        if (uri.startsWith(contextPath + "/admin/") && role != 3) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN); // 403
            return;
        }

        // manager
        if (uri.startsWith(contextPath + "/manager/") && role != 2) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN); // 403
            return;
        }

        // /user/* tất cả role đều vào được
        chain.doFilter(request, response);
    }
}
