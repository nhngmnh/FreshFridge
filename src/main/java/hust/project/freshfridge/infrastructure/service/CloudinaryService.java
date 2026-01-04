package hust.project.freshfridge.infrastructure.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.service.IImageStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Transformation;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unchecked")
public class CloudinaryService implements IImageStorageService {

    private final Cloudinary cloudinary;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    /**
     * Upload image to Cloudinary
     * @param file MultipartFile to upload
     * @param folder Cloudinary folder name (e.g., "avatars", "foods")
     * @return URL of uploaded image
     */
    public String uploadImage(MultipartFile file, String folder) {
        validateFile(file);

        try {
            String publicId = folder + "/" + UUID.randomUUID().toString();
            
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", folder,
                    "resource_type", "image",
                    "overwrite", true,
                    "transformation", new Transformation().quality("auto:good").fetchFormat("auto")
            );

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            
            String url = (String) result.get("secure_url");
            log.info("Image uploaded successfully to Cloudinary: {}", url);
            
            return url;
        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary", e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED, "Không thể tải lên hình ảnh");
        }
    }

    /**
     * Upload avatar image with specific transformations
     */
    public String uploadAvatar(MultipartFile file, Long userId) {
        validateFile(file);

        try {
            String publicId = "avatars/user_" + userId;
            
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", "avatars",
                    "resource_type", "image",
                    "overwrite", true,
                    "transformation", new Transformation()
                            .width(200).height(200)
                            .crop("fill").gravity("face")
                            .quality("auto:good").fetchFormat("auto")
            );

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            
            String url = (String) result.get("secure_url");
            log.info("Avatar uploaded successfully for user {}: {}", userId, url);
            
            return url;
        } catch (IOException e) {
            log.error("Failed to upload avatar to Cloudinary", e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED, "Không thể tải lên ảnh đại diện");
        }
    }

    /**
     * Upload food image with specific transformations
     */
    public String uploadFoodImage(MultipartFile file, Long groupId) {
        validateFile(file);

        try {
            String publicId = "foods/group_" + groupId + "_" + UUID.randomUUID().toString();
            
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", "foods",
                    "resource_type", "image",
                    "overwrite", true,
                    "transformation", new Transformation()
                            .width(400).height(400)
                            .crop("fill")
                            .quality("auto:good").fetchFormat("auto")
            );

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            
            String url = (String) result.get("secure_url");
            log.info("Food image uploaded successfully: {}", url);
            
            return url;
        } catch (IOException e) {
            log.error("Failed to upload food image to Cloudinary", e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED, "Không thể tải lên hình ảnh thực phẩm");
        }
    }

    /**
     * Delete image from Cloudinary by URL
     */
    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        try {
            // Extract public_id from URL
            String publicId = extractPublicId(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                log.info("Image deleted from Cloudinary: {}", publicId);
            }
        } catch (IOException e) {
            log.warn("Failed to delete image from Cloudinary: {}", imageUrl, e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "File không được để trống");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Kích thước file vượt quá 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Định dạng file không được hỗ trợ. Chỉ chấp nhận: JPEG, PNG, GIF, WebP");
        }
    }

    private String extractPublicId(String url) {
        try {
            // URL format: https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{public_id}.{format}
            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex == -1) return null;
            
            String afterUpload = url.substring(uploadIndex + 8);
            // Remove version if present (v1234567890/)
            if (afterUpload.startsWith("v")) {
                int slashIndex = afterUpload.indexOf("/");
                if (slashIndex != -1) {
                    afterUpload = afterUpload.substring(slashIndex + 1);
                }
            }
            
            // Remove file extension
            int dotIndex = afterUpload.lastIndexOf(".");
            if (dotIndex != -1) {
                afterUpload = afterUpload.substring(0, dotIndex);
            }
            
            return afterUpload;
        } catch (Exception e) {
            log.warn("Failed to extract public_id from URL: {}", url);
            return null;
        }
    }
}
