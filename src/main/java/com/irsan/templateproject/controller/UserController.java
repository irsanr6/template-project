package com.irsan.templateproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.irsan.templateproject.exception.AppException;
import com.irsan.templateproject.model.request.LoginRequest;
import com.irsan.templateproject.model.request.UserRequest;
import com.irsan.templateproject.model.response.UserResponse;
import com.irsan.templateproject.service.user.UserService;
import com.irsan.templateproject.utility.annotation.Logging;
import com.irsan.templateproject.utility.helper.ResponseHelper;
import com.irsan.templateproject.utility.util.AesUtil;
import com.irsan.templateproject.utility.util.JsonUtil;
import com.irsan.templateproject.utility.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    LoggerUtil log = new LoggerUtil(UserController.class);
    private final AesUtil aesUtil;

    private final UserService userService;

    @Logging(traceId = true)
    @PostMapping("auth/register")
    public ResponseEntity<?> saveUser(@RequestBody Map<String, String> encRequest) throws JsonProcessingException {
        UserRequest userRequest = getData(encRequest, UserRequest.class);

        UserResponse saveUser = userService.save(userRequest);

        return ResponseHelper.build(saveUser, SUCCESS, HttpStatus.OK);
    }

    @Logging(traceId = true)
    @PostMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> encRequest) {
        LoginRequest loginRequest = getData(encRequest, LoginRequest.class);

        String token = userService.login(loginRequest);

        return ResponseHelper.build(token, SUCCESS, HttpStatus.OK);
    }

    private <T> T getData(Map<String, String> encRequest, Class<T> clazz) {
        try {
            String decRequest = aesUtil.decrypt(encRequest.get("enc"));
            return JsonUtil.jsonToClass(decRequest, clazz);
        } catch (JsonProcessingException e) {
            throw new AppException(e);
        }
    }

}
