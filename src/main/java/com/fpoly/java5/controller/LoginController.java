package com.fpoly.java5.controller;

import com.fpoly.java5.entity.User;
import com.fpoly.java5.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpServletRequest request,
                               Model model) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) { // Kiểm tra password
                HttpSession session = request.getSession();
                session.setAttribute("user", user); // Lưu user vào session
                return "redirect:/users"; // Chuyển về trang users
            }
        }

        model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
        return "login"; // Quay lại trang login nếu sai
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate(); // Xóa session
        return "redirect:/login";
    }
}
