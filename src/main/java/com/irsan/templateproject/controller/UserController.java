package com.irsan.templateproject.controller;

import com.irsan.templateproject.model.request.LoginRequest;
import com.irsan.templateproject.model.request.UserRequest;
import com.irsan.templateproject.model.response.UserResponse;
import com.irsan.templateproject.service.user.UserService;
import com.irsan.templateproject.utility.annotation.Logging;
import com.irsan.templateproject.utility.helper.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.irsan.templateproject.utility.constant.GlobalConstant.SUCCESS;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 23/05/2024
 */
@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Logging(traceId = true)
    @PostMapping("auth/register")
    public ResponseEntity<?> saveUser(@RequestBody UserRequest userRequest) {
        UserResponse saveUser = userService.save(userRequest);

        return ResponseHelper.build(saveUser, SUCCESS, HttpStatus.OK);
    }

    @Logging(traceId = true)
    @PostMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        String token = userService.login(loginRequest);

        return ResponseHelper.build(token, SUCCESS, HttpStatus.OK);
    }

}
