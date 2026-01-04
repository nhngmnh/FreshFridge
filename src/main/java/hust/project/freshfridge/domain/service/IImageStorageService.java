package hust.project.freshfridge.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface IImageStorageService {
    String uploadImage(MultipartFile file, String folder);
    String uploadAvatar(MultipartFile file, Long userId);
    String uploadFoodImage(MultipartFile file, Long groupId);
    void deleteImage(String imageUrl);
}
