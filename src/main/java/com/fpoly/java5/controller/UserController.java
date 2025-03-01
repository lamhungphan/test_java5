package com.fpoly.java5.controller;

import com.fpoly.java5.entity.User;
import com.fpoly.java5.repository.UserRepository;
import com.fpoly.java5.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/", "/users"})
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
        System.out.println("User Image: " + user.orElse(new User()).getImage());  // Debug
        return "user";
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createUser(@ModelAttribute("user") User user,
                             @RequestPart(value = "imageFile", required = false) MultipartFile file) {
        try {
            String filename = FileUploadUtil.saveFile(file);
            if (filename != null) {
                user.setImage(filename);
            }
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/users";
    }

    @GetMapping("/uploads/{filename}")
    @ResponseBody
    public ResponseEntity<UrlResource> getFile(@PathVariable String filename) throws IOException {
        Path path = Paths.get("uploads").resolve(filename);
        UrlResource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateUser(@ModelAttribute User user,
                             @RequestPart(value = "imageFile", required = false) MultipartFile file) {
        try {
            Optional<User> existingUser = userRepository.findById(user.getId());

            if (existingUser.isPresent()) {
                User updatedUser = existingUser.get();

                // Nếu có file ảnh mới
                if (file != null && !file.isEmpty()) {
                    // Xóa ảnh cũ trước khi lưu ảnh mới
                    FileUploadUtil.deleteFile(updatedUser.getImage());

                    // Lưu ảnh mới
                    String filename = FileUploadUtil.saveFile(file);
                    updatedUser.setImage(filename);
                }
                // Cập nhật thông tin user
                updatedUser.setFullname(user.getFullname());
                updatedUser.setEmail(user.getEmail());
                updatedUser.setPassword(user.getPassword());
                updatedUser.setAdmin(user.isAdmin());

                userRepository.save(updatedUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
