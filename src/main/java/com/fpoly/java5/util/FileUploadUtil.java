package com.fpoly.java5.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    private static final String UPLOAD_DIR = "src/main/resources/static/";

    // Lưu file ảnh và trả về tên file
    public static String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Tạo thư mục nếu chưa tồn tại
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Lấy tên file gốc
        String originalFilename = file.getOriginalFilename();

        // Nếu file đã có timestamp (chứa dấu "_"), chỉ lấy phần sau cùng làm tên
        if (originalFilename.contains("_")) {
            originalFilename = originalFilename.substring(originalFilename.lastIndexOf("_") + 1);
        }

        // Thêm timestamp mới vào tên file
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    // Xóa file ảnh cũ
    public static void deleteFile(String filename) throws IOException {
        if (filename != null && !filename.isEmpty()) {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            Files.deleteIfExists(filePath);
        }
    }
}
