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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 23/05/2024
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
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
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(new User());

        List<String> collectTokenActives = user.getUserTokenActives().stream()
                .map(UserTokenActive::getAccessToken)
                .collect(Collectors.toList());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .fullName(user.getUserProfile().getFullName())
                .email(user.getUserProfile().getEmail())
                .address(user.getUserProfile().getAddress())
                .phone(user.getUserProfile().getPhone())
                .tokenActives(collectTokenActives)
                .build();
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public String login(LoginRequest loginRequest) {
        String token = jwtTokenUtil.generateAccessToken(loginRequest.getUsername());

        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(new User());

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserResponse userResponse = getUserByUsername(username);

        GrantedAuthority authorities = new SimpleGrantedAuthority(userResponse.getAuthorities().name());

        return new org.springframework.security.core.userdetails.User(userResponse.getUsername(), userResponse.getPassword(), Collections.singletonList(authorities));
    }

}
