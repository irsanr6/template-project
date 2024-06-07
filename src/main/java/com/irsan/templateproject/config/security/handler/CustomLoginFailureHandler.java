package com.irsan.templateproject.config.security.handler;

import com.irsan.templateproject.config.security.LoginAttemptService;
import com.irsan.templateproject.entity.User;
import com.irsan.templateproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 06/06/2024
 */
@Component
@RequiredArgsConstructor
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final LoginAttemptService loginAttemptService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        User user = userRepository.findByUsername(username).orElse(new User());

        if (user.isEnabled() && user.isAccountNonLocked()) {
            if (user.getFailedAttempt() < LoginAttemptService.MAX_FAILED_ATTEMPTS - 1) {
                loginAttemptService.increaseFailedAttempts(user);
            } else {
                loginAttemptService.lock(user);
                exception = new LockedException("Your account has been locked due to 3 failed attempts. It will be unlocked after 24 hours.");
            }
        } else if (!user.isAccountNonLocked()) {
            if (loginAttemptService.unlockWhenTimeExpired(user)) {
                exception = new LockedException("Your account has been unlocked. Please try to login again.");
            }
        }

        super.onAuthenticationFailure(request, response, exception);
    }

}
