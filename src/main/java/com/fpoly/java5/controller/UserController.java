package com.fpoly.java5.controller;

import com.fpoly.java5.entity.User;
import com.fpoly.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String listUsers(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<User> users;
        if (keyword != null && !keyword.isEmpty()) {
            users = userRepository.findByFullname(keyword);
        } else {
            users = userRepository.findAll();
        }
        model.addAttribute("users", users);
        model.addAttribute("user", new User());
        return "user";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") String id, Model model) {
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("users", userRepository.findAll());
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
