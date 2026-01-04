package hust.project.freshfridge.infrastructure.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FirebaseConfig {

    @Value("${firebase.credentials.path:}")
    private String credentialsPath;

    @Value("${firebase.credentials.json:}")
    private String credentialsJson;

    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount = getCredentialsStream();
                
                if (serviceAccount != null) {
                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                            .build();

                    FirebaseApp.initializeApp(options);
                    log.info("Firebase đã được khởi tạo thành công");
                } else {
                    log.warn("Không tìm thấy Firebase credentials. FCM sẽ bị vô hiệu hóa.");
                }
            }
        } catch (IOException e) {
            log.error("Lỗi khởi tạo Firebase: {}", e.getMessage());
        }
    }

    private InputStream getCredentialsStream() {
        // Ưu tiên đọc từ JSON string (cho production/environment variable)
        if (credentialsJson != null && !credentialsJson.isBlank()) {
            return new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8));
        }

        // Nếu không có JSON, đọc từ file path
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            try {
                return resourceLoader.getResource(credentialsPath).getInputStream();
            } catch (IOException e) {
                log.warn("Không thể đọc Firebase credentials từ file: {}", credentialsPath);
            }
        }

        return null;
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        if (FirebaseApp.getApps().isEmpty()) {
            return null;
        }
        return FirebaseMessaging.getInstance();
    }
}
