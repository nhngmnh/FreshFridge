package hust.project.freshfridge.infrastructure.config;

import hust.project.freshfridge.domain.constant.Role;
import hust.project.freshfridge.domain.entity.User;
import hust.project.freshfridge.domain.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AdminInitConfig {

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.name}")
    private String adminName;

    @Bean
    public CommandLineRunner initAdmin(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin already exists
            if (userRepository.existsByEmail(adminEmail)) {
                log.info("Admin account already exists: {}", adminEmail);
                return;
            }

            // Create admin account
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .name(adminName)
                    .role(Role.ADMIN.getValue())
                    .isVerified(true)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(admin);
            log.info("Admin account created successfully: {}", adminEmail);
        };
    }
}
