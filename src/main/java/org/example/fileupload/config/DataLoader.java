package org.example.fileupload.config;

import lombok.RequiredArgsConstructor;
import org.example.fileupload.entity.User;
import org.example.fileupload.entity.enums.Role;
import org.example.fileupload.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    @Bean
    public CommandLineRunner loadSuperAdmin() {
        return args -> {

            // Faqat dev yoki local muhitda ishlasin
            String activeProfile = environment.getActiveProfiles().length > 0
                    ? environment.getActiveProfiles()[0]
                    : "default";

            if (!"dev".equals(activeProfile) && !"local".equals(activeProfile)) {
                System.out.println("DataLoader faqat dev yoki local profilida ishlaydi. Joriy profil: " + activeProfile);
                return;
            }

            String superAdminEmail = "superadmin@autoedu.uz";
            String superAdminPassword = "admin123";

            if (!userRepository.existsByEmail(superAdminEmail)) {

                User superAdmin = User.builder()
                        .fullName("Super Admin")
                        .email(superAdminEmail)
                        .password(passwordEncoder.encode(superAdminPassword))
                        .role(Role.ADMIN)   // yoki Role.SUPER_ADMIN bo'lsa shuni qo'ying
                        .build();

                userRepository.save(superAdmin);

                System.out.println("╔═══════════════════════════════════════════════╗");
                System.out.println("║         SUPER ADMIN YARATILDI                 ║");
                System.out.println("║ Email     : " + superAdminEmail);
                System.out.println("║ Parol     : " + superAdminPassword);
                System.out.println("║ Role      : " + superAdmin.getRole());
                System.out.println("╚═══════════════════════════════════════════════╝");
            } else {
                System.out.println("Super admin allaqachon mavjud → qayta yaratilmadi.");
            }
        };
    }
}