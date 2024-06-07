package com.irsan.templateproject.service.user;

import com.irsan.templateproject.model.request.LoginRequest;
import com.irsan.templateproject.model.request.UserRequest;
import com.irsan.templateproject.model.response.UserResponse;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 23/05/2024
 */
public interface UserService {

    UserResponse save(UserRequest userRequest);

    boolean isUsernameExist(String username);

    String login(LoginRequest loginRequest);

}
