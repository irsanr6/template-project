package com.irsan.templateproject.service.user;

import com.irsan.templateproject.entity.User;
import com.irsan.templateproject.entity.UserProfile;
import com.irsan.templateproject.entity.UserTokenActive;
import com.irsan.templateproject.exception.AppException;
import com.irsan.templateproject.model.request.LoginRequest;
import com.irsan.templateproject.model.request.UserRequest;
import com.irsan.templateproject.model.response.UserResponse;
import com.irsan.templateproject.repository.UserRepository;
import com.irsan.templateproject.utility.util.JwtTokenUtil;
import com.irsan.templateproject.utility.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 23/05/2024
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final LoggerUtil log = new LoggerUtil(UserServiceImpl.class);

    @Transactional
    @Override
    public UserResponse save(UserRequest userRequest) {
        boolean usernameExist = isUsernameExist(userRequest.getUsername());
        if (usernameExist) {
            throw new AppException(String.format("User with username:%s already exist", userRequest.getUsername()));
        }
        UserProfile userProfile = UserProfile.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .address(userRequest.getAddress())
                .phone(userRequest.getPhone())
                .build();

        User user = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .authorities(userRequest.getAuthorities())
                .userProfile(userProfile)
                .build();

        userProfile.setUser(user);

        User saveUser = userRepository.save(user);

        return UserResponse.builder()
                .id(saveUser.getId())
                .username(saveUser.getUsername())
                .authorities(saveUser.getAuthorities())
                .fullName(saveUser.getUserProfile().getFullName())
                .email(saveUser.getUserProfile().getEmail())
                .address(saveUser.getUserProfile().getAddress())
                .phone(saveUser.getUserProfile().getPhone())
                .tokenActives(new ArrayList<>())
                .build();
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public String login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(new User());

        String token = jwtTokenUtil.generateAccessToken(loginRequest.getUsername());

        UserTokenActive userTokenActive = UserTokenActive.builder()
                .id(jwtTokenUtil.getJwtId(token))
                .accessToken(token)
                .expiredDate(jwtTokenUtil.getExpiration(token))
                .createdBy(loginRequest.getUsername())
                .modifiedBy(loginRequest.getUsername())
                .user(user)
                .build();

        user.getUserTokenActives().add(userTokenActive);

        userRepository.save(user);

        return token;
    }

}
