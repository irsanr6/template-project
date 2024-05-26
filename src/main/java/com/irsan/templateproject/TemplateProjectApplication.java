package com.irsan.templateproject;

import com.irsan.templateproject.model.enumeration.Role;
import com.irsan.templateproject.model.request.UserRequest;
import com.irsan.templateproject.model.response.UserResponse;
import com.irsan.templateproject.service.user.UserService;
import com.irsan.templateproject.utility.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class TemplateProjectApplication {
    private final UserService userService;
    private final LoggerUtil log = new LoggerUtil(TemplateProjectApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TemplateProjectApplication.class, args);
    }

    @PostConstruct
    public void initUserAdmin() {
        log.info("Init user admin");
        UserRequest userRequest = UserRequest.builder()
                .username("admin")
                .password("admin123")
                .authorities(Role.ROLE_SUPER_ADMIN)
                .fullName("Admin")
                .email("admin@example.com")
                .phone("081234567890")
                .address("Jl. Admin No. 1")
                .build();
        boolean usernameExist = userService.isUsernameExist(userRequest.getUsername());
        if (usernameExist) {
            log.warn("User admin already exist");
            return;
        }
        UserResponse save = userService.save(userRequest);
        log.info("User admin created: {}", save);
    }
}
