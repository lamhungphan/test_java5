package com.fpoly.java5.controller;

import com.fpoly.java5.entity.User;
import com.fpoly.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/","/users"})
    public String listUsers( Model model,
            @RequestParam(name = "p", required = false) Optional<Integer> p,
            @RequestParam(name = "keyword", required = false) String keyword) {

        int currentPage = p.orElse(0);
        int pageSize = 5;

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<User> userPage;

        if (keyword != null && !keyword.isEmpty()) {
            userPage = userRepository.findByFullnameContaining(keyword, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        model.addAttribute("page", userPage);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("user", new User());
        return "user";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") String id,
                           @RequestParam(name = "p", required = false) Optional<Integer> p,
                           Model model) {
        Optional<User> user = userRepository.findById(id);

        int currentPage = p.orElse(0);
        int pageSize = 5;

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<User> userPage = userRepository.findAll(pageable);

        model.addAttribute("page", userPage);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("user", user.orElse(new User()));
        return "user";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user) {
        if (user.getId() != null && !user.getId().isEmpty()) {
            userRepository.save(user);
        }
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
        return "redirect:/users";
    }
}
