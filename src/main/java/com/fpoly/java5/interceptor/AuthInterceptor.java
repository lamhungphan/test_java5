package com.fpoly.java5.interceptor;

import com.fpoly.java5.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("user");

        if (userObj == null) {
            response.sendRedirect("/login?error=not_logged_in");
            return false;
        }

        // Nếu user truy cập CRUD mà không phải admin -> Chặn
        if (request.getRequestURI().matches(".*/(create|update|delete).*")) {
            User user = (User) userObj;
            if (!user.isAdmin()) {
                response.sendRedirect("/users?error=no_permission");
                return false;
            }
        }

        return true;
    }
}
